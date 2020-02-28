package com.finlabs.finexa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.finlabs.finexa.dto.AnnualExpensesDetailed;
import com.finlabs.finexa.dto.ClientFamilyIncomeOutput;
import com.finlabs.finexa.dto.ClientFamilyLoanOutput;
import com.finlabs.finexa.dto.ClientFamilyNetSurplusOutput;
import com.finlabs.finexa.dto.CommittedOutFlowOutput;

public class ExcelUtility {
	public static XSSFWorkbook writeExcelOutputData(File file, List<ClientFamilyIncomeOutput> clientFamilyList)
			throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue("Year");
		headRow.createCell(1).setCellValue("Salary,Bonus & Professional Income");
		headRow.createCell(2).setCellValue("Business Income");
		headRow.createCell(3).setCellValue("Rental Income");
		headRow.createCell(4).setCellValue("Pension");
		headRow.createCell(5).setCellValue("Other Income");
		headRow.createCell(6).setCellValue("Interest Income");
		//
		headRow.createCell(7).setCellValue("Total Individual Income");
		//
		headRow.createCell(8).setCellValue("Total Income");
		
		int rownum = 2;
		for (ClientFamilyIncomeOutput output : clientFamilyList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getYear());
			
			row.createCell(1).setCellValue(output.getSalaryIncome());
			
			row.createCell(2).setCellValue(output.getBussinessIncome());

			row.createCell(3).setCellValue(output.getRentalIncome());
			
			row.createCell(4).setCellValue(output.getPension());
			
			/*if(output.getOtherIncome() != 0) {*/
			
			row.createCell(5).setCellValue(output.getOtherIncome());
			/*}else {
			row.createCell(5).setCellValue("N/A");	
			}*/
			
			
			row.createCell(6).setCellValue(output.getInterestIncome());
		
			//
			row.createCell(7).setCellValue(output.getIndividualTotalIncome());
		
			//
			row.createCell(8).setCellValue(output.getTotalIncome());
		
