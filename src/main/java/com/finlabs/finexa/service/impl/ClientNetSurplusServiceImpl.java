package com.finlabs.finexa.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import com.finlabs.finexa.dto.AnnualExpensesDetailed;
import com.finlabs.finexa.dto.ClientFamilyIncomeOutput;
import com.finlabs.finexa.dto.ClientFamilyLoanOutput;
import com.finlabs.finexa.dto.ClientFamilyNetSurplusOutput;
import com.finlabs.finexa.dto.CommittedOutFlowOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.repository.ClientFamilyExpenseDAO;
import com.finlabs.finexa.repository.ClientFamilyLoanDAO;
import com.finlabs.finexa.repository.impl.ClientFamilyExpenseDAOImpl;
import com.finlabs.finexa.repository.impl.ClientFamilyLoanDAOImpl;
import com.finlabs.finexa.service.ClientExpenseService;
import com.finlabs.finexa.service.ClientIncomeService;
import com.finlabs.finexa.service.ClientLoanService;
import com.finlabs.finexa.service.ClientNetSurplusService;
import com.finlabs.finexa.util.FinexaConstant;

//@Transactional
//@Service
public class ClientNetSurplusServiceImpl implements ClientNetSurplusService {

	// @Autowired
	// private ClientIncomeService clientIncomeSerive;

	// @Autowired
	// private ClientExpenseService clientExpenseSerive;

	// @Autowired
	// private ClientFamilyLoanDAO clientFamilyLoanDao;

	// @Autowired
	// private ClientFamilyExpenseDAO clientFamilyExpenseDAO;

	// @Autowired
	// private ClientLoanService clientLoanService;

