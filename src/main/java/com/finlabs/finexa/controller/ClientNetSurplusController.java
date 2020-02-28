package com.finlabs.finexa.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.finlabs.finexa.dto.ClientFamilyNetSurplusOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.service.ClientNetSurplusService;
import com.finlabs.finexa.service.impl.ClientNetSurplusServiceImpl;
import com.finlabs.finexa.util.ExcelUtility;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientNetSurplusController {
	// @Autowired
	// private ClientNetSurplusService clientNetSurplusService;
	@Autowired
	private SessionFactory sessionfactory;
	Logger log = Logger.getLogger(ClientNetSurplusController.class);
	@Autowired
	private CacheInfoService cacheInfoService;

	@PreAuthorize("hasAnyRole('BudgetManagementView','FinancialPlanningView')")
	//@PreAuthorize("Admin")
	@RequestMapping(value = "/getClientNetSurplusInfo", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientNetSurplusInfo(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "mode") String mode, @RequestParam(value = "fpFlag") int fpFlag, HttpServletRequest request) {
		log.info("ClientNetSurplusController >>> Entering getClientNetSurplusInfo() ");
		List<ClientFamilyNetSurplusOutput> clientFamilyNetSurplusOutput = null;
		Session session = null;
		String header = "",token = "";
		ClientNetSurplusService clientNetSurplusService = null;
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			clientNetSurplusService = new ClientNetSurplusServiceImpl();
			clientFamilyNetSurplusOutput = clientNetSurplusService.getClientNetSurplusInfo(token, clientId, mode, fpFlag, session, cacheInfoService);

			return new ResponseEntity<List<ClientFamilyNetSurplusOutput>>(clientFamilyNetSurplusOutput, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getMessage(), HttpStatus.OK);
		} catch (Exception exp) {
			log.info("ClientNetSurplusController <<< Exiting getClientNetSurplusInfo() ");
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
	}
	

	@PreAuthorize("hasAnyRole('BudgetManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/downloadNetSurplus", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadExcelOutputExl(HttpServletResponse response,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "mode") String mode,
			@RequestParam(value = "fpFlag") int fpFlag,HttpServletRequest request) {
		log.info("ClientNetSurplusController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		String requestHeader = "",token = "";
		ClientNetSurplusService clientNetSurplusService = new ClientNetSurplusServiceImpl();
		try {
			session = sessionfactory.openSession();
			requestHeader = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(requestHeader);
			List<ClientFamilyNetSurplusOutput> clientFamilyNetSurplusOutput = clientNetSurplusService
					.getClientNetSurplusInfo(token, clientId, mode, fpFlag, session, cacheInfoService);
			ClassLoader loader = getClass().getClassLoader();
			File file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			workbook = ExcelUtility.writeExcelNetSurplusOutputData(file, clientFamilyNetSurplusOutput);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientNetSurplus", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		log.info("ClientNetSurplusController <<< Exiting downloadExcelOutputExl() ");
		return returner;
	}
}
