package com.ticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.dao.EmployeeDAO;
import com.ticket.exception.ServiceException;
import com.ticket.model.EmployeeModel;
import com.ticket.model.RoleModel;
import com.ticket.model.TicketDetailsModel;
import com.ticket.service.EmployeeService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	EmployeeService empSer = new EmployeeService();
	RoleModel role = new RoleModel();
	EmployeeDAO empDAO = new EmployeeDAO();
	
	@GetMapping("/viewTickets/{depId}")
	public List<TicketDetailsModel> view(@PathVariable("depId") int depId , ModelMap map) throws ServiceException {

				
		 List<TicketDetailsModel> ticketList =
		 empSer.viewTicketByDepartment(depId);

		 map.addAttribute("TICKET_LIST", ticketList);
		 if (ticketList == null) {
		 map.addAttribute("ERROR", "No tickets found");
		 return null;
		}
	return ticketList;
	}


	@GetMapping("/assignEmp")
	public String assignTicket(@RequestParam("ticId") int ticId, @RequestParam("empId") int toEmpId,@RequestParam("email") String email)
			throws ServiceException {
		

		if (empSer.assignTicket(email, toEmpId, ticId)) {

			return "Assignment successful";
		} else
			return "Assignment failed";
	}




	
	@GetMapping("/deleteTicket")
	public String delete(@RequestParam("ticId") int ticketId,ModelMap map) throws ServiceException {
		empSer.deleteTicket(ticketId);
		return "Success";
	}
}
