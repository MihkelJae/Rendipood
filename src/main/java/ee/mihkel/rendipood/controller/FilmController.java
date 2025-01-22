package ee.mihkel.rendipood.controller;

import ee.mihkel.rendipood.Entity.Film;
import ee.mihkel.rendipood.Entity.FilmType;
import ee.mihkel.rendipood.model.FilmAddDTO;
import ee.mihkel.rendipood.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FilmController {

    @Autowired
    FilmRepository filmRepository;

    @GetMapping("films")
    public List<Film> getFilm() {
        return filmRepository.findAll();
    }

//    @GetMapping("film")
//    public List<Film> isInStock() {
//        return filmRepository.findAll();
//    }

    @PostMapping("films")
    public List<Film> addFilm(@RequestBody FilmAddDTO filmDTO) {
        Film film = new Film();
//        film.setRental(null);
//        film.setDaysRented(0);
        film.setName(filmDTO.getName());
        film.setType(filmDTO.getType());
        filmRepository.save(film);
        return filmRepository.findAll();
    }

    @PatchMapping("film-type")
    public void changFilmType(@RequestParam Long id, @RequestParam FilmType newType) {
        Film film = filmRepository.getReferenceById(id);
        film.setType(newType);
        filmRepository.save(film);
    }


//    @PostMapping("film")
//    public List<Film> addFilm(@RequestBody Film film) {
//        filmRepository.save(film);
//        return filmRepository.findAll();
//    }


    @DeleteMapping("films")
    public List<Film> deleteFilm(@RequestParam Long id) {
        filmRepository.deleteById(id);
        return filmRepository.findAll();
    }

    @GetMapping("availablefilms")
    public List<Film> getAvailableFilms() {
        return filmRepository.findByRentalNull();
    }
}
