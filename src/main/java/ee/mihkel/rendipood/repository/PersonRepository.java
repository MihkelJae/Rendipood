package ee.mihkel.rendipood.repository;

import ee.mihkel.rendipood.Entity.Film;
import ee.mihkel.rendipood.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
