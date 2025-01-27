package ZLBBS.Racing.controllers;

import ZLBBS.Racing.Services.RaceService;
import ZLBBS.Racing.models.Car;
import ZLBBS.Racing.Services.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private RaceService raceService;

    @GetMapping
    public String getAllCars(Model model) {
        model.addAttribute("cars", carService.findAll());
        return "cars_list"; 
    }

    // Отображение формы для добавления нового автомобиля
    @GetMapping("/new")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car()); 
        return "car_add";
    }

    // Обработка данных из формы
    @PostMapping("/new")
    public String addCar(@ModelAttribute("car") Car car, Model model) {
        try {
            carService.save(car);
            return "redirect:/cars";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to add car.");
            return "car_add";
        }
    }

    // Страница для редактирования автомобиля
    @GetMapping("/edit/{id}")
    public String editCar(@PathVariable("id") Long id, Model model) {
        Optional<Car> carOptional = carService.findById(id);
        if (carOptional.isPresent()) {
            model.addAttribute("car", carOptional.get());
            return "car_edit";
        } else {
            return "car_not_found";
        }
    }

    // Обработчик для сохранения изменений
    @PostMapping("/edit/{id}")
    public String saveCar(@PathVariable("id") Long id, @ModelAttribute Car car) {
        car.setId(id);
        carService.save(car);
        return "redirect:/cars";
    }

    // Метод для удаления автомобиля
    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") Long id) {
        if (raceService.isCarUsedInRace(id)) {
            return "redirect:/cars?error=Car+is+used+in+a+race";
        }
        carService.deleteById(id);
        return "redirect:/cars";
    }
}
