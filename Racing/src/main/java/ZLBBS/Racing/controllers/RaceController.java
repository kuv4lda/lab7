package ZLBBS.Racing.controllers;

import ZLBBS.Racing.models.Race;
import ZLBBS.Racing.models.Car;
import ZLBBS.Racing.models.Driver;
import ZLBBS.Racing.Services.RaceService;
import ZLBBS.Racing.Services.CarService;
import ZLBBS.Racing.Services.DriverService;
import ZLBBS.Racing.dto.RaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/races")
public class RaceController {

    @Autowired
    private RaceService raceService;

    @Autowired
    private CarService carService;

    @Autowired
    private DriverService driverService;

    // Страница для отображения всех заездов
    @GetMapping
    public String getAllRaces(Model model) {
        List<Race> races = raceService.findAll();
        List<RaceDTO> raceDtos = races.stream().map(race -> {
            String driverName = race.getDriver().getFullName();
            String carInfo = race.getCar().getBrand() + " " + race.getCar().getModel();
            return new RaceDTO(race.getId(), driverName, carInfo, race.getTrack());
        }).collect(Collectors.toList());
        model.addAttribute("races", raceDtos);
        return "race_list"; 
    }

    // Страница создания нового заезда
    @GetMapping("/new")
    public String showCreateRaceForm(Model model) {
        model.addAttribute("race", new Race());
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("drivers", driverService.findAll());

        return "race_add";
    }

    // Обработка для создания нового заезда
    @PostMapping("/new")
    public String createRace(@Valid @ModelAttribute Race race, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cars", carService.findAll());
            model.addAttribute("drivers", driverService.findAll());
            return "race_add";
        }
        try {
            Car car = carService.findById(race.getCar().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid car ID"));
            Driver driver = driverService.findById(race.getDriver().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid driver ID"));
            race.setCar(car);
            race.setDriver(driver);
            raceService.save(race);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while saving the race.");
            model.addAttribute("cars", carService.findAll());
            model.addAttribute("drivers", driverService.findAll());
            return "race_add";
        }
        return "redirect:/races";
    }

    // Страница для редактирования заезда
    @GetMapping("/edit/{id}")
    public String showEditRaceForm(@PathVariable Long id, Model model) {
        try {
            Race race = raceService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid race ID: " + id));
            model.addAttribute("race", race);
            model.addAttribute("cars", carService.findAll());
            model.addAttribute("drivers", driverService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to fetch race for editing");
            return "redirect:/races";
        }
        return "race_edit";
    }

    // Обработка обновления заезда
    @PostMapping("/edit/{id}")
    public String updateRace(@PathVariable Long id, @ModelAttribute Race race, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "race_edit";
        }
        try {
            race.setId(id);
            raceService.save(race);
        } catch (Exception e) {
            e.printStackTrace();
            return "race_edit"; 
        }
        return "redirect:/races"; 
    }

    // Удаление заезда
    @GetMapping("/delete/{id}")
    public String deleteRace(@PathVariable Long id) {
        raceService.deleteById(id);
        return "redirect:/races";
    }
}
