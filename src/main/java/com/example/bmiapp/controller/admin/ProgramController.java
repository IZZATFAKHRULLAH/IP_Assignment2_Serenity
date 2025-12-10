package com.example.bmiapp.controller.admin;

import com.example.bmiapp.dao.ProgramDao;
import com.example.bmiapp.model.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/program")
public class ProgramController {

    @Autowired
    private ProgramDao programDao;

    private boolean isAdmin(HttpSession session) {
        return session.getAttribute("role") != null && 
               session.getAttribute("role").equals("admin");
    }

    // 1. List all programs
    @GetMapping("/list")
    public String listPrograms(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("programs", programDao.findAll());
        return "admin/programList";
    }

    // 2. Show Form for Adding
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("program", new Program());
        return "admin/programForm";
    }

    // 3. Show Form for Editing
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        Program program = programDao.findById(id);
        model.addAttribute("program", program);
        return "admin/programForm";
    }

    // 4. Process Save (Insert or Update)
    @PostMapping("/save")
    public String saveProgram(@ModelAttribute("program") Program program, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        programDao.save(program);
        return "redirect:/admin/program/list";
    }

    // 5. Delete Program
    @GetMapping("/delete")
    public String deleteProgram(@RequestParam("id") Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        programDao.delete(id);
        return "redirect:/admin/program/list";
    }
}