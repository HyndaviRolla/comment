package com.example.demo.controller;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.business.LoggedInUser;
import com.example.demo.entity.Userlog;
import com.example.demo.service.UserService;


@Controller
@RequestMapping("/loginpage")
public class LoginController {

    private UserService userService;
    private LoggedInUser loggedInUser;
    
    public LoginController(@Autowired UserService userService,
    @Autowired LoggedInUser loggedInUser) {
        this.userService = userService;
        this.loggedInUser = loggedInUser;
    }
    @GetMapping
    public String getLoginForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new Userlog());
        }
        return "login";
    }
    @PostMapping
    public String login(@ModelAttribute("user") Userlog user, BindingResult bindingResult, RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            attr.addFlashAttribute("user", user);
            return "redirect:/loginpage";
        }
        Optional<Userlog> authResult = userService.authenticate(user.getName(), user.getPassword());
        if (authResult.isEmpty()) {
            attr.addFlashAttribute("result", "Username-password combination not found in database");
            attr.addFlashAttribute("user", user);
            return "redirect:/loginpage";
        }
        this.loggedInUser.setLoggedInUser(authResult.get());
        return "redirect:/forum/post/form";
    }

}