package com.finlabs.finexa.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.finlabs.finexa.dto.ClientBudgetRatioOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.service.ClientBudgetRatioService;
import com.finlabs.finexa.service.impl.ClientBudgetRatioServiceImpl;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientBudgetRatioController {

//	@Autowired
//	private ClientBudgetRatioService clientBudgetRatioService;
	@Autowired
	private CacheInfoService cacheInfoService;
	Logger log = Logger.getLogger(ClientBudgetRatioController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;

	@PreAuthorize("hasAnyRole('BudgetManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientBudgetRatioInfo", method = RequestMethod.GET, headers = "Accept=application/json")
	public Object getClientBudgetRatioInfo(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {

		log.info("ClientBudgetRatioController >>> Entering getClientBudgetRatioInfo() ");
		ClientBudgetRatioOutput clientBudgetRatioOutput = null;
		Session session = null;
		String header = "",token = "";
		ClientBudgetRatioService clientBudgetRatioServiceImpl = null;
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			clientBudgetRatioServiceImpl = new ClientBudgetRatioServiceImpl();
			clientBudgetRatioOutput = clientBudgetRatioServiceImpl.getClientBudgetRatioInfo(token, clientId, session,cacheInfoService);
			return new ResponseEntity<ClientBudgetRatioOutput>(clientBudgetRatioOutput, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getMessage(), HttpStatus.OK);
		} catch (Exception exp) {
			log.info("ClientBudgetRatioController <<< Exiting getClientBudgetRatioInfo() ");
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
	}

}
