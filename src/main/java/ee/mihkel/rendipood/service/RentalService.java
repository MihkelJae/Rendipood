package ee.mihkel.rendipood.service;

import ee.mihkel.rendipood.Entity.Film;
import ee.mihkel.rendipood.Entity.FilmType;
import ee.mihkel.rendipood.Entity.Person;
import ee.mihkel.rendipood.Entity.Rental;
import ee.mihkel.rendipood.model.FilmRentalDTO;
import ee.mihkel.rendipood.repository.FilmRepository;
import ee.mihkel.rendipood.repository.PersonRepository;
import ee.mihkel.rendipood.repository.RentalRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.Remote;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    FilmRepository filmRepository;
    @Autowired
    private PersonRepository personRepository;

    private final double PREMIUM_PRICE = 4;
    private final double BASIC_PRICE = 3;


    public void saveRental(List<FilmRentalDTO> films, Person person, int bonusDays) {
        Rental rental = new Rental(); //id:0
        rental = rentalRepository.save(rental);// saab id
        double sum = 0;
        for (FilmRentalDTO f : films) {
            Film dbFilm = filmRepository.findById(f.getId()).orElseThrow();
            dbFilm.setDaysRented(f.getDays());
            dbFilm.setRental(rental);
            filmRepository.save(dbFilm);
            // arvuta iga filmi maksumus
            sum += calculateFilmFee(dbFilm, person, bonusDays);
        }
        rental.setInitialFee(sum);
//        Person person = new Person();
//        person.setId(id);
        rental.setPerson(person);
        rentalRepository.save(rental);
    }

    private double calculateFilmFee(Film dbFilm, Person person, int bonusDays) {
        switch (dbFilm.getType()) {
            case NEW -> {
                Rental rental = dbFilm.getRental();
                int bonusDaysToUse = bonusDays - rental.getBonusDaysUsed();
                if (dbFilm.getDaysRented() < bonusDaysToUse) {
                    bonusDaysToUse = dbFilm.getDaysRented();
                }
                int personPoints = person.getBonusPoints();
                if (bonusDays > 0) {
                    person.setBonusPoints(personPoints - 25 * bonusDaysToUse);
                }
                person.setBonusPoints(personPoints + 2);
                personRepository.save(person);

                rental.setBonusDaysUsed(personPoints + bonusDaysToUse);
                rentalRepository.save(rental);

                return (dbFilm.getDaysRented() - bonusDaysToUse) * PREMIUM_PRICE;
            }
            case REGULAR -> {
                person.setBonusPoints(person.getBonusPoints() + 1);
                personRepository.save(person);
                if (dbFilm.getDaysRented() <= 3) {
                    return BASIC_PRICE ;
                }
                return (dbFilm.getDaysRented()-3) * BASIC_PRICE + BASIC_PRICE;
            }
            case OLD -> {
                person.setBonusPoints(person.getBonusPoints() + 1);
                personRepository.save(person);
                if (dbFilm.getDaysRented() <= 5) {
                    return BASIC_PRICE ;
                }
                return (dbFilm.getDaysRented()-5) * BASIC_PRICE + BASIC_PRICE;
            }
            case null, default -> {
                return 0;
            }
        }
    }

    public void checkIfAllAvailable(List<FilmRentalDTO> films, Person person, int bonusDays) {
        for (FilmRentalDTO f : films) {
            Film dbFilm = filmRepository.findById(f.getId()).orElseThrow();
            if (dbFilm.getDaysRented() > 0 || dbFilm.getRental() != null) {
                throw new RuntimeException("ERROR_FILM_RENTED: " + dbFilm.getId());
            }
            if (dbFilm.getType() == FilmType.NEW && bonusDays > 0 &&  person.getBonusPoints() < bonusDays * 25) {
                throw new RuntimeException("ERROR_NOT_ENOUGH_BONUS_POINTS");
            }
        }
    }

    public void calculateLateFee(List<FilmRentalDTO> films) {
        for (FilmRentalDTO f : films) {
            Film dbFilm = filmRepository.findById(f.getId()).orElseThrow();
            if (dbFilm.getDaysRented() < f.getDays()) {
                Rental filmRental = dbFilm.getRental();
                if (filmRental == null) {
                    throw new RuntimeException("ERROR_FILM_NOT_RENTED: " + dbFilm.getId());
                }
                filmRental.setLateFee(filmRental.getLateFee() + getFilmLateFee(dbFilm, f.getDays()));
                rentalRepository.save(filmRental);
            }
            dbFilm.setRental(null);
            dbFilm.setDaysRented(0);
            filmRepository.save(dbFilm);
        }
    }

    private double getFilmLateFee(Film dbFilm, int days) {
        switch (dbFilm.getType()) {
            case NEW -> {
                return (days - dbFilm.getDaysRented()) * PREMIUM_PRICE;
            }
            case OLD,REGULAR -> {
                return (days - dbFilm.getDaysRented()) * BASIC_PRICE;
            }
            case null, default -> {
                return 0;
            }
        }
    }
}
