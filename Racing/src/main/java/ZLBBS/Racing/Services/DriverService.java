package ZLBBS.Racing.Services;

import ZLBBS.Racing.models.Driver;
import ZLBBS.Racing.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    // Получение всех водителей
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    // Поиск водителя по ID
    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }

    // Сохранение водителя
    public void save(Driver driver) {
        driverRepository.save(driver);
    }

    // Удаление водителя
    public void deleteById(Long id) {
        driverRepository.deleteById(id);
    }
}
