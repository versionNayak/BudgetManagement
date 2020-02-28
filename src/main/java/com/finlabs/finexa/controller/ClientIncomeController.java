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

import com.finlabs.finexa.dto.ClientFamilyIncomeOutput;
import com.finlabs.finexa.dto.FamilyMemberIncomeDetailOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.ClientIncomeService;
import com.finlabs.finexa.service.impl.ClientIncomeServiceImpl;
import com.finlabs.finexa.util.ExcelUtility;

@RestController
public class ClientIncomeController {
	// @Autowired
	// private ClientIncomeService clientIncomeSerive;
	Logger log = Logger.getLogger(ClientIncomeController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;

	@PreAuthorize("hasAnyRole('Admin', 'BudgetManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientIncomeInfo", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientIncomeInfo(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "mode") String mode, @RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientIncomeController >>> Entering getClientIncomeInfo() ");
		Session session = null;
		try {
			session = sessionfactory.openSession();
			ClientFamilyIncomeOutput incomeOutput = null;
			ClientIncomeService clientIncomeSeriveImpl = new ClientIncomeServiceImpl();
			incomeOutput = clientIncomeSeriveImpl.getCLientFamilyAllIncomes(clientId, mode, fpFlag, session);
			log.info("ClientIncomeController <<< Exiting getClientIncomeInfo() ");
			return new ResponseEntity<ClientFamilyIncomeOutput>(incomeOutput, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed to get Family Income List , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

	@PreAuthorize("hasRole('BudgetManagementView')")
	@RequestMapping(value = "/getIncomeDetailOfFamilyMember", method = RequestMethod.GET, headers = "Accept=application/json")
	public Object getIncomeDetailOfFamilyMember(@RequestParam(value = "memberId") int memberId) {
		Session session = null;
		try {
			ClientIncomeService clientIncomeSeriveImpl = new ClientIncomeServiceImpl();
			session = sessionfactory.openSession();
			List<FamilyMemberIncomeDetailOutput> familyincomeOutputList = clientIncomeSeriveImpl
					.getIncomeOfFamilyMember(memberId, session);
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
	@PreAuthorize("hasRole('BudgetManagementView')")
	@RequestMapping(value = "/download", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadExcelOutputExl(HttpServletResponse response,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "mode") String mode,
			@RequestParam(value = "fpFlag") int fpFlag) {
		log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		try {
			ClientIncomeService clientIncomeSeriveImpl = new ClientIncomeServiceImpl();
			session = sessionfactory.openSession();
			ClientFamilyIncomeOutput incomeOutput = clientIncomeSeriveImpl.getCLientFamilyAllIncomes(clientId, mode,
					fpFlag, session);
			ClassLoader loader = getClass().getClassLoader();
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}

			workbook = ExcelUtility.writeExcelOutputData(file, incomeOutput.getClientFamilyList());
			
						
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		log.info("ClientIncomeController <<< Exiting downloadExcelOutputExl() ");
		return returner;
	}

}