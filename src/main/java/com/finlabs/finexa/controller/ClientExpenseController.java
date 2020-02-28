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

import com.finlabs.finexa.dto.AnnualExpensesDetailed;
import com.finlabs.finexa.dto.ClientDetailedOutput;
import com.finlabs.finexa.dto.CommittedOutFlowOutput;
import com.finlabs.finexa.dto.FamilyMemberIncomeDetailOutput;
import com.finlabs.finexa.dto.MasterExpenseIndustryStandardDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.ClientExpenseService;
import com.finlabs.finexa.service.impl.ClientExpenseServiceImpl;
import com.finlabs.finexa.util.ExcelUtility;

@RestController
public class ClientExpenseController {

	// @Autowired
	// private ClientExpenseService clientExpenseSerive;
	Logger log = Logger.getLogger(ClientExpenseController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;
	
	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientExpenseInfo", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientExpenseInfo(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "mode") String mode, @RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientExpenseController >>> Exiting getClientExpenseInfo() ");
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {
			session = sessionfactory.openSession();
			AnnualExpensesDetailed expenseOutput = clientExpenseSeriveImpl.getAnnualExpensesDetailed(clientId, mode,
					fpFlag, session);

			return new ResponseEntity<AnnualExpensesDetailed>(expenseOutput, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			log.info("ClientExpenseController <<< Exiting getClientExpenseInfo() ");
			FinexaBussinessException businessException = new FinexaBussinessException("ClientExpense", "111",
					"Failed to get  ClientExpenseInfo Details , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView')")
	@RequestMapping(value = "/downloadExpense", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadExcelOutputExl(HttpServletResponse response,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "mode") String mode,
			@RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientExpenseController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {
			session = sessionfactory.openSession();
			AnnualExpensesDetailed expenseOutput = clientExpenseSeriveImpl.getAnnualExpensesDetailed(clientId, mode,
					fpFlag, session);
			ClassLoader loader = getClass().getClassLoader();
			File file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			workbook = ExcelUtility.writeExcelOutputExpenseData(file, expenseOutput.getExpenseProjectionList());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientExpense", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		log.info("ClientExpenseController <<< Exiting downloadExcelOutputExl() ");
		return returner;
	}

	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView')")
	@RequestMapping(value = "/downloadExpenseDetailed", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadExpenseDetailed(HttpServletResponse response,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "mode") String mode,
			@RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientExpenseController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {
			session = sessionfactory.openSession();
			AnnualExpensesDetailed expenseOutput = clientExpenseSeriveImpl.getAnnualExpensesDetailed(clientId, mode,
					fpFlag, session);
			ClassLoader loader = getClass().getClassLoader();
			File file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			workbook = ExcelUtility.writeExcelOutputExpenseDataDetailed(file, expenseOutput.getExpenseProjectionList());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientExpense", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		log.info("ClientExpenseController <<< Exiting downloadExcelOutputExl() ");
		return returner;
	}
	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView')")
	@RequestMapping(value = "/getExpenseDetailOfFamilyMember", method = RequestMethod.GET, headers = "Accept=application/json")
	public Object getExpenseDetailOfFamilyMember(@RequestParam(value = "memberId") int memberId) {
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {
			session = sessionfactory.openSession();
			List<FamilyMemberIncomeDetailOutput> familyincomeOutputList = clientExpenseSeriveImpl
					.getExpenseOfFamilyMember(memberId, session);
			return familyincomeOutputList;
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return busExcep.getMessage();
		} catch (Exception exp) {
			log.info("IncomeController <<< Exiting getClientIncomeInfo) ");
			return "failed to get Family Income Details";
		} finally {
			session.close();

		}

	}
	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getCommitedOutflow", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getCommitedOutflow(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "mode") String mode, @RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientLoanController >>> Entering getClientIncomeInfo() ");
		CommittedOutFlowOutput clientCommittedOutFlowOuput = null;
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {

			session = sessionfactory.openSession();

			clientCommittedOutFlowOuput = clientExpenseSeriveImpl.getClientCommitedOutFlows(clientId, mode, fpFlag,
					session);
			log.info("ClientLoanController <<< Exiting getClientIncomeInfo() ");
			return new ResponseEntity<CommittedOutFlowOutput>(clientCommittedOutFlowOuput, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("CommitedOutflow", "111",
					"Failed to get  CommitedOutflow Details , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}
	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView')")
	@RequestMapping(value = "/downloadOutFlow", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadOutFlow(HttpServletResponse response,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "mode") String mode,
			@RequestParam(value = "fpFlag") int fpFlag) {
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {
			session = sessionfactory.openSession();
			CommittedOutFlowOutput clientCommittedOutFlowOuput = null;
			clientCommittedOutFlowOuput = clientExpenseSeriveImpl.getClientCommitedOutFlows(clientId, mode, fpFlag,
					session);
			ClassLoader loader = getClass().getClassLoader();
			File file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			workbook = ExcelUtility.writeExcelOutputCommitedOutFlowData(file,
					clientCommittedOutFlowOuput.getOutFlowList());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			log.info("HelloRestController <<< Exiting saveFamilyIncome() ");
			returner = new ResponseEntity<String>("Failed To download  , Please Try again later.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			session.close();

		}
		return returner;
	}
	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientDetails", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientDetails(@RequestParam(value = "clientId") int clientId) {
		log.info("ClientIncomeController >>> Entering getClientDetailsInfo() ");
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {
			session = sessionfactory.openSession();
			ClientDetailedOutput clientOutput = null;
			clientOutput = clientExpenseSeriveImpl.getClientDetails(clientId, session);
			log.info("ClientExpenseController <<< Exiting getClientDetailsInfo() ");
			return new ResponseEntity<ClientDetailedOutput>(clientOutput, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientExpense", "111",
					"Failed to get Client Details , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

	/**********************
	 * Retrieving Master Expense Industry Standard
	 ******************/
	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView')")
	@RequestMapping(value = "/getMasterExpenseIndustryStandard", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getMasterExpenseIndustryStandard() {
		log.info("ClientExpenseController >>> Entering getMasterExpenseIndustryStandard() ");
		Session session = null;
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		try {
			session = sessionfactory.openSession();
			List<MasterExpenseIndustryStandardDTO> masterExpenseIndustryStandardDTOList = null;
			masterExpenseIndustryStandardDTOList = clientExpenseSeriveImpl.getExpenseIndustryStandard(session);

			log.info("ClientExpenseController <<< Exiting getMasterExpenseIndustryStandard() ");
			return new ResponseEntity<List<MasterExpenseIndustryStandardDTO>>(masterExpenseIndustryStandardDTOList,
					HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientExpense", "111",
					"Failed to get Client Details , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

}
