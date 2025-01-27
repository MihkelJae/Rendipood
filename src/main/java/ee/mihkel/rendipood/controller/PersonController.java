package ee.mihkel.rendipood.controller;

import ee.mihkel.rendipood.Entity.Person;
import ee.mihkel.rendipood.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @PostMapping("persons")
    public List<Person> addPerson(@RequestBody Person person) {
        person.setBonusPoints(0);
        personRepository.save(person);
        return personRepository.findAll();
    }

    @DeleteMapping("persons")
    public List<Person> deletePerson(@RequestParam Long personId) {
        personRepository.deleteById(personId);
        return personRepository.findAll();
    }

    @GetMapping("bonus-points")
    public int getBonusPoints(@RequestParam Long id) {
        return personRepository.findById(id).orElseThrow().getBonusPoints();
    }
}
