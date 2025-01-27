package ZLBBS.Racing.controllers;

import ZLBBS.Racing.Services.DriverService;
import ZLBBS.Racing.Services.RaceService;
import ZLBBS.Racing.models.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private RaceService raceService;

    // Получить всех водителей
    @GetMapping
    public String getAllDrivers(Model model) {
        model.addAttribute("drivers", driverService.findAll());
        return "drivers_list";
    }

    // Форма для добавления нового водителя
    @GetMapping("/new")
    public String showAddDriverForm(Model model) {
        model.addAttribute("driver", new Driver());
        return "driver_add";
    }

    // Добавление водителя
    @PostMapping("/new")
    public String addDriver(@ModelAttribute Driver driver, Model model) {
        try {
            driverService.save(driver);
            return "redirect:/drivers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding driver.");
            return "driver_add";
        }
    }

    // Редактирование водителя
    @GetMapping("/edit/{id}")
    public String editDriver(@PathVariable("id") Long id, Model model) {
        Optional<Driver> driverOptional = driverService.findById(id);
        if (driverOptional.isPresent()) {
            model.addAttribute("driver", driverOptional.get());
            return "driver_edit"; 
        } else {
            model.addAttribute("errorMessage", "Driver not found.");
            return "driver_not_found";
        }
    }

    // Сохранение изменений водителя
    @PostMapping("/edit/{id}")
    public String saveDriver(@PathVariable("id") Long id, @ModelAttribute Driver driver, Model model) {
        try {
            driver.setId(id); 
            driverService.save(driver);
            return "redirect:/drivers"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving driver.");
            return "driver_edit";
        }
    }

    // Метод для удаления водителя
    @PostMapping("/delete/{id}")
    public String deleteDriver(@PathVariable("id") Long id) {
        if (raceService.isDriverUsedInRace(id)) {
            return "redirect:/drivers?error=Driver+is+used+in+a+race";
        }
        driverService.deleteById(id);
        return "redirect:/drivers"; 
    }
}
