package com.ticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.exception.PersistenceException;
import com.ticket.exception.ServiceException;
import com.ticket.model.DepartmentModel;
import com.ticket.model.PriorityModel;
import com.ticket.model.TicketDetailsModel;
import com.ticket.model.UserModel;
import com.ticket.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	UserModel user = new UserModel();
	UserService userSer = new UserService();
	TicketDetailsModel tic = new TicketDetailsModel();
	DepartmentModel dep = new DepartmentModel();
	PriorityModel pri = new PriorityModel();

	@PostMapping("/login")
	public String login(@RequestBody UserModel user,ModelMap map)
			{
String email=user.getEmailId();
String password=user.getPassword();
		try {
			if (userSer.logIn(email, password)) {
				
				return "Login success";
			}
			else
			{
				map.addAttribute("ERROR","Enter proper id and password");
			}
		} catch (ServiceException e) {
		
			map.addAttribute("ERROR",e.getMessage());
		}
		
return "Login failure";
		

	}

	@PostMapping("/reg")
	public String register(@RequestBody UserModel user,ModelMap map){
		
		try {
			
			userSer.register(user);
			return "Registration success";
		} catch (ServiceException e) {
			
			map.addAttribute("ERROR",e.getMessage());	
		}
		return "Registration failure";

	}

@PutMapping("/reopenTicket")
public String ticReopen(@RequestParam("ticId") int ticketId,ModelMap map){
	
	try {
		if(userSer.reopenTicket(ticketId))
		{
			return "Ticket reopened";	
		}
	} catch (ServiceException e) {
		map.addAttribute("ERROR",e.getMessage());
	}
	
	return "Call failed";
	
	
	
}
@PutMapping("/closeTicket")
public String ticClose(@RequestParam("ticId") int ticketId,ModelMap map) {
 	tic.setId(ticketId);
	try {
		if(userSer.closeTicket(tic))
		{
			return "Ticket Closed";	
		}
	} catch (ServiceException e) {
		
		map.addAttribute("ERROR",e.getMessage());
	} catch (PersistenceException e) {
	
		map.addAttribute("ERROR",e.getMessage());
	}
	
	return "Failure";
	
	
	
	
}
	@PutMapping("/ticketGen")
	public String ticketGenerate(@RequestParam("email") String email, @RequestParam("sub") String sub,
			@RequestParam("desc") String desc, @RequestParam("dept") String depart, @RequestParam("prior") String prio)
			{
		
		try {
			if (userSer.ticketGeneration( email,sub, desc, depart, prio)) {
				return "Ticket generated Successfully";
			}
		} catch (ServiceException e) {
			
			e.printStackTrace();
		}
		return "Ticket Creation failed";	
	}



	@GetMapping("/viewTickets/{email}")
	public List<TicketDetailsModel> viewByUser(@PathVariable("email") String email) throws ServiceException {
		 return userSer.viewTicket(email);
		
	}
	
	
	
}
