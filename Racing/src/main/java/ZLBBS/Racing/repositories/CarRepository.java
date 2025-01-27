package ZLBBS.Racing.repositories;

import ZLBBS.Racing.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}