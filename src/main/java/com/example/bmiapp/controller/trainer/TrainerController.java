package com.example.bmiapp.controller.trainer;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    private boolean isTrainer(HttpSession session) {
        return session.getAttribute("role") != null && 
               session.getAttribute("role").equals("trainer");
    }

    @GetMapping("/dashboard")
    public String trainerDashboard(HttpSession session) {
        if (!isTrainer(session)) return "redirect:/auth/login";
        return "trainer/dashboard";
    }

    @GetMapping("/plan/create")
    public String createPlanForm(HttpSession session) {
        if (!isTrainer(session)) return "redirect:/auth/login";
        return "trainer/createPlan";
    }

    @PostMapping("/plan/create")
    public String processCreatePlan(@RequestParam("planName") String planName,
                                   @RequestParam("description") String description,
                                   @RequestParam("difficulty") String difficulty,
                                   @RequestParam("duration") int duration,
                                   HttpSession session) {
        if (!isTrainer(session)) return "redirect:/auth/login";
        // In a real app, save to database
        return "redirect:/trainer/dashboard?success=Plan created successfully";
    }

    @GetMapping("/session/schedule")
    public String scheduleSessionForm(HttpSession session) {
        if (!isTrainer(session)) return "redirect:/auth/login";
        return "trainer/scheduleSession";
    }

    @PostMapping("/session/schedule")
    public String processScheduleSession(@RequestParam("memberId") String memberId,
                                        @RequestParam("sessionType") String sessionType,
                                        @RequestParam("sessionTime") String sessionTime,
                                        @RequestParam("duration") int duration,
                                        @RequestParam("notes") String notes,
                                        HttpSession session) {
        if (!isTrainer(session)) return "redirect:/auth/login";
        // In a real app, save to database
        return "redirect:/trainer/dashboard?success=Session scheduled successfully";
    }

    @GetMapping("/progress")
    public String monitorProgress(HttpSession session, Model model) {
        if (!isTrainer(session)) return "redirect:/auth/login";
        // Add progress data to model
        return "trainer/progress";
    }
}