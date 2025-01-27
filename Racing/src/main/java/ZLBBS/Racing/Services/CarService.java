package ZLBBS.Racing.Services;

import ZLBBS.Racing.models.Car;
import ZLBBS.Racing.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    // Возвращает все автомобили
    public List<Car> findAll() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            System.out.println("No cars found in the database.");
        }
        return cars;
    }

    // Находит автомобиль по ID
    public Optional<Car> findById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            // Логируем или обрабатываем случай, когда не нашли автомобиль
            System.out.println("Car with ID " + id + " not found.");
        }
        return car;
    }

    // Сохраняет новый или обновленный автомобиль
    public void save(Car car) {
        if (car == null) {
            System.out.println("Cannot save null car object.");
            return;
        }
        carRepository.save(car);
        System.out.println("Car saved with ID: " + car.getId());
    }

    // Удаляет автомобиль по ID
    public void deleteById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            // Логируем, если не нашли автомобиль для удаления
            System.out.println("Car with ID " + id + " not found for deletion.");
            return;
        }
        carRepository.deleteById(id);
        System.out.println("Car with ID " + id + " deleted.");
    }
}
