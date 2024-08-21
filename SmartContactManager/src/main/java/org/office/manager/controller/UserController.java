package org.office.manager.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.office.manager.entity.Contact;
import org.office.manager.entity.User;
import org.office.manager.helper.MessageHelper;
import org.office.manager.repository.ContactRepo;
import org.office.manager.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepo ur;

	@Autowired
	private ContactRepo cr;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		User user = ur.getUserByUserName(userName);
		model.addAttribute("user", user);

	};

	@GetMapping("/dash")
	public String dashboard(Model model, Principal principal) {
		return "normal/dash";
	}

	@GetMapping("/showcontact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model model, Principal principal) {
		String name = principal.getName();
		User user = ur.getUserByUserName(name);
		PageRequest pg = PageRequest.of(page, 5);
		Page<Contact> contact = cr.findContactsByUser(user.getId(), pg);
		model.addAttribute("contact", contact);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", contact.getTotalPages());

		return "normal/showcontact";
	}

	@GetMapping("/addcontact")
	public String addContactForm(Model model) {
		model.addAttribute("contact", new Contact());
		return "normal/addcontact";
	}

	@PostMapping("/process-contact")
	public String processContactForm(@ModelAttribute Contact contact, Principal principal, Model model,
			HttpSession session) {
		try {
			String name = principal.getName();
			User user = ur.getUserByUserName(name);
			contact.setUser(user);
			user.getContact().add(contact);
			ur.save(user);
			model.addAttribute("session", session);
			session.setAttribute("message", new MessageHelper("Contact Added", "alert-success"));
		} catch (Exception e) {
			model.addAttribute(contact);
			session.setAttribute("message",
					new MessageHelper("Something went wrong!! Duplicate Entry!", "alert-danger"));
			e.printStackTrace();
		}
		return "normal/addcontact";
	}

	@GetMapping("/{cId}/contact")
	public String contactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		Optional<Contact> contactOp = cr.findById(cId);
		Contact contact = contactOp.get();
		String name = principal.getName();
		User user = ur.getUserByUserName(name);

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
		}

		return "normal/contactdetail";
	}

	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model) {
		Contact contact = cr.findById(cId).get();
		contact.setUser(null);
		cr.delete(contact);
		return "redirect:/user/showcontact/0";
	}

	@GetMapping("/update/{cId}")
	public String updateContact(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		Contact contact = cr.findById(cId).get();
		model.addAttribute("contact", contact);
		return "normal/update";
	}

	@PostMapping("/doupdate")
	public String processUpdateForm(@ModelAttribute Contact contact, Principal principal, Model model,
			HttpSession session) {
		try {
			String name = principal.getName();
			User user = ur.getUserByUserName(name);
			contact.setUser(user);
			cr.save(contact);
			model.addAttribute("session", session);
			session.setAttribute("message", new MessageHelper("Contact Updated", "alert-success"));
		} catch (Exception e) {
			model.addAttribute(contact);
			session.setAttribute("message",
					new MessageHelper("Something went wrong!! Duplicate Entry!", "alert-danger"));
			e.printStackTrace();
		}
		return "redirect:/user/" + contact.getCId() + "/contact";
	}

	@GetMapping("/profile")
	public String profile() {
		return "normal/profile";
	}
}
