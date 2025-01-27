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
        return "cars_list"; // Здесь указываем имя шаблона
    }

    // Отображение формы для добавления нового автомобиля
    @GetMapping("/new")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car()); // Пустой объект для формы
        return "car_add"; // Название шаблона car_add.html
    }

    // Обработка данных из формы
    @PostMapping("/new")
    public String addCar(@ModelAttribute("car") Car car, Model model) {
        try {
            carService.save(car); // Сохраняем новый автомобиль
            return "redirect:/cars"; // Перенаправление на страницу списка машин после добавления
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to add car.");
            return "car_add"; // Если ошибка, возвращаем на форму добавления
        }
    }

    // Страница для редактирования автомобиля
    @GetMapping("/edit/{id}")
    public String editCar(@PathVariable("id") Long id, Model model) {
        Optional<Car> carOptional = carService.findById(id); // Используем findById
        if (carOptional.isPresent()) {
            model.addAttribute("car", carOptional.get());
            return "car_edit"; // Имя шаблона для редактирования
        } else {
            // Если автомобиль не найден, можно вернуть ошибку или страницу с сообщением
            return "car_not_found"; // Страница ошибки
        }
    }

    // Обработчик для сохранения изменений
    @PostMapping("/edit/{id}")
    public String saveCar(@PathVariable("id") Long id, @ModelAttribute Car car) {
        car.setId(id);  // Устанавливаем ID, чтобы не потерять его при редактировании
        carService.save(car); // Используем метод save
        return "redirect:/cars"; // Перенаправляем на список всех автомобилей
    }

    // Метод для удаления автомобиля
    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") Long id) {
        // Проверяем, используется ли автомобиль в заезде
        if (raceService.isCarUsedInRace(id)) {
            // Если автомобиль используется в заезде, не удаляем и показываем сообщение
            return "redirect:/cars?error=Car+is+used+in+a+race";
        }

        // Если не используется в заезде, удаляем
        carService.deleteById(id);
        return "redirect:/cars"; // Перенаправляем на страницу с автомобилями
    }
}
