package ZLBBS.Racing.Services;

import ZLBBS.Racing.models.Race;
import ZLBBS.Racing.repositories.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RaceService {

    @Autowired
    private RaceRepository raceRepository;

    public List<Race> findAll() {
        return raceRepository.findAll();
    }

    public Optional<Race> findById(Long id) {
        return raceRepository.findById(id);
    }

    public void save(Race race) {
        raceRepository.save(race);
    }

    public void deleteById(Long id) {
        raceRepository.deleteById(id);
    }

    // Метод для проверки, используется ли автомобиль в заезде
    public boolean isCarUsedInRace(Long carId) {
        // Проверяем, есть ли заезд, в котором участвует автомобиль с заданным ID
        return raceRepository.existsByCarId(carId);
    }

    public boolean isDriverUsedInRace(Long driverId) {
        return raceRepository.existsByDriverId(driverId);
    }
}
