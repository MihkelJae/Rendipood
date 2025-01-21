package ee.mihkel.rendipood.controller;

import ee.mihkel.rendipood.Entity.Film;
import ee.mihkel.rendipood.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FilmController {

    @Autowired
    FilmRepository filmRepository;

    @GetMapping("film/all")
    public List<Film> getFilm() {
        return filmRepository.findAll();
    }

    @GetMapping("film")
    public List<Film> isInStock() {
        return filmRepository.findAll();
    }

    @PostMapping("film")
    public List<Film> addFilm(@RequestBody Film film) {
        filmRepository.save(film);
        return filmRepository.findAll();
    }

    @DeleteMapping("film/{id}")
    public List<Film> deleteFilm(@RequestBody Long id) {
        filmRepository.deleteById(id);
        return filmRepository.findAll();
    }

    @PutMapping("film")
    public Film updateFilm(@RequestBody Film film) {
    }
}
