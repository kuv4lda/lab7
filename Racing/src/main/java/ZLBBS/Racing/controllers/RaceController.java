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
        return "race_list"; // Шаблон для отображения
    }

    // Страница создания нового заезда
    @GetMapping("/new")
    public String showCreateRaceForm(Model model) {
        model.addAttribute("race", new Race());

        // Получаем все автомобили и водителей для выпадающих списков
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("drivers", driverService.findAll());

        return "race_add";
    }

    // Обработка POST-запроса для создания нового заезда
    @PostMapping("/new")
    public String createRace(@Valid @ModelAttribute Race race, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки, перезаполняем списки и показываем форму заново
            model.addAttribute("cars", carService.findAll());
            model.addAttribute("drivers", driverService.findAll());
            return "race_add";
        }

        try {
            // Находим объекты по ID и устанавливаем их в модель
            Car car = carService.findById(race.getCar().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid car ID"));
            Driver driver = driverService.findById(race.getDriver().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid driver ID"));
            race.setCar(car);
            race.setDriver(driver);

            // Сохраняем новый заезд
            raceService.save(race);
        } catch (Exception e) {
            // Логируем ошибку и возвращаем страницу с сообщением
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
            // Загружаем заезд по ID
            Race race = raceService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid race ID: " + id));

            // Добавляем заезд в модель для редактирования
            model.addAttribute("race", race);

            // Загружаем все автомобили и водителей для выпадающих списков
            model.addAttribute("cars", carService.findAll());
            model.addAttribute("drivers", driverService.findAll());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to fetch race for editing");
            return "redirect:/races";
        }
        return "race_edit";  // Путь к шаблону редактирования
    }

    // Обработка обновления заезда
    @PostMapping("/edit/{id}")
    public String updateRace(@PathVariable Long id, @ModelAttribute Race race, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "race_edit"; // Если есть ошибки, возвращаем форму с ошибками
        }
        try {
            race.setId(id);
            raceService.save(race);
        } catch (Exception e) {
            e.printStackTrace();
            return "race_edit"; // Возвращаем форму с ошибками
        }
        return "redirect:/races"; // После успешного обновления перенаправляем на список заездов
    }

    // Удаление заезда
    @GetMapping("/delete/{id}")
    public String deleteRace(@PathVariable Long id) {
        raceService.deleteById(id); // Удаляем заезд по id
        return "redirect:/races"; // Перенаправляем на страницу всех заездов
    }
}
