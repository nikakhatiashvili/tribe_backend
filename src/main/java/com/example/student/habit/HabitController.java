package com.example.student.habit;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/habit")
public class HabitController {

    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }
//
//    @PostMapping("/create")
//    public void createNewHabit(@Valid @RequestBody TribeHabit tribeHabit, @RequestParam String firebaseId) throws Exception {
//        habitService.createNewHabit(tribeHabit, firebaseId);
//    }

//    @GetMapping("/get_habits")
//    public List<TribeHabit> getHabits(@RequestParam String firebaseId) throws Exception {
//        return habitService.getHabits(firebaseId);
//    }
//    @DeleteMapping("/remove")
//    public void removeHabit(@RequestParam String firebaseId, @RequestParam Long id) throws Exception {
//        habitService.removeHabit(firebaseId, id,"");
//    }

}


