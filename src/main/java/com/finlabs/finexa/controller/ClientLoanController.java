package com.finlabs.finexa.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientFamilyLoanOutput;
import com.finlabs.finexa.dto.ClientLoanProjectionOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.ClientLoanService;
import com.finlabs.finexa.service.impl.ClientLoanServiceImpl;
import com.finlabs.finexa.util.ExcelUtility;

@RestController
public class ClientLoanController {
	//@Autowired
	//private ClientLoanService clientLoanService;
	Logger log = Logger.getLogger(ClientLoanController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;

	@PreAuthorize("hasAnyRole('BudgetManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientLoanInfo", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientLoanInfo(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientLoanController >>> Entering getClientIncomeInfo() ");
		List<ClientFamilyLoanOutput> clientFamilyLoanOuputList = null;
		Session session = null;
		try {
			session = sessionfactory.openSession();
			ClientLoanService clientLoanService = new ClientLoanServiceImpl();
			clientFamilyLoanOuputList = clientLoanService.getCLientFamilyAllLoans(clientId, fpFlag, session);
			log.info("ClientLoanController <<< Exiting getClientIncomeInfo() ");
			return new ResponseEntity<List<ClientFamilyLoanOutput>>(clientFamilyLoanOuputList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {

			FinexaBussinessException businessException = new FinexaBussinessException("ClientLoan", "111",
					"Failed to get  Loan Details , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

	@PreAuthorize("hasAnyRole('BudgetManagementView','PortFolioManagementView')")
	@RequestMapping(value = "/getClientBankLoanDetails", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientBankLoanDetails(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "mode") String mode, @RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientLoanController >>> Entering getClientBankLoanDetails() ");
		List<ClientLoanProjectionOutput> clientFamilyLoanOuputList = null;
		Session session = null;
		try {
			session = sessionfactory.openSession();
			ClientLoanService clientLoanService = new ClientLoanServiceImpl();
			clientFamilyLoanOuputList = clientLoanService.getClientLoanProjectionList(clientId, mode, fpFlag, session);
			log.info("ClientLoanController <<< Exiting getClientBankLoanDetails() ");
			return new ResponseEntity<List<ClientLoanProjectionOutput>>(clientFamilyLoanOuputList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {

			FinexaBussinessException businessException = new FinexaBussinessException("ClientLoan", "111",
					"Failed to get  Loan Details , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();
		}

	}
	@PreAuthorize("hasAnyRole('BudgetManagementView','PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientAllBankLoanDetails", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientAllBankLoanDetails(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "mode") String mode, @RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientLoanController >>> Entering getClientAllBankLoanDetails() ");
		List<ClientFamilyLoanOutput> clientFamilyLoanOuputList = null;
		Session session = null;
		try {
			session = sessionfactory.openSession();
			ClientLoanService clientLoanService = new ClientLoanServiceImpl();
			clientFamilyLoanOuputList = clientLoanService.getClientAllLoanStatusDetails(clientId, mode, fpFlag,
					session);
			log.info("ClientLoanController <<< Exiting getClientAllBankLoanDetails() ");
			return new ResponseEntity<List<ClientFamilyLoanOutput>>(clientFamilyLoanOuputList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getMessage(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientLoan", "111",
					"Failed to get Total Loan Details , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();
		}

	}
	@PreAuthorize("hasAnyRole('BudgetManagementView','PortFolioManagementView')")
	@RequestMapping(value = "/downloadLoanReport", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadExcelOutputExl(HttpServletResponse response,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "mode") String mode,
			@RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientLoanController >>> Enter downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		try {
			session = sessionfactory.openSession();
			ClientLoanService clientLoanService = new ClientLoanServiceImpl();
			List<ClientFamilyLoanOutput> loanOutputList = clientLoanService.getClientAllLoanStatusDetails(clientId,
					mode, fpFlag, session);
			ClassLoader loader = getClass().getClassLoader();
			File file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			workbook = ExcelUtility.writeExcelOutputLoanData(file, loanOutputList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientLoan", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();
		}
		log.info("ClientLoanController <<< Exiting downloadExcelOutputExl() ");
		return returner;
	}

}
