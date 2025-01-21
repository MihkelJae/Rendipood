package ee.mihkel.rendipood.repository;

import ee.mihkel.rendipood.Entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
}
