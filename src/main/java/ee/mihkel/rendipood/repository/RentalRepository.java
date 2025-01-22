package ee.mihkel.rendipood.repository;

import ee.mihkel.rendipood.Entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
