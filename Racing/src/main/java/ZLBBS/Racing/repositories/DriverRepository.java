package ZLBBS.Racing.repositories;

import ZLBBS.Racing.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
