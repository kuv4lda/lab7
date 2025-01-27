package ZLBBS.Racing.repositories;

import ZLBBS.Racing.models.Race;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Race, Long> {

    // Метод для проверки, используется ли автомобиль в заезде
    boolean existsByCarId(Long carId);
    boolean existsByDriverId(Long carId);
}
