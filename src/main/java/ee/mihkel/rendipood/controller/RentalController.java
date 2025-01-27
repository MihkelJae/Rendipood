package ee.mihkel.rendipood.controller;

import ee.mihkel.rendipood.Entity.Film;
import ee.mihkel.rendipood.Entity.Person;
import ee.mihkel.rendipood.Entity.Rental;
import ee.mihkel.rendipood.model.FilmRentalDTO;
import ee.mihkel.rendipood.repository.FilmRepository;
import ee.mihkel.rendipood.repository.PersonRepository;
import ee.mihkel.rendipood.repository.RentalRepository;
import ee.mihkel.rendipood.service.RentalService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RentalController {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    RentalService rentalService;

    @Autowired
    PersonRepository personRepository;

    @GetMapping("rentals")
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    @PostMapping("start-rental")
    public List<Rental> startRental(
            @RequestBody List<FilmRentalDTO> films,
            @RequestParam Long personId,
            @RequestParam(required = false, defaultValue = "0") int bonusDays) {
        // ära luba laenutada filme, mis on juba laenutatud
        Person person = personRepository.findById(personId).orElseThrow();
        rentalService.checkIfAllAvailable(films, person, bonusDays);
        rentalService.saveRental(films, person, bonusDays);
        return rentalRepository.findAll();
    }

    // Tagasitoomine
    //ID + days
    //Panema filmi küljes Rentali null, Days number 0 ja arvuta mis on lateFee
    @PostMapping("end-rental")
    public List<Rental> endRental(@RequestBody List<FilmRentalDTO> films) {
        rentalService.calculateLateFee(films);
        return rentalRepository.findAll();
    }

}