			rownum++;
		}

		fis.close();
		return XLSXFilter(workbook,sheet);
	}

	public static XSSFWorkbook writeExcelOutputLoanData(File file, List<ClientFamilyLoanOutput> clientFamilyLoanList)
			throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Beginning Balance");
		headRow.createCell(2).setCellValue(" Interest Payment");
		headRow.createCell(3).setCellValue(" Principal Payment");
		headRow.createCell(4).setCellValue(" Ending Balance");
		headRow.createCell(5).setCellValue(" EMI Amount");
		// headRow.createCell(6).setCellValue(" Total Principal Paid to Date");
		// headRow.createCell(7).setCellValue(" Total Interest Paid to Date");
		int rownum = 2;
		for (ClientFamilyLoanOutput output : clientFamilyLoanList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getProjectionYear());
			row.createCell(1).setCellValue(output.getBegningBal());
			row.createCell(2).setCellValue(output.getInterestPay());
			row.createCell(3).setCellValue(output.getPrincipalPay());
			row.createCell(4).setCellValue(output.getEndBal());
			row.createCell(5).setCellValue(output.getEmiAmount());
			// row.createCell(6).setCellValue(output.getTotalPrincipalPaid());
			// row.createCell(7).setCellValue(output.getTotalInterestPaid());
			rownum++;
		}

		fis.close();
		return XLSXFilter(workbook,sheet);
	}

	public static XSSFWorkbook writeExcelOutputExpenseData(File file,
			List<AnnualExpensesDetailed> clientAnnualExpenseDetailsList) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Living Expenses");
		headRow.createCell(2).setCellValue(" Discretionary Expenses");
		headRow.createCell(3).setCellValue(" Total Expenses");
		int rownum = 2;
		for (AnnualExpensesDetailed output : clientAnnualExpenseDetailsList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getFinYear());
			row.createCell(1).setCellValue(output.getLivingExpense());
			row.createCell(2).setCellValue(output.getDiscretionaryExpense());
			row.createCell(3).setCellValue(output.getTotalExpense());
			rownum++;
		}

		fis.close();
		return XLSXFilter(workbook,sheet);
	}

	public static XSSFWorkbook writeExcelOutputExpenseDataDetailed(File file,
			List<AnnualExpensesDetailed> clientAnnualExpenseDetailsList) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Groceries");
		headRow.createCell(2).setCellValue(" Utilities (Electricity, Gas etc.)");
		headRow.createCell(3).setCellValue(" Transport (Petrol, Driver, Vehicle maintenance, Local conveyance etc.)");
		headRow.createCell(4).setCellValue(" Household & Personal Care");
		headRow.createCell(5).setCellValue(" Housing & Maintenance (Rent, Flat maintenance, Household maid etc.)");
		headRow.createCell(6).setCellValue(" Communication (Mobile, Telephone, Internet etc.)");
		headRow.createCell(7).setCellValue(" Lifestyle & Entertainment (Dining out, Movies, Vacation, Cable etc.)");
		headRow.createCell(8).setCellValue(" Apparels & Accessories");
		headRow.createCell(9).setCellValue(" Children Fees (School, tution, books etc.)");
		headRow.createCell(10).setCellValue(" Healthcare Expenses (Doctor fees, medicines, path lab fees etc.)");
		headRow.createCell(11).setCellValue(" Others (Charity, etc)");
		headRow.createCell(12).setCellValue(" Living Expenses");
		headRow.createCell(14).setCellValue(" Total Expenses");
		int rownum = 2;
		for (AnnualExpensesDetailed output : clientAnnualExpenseDetailsList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getFinYear());
			row.createCell(1).setCellValue(output.getGroceries_amt());
			row.createCell(2).setCellValue(output.getUtilities_amt());
			row.createCell(3).setCellValue(output.getTransport_amt());
			row.createCell(4).setCellValue(output.getHouseHoldPersonal_amt());
			row.createCell(5).setCellValue(output.getHousing_amt());
			row.createCell(6).setCellValue(output.getCommunication_amt());
			row.createCell(7).setCellValue(output.getLifeStyle_amt());
			row.createCell(8).setCellValue(output.getApparels_amt());
			row.createCell(9).setCellValue(output.getChildrenFees_amt());
			row.createCell(10).setCellValue(output.getHealthCare_amt());
			row.createCell(11).setCellValue(output.getOthers_amt());
			row.createCell(12).setCellValue(output.getTotalFamilyExpense());
			row.createCell(14).setCellValue(output.getTotalExpense());
			rownum++;
		}

		fis.close();
		return XLSXFilter(workbook, sheet);
	}

	public static XSSFWorkbook writeExcelOutputCommitedOutFlowData(File file,
			List<CommittedOutFlowOutput> commitedOutFlowList) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Life Insurance Premium ");
		headRow.createCell(2).setCellValue(" Health Insurance Premium");
		headRow.createCell(3).setCellValue(" General Insurance Premium");
		headRow.createCell(4).setCellValue(" Investments");
		headRow.createCell(5).setCellValue(" Total");

		int rownum = 2;
		for (CommittedOutFlowOutput output : commitedOutFlowList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getProjectionYear());
			row.createCell(1).setCellValue(output.getPremiumAMount());
			row.createCell(2).setCellValue(output.getPremiumAmountHealth());
			row.createCell(3).setCellValue(output.getPremiumAmountGeneral());
			row.createCell(4).setCellValue(output.getInvestmentAmount());
			row.createCell(5).setCellValue(output.getTotalOutFlow());
			rownum++;
		}

		fis.close();
		return XLSXFilter(workbook,sheet);
	}

	public static XSSFWorkbook writeExcelNetSurplusOutputData(File file,
			List<ClientFamilyNetSurplusOutput> clientFamilyNetSurplusOutput) throws IOException {

		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue("Year");
		headRow.createCell(1).setCellValue("Income");
		headRow.createCell(2).setCellValue("Expense");
		headRow.createCell(3).setCellValue("Committed Outflows");
		headRow.createCell(4).setCellValue("Loan Outflow");
		headRow.createCell(5).setCellValue("Net Surplus");
		int rownum = 2;
		for (ClientFamilyNetSurplusOutput output : clientFamilyNetSurplusOutput) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getFinYear());
			row.createCell(1).setCellValue(output.getIncome());
			row.createCell(2).setCellValue(output.getExpense());
			row.createCell(3).setCellValue(output.getCommitted_outflows());
			row.createCell(4).setCellValue(output.getLoan_outflows());
			row.createCell(5).setCellValue(output.getNet_surplus());
			rownum++;
		}

		fis.close();
		return XLSXFilter(workbook,sheet);
	}

	public static XSSFWorkbook XLSXFilter(XSSFWorkbook workbook, Sheet sheet){
        sheet = workbook.getSheetAt(0);
        boolean isNull = false;
        int noOfCol = sheet.getRow(1).getPhysicalNumberOfCells();
        for (int i = 0; i<noOfCol; i++){
            int counter=0;
            for (Row row : sheet) {
                if(counter>1) {
                    Cell cell = row.getCell(i);
                    try {
                        if (cell != null) {
                            try {
                                if (!cell.getStringCellValue().equals("0")) {
                                    isNull = false;
                                    break;
                                } else {
                                    isNull = true;
                                }
                            } catch (Exception e) {
                                if (cell.getNumericCellValue() != 0) {
                                    isNull = false;
                                    break;
                                } else {
                                    isNull = true;
                                }
                            }
                        }
                        if (isNull)
                            sheet.setColumnHidden(i, true);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                counter++;
            }
        }

        return workbook;
    }
}
