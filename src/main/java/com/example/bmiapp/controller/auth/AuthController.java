package com.example.bmiapp.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bmiapp.dao.UserDao;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/login")
    public String loginForm(@RequestParam(name = "error", required = false) String error,
                            @RequestParam(name = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Invalid username or password.");
        if (logout != null) model.addAttribute("logoutMsg", "You have successfully logged out.");
        return "auth/login"; 
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("name") String name,
                               @RequestParam("password") String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        // Use DAO to check database
        if (userDao.validateUser(name, password)) {
            String role = userDao.getUserRole(name);
            
            session.setAttribute("username", name);
            session.setAttribute("role", role); 

            if ("admin".equals(role)) {
                return "redirect:/admin/dashboard";
            } else if ("member".equals(role)) {
                return "redirect:/member/dashboard";
            }
            else if ("trainer".equals(role)) {
                return "redirect:/trainer/dashboard";
            } else {
                // Unknown role, redirect to login with error
                redirectAttributes.addAttribute("error", "true");
                return "redirect:/auth/login";
            }
        } else {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam("name") String name,
                                      @RequestParam("password") String password) {
        // Register as 'member' by default
        userDao.register(name, password, "member");
        return "redirect:/auth/login";
    }

    @GetMapping("/logout") 
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session != null) session.invalidate();
        redirectAttributes.addAttribute("logout", "true");
        return "redirect:/auth/login"; 
    }
}