package ee.mihkel.rendipood.controller;

import ee.mihkel.rendipood.Entity.Film;
import ee.mihkel.rendipood.Entity.Rental;
import ee.mihkel.rendipood.model.FilmRentalDTO;
import ee.mihkel.rendipood.repository.FilmRepository;
import ee.mihkel.rendipood.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentalController {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    FilmRepository filmRepository;

    @GetMapping("rentals")
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    @PostMapping("start-rental")
    public List<Rental> startRental(@RequestBody List<FilmRentalDTO> films) {
        //TODO: 2 ära luba laenutada filme, mis on juba laenutatud
        Rental rental = new Rental(); //id:0
        rental = rentalRepository.save(rental);// saab id
        double sum = 0;
        for (FilmRentalDTO f : films) {
            Film dbFilm = filmRepository.findById(f.getId()).orElseThrow();
            dbFilm.setDaysRented(f.getDays());
            dbFilm.setRental(rental);
            filmRepository.save(dbFilm);
            //TODO: 1. arvuta iga filmi maksumus
            sum += 5;
        }
        rental.setInitialFee(sum);
        rentalRepository.save(rental);
        return rentalRepository.findAll();
    }

    //TODO: 3 Tagasitoomine
    //ID + days
    //Panema filmi küljes Rentali null, Days number 0 ja arvuta mis on lateFee
}
