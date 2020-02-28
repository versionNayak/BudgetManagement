package com.finlabs.finexa.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.ClientFamilyLoanOutput;
import com.finlabs.finexa.dto.ClientLoanProjectionOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.repository.ClientFamilyLoanDAO;
import com.finlabs.finexa.repository.impl.ClientFamilyLoanDAOImpl;
import com.finlabs.finexa.resources.model.SimpleLoanCalLookup;
import com.finlabs.finexa.resources.model.SimpleLoanCalculator;
import com.finlabs.finexa.resources.service.SimpleLoanCalEMIBasedService;
import com.finlabs.finexa.resources.service.SimpleLoanCalNonEMIBasedService;
import com.finlabs.finexa.service.ClientLoanService;

//@Transactional
//@Service
public class ClientLoanServiceImpl implements ClientLoanService {
	// @Autowired
	// private ClientFamilyLoanDAO clientFamilyLoanDao;
	private SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

	private String monthArray[] = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov",
			"Dec" };

	@Override
	public List<ClientFamilyLoanOutput> getCLientFamilyAllLoans(int clientId, int fpFlag, Session session)
			throws FinexaBussinessException {
		List<ClientLoan> clientFamilyLoanList = new ArrayList<>();
		List<ClientFamilyLoanOutput> clientFamilyLoanOuputList = new ArrayList<>();
		ClientFamilyLoanDAO clientFamilyLoanDao = new ClientFamilyLoanDAOImpl();
		try {
			clientFamilyLoanList = clientFamilyLoanDao.getLoanClientAllDetailsById(clientId, session);
			if (!clientFamilyLoanList.isEmpty()) {
				for (ClientLoan clLoan : clientFamilyLoanList) {
					ClientFamilyLoanOutput output = new ClientFamilyLoanOutput();
					output.setLoanId(clLoan.getId());
					output.setLoanEmiNonEmi(clLoan.getLoanType());
					output.setLoanOriginalFlag(clLoan.getLoanOriginalFlag());
					output.setLoanType(clLoan.getLookupLoanCategory() == null ? "N/A"
							: clLoan.getLookupLoanCategory().getDescription());
					/*output.setLoanProvider(
							clLoan.getMasterLoanProvider() == null ? "N/A" : clLoan.getMasterLoanProvider().getName());*/
					
					output.setEmi(clLoan.getEmiAmount() != null ? round(clLoan.getEmiAmount().doubleValue(), 2) : 0.0);
					output.setInterestPayoutFreq(String.valueOf(clLoan.getInterestPaymentFrequency()));
					output.setInterestRate(round((clLoan.getInterestRate().doubleValue() * 100), 2));
					output.setLoanEndDate(outputDateFormat.format(clLoan.getLoanEndDate()));
					output.setLoanStartDate(outputDateFormat.format(clLoan.getLoanStartDate()));
					/*output.setLoanProviderId(
							clLoan.getMasterLoanProvider() == null ? 0 : clLoan.getMasterLoanProvider().getId());*/
					//new Code
					if(clLoan.getLoanType() == 1) {
					output.setLoanProvider(clLoan.getEmiLoanProviderName() == null ? "": clLoan.getEmiLoanProviderName());
					}else {
					output.setLoanProvider(clLoan.getLoanProviderName() == null ? "": clLoan.getLoanProviderName());	
					}
					
					output.setLoanCategoryId(
							clLoan.getLookupLoanCategory() == null ? 0 : clLoan.getLookupLoanCategory().getId());
					if (clLoan.getLoanOriginalFlag().equals("Y")) {
						output.setOriginalPrincipal(round(clLoan.getLoanAmount().doubleValue(), 2));
					} else {
						output.setOutstandingPrincipal(round(clLoan.getLoanAmount().doubleValue(), 2));
					}
					// for non-emi loan
					if (clLoan.getLoanType() == 2) {
						List<ClientFamilyLoanOutput> clientFamilyLoanOutputList = this
								.getClientLoanStatusDetails(clLoan, "yearly", fpFlag, session);
						if (clientFamilyLoanOutputList != null && !clientFamilyLoanOutputList.isEmpty()) {
							ClientFamilyLoanOutput temp = (this.getClientLoanStatusDetails(clLoan, "yearly", fpFlag,
									session)).get(0);
							if (temp != null) {
								output.setPrincipalPay(temp.getPrincipalPay());
								output.setInterestPay(temp.getInterestPay());
							}
						}
					}
					clientFamilyLoanOuputList.add(output);

				}
			} else {
				return clientFamilyLoanOuputList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FinexaBussinessException businessException = new FinexaBussinessException("ClientLoan", "111",
					"Failed to get  Loan Details , Please try again.", e);
			throw businessException;
		}
		// Sumit Samaddar [This code should go in DAO implementation class]
		/*
		 * finally { genericDao.closeCurrentSession(); }
		 */
		return clientFamilyLoanOuputList;
	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.00");
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		String valueString = df.format(value);
		value = Double.parseDouble(valueString);
		return value;
	}

	// new service impl written to bring out details of all loan provider and
	// category
	@Override
	public List<ClientFamilyLoanOutput> getClientLoanStatusDetails(ClientLoan loan, String mode, int fpFlag,
			Session session) throws FinexaBussinessException {
		List<ClientFamilyLoanOutput> clientFamilyLoanOuputList = new ArrayList<>();
		List<SimpleLoanCalLookup> simpleCalLookupList = new ArrayList<>();
		try {
			int count = 0;
			double yearlyBegBal = 0.0;
			double yearlyInterest = 0.0;
			double yearlyPrinciplePay = 0.0;
			double yearlyEndBal = 0.0;
			double yearlyEMI = 0.0;
			double totalPrinciplePaid = 0.0;
			double totalInterestPaid = 0.0;
			ClientFamilyLoanOutput lastLoanCalResultRow = new ClientFamilyLoanOutput();
			if (loan != null) {
				Calendar planRefDate = Calendar.getInstance();
				// Code Added by Debolina on 1st Jan, 2018 to match business
				// logic because
				// For months Jan, Feb & Apr the calculations are not getting
				// reflected
				/*
				 * if (planRefDate.get(Calendar.MONTH) < 3) { planRefDate.set(Calendar.YEAR,
				 * planRefDate.get(Calendar.YEAR) - 1); } else { planRefDate.set(Calendar.YEAR,
				 * planRefDate.get(Calendar.YEAR)); }
				 */

				// fpflag 1 = pro rata
				if (!loan.getLoanOriginalFlag().equals("Y")) {
					planRefDate.setTime(loan.getLoanStartDate());
					planRefDate.set(Calendar.DAY_OF_MONTH, 1);
				}
				Calendar loanStartDate = Calendar.getInstance();
				loanStartDate.setTime(loan.getLoanStartDate());

				if (fpFlag != 1 && loan.getLoanOriginalFlag().equals("Y")
						&& (loanStartDate.get(Calendar.YEAR) < planRefDate.get(Calendar.YEAR))) {
					if (planRefDate.get(Calendar.MONTH) < 3) {
						planRefDate.set(Calendar.YEAR, planRefDate.get(Calendar.YEAR) - 1);
					}
					planRefDate.set(Calendar.MONTH, 3);
					planRefDate.set(Calendar.MONTH, 3);
                }

				boolean currentLoanStatFlag = false;
				boolean oneTimeFlag = true;
				String fiscalYear = "";
				String lastFiscalyear = "";
				SimpleLoanCalculator loanCal = new SimpleLoanCalculator();
				if (loan.getLoanType() == 1) {
					oneTimeFlag = true;
					int specialCount = 0;
					currentLoanStatFlag = false;
					SimpleLoanCalEMIBasedService calService = new SimpleLoanCalEMIBasedService();

					loanCal = calService.calculateEMIBasedLoanValue(loan.getLoanOriginalFlag(),
							loan.getInterestRate().doubleValue(), loan.getLoanAmount().doubleValue(),
							loan.getLoanStartDate(), loan.getEmiAmount().doubleValue(), loan.getLoanTenure());
					simpleCalLookupList = calService.getEMIBasedLoanList(loanCal.getNumberOfEMI(),
							loan.getLoanStartDate(), loan.getLoanAmount().doubleValue(), loanCal.getEmiAmount(),
							loan.getInterestRate().doubleValue(), loan.getLoanOriginalFlag());
					
					for (SimpleLoanCalLookup lookup : simpleCalLookupList) {
                       System.out.println(" ================ start ==================");
						Calendar refCal = Calendar.getInstance();
						fiscalYear = lookup.getFinancialYear();
						System.out.println("fiscalYear "+fiscalYear);
						System.out.println("lookup.getRefDate() "+lookup.getRefDate());
						refCal.setTime(lookup.getRefDate());
						// System.out.println(refCal.getTime());
						
						System.out.println("refCal.get "+refCal.getTime());
						System.out.println("planRefDate.get "+planRefDate.getTime());

						if ((refCal.get(Calendar.YEAR) == planRefDate.get(Calendar.YEAR)
								&& (refCal.get(Calendar.MONTH) == planRefDate.get(Calendar.MONTH)
										|| refCal.get(Calendar.MONTH) > planRefDate.get(Calendar.MONTH)))
								&& oneTimeFlag) {
							currentLoanStatFlag = true;
							lastFiscalyear = fiscalYear;
							oneTimeFlag = false;
							System.out.println("inside ");
						}
						
						if (currentLoanStatFlag) {

							ClientFamilyLoanOutput loanOutput = new ClientFamilyLoanOutput();

							loanOutput.setProjectionYear(lookup.getFinancialYear());
							double begningBal = lookup.getBegningBal();
							loanOutput.setBegningBal(round(begningBal, 2));
							loanOutput.setInterestPay(round(lookup.getInterestPayment(), 2));
							loanOutput.setPrincipalPay(round(lookup.getPrincipalPayment(), 2));
							loanOutput.setEndBal(round(lookup.getEndingBalance(), 2));
							loanOutput.setEmiAmount(round(lookup.getEmiAmount(), 2));
							System.out.println("1111 emiamount "+loanOutput.getEmiAmount());
							loanOutput.setTotalPrincipalPaid(round(lookup.getTotalPrincipalPaid(), 2));
							loanOutput.setTotalInterestPaid(round(lookup.getTotalInterestPaid(), 2));
							loanOutput.setRefDate(refCal);							
							//new Code
							if(loan.getLoanType() == 1) {
								if(loan.getEmiLoanProviderName() == null) {
									loanOutput.setLoanProvider("");
								}else {
									loanOutput.setLoanProvider(loan.getEmiLoanProviderName());
								}
							}else {
								if(loan.getLoanProviderName() == null) {
									loanOutput.setLoanProvider("");
								}else {
									loanOutput.setLoanProvider(loan.getLoanProviderName());
								}
							}
							/*loanOutput.setLoanProviderId(
							loan.getMasterLoanProvider() == null ? 0 : loan.getMasterLoanProvider().getId());*/
							
							loanOutput.setLoanCategoryId(loan.getLookupLoanCategory().getId());
							loanOutput.setLoanEmiNonEmi(1);
							if (mode.equals("yearly")) {
								System.out.println("fiscalYear "+fiscalYear);
								System.out.println("lastFiscalyear "+lastFiscalyear);
								if (!fiscalYear.equals(lastFiscalyear)) {
									loanOutput.setProjectionYear(lastFiscalyear);
									loanOutput.setBegningBal(round(yearlyBegBal, 2));
									loanOutput.setInterestPay(round(yearlyInterest, 2));
									loanOutput.setPrincipalPay(round(yearlyPrinciplePay, 2));
									yearlyEndBal = yearlyBegBal - yearlyPrinciplePay;
									loanOutput.setEndBal(round(yearlyEndBal, 2));
									loanOutput.setEmiAmount(round(yearlyEMI, 2));
									System.out.println("2222 emiamount "+loanOutput.getEmiAmount());
									totalPrinciplePaid = totalPrinciplePaid + yearlyPrinciplePay;
									totalInterestPaid = totalInterestPaid + yearlyInterest;
									loanOutput.setTotalPrincipalPaid(round(totalPrinciplePaid, 2));
									loanOutput.setTotalInterestPaid(round(totalInterestPaid, 2));
									loanOutput.setLoanEmiNonEmi(1);
									clientFamilyLoanOuputList.add(loanOutput);
									yearlyBegBal = 0.0;
									yearlyInterest = 0.0;
									yearlyPrinciplePay = 0.0;
									yearlyEndBal = 0.0;
									yearlyEMI = 0.0;
									// totalPrinciplePaid = 0.0;
									// totalInterestPaid = 0.0;
									count = 0;
									if (count == 0) {
										yearlyBegBal = lookup.getBegningBal();
									}
									count++;
									lastLoanCalResultRow.setBegningBal(round(yearlyBegBal, 2));
									yearlyInterest = yearlyInterest + lookup.getInterestPayment();
									yearlyPrinciplePay = yearlyPrinciplePay + lookup.getPrincipalPayment();
									// yearlyEndBal = yearlyBegBal -
									// yearlyPrinciplePay;
									yearlyEMI = yearlyEMI + lookup.getEmiAmount();
								} else {
									if (count == 0) {
										yearlyBegBal = lookup.getBegningBal();
									}
									lastLoanCalResultRow.setBegningBal(round(yearlyBegBal, 2));
									yearlyInterest = yearlyInterest + lookup.getInterestPayment();
									yearlyPrinciplePay = yearlyPrinciplePay + lookup.getPrincipalPayment();
									// yearlyEndBal = yearlyBegBal -
									// yearlyPrinciplePay;
									yearlyEMI = yearlyEMI + lookup.getEmiAmount();

									count++;
								}
								if (specialCount == simpleCalLookupList.size() - 1) {
									lastFiscalyear = fiscalYear;
									ClientFamilyLoanOutput lastloanOutput = new ClientFamilyLoanOutput();
									lastloanOutput.setProjectionYear(lastFiscalyear);
									lastloanOutput.setBegningBal(round(yearlyBegBal, 2));
									lastloanOutput.setInterestPay(round(yearlyInterest, 2));
									lastloanOutput.setPrincipalPay(round(yearlyPrinciplePay, 2));
									yearlyEndBal = yearlyBegBal - yearlyPrinciplePay;
									lastloanOutput.setEndBal(round(yearlyEndBal, 2));
									lastloanOutput.setEmiAmount(round(yearlyEMI, 2));
									System.out.println("3333 emiamount "+loanOutput.getEmiAmount());
									totalPrinciplePaid = totalPrinciplePaid + yearlyPrinciplePay;
									totalInterestPaid = totalInterestPaid + yearlyInterest;
									lastloanOutput.setTotalPrincipalPaid(round(totalPrinciplePaid, 2));
									lastloanOutput.setTotalInterestPaid(round(totalInterestPaid, 2));
									lastloanOutput.setLoanEmiNonEmi(1);
									clientFamilyLoanOuputList.add(lastloanOutput);
								}
							} else {
								loanOutput.setLoanEmiNonEmi(1);
								loanOutput.setProjectionYear(
										monthArray[refCal.get(Calendar.MONTH)] + "," + lookup.getFinancialYear());
								clientFamilyLoanOuputList.add(loanOutput);
							}

						}
						lastFiscalyear = fiscalYear;
						specialCount++;
						 System.out.println(" ================ end ==================");
					}

				} else {
					int specialCount = 0;
					currentLoanStatFlag = false;
					oneTimeFlag = true;
					SimpleLoanCalNonEMIBasedService calNonEmiService = new SimpleLoanCalNonEMIBasedService();
					loanCal.setLoanStartDate(loan.getLoanStartDate());
					// for product calculator tenure will be in months.
					loanCal.setLoanTenure(loan.getLoanTenure());
					loanCal.setInterestPaymentFrequency(loan.getInterestPaymentFrequency());
					loanCal.setInterestRate(round(loan.getInterestRate().doubleValue(), 2));
					loanCal.setLoanAmount(round(loan.getLoanAmount().doubleValue(), 2));
					loanCal = calNonEmiService.calculateNonEmiDetails(loanCal);
					simpleCalLookupList = calNonEmiService.getNonEMIDetails(loanCal.getLoanAmount(),
							loan.getInterestPaymentFrequency(), loanCal.getLoanStartDate(), loanCal.getLoanEndDate(),
							loanCal.getEmiAmount(), loanCal.getLoanTenure());

					for (SimpleLoanCalLookup lookup : simpleCalLookupList) {
						Calendar refCal = Calendar.getInstance();
						fiscalYear = lookup.getFinancialYear();
						refCal.setTime(lookup.getRefDate());

						if (refCal.get(Calendar.YEAR) == planRefDate.get(Calendar.YEAR)
								&& (refCal.get(Calendar.MONTH) == planRefDate.get(Calendar.MONTH)
										|| refCal.get(Calendar.MONTH) > planRefDate.get(Calendar.MONTH))
								&& oneTimeFlag) {
							currentLoanStatFlag = true;
							lastFiscalyear = fiscalYear;
							oneTimeFlag = false;
						}
						if (currentLoanStatFlag) {
							ClientFamilyLoanOutput loanOutput = new ClientFamilyLoanOutput();

							loanOutput.setProjectionYear(lookup.getFinancialYear());
							loanOutput.setBegningBal(round(lookup.getBegningBal(), 2));
							loanOutput.setInterestPay(round(lookup.getInterestPayment(), 2));
							loanOutput.setPrincipalPay(round(lookup.getPrincipalPayment(), 2));
							loanOutput.setEndBal(round(lookup.getEndingBalance(), 2));
							// loanOutput.setEmiAmount(round(lookup.getEmiAmount(),
							// 2));
							loanOutput.setTotalPrincipalPaid(round(lookup.getTotalPrincipalPaid(), 2));
							loanOutput.setTotalInterestPaid(round(lookup.getTotalInterestPaid(), 2));
							loanOutput.setRefDate(refCal);
							/*loanOutput.setLoanProviderId(
									loan.getMasterLoanProvider() == null ? 0 : loan.getMasterLoanProvider().getId());*/
							if(loan.getLoanType() == 1) {
								loanOutput.setLoanProvider(
										loan.getEmiLoanProviderName() == null ? "" : loan.getEmiLoanProviderName());
								}else {
								loanOutput.setLoanProvider(
										loan.getLoanProviderName() == null ? "" : loan.getLoanProviderName());	
							     }
							loanOutput.setLoanCategoryId(loan.getLookupLoanCategory().getId());
							loanOutput.setLoanEmiNonEmi(2);
							if (mode.equals("yearly")) {
								if (!fiscalYear.equals(lastFiscalyear)) {
									loanOutput.setProjectionYear(lastFiscalyear);
									loanOutput.setBegningBal(round(yearlyBegBal, 2));
									loanOutput.setInterestPay(round(yearlyInterest, 2));
									loanOutput.setPrincipalPay(round(yearlyPrinciplePay, 2));
									yearlyEndBal = yearlyBegBal - yearlyPrinciplePay;
									loanOutput.setEndBal(round(yearlyEndBal, 2));
									// loanOutput.setEmiAmount(round(yearlyEMI,
									// 2));
									totalPrinciplePaid = totalPrinciplePaid + yearlyPrinciplePay;
									totalInterestPaid = totalInterestPaid + yearlyInterest;
									loanOutput.setTotalPrincipalPaid(round(totalPrinciplePaid, 2));
									loanOutput.setTotalInterestPaid(round(totalInterestPaid, 2));
									loanOutput.setLoanEmiNonEmi(2);// for NonEMI
									clientFamilyLoanOuputList.add(loanOutput);
									yearlyBegBal = 0.0;
									yearlyInterest = 0.0;
									yearlyPrinciplePay = 0.0;
									yearlyEndBal = 0.0;
									yearlyEMI = 0.0;
									// totalPrinciplePaid = 0.0;
									// totalInterestPaid = 0.0;
									count = 0;
									if (count == 0) {
										yearlyBegBal = lookup.getBegningBal();
									}
									count++;
									lastLoanCalResultRow.setBegningBal(round(yearlyBegBal, 2));
									yearlyInterest = yearlyInterest + lookup.getInterestPayment();
									yearlyPrinciplePay = yearlyPrinciplePay + lookup.getPrincipalPayment();
									// yearlyEndBal = yearlyBegBal -
									// yearlyPrinciplePay;
									// yearlyEMI = yearlyEMI +
									// lookup.getPrincipalPayment() +
									// lookup.getInterestPayment();
								} else {
									if (count == 0) {
										yearlyBegBal = lookup.getBegningBal();
									}
									lastLoanCalResultRow.setBegningBal(round(yearlyBegBal, 2));
									yearlyInterest = yearlyInterest + lookup.getInterestPayment();
									yearlyPrinciplePay = yearlyPrinciplePay + lookup.getPrincipalPayment();
									// yearlyEndBal = yearlyBegBal -
									// yearlyPrinciplePay;
									// yearlyEMI = yearlyEMI +
									// lookup.getPrincipalPayment() +
									// lookup.getInterestPayment();

									count++;
								}
								if (specialCount == simpleCalLookupList.size() - 1) {
									ClientFamilyLoanOutput lastloanOutput = new ClientFamilyLoanOutput();
									lastloanOutput.setProjectionYear(fiscalYear);
									lastloanOutput.setBegningBal(round(yearlyBegBal, 2));
									lastloanOutput.setInterestPay(round(yearlyInterest, 2));
									lastloanOutput.setPrincipalPay(round(yearlyPrinciplePay, 2));
									yearlyEndBal = yearlyBegBal - yearlyPrinciplePay;
									lastloanOutput.setEndBal(Math.round(yearlyEndBal));
									// loanOutput.setEmiAmount(round(yearlyEMI,
									// 2));
									totalPrinciplePaid = totalPrinciplePaid + yearlyPrinciplePay;
									totalInterestPaid = totalInterestPaid + yearlyInterest;
									lastloanOutput.setTotalPrincipalPaid(round(totalPrinciplePaid, 2));
									lastloanOutput.setTotalInterestPaid(round(totalInterestPaid, 2));
									lastloanOutput.setLoanEmiNonEmi(2);// for
																		// NonEMI
									clientFamilyLoanOuputList.add(lastloanOutput);
								}
							} else {
								loanOutput.setProjectionYear(
										monthArray[refCal.get(Calendar.MONTH)] + "," + lookup.getFinancialYear());
								loanOutput.setLoanEmiNonEmi(2);// for NonEMI
								clientFamilyLoanOuputList.add(loanOutput);
							}

						}
						lastFiscalyear = fiscalYear;
						specialCount++;
					}
				}

			} else {
				FinexaBussinessException businessException = new FinexaBussinessException("ClientLoan", "111",
						"Details not available for seleted loan");
				throw businessException;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientFamilyLoanOuputList;
	}

	@Override
	public List<ClientFamilyLoanOutput> getClientAllLoanStatusDetails(int clientId, String mode, int fpFlag,
			Session session) throws FinexaBussinessException {

		List<ClientFamilyLoanOutput> clientFamilyLoanOuputList = new ArrayList<>();

		List<ClientFamilyLoanOutput> tempList = new ArrayList<>();
		ClientFamilyLoanDAO clientFamilyLoanDao = new ClientFamilyLoanDAOImpl();
		try {
			List<ClientLoan> loanList = clientFamilyLoanDao.getLoanClientAllDetailsById(clientId, session);

			for (ClientLoan loan : loanList) {
				List<ClientFamilyLoanOutput> tempOutputList = new ArrayList<>();
				// String providerId = "" +
				// loan.getMasterLoanProvider().getId();
				tempOutputList = this.getClientLoanStatusDetails(loan, mode, fpFlag, session);

				if (clientFamilyLoanOuputList.size() > 0) {

					if (tempOutputList.size() > clientFamilyLoanOuputList.size()) {
						tempList = clientFamilyLoanOuputList;
						clientFamilyLoanOuputList = tempOutputList;
						tempOutputList = tempList;
					}
					for (ClientFamilyLoanOutput loanoutput : clientFamilyLoanOuputList) {
						for (ClientFamilyLoanOutput tempLoanOutput : tempOutputList) {
							if (loanoutput.getProjectionYear().equals(tempLoanOutput.getProjectionYear())) {
								loanoutput.setBegningBal(
										round((loanoutput.getBegningBal() + tempLoanOutput.getBegningBal()), 2));
								loanoutput.setInterestPay(
										round((loanoutput.getInterestPay() + tempLoanOutput.getInterestPay()), 2));
								loanoutput.setPrincipalPay(
										round((loanoutput.getPrincipalPay() + tempLoanOutput.getPrincipalPay()), 2));
								loanoutput.setEndBal(round((loanoutput.getEndBal() + tempLoanOutput.getEndBal()), 2));
								loanoutput.setEmiAmount(
										round((loanoutput.getEmiAmount() + tempLoanOutput.getEmiAmount()), 2));

							}
							// System.out.println(i);
							// i++;
						}
					}

				} else {
					clientFamilyLoanOuputList.addAll(tempOutputList);
				}
			}

		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientFamilyLoanOuputList;
	}

	@Override
	public List<ClientLoanProjectionOutput> getClientLoanProjectionList(int clientId, String mode, int fpFlag,
			Session session) throws FinexaBussinessException {
		List<ClientLoanProjectionOutput> clientLoanProjectionOutputList = new ArrayList<>();
		ClientFamilyLoanDAO clientFamilyLoanDao = new ClientFamilyLoanDAOImpl();
		try {
			List<ClientLoan> loanList = clientFamilyLoanDao.getLoanClientAllDetailsById(clientId, session);

			if (loanList != null && !loanList.isEmpty()) {

				for (ClientLoan loan : loanList) {
					// String providerId = "" +
					// loan.getMasterLoanProvider().getId();
					List<ClientFamilyLoanOutput> clientFamilyLoanOuputList = getClientLoanStatusDetails(loan, mode,
							fpFlag, session);

					ClientLoanProjectionOutput projectionOutput = new ClientLoanProjectionOutput();
					//Due to Loan Database Change
					/*projectionOutput.setProviderName(
							loan.getMasterLoanProvider() == null ? "" : loan.getMasterLoanProvider().getName());*/
					//new Code
					if(loan.getLoanType() == 1) {
					projectionOutput.setProviderName(
							loan.getEmiLoanProviderName() == null ? "" : loan.getEmiLoanProviderName());
					}else {
					projectionOutput.setProviderName(
							loan.getLoanProviderName() == null ? "" : loan.getLoanProviderName());	
					}
					projectionOutput.setCategoryName(loan.getLookupLoanCategory().getDescription());
					projectionOutput.setCategoryId(loan.getLookupLoanCategory().getId());
					/*projectionOutput.setProviderId(
							loan.getMasterLoanProvider() == null ? "" : "" + loan.getMasterLoanProvider().getId());*/
					projectionOutput.setRelation(loan.getClientFamilyMember().getLookupRelation().getDescription());
					projectionOutput.setClientFamilyLoanOutputList(clientFamilyLoanOuputList);
					clientLoanProjectionOutputList.add(projectionOutput);

				}
			}

		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientLoanProjectionOutputList;
	}

	@Override
	public List<ClientLoanProjectionOutput> getMemberLoanProjectionListForFamilyMember(int memberId, String mode,
			int fpFlag, Session session) throws FinexaBussinessException {
		List<ClientLoanProjectionOutput> clientLoanProjectionOutputList = new ArrayList<>();
		ClientFamilyLoanDAO clientFamilyLoanDao = new ClientFamilyLoanDAOImpl();
		try {
			List<ClientLoan> loanList = clientFamilyLoanDao.getLoanClientAllDetailsByMemberId(memberId, session);

			if (loanList != null && !loanList.isEmpty()) {

				for (ClientLoan loan : loanList) {
					// String providerId = "" +
					// loan.getMasterLoanProvider().getId();
					List<ClientFamilyLoanOutput> clientFamilyLoanOuputList = getClientLoanStatusDetails(loan, mode,
							fpFlag, session);

					ClientLoanProjectionOutput projectionOutput = new ClientLoanProjectionOutput();
					//new Database Change in Loan
					/*projectionOutput.setProviderName(
							loan.getMasterLoanProvider() == null ? "" : loan.getMasterLoanProvider().getName());*/
					projectionOutput.setProviderName(
							loan.getEmiLoanProviderName() == null ? "" : loan.getEmiLoanProviderName());
					projectionOutput.setCategoryName(loan.getLookupLoanCategory().getDescription());
					projectionOutput.setCategoryId(loan.getLookupLoanCategory().getId());
					/*projectionOutput.setProviderId(
							loan.getMasterLoanProvider() == null ? "" : "" + loan.getMasterLoanProvider().getId());*/
					projectionOutput.setClientFamilyLoanOutputList(clientFamilyLoanOuputList);
					clientLoanProjectionOutputList.add(projectionOutput);

				}
			}

		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientLoanProjectionOutputList;
	}
}