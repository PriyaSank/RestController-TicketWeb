package com.ticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.dao.EmployeeDAO;
import com.ticket.exception.PersistenceException;
import com.ticket.exception.ServiceException;
import com.ticket.model.EmployeeModel;
import com.ticket.model.IssueModel;
import com.ticket.model.RoleModel;
import com.ticket.model.TicketDetailsModel;
import com.ticket.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	int roleId;
	EmployeeService empSer = new EmployeeService();
		RoleModel role = new RoleModel();
	EmployeeDAO empDAO = new EmployeeDAO();
	
	@PostMapping("/login")
	public String logIn(@RequestBody EmployeeModel emp, HttpSession session,
			ModelMap map) {
	String email=emp.getEmailId();
	String password=emp.getPassword();
		try {
			if (empSer.logIn(email, password)) {
				roleId = empDAO.getRoleId(email);
				role.setId(roleId);
				emp.setRole(role);
				
				
				if (roleId == 1) {
					return "Welcome Admin";
				} else {
					return "Welcome employee";
				}
			}
		} catch (ServiceException e) {

			map.addAttribute("ERROR", e.getMessage());
		} catch (PersistenceException e) {

			e.printStackTrace();
		}

		return "Login failed";

	}

	@GetMapping("/viewTickets/{email}")
	public List<TicketDetailsModel> view(@PathVariable("email") String email, ModelMap map) throws ServiceException {
		
		
		List<TicketDetailsModel> ticketList = empSer.viewAssignedTicket(email);

		
		if (ticketList == null) {
			map.addAttribute("ERROR", "No tickets found");
			return null;
		}else
		{
			return ticketList;
		}
	}

	@GetMapping("/replyTicket")
	public String reply(ModelMap map, @RequestParam("soln") String solution,@RequestParam("ticId") int ticketId,@RequestParam("email") String email) 
 {
		
		
		try {
			EmployeeModel empl=new EmployeeModel();
			EmployeeDAO eDAO=new EmployeeDAO();
			TicketDetailsModel tic=new TicketDetailsModel();
			int empId=eDAO.getId(email);
			IssueModel issue=new IssueModel();
			empl.setId(empId);
			issue.setEmp(empl);
			tic.setId(ticketId);
			issue.setSolution(solution);
			issue.setTic(tic);
			
			
			empSer.replyToTicket(issue);
			return  "Reply success";
		} catch (ServiceException e) {
			map.addAttribute("ERROR", "No tickets found");
			return "Reply not registered";
		} catch (PersistenceException e) {
			map.addAttribute("ERROR", "Error found");
			return "Reply not registered";
		}

		
	}




	
}
