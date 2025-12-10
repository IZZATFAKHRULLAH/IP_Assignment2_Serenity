package com.example.bmiapp.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bmiapp.dao.PersonDao;
import com.example.bmiapp.dao.ProgramDao;
import com.example.bmiapp.model.Person;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ProgramDao programDao;

    private boolean isMember(HttpSession session) {
        return session.getAttribute("role") != null && 
               session.getAttribute("role").equals("member");
    }

    @GetMapping("/dashboard")
    public String memberDashboard(Model model, HttpSession session) {
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        model.addAttribute("appName", "HealthHub@UTM");
        model.addAttribute("username", session.getAttribute("username"));
        
        return "member/dashboard";
    }

    @GetMapping("/bmi")
    public String bmiForm(@RequestParam(name = "error", required = false) String error, 
                          Model model, HttpSession session) {
        if (!isMember(session)) return "redirect:/auth/login"; // Check session
        
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "member/bmi-form";
    }

    @PostMapping("/bmi/compute")
    public String computeBmi(
        @RequestParam("name") String name,
        @RequestParam("id") String id,
        @RequestParam("yob") String yobStr,
        @RequestParam("weight") String weightStr,
        @RequestParam("height") String heightStr,
        @RequestParam("gender") String gender,
        @RequestParam(name = "interest", required = false) String[] interests,
        Model model,
        RedirectAttributes redirectAttributes,
        HttpSession session) {

        if (!isMember(session)) return "redirect:/auth/login"; 
    
        try {
            if (name.isEmpty() || id.isEmpty() || yobStr.isEmpty() ||
                weightStr.isEmpty() || heightStr.isEmpty() || gender.isEmpty()) {
                throw new IllegalArgumentException("All fields (except interests) are mandatory.");
            }
            int yob = Integer.parseInt(yobStr);
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr);

            if (yob < 1900 || yob > 2035) {
                throw new IllegalArgumentException("Please enter a valid Year of Birth (1900-2035).");
            }
            if (weight <= 0 || height <= 0) {
                throw new IllegalArgumentException("Height and weight must be positive numbers.");
            }

            Person person = new Person();
            person.setName(name);
            person.setId(id);
            person.setYob(yob);
            person.setWeight(weight);
            person.setHeight(height);
            person.setGender(gender);
            
            // Fix: Use setInterestsArray instead of setInterests
            person.setInterestsArray(interests);
            
            personDao.add(person);

            String category = person.getCategory();
            model.addAttribute("person", person);
            model.addAttribute("message", category);
            return "member/bmi-result";

        } catch (NumberFormatException e) {
            redirectAttributes.addAttribute("error", "Invalid input. Year, Height, and Weight must be numbers.");
            return "redirect:/member/bmi";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/member/bmi";
        }
    }

    @GetMapping("/bmi/result")
    public String viewBmiResult(HttpSession session) {
        if (!isMember(session)) return "redirect:/auth/login"; 
        return "member/bmi-result";
    }

    @GetMapping("/bmi/history")
    public String viewBmiHistory(HttpSession session, Model model) { 
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        java.util.List<Person> allBmiRecords = personDao.findAll();
        model.addAttribute("historyList", allBmiRecords);
        
        return "member/bmiHistory"; 
    }

    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        // Pass username to profile page
        model.addAttribute("username", session.getAttribute("username"));
        return "member/profile"; 
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String gender,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("postalCode") String postalCode,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        // In a real application, update user profile in database
        // For now, just update session username
        if (name != null && !name.trim().isEmpty()) {
            session.setAttribute("username", name);
        }
        
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/member/profile";
    }

    @GetMapping("/programs")
    public String browsePrograms(HttpSession session, Model model) {
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        // Get programs from database
        model.addAttribute("programs", programDao.findAll());
        return "member/programs";
    }

    @PostMapping("/programs/enroll")
    public String enrollInProgram(
            @RequestParam("programId") Integer programId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isMember(session)) return "redirect:/auth/login";
        
        // In a real application, save enrollment to database
        redirectAttributes.addFlashAttribute("success", "Successfully enrolled in program!");
        return "redirect:/member/programs";
    }

    @GetMapping("/my-plan")
    public String viewMyPlan(HttpSession session, Model model) {
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        // In a real application, get member's plan from database
        model.addAttribute("username", session.getAttribute("username"));
        return "member/myPlan";
    }

    @PostMapping("/workout/done")
    public String markWorkoutDone(
            @RequestParam("workoutId") String workoutId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        // In a real application, update workout status in database
        redirectAttributes.addFlashAttribute("success", "Workout marked as completed!");
        return "redirect:/member/my-plan";
    }

    @GetMapping("/sessions")
    public String viewSessions(HttpSession session, Model model) {
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        // In a real application, get member's sessions from database
        model.addAttribute("username", session.getAttribute("username"));
        return "member/sessions";
    }

    @PostMapping("/sessions/confirm")
    public String confirmSessionAttendance(
            @RequestParam("sessionId") String sessionId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isMember(session)) return "redirect:/auth/login";
        
        // In a real application, update session attendance in database
        redirectAttributes.addFlashAttribute("success", "Attendance confirmed!");
        return "redirect:/member/sessions";
    }

    @PostMapping("/sessions/reschedule")
    public String rescheduleSession(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("newDate") String newDate,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isMember(session)) return "redirect:/auth/login"; 
        
        // In a real application, update session date in database
        redirectAttributes.addFlashAttribute("success", "Session rescheduled!");
        return "redirect:/member/sessions";
    }
}