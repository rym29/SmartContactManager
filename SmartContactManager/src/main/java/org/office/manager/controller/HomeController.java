package org.office.manager.controller;

import org.office.manager.entity.User;
import org.office.manager.helper.MessageHelper;
import org.office.manager.repository.UserRepo;
import org.office.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder b;
	@Autowired
	private UserRepo ur;

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

//	@GetMapping("/logout")
//	public String logout() {
//		return "redirect:/login?logout";
//	}

	@GetMapping("/signup/")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/doregister")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,
			HttpSession session) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("USER");
			user.setEnabled(true);
			user.setPassword(b.encode(user.getPassword()));
			ur.save(user);
			model.addAttribute("user", new User());
			model.addAttribute("session", session);
			session.setAttribute("message", new MessageHelper("Successfully Registered", "alert-success"));

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(user);
			session.setAttribute("message",
					new MessageHelper("Somehting went wrong!! Duplicate Entry", "alert-danger"));
		}
		return "signup";
	}
}
