package com.ticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ticket.dao.EmployeeDAO;
import com.ticket.exception.ServiceException;
import com.ticket.model.EmployeeModel;
import com.ticket.model.RoleModel;
import com.ticket.model.TicketDetailsModel;
import com.ticket.service.EmployeeService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	EmployeeService empSer = new EmployeeService();
	RoleModel role = new RoleModel();
	EmployeeDAO empDAO = new EmployeeDAO();
	
	@GetMapping("/viewTickets/{roleId}")
	public List<TicketDetailsModel> view(@PathVariable("roleId") int roleId , ModelMap map) throws ServiceException {

				
		 List<TicketDetailsModel> ticketList =
		 empSer.viewTicketByDepartment(roleId);

		 map.addAttribute("TICKET_LIST", ticketList);
		 if (ticketList == null) {
		 map.addAttribute("ERROR", "No tickets found");
		 return null;
		}
	return ticketList;
	}


	@GetMapping("/assignEmp")
	public String assignTicket(@RequestParam("ticId") int ticId, @RequestParam("empId") int toEmpId, HttpSession session)
			throws ServiceException {
		EmployeeModel b = (EmployeeModel) session.getAttribute("LOGGED_IN_ADMIN");
System.out.println(b.getEmailId() + "aa");
		if (empSer.assignTicket(b.getEmailId(), toEmpId, ticId)) {

			return "redirect:../AdminMainPage.jsp";
		} else
			return "redirect:../Index.jsp";
	}

	@GetMapping("/reassignTicket")
	public String reassign() throws ServiceException {

		return "../ReassignTicket.jsp";
	}

	@GetMapping("/reassignTic")
	public String reassignTic(@RequestParam("ticId") int ticId, @RequestParam("empId") int emp1Id,
			@RequestParam("toEmpId") int toEmpId) throws ServiceException {


		if (empSer.reassignTicket(emp1Id, toEmpId, ticId)) {

			return "redirect:../AdminMainPage.jsp";
		} else
			return "redirect:../Index.jsp";
	}
	
//	@GetMapping("/deleteTicket")
//	public String delete(@RequestParam("ticId") int ticketId,ModelMap map) throws ServiceException {
//		empSer.deleteTicket(ticketId);
//		return "../AdminMainPage.jsp";
//	}
}
