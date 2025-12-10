package com.example.bmiapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bmiapp.dao.PersonDao;
import com.example.bmiapp.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession; 
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PersonDao personDao;

    private boolean isAdmin(HttpSession session) {
        return session.getAttribute("role") != null && 
               session.getAttribute("role").equals("admin");
    }

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        return "admin/dashboard"; 
    }

    @GetMapping("/person/list")
    public String listMembers(@RequestParam(name="searchId", required=false) String searchId,
                              Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login"; 
        
        List<Person> personList;
        if (searchId != null && !searchId.trim().isEmpty()) {
            personList = personDao.findBySearchId(searchId);
        } else {
            personList = personDao.findAll();
        }
        model.addAttribute("persons", personList);
        return "admin/personList"; 
    }
    
    @GetMapping("/person/view")
    public String viewMember(@RequestParam("id") String id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login"; 
        
        Person person = personDao.findById(id);
        if (person != null) {
            model.addAttribute("person", person);
            return "admin/personView"; 
        }
        return "redirect:/admin/person/list";
    }

    @PostMapping("/person/delete")
    public String deleteMember(@RequestParam("id") String id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login"; 
        
        Person person = personDao.findById(id);
        
        if (person != null) {
            personDao.delete(id);
            model.addAttribute("deletedName", person.getName());
            return "admin/personDelete"; 
        }
        
        return "redirect:/admin/person/list";
    }

    @GetMapping("/person/edit")
    public String showEditForm(@RequestParam("id") String id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login"; 
        
        Person person = personDao.findById(id);
        if (person != null) {
            model.addAttribute("person", person);
            return "admin/personEdit";
        }
        return "redirect:/admin/person/list";
    }

    @PostMapping("/person/edit")
    public String processEditForm(
            @RequestParam("originalId") String originalId,
            @RequestParam("id") String newId,
            @RequestParam("name") String name,
            @RequestParam("yob") int yob,
            @RequestParam("weight") double weight,
            @RequestParam("height") double height,
            HttpSession session) {
        
        if (!isAdmin(session)) return "redirect:/auth/login"; 
        
        Person updatedPerson = new Person();
        updatedPerson.setId(newId);
        updatedPerson.setName(name);
        updatedPerson.setYob(yob);
        updatedPerson.setWeight(weight);
        updatedPerson.setHeight(height);
        
        personDao.update(originalId, updatedPerson);
        return "redirect:/admin/person/list";
    }

    @GetMapping("/program-categories")
    public String manageProgramCategories(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login"; 
        return "admin/programCategories"; 
    }

    @GetMapping("/reports")
    public String viewReports(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        return "admin/reports";
    }
    
    
}