	String monthArray[] = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };
	/*	@Autowired
	private CacheInfoService cacheInfoService;*/
	private Hashtable<Integer, Double> yearWiseNetsurplus = new Hashtable<Integer, Double>();

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFamilyNetSurplusOutput> getClientNetSurplusInfo(String token, int clientId, String mode, int fpFlag,
			Session session,CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<ClientFamilyNetSurplusOutput> clientFamilyNetSurplusOutputList = null;
		ClientFamilyIncomeOutput incomeOutput = null;
		AnnualExpensesDetailed expenseOutput = null;
		CommittedOutFlowOutput clientCommittedOutFlowOuput = null;
		ClientFamilyMember clientFamilyMember = null;
		ArrayList<Double> loanOutflowList = null;
		int lifeExp = 0;
		ClientIncomeService clientIncomeSerive = new ClientIncomeServiceImpl();
		ClientExpenseService clientExpenseSerive = new ClientExpenseServiceImpl();
		ClientFamilyExpenseDAO clientFamilyExpenseDAO = new ClientFamilyExpenseDAOImpl();
		// 0 financial year and 1 pro rata
		if (fpFlag == 0 && !mode.equals("monthly")) {
			clientFamilyNetSurplusOutputList = (List<ClientFamilyNetSurplusOutput>) cacheInfoService.getCacheList(
					FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
					FinexaConstant.CALCULATION_SUB_TYPE_NETSURPLUS_CONSTANT);
		} else {
			if (fpFlag == 1) {
				clientFamilyNetSurplusOutputList = (List<ClientFamilyNetSurplusOutput>) cacheInfoService.getCacheList(
						FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
						FinexaConstant.CALCULATION_SUB_TYPE_NETSURPLUS_CONSTANT_PRO_RATA);
			}
		}

		if (clientFamilyNetSurplusOutputList == null || clientFamilyNetSurplusOutputList.isEmpty()) {
			clientFamilyNetSurplusOutputList = new ArrayList<>();
			try {
				clientFamilyMember = clientFamilyExpenseDAO.getClientFromFamilyMemberById(clientId, session);
				lifeExp = clientFamilyMember.getLifeExpectancy();
				// Fetching Life Expectancy of Client
			} catch (Exception e) {
				e.printStackTrace();
			}
			Calendar maxDate = Calendar.getInstance();
			Calendar tempDate = Calendar.getInstance();
			Calendar cInterimDate = Calendar.getInstance();
			Date planRefDate = new Date();// Getting current date
			cInterimDate.setTime(planRefDate);
			cInterimDate.set(Calendar.MILLISECOND, 0);
			cInterimDate.set(Calendar.SECOND, 0);
			cInterimDate.set(Calendar.MINUTE, 0);
			cInterimDate.set(Calendar.HOUR, 0);

			if (lifeExp > 0) {
				tempDate.setTime(clientFamilyMember.getBirthDate());
				if (clientFamilyMember.getLifeExpectancy() != null) {
					tempDate.add(Calendar.MONTH, (int) (clientFamilyMember.getLifeExpectancy() * 12));
					maxDate.setTime(tempDate.getTime());
				} else {
					FinexaBussinessException businessException = new FinexaBussinessException("ClientExpense", "111",
							"Life Expectancy for the client needs to be entered to do Budget Analysis for the client.");
					throw businessException;
				}
				int startYearOfIncome = cInterimDate.get(Calendar.YEAR);
				if (cInterimDate.get(Calendar.MONTH) < 3) {
					startYearOfIncome = startYearOfIncome - 1;
					if (fpFlag == 0) {
						cInterimDate.set(Calendar.YEAR, startYearOfIncome);
					}
				}
				if (fpFlag == 0) {
					cInterimDate.set(Calendar.MONTH, 3);
				}
				cInterimDate.set(Calendar.DAY_OF_MONTH, cInterimDate.getActualMaximum(Calendar.DAY_OF_MONTH));

				int startMonthOfIncome = cInterimDate.get(Calendar.MONTH) + 1;

				int index = 0;
				// int continueUpto = lifeExp - 1;// Since index is starting
				// from 0

				try {
					incomeOutput = clientIncomeSerive.getCLientFamilyAllIncomes(clientId, mode, fpFlag, session);
				
					expenseOutput = clientExpenseSerive.getAnnualExpensesDetailed(clientId, mode, fpFlag, session);
			
					clientCommittedOutFlowOuput = clientExpenseSerive.getClientCommitedOutFlows(clientId, mode, fpFlag,
							session);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					loanOutflowList = getAnnualLoanOutFlow(clientId, mode, fpFlag, session);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (mode.equals("yearly")) {
						/*********************************
						 * For Yearly
						 ***************************************/
						int age = incomeOutput.getClientFamilyList().get(0) == null ? 0
								: incomeOutput.getClientFamilyList().get(0).getAge();
						for (int currentYear = startYearOfIncome; currentYear <= maxDate
								.get(Calendar.YEAR); currentYear++) {
							ClientFamilyNetSurplusOutput clientFamilyNetSurplusOutput = new ClientFamilyNetSurplusOutput();
							double net_surplus = 0.0;
							double income = 0.0;

							if (incomeOutput != null && incomeOutput.getClientFamilyList() != null
									&& incomeOutput.getClientFamilyList().size() > 0
									&& index < incomeOutput.getClientFamilyList().size()) {
								if (incomeOutput.getClientFamilyList().get(index) != null) {
									income = incomeOutput.getClientFamilyList().get(index).getTotalIncome();
								}
							}
							double expense = 0.0;
							double livExp = 0.0;
							double discreExp = 0.0;
							if (expenseOutput != null && expenseOutput.getExpenseProjectionList() != null
									&& expenseOutput.getExpenseProjectionList().size() > 0) {
								if (index < expenseOutput.getExpenseProjectionList().size()
										&& expenseOutput.getExpenseProjectionList().get(index).getTotalExpense() > 0) {
									expense = expenseOutput.getExpenseProjectionList().get(index).getTotalExpense();
									livExp = expenseOutput.getExpenseProjectionList().get(index).getLivingExpense();
									discreExp = expenseOutput.getExpenseProjectionList().get(index)
											.getDiscretionaryExpense();
								}
							}
							double committed_of = 0.0;
							double otherCo = 0.0;
							double investment = 0.0;
							if (clientCommittedOutFlowOuput != null
									&& clientCommittedOutFlowOuput.getOutFlowList() != null
									&& index < clientCommittedOutFlowOuput.getOutFlowList().size()) {
								committed_of = clientCommittedOutFlowOuput.getOutFlowList().get(index)
										.getTotalOutFlow();
								otherCo = clientCommittedOutFlowOuput.getOutFlowList().get(index).getPremiumAMount()
										+ clientCommittedOutFlowOuput.getOutFlowList().get(index)
										.getPremiumAmountGeneral()
										+ clientCommittedOutFlowOuput.getOutFlowList().get(index)
										.getPremiumAmountHealth();
								investment = clientCommittedOutFlowOuput.getOutFlowList().get(index)
										.getInvestmentAmount();

							}
							double loan_outflows = 0.0;

							if (loanOutflowList != null && loanOutflowList.size() > 0
									&& index < loanOutflowList.size()) {
								loan_outflows = loanOutflowList.get(index);
							}
							String finYear = currentYear + "-" + (currentYear + 1);
							net_surplus = income - expense - committed_of - loan_outflows;
							yearWiseNetsurplus.put(currentYear, net_surplus);
							if (!(income == 0 && expense == 0 && committed_of == 0 && loan_outflows == 0)) {
								clientFamilyNetSurplusOutput.setFinYear(finYear);
								clientFamilyNetSurplusOutput.setAge(age);
								clientFamilyNetSurplusOutput.setIncome(round(income, 2));
								
								String incomeForReport = "\u20B9" + " " + (roundOff(income/100000, 1)) + " L";
								//System.out.println("incomeForReport: " + incomeForReport);
								
								clientFamilyNetSurplusOutput.setIncomeForReport(incomeForReport);

								clientFamilyNetSurplusOutput.setExpense(round(expense, 2));
								
								String expenseForReport = "\u20B9" + " " + (roundOff(expense/100000, 1)) + " L";
								//System.out.println("expenseForReport: " + expenseForReport);
								
								clientFamilyNetSurplusOutput.setExpenseForReport(expenseForReport);

								clientFamilyNetSurplusOutput.setCommitted_outflows(round(committed_of, 2));
								clientFamilyNetSurplusOutput.setLoan_outflows(round(loan_outflows, 2));
								
								String netSurplusForReport = "\u20B9" + " " + (roundOff(net_surplus/100000, 1)) + " L";
								
								clientFamilyNetSurplusOutput.setNet_surplus(round(net_surplus, 2));
								clientFamilyNetSurplusOutput.setNetSurplusForReport(netSurplusForReport);

								// extra fields added for financial Planning
								clientFamilyNetSurplusOutput.setOtherCO(round(otherCo, 2));
								clientFamilyNetSurplusOutput.setLivingExpense(round(livExp, 2));
								clientFamilyNetSurplusOutput.setDiscreExpense(round(discreExp, 2));
								clientFamilyNetSurplusOutput.setInvestment(round(investment, 2));

								clientFamilyNetSurplusOutputList.add(clientFamilyNetSurplusOutput);
							}
							index++;
							age++;
						}
					} else {
						/*********************************
						 * For Monthly
						 ***************************************/
						for (int currentYear = startYearOfIncome; currentYear <= maxDate
								.get(Calendar.YEAR); currentYear++) {
							do {
								ClientFamilyNetSurplusOutput clientFamilyNetSurplusOutput = new ClientFamilyNetSurplusOutput();
								double net_surplus = 0.0;
								double income = 0.0;
								int age = 0;
								if (incomeOutput != null && incomeOutput.getClientFamilyList() != null
										&& incomeOutput.getClientFamilyList().size() > 0
										&& index < incomeOutput.getClientFamilyList().size()) {
									if (incomeOutput.getClientFamilyList().get(index) != null) {
										income = incomeOutput.getClientFamilyList().get(index).getTotalIncome();
										age = incomeOutput.getClientFamilyList().get(index).getAge();
									}
								}
								double expense = 0.0;
								// double livExp = 0.0;
								// double discreExp = 0.0;
								if (expenseOutput != null && expenseOutput.getExpenseProjectionList() != null
										&& expenseOutput.getExpenseProjectionList().size() > 0) {
									if (index < expenseOutput.getExpenseProjectionList().size() && expenseOutput
											.getExpenseProjectionList().get(index).getTotalExpense() > 0) {
										expense = expenseOutput.getExpenseProjectionList().get(index).getTotalExpense();
										// livExp =
										// expenseOutput.getExpenseProjectionList().get(index).getLivingExpense();
										// discreExp = expenseOutput.getExpenseProjectionList().get(index)
										// .getDiscretionaryExpense();
									}
								}
								double committed_of = 0.0;
								// double otherCo = 0.0;
								// double investment = 0.0;
								if (clientCommittedOutFlowOuput != null
										&& clientCommittedOutFlowOuput.getOutFlowList() != null
										&& index < clientCommittedOutFlowOuput.getOutFlowList().size()) {
									committed_of = clientCommittedOutFlowOuput.getOutFlowList().get(index)
											.getTotalOutFlow();
									// otherCo =
									// clientCommittedOutFlowOuput.getOutFlowList().get(index).getPremiumAMount()
									// + clientCommittedOutFlowOuput.getOutFlowList().get(index)
									// .getPremiumAmountGeneral()
									// + clientCommittedOutFlowOuput.getOutFlowList().get(index)
									// .getPremiumAmountHealth();
									// investment = clientCommittedOutFlowOuput.getOutFlowList().get(index)
									// .getInvestmentAmount();
								}
								double loan_outflows = 0.0;

								if (loanOutflowList != null && loanOutflowList.size() > 0
										&& index < loanOutflowList.size()) {
									loan_outflows = loanOutflowList.get(index);
								}
								String finYear = currentYear + "-" + (currentYear + 1);
								net_surplus = income - expense - committed_of - loan_outflows;
								yearWiseNetsurplus.put(currentYear, net_surplus);

								clientFamilyNetSurplusOutput
								.setFinYear(monthArray[cInterimDate.get(Calendar.MONTH)] + "," + finYear);
								clientFamilyNetSurplusOutput.setAge(age);
								clientFamilyNetSurplusOutput.setIncome(round(income, 2));

								clientFamilyNetSurplusOutput.setExpense(round(expense, 2));

								clientFamilyNetSurplusOutput.setCommitted_outflows(round(committed_of, 2));
								clientFamilyNetSurplusOutput.setLoan_outflows(round(loan_outflows, 2));
								clientFamilyNetSurplusOutput.setNet_surplus(round(net_surplus, 2));

								clientFamilyNetSurplusOutputList.add(clientFamilyNetSurplusOutput);

								index++;
								if (startMonthOfIncome == 12) {
									startMonthOfIncome = 1; // M
								} else {
									startMonthOfIncome++;
									// System.out.println("Start Month
									// incremented
									// to 1, new Month " + startMonthOfIncome);
								}
								cInterimDate.add(Calendar.MONTH, 1);

							} while (startMonthOfIncome != 4);
						}
					}
				} catch (Exception e) {
					throw new FinexaBussinessException("ClientNetSurplus", "111", e.getMessage());
				}
			} else {
				FinexaBussinessException businessException = new FinexaBussinessException("ClientNetSurplus", "111",
						"Life expectancy of Client Not Present");
				throw businessException;

			}

			// 0 financial year and 1 pro rata
			if (fpFlag == 0 && !mode.equals("monthly")) {
				cacheInfoService.addCacheList(FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
						FinexaConstant.CALCULATION_SUB_TYPE_NETSURPLUS_CONSTANT, clientFamilyNetSurplusOutputList);
			} else {
				if (fpFlag == 1) {
					cacheInfoService.addCacheList(FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
							FinexaConstant.CALCULATION_SUB_TYPE_NETSURPLUS_CONSTANT_PRO_RATA,
							clientFamilyNetSurplusOutputList);
				}
			}

		}
		clientIncomeSerive = null;
		clientExpenseSerive = null;
		clientFamilyExpenseDAO = null;
		return clientFamilyNetSurplusOutputList;
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
	
	public static double roundOff(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.000");
		df.setMaximumFractionDigits(places);
		df.setMinimumFractionDigits(0);
		String valueString = df.format(value);
		value = Double.parseDouble(valueString);
		return value;
	}

	private ArrayList<Double> getAnnualLoanOutFlow(int clientId, String mode, int fpFlag, Session session)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ArrayList<Double> loanOutflowList = new ArrayList<Double>();
		/************
		 * code Change by Debolina on 12thOct 2017 to match Net surplus calculations
		 ***************/
		try {
			ArrayList<ClientFamilyLoanOutput> loanList = (ArrayList<ClientFamilyLoanOutput>) this
					.getClientAllLoanStatusDetailsForNetSurplus(clientId, mode, fpFlag, session);
			if (loanList != null && loanList.size() > 0) {
				for (int i = 0; i < loanList.size(); i++) {
					loanOutflowList.add(round(loanList.get(i).getEmiAmount(), 2));
				}
			}
			return loanOutflowList;
		} catch (Exception e) {
			throw new FinexaBussinessException("NerSurplus", "", e.getMessage());
		}
	}

	private ArrayList<ClientFamilyLoanOutput> getClientAllLoanStatusDetailsForNetSurplus(int clientId, String mode,
			int fpFlag, Session session) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ArrayList<ClientFamilyLoanOutput> clientFamilyLoanOuputList = new ArrayList<>();
		ClientFamilyLoanDAO clientFamilyLoanDao = new ClientFamilyLoanDAOImpl();
		List<ClientFamilyLoanOutput> tempList = new ArrayList<>();
		ClientLoanService clientLoanService = new ClientLoanServiceImpl();
		try {
			List<ClientLoan> loanList = clientFamilyLoanDao.getLoanClientAllDetailsById(clientId, session);

			for (ClientLoan loan : loanList) {
				List<ClientFamilyLoanOutput> tempOutputList = new ArrayList<>();
				// String providerId = "" +
				// loan.getMasterLoanProvider().getId();
				tempOutputList = clientLoanService.getClientLoanStatusDetails(loan, mode, fpFlag, session);

				if (clientFamilyLoanOuputList.size() > 0) {
					int swapFlag = 0;
					if (tempOutputList.size() > clientFamilyLoanOuputList.size()) {
						tempList = clientFamilyLoanOuputList;
						clientFamilyLoanOuputList = (ArrayList<ClientFamilyLoanOutput>) tempOutputList;
						tempOutputList = tempList;
						swapFlag = 1;
					}
					for (ClientFamilyLoanOutput loanoutput : clientFamilyLoanOuputList) {
						for (ClientFamilyLoanOutput tempLoanOutput : tempOutputList) {
							if (loanoutput.getProjectionYear().equals(tempLoanOutput.getProjectionYear())) {
								if (swapFlag == 0) {
									if (tempLoanOutput.getLoanEmiNonEmi() == 2) {// for
										// Non
										// Emi
										// Loans
										loanoutput.setEmiAmount(round((tempLoanOutput.getInterestPay()
												+ tempLoanOutput.getPrincipalPay() + loanoutput.getEmiAmount()), 2));
									} else {
										loanoutput.setEmiAmount(
												round((loanoutput.getEmiAmount() + tempLoanOutput.getEmiAmount()), 2));
									}
								} else {
									if (loanoutput.getLoanEmiNonEmi() == 2) {// for
										// Non
										// Emi
										// Loans
										loanoutput.setEmiAmount(round((loanoutput.getInterestPay()
												+ loanoutput.getPrincipalPay() + tempLoanOutput.getEmiAmount()), 2));
									} else {
										loanoutput.setEmiAmount(
												round((loanoutput.getEmiAmount() + tempLoanOutput.getEmiAmount()), 2));
									}
								}
								loanoutput.setBegningBal(
										round((loanoutput.getBegningBal() + tempLoanOutput.getBegningBal()), 2));
								loanoutput.setInterestPay(
										round((loanoutput.getInterestPay() + tempLoanOutput.getInterestPay()), 2));
								loanoutput.setPrincipalPay(
										round((loanoutput.getPrincipalPay() + tempLoanOutput.getPrincipalPay()), 2));
								loanoutput.setEndBal(round((loanoutput.getEndBal() + tempLoanOutput.getEndBal()), 2));

							} /*
							 * else { if (loanoutput.getLoanEmiNonEmi() == 2) {// for // Non // Emi // Loans
							 * loanoutput.setEmiAmount(round((loanoutput.getInterestPay() +
							 * loanoutput.getPrincipalPay() + tempLoanOutput.getEmiAmount()), 2)); } else {
							 * loanoutput.setEmiAmount( round((loanoutput.getEmiAmount() +
							 * tempLoanOutput.getEmiAmount()), 2)); } }
							 */
							// System.out.println(i);
							// i++;
						}
					}
					for (ClientFamilyLoanOutput loanoutput : clientFamilyLoanOuputList) {
						if (loanoutput.getEmiAmount() == 0) {
							loanoutput.setEmiAmount(loanoutput.getPrincipalPay() + loanoutput.getInterestPay());
						}
					}

				} else {
					// Code added by Debolina on 1st Nov to
					for (int tempIndex = 0; tempIndex < tempOutputList.size(); tempIndex++) {
						if (tempOutputList.get(tempIndex).getLoanEmiNonEmi() == 2) {// for
							// Non
							// Emi
							// Loans
							tempOutputList.get(tempIndex)
							.setEmiAmount(round((tempOutputList.get(tempIndex).getInterestPay()
									+ tempOutputList.get(tempIndex).getPrincipalPay()), 2));
						} else {
							tempOutputList.get(tempIndex)
							.setEmiAmount(round((tempOutputList.get(tempIndex).getEmiAmount()), 2));
						}
					}
					clientFamilyLoanOuputList.addAll(tempOutputList);
				}
			}

		} catch (FinexaDaoException e) {
			e.printStackTrace();
		}
		return clientFamilyLoanOuputList;
	}

	public Hashtable<Integer, Double> getYearWiseNetsurplus() {
		return yearWiseNetsurplus;
	}

	public void setYearWiseNetsurplus(Hashtable<Integer, Double> yearWiseNetsurplus) {
		this.yearWiseNetsurplus = yearWiseNetsurplus;
	}

	@Override
	public Hashtable<Integer, Double> getNetSurplusYearWiseMap(String token, int clientId, Session session,CacheInfoService cacheInfoService)
			throws FinexaBussinessException {
		// this service will be called only for financial planning, so flag set
		// to 1
		getClientNetSurplusInfo(token, clientId, "yearly", 1, session,cacheInfoService);
		return yearWiseNetsurplus;
	}
}
