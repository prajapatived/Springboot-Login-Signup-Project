package com.main.project.controller;

import java.security.Principal;

import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.main.project.entity.User;
import com.main.project.repository.UserRepo;
import com.main.project.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/user/profile")
	public String profile(Principal p, Model m) {
		String email = p.getName();
		User user = userRepo.findByEmail(email);
		m.addAttribute("user", user);
		return "profile";
	}

	@GetMapping("/user/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/user/contact")
	public String contact() {
		return "contact";
	}

	@GetMapping("/user/changepassword")
	public String change() {
		return "change";
	}

	@GetMapping("/user/logout")
	public String logout() {
		return "redirect:/signin?logout";
	}

	@ModelAttribute
	public void commomUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, HttpSession session) {
		System.out.println(user);
		User u = userService.saveUser(user);
		if (u != null) {
			session.setAttribute("msg", "Register successfully");
		} else {
			session.setAttribute("msg", "Register failed!");
		}

		return "redirect:/register";
	}

	@PostMapping("/changePass")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal p, HttpSession session) {
		String email = p.getName();
		User CurrentUser = userRepo.findByEmail(email);

		if (this.bCryptPasswordEncoder.matches(oldPassword, CurrentUser.getPassword())) {
			CurrentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepo.save(CurrentUser);
			session.setAttribute("message", "Password Changed");

		} else {
			session.setAttribute("message", "Password not Changed");
		}
		return "redirect:/home";

	}
}
