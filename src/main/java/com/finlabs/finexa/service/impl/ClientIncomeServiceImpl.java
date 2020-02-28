package com.finlabs.finexa.service.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import com.finlabs.finexa.dto.ClientFamilyIncomeOutput;
import com.finlabs.finexa.dto.FamilyMemberIncomeDetailOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.LookupIncomeExpenseDuration;
import com.finlabs.finexa.model.MasterIncomeGrowthRate;
import com.finlabs.finexa.repository.ClientFamilyIncomeDAO;
import com.finlabs.finexa.repository.ClientFamilyMemberDAO;
import com.finlabs.finexa.repository.ClientMasterDAO;
import com.finlabs.finexa.repository.MasterdataIncomeGrowthRateDAO;
import com.finlabs.finexa.repository.impl.ClientFamilyIncomeDAOImpl;
import com.finlabs.finexa.repository.impl.ClientFamilyMemberDAOImpl;
import com.finlabs.finexa.repository.impl.ClientMasterDAOImpl;
import com.finlabs.finexa.repository.impl.MasterdataIncomeGrowthRateDAOImpl;
import com.finlabs.finexa.resources.model.Annuity2ProductCalculator;
import com.finlabs.finexa.resources.model.Annuity2ProductLookup;
import com.finlabs.finexa.resources.model.AtalPensionYojana;
import com.finlabs.finexa.resources.model.AtalPensionYojanaLookup;
import com.finlabs.finexa.resources.model.BankBondDebenturesLookup;
import com.finlabs.finexa.resources.model.BankFDTDRLookup;
import com.finlabs.finexa.resources.model.BankFDTDRPC;
import com.finlabs.finexa.resources.model.BondDebentures;
import com.finlabs.finexa.resources.model.POTimeDeposit;
import com.finlabs.finexa.resources.model.POTimeDepositLookup;
import com.finlabs.finexa.resources.model.PerpetualBond;
import com.finlabs.finexa.resources.model.PerpetualBondLookup;
import com.finlabs.finexa.resources.model.PostOfficeMonthlyIncomeScheme;
import com.finlabs.finexa.resources.model.PostOfficeMonthlyIncomeSchemeLookup;
import com.finlabs.finexa.resources.model.SeniorCitizenSavingScheme;
import com.finlabs.finexa.resources.model.SeniorCitizenSavingSchemeLookup;
import com.finlabs.finexa.resources.service.Annuity2ProductService;
import com.finlabs.finexa.resources.service.AtalPensionYojanaService;
import com.finlabs.finexa.resources.service.BankFDTDRService;
import com.finlabs.finexa.resources.service.BondDebenturesService;
import com.finlabs.finexa.resources.service.POTimeDespositService;
import com.finlabs.finexa.resources.service.PerpetualBondService;
import com.finlabs.finexa.resources.service.PostOfficeMonthlyIncomeSchemeService;
import com.finlabs.finexa.resources.service.SeniorCitizenSavingSchemeService;
import com.finlabs.finexa.service.ClientIncomeService;
import com.finlabs.finexa.util.FinexaConstant;

//@Transactional
//@Service
public class ClientIncomeServiceImpl implements ClientIncomeService {
	// @Autowired
	// private ClientFamilyIncomeDAO clientFamilyDao;
	// @Autowired
	// private ClientMasterDAO clientMasterDao;
	// @Autowired
	// private ClientFamilyMemberDAO clientFamilyMemberDao;
	//@Autowired
	//private MasterdataIncomeGrowthRateDAO masterdataIncomeGrowthRateDAO;
	Calendar cInterimDate = Calendar.getInstance();
	@Autowired
	GenericDao genericDao;
	Map<Date, Double> datePensionMap = new HashMap<>();
	private HashMap<HashMap<Long, Integer>, ArrayList<Integer>> familyIncomeMap = new HashMap<HashMap<Long, Integer>, ArrayList<Integer>>();
	Calendar endDate = Calendar.getInstance();
	// Calendar oneTimePayment = Calendar.getInstance();
	String monthArray[] = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };

	@Override
	public ClientFamilyIncomeOutput getCLientFamilyAllIncomes(int clientId, String mode, int financialPlanningFlag,
			Session session) throws FinexaBussinessException {
		System.out.println("In Income Module *******************");
		ClientFamilyIncomeDAO clientFamilyDaoImpl = new ClientFamilyIncomeDAOImpl();
		ClientFamilyMemberDAO clientFamilyMemberDao = new ClientFamilyMemberDAOImpl();
		MasterdataIncomeGrowthRateDAO masterdataIncomeGrowthRateDAO = new MasterdataIncomeGrowthRateDAOImpl();
		try {
			List<ClientFamilyIncome> incomeList = clientFamilyDaoImpl.getAllIncomeById(clientId, session);
			
				if (!incomeList.isEmpty()) {
					List<ClientFamilyMember> clientFamilyMember = clientFamilyMemberDao
							.getAllClientFamilyMemberByClientId(clientId, session);

					Map<Date, Double> dateInterestMap = null;
					try {
						dateInterestMap = this.getInterestRateFromProductCal(clientId, session);
					} catch (Exception e) {
					}

					ClientFamilyIncomeOutput incomeOutput = new ClientFamilyIncomeOutput();
					ClientFamilyIncomeOutput incomeOutputCurrentYear = new ClientFamilyIncomeOutput();
					List<ClientFamilyIncomeOutput> incomeOutputLookupList = new ArrayList<ClientFamilyIncomeOutput>();

					Date planRefDate = new Date();// Getting current date
					Map<Integer, ClientFamilyIncome> clientIncomeMap = new HashMap<>();
					double incomeGrowthRate = 0;
					cInterimDate.setTime(planRefDate);
					cInterimDate.set(Calendar.MILLISECOND, 0);
					cInterimDate.set(Calendar.SECOND, 0);
					cInterimDate.set(Calendar.MINUTE, 0);
					cInterimDate.set(Calendar.HOUR, 0);

					Calendar maxDate = Calendar.getInstance();
					Calendar tempDate = Calendar.getInstance();
					Calendar bDate = Calendar.getInstance();
					Calendar lifeExpClient = Calendar.getInstance();

					// Fetching Life Expectancy of Client

					for (ClientFamilyMember familyMember : clientFamilyMember) {

						if (financialPlanningFlag == 0) {
							if (familyMember.getLookupRelation().getId() == 0) {
								tempDate.setTime(familyMember.getBirthDate());
								bDate.setTime(familyMember.getBirthDate());
								if (familyMember.getLifeExpectancy() != null) {
									tempDate.add(Calendar.MONTH, (int) (familyMember.getLifeExpectancy() * 12));
									maxDate.setTime(tempDate.getTime());
									lifeExpClient = maxDate;
								} else {
									FinexaBussinessException businessException = new FinexaBussinessException(
											"ClientIncome", "111",
											"Life Expectancy for the client needs to be entered to do Budget Analysis for the client.");
									throw businessException;
								}
							}
						} else {
							// choosing the greater life Exp between self & spouse
							if (familyMember.getLookupRelation().getId() == 0
									|| familyMember.getLookupRelation().getId() == 1) {
								if (familyMember.getLookupRelation().getId() == 0) {
									bDate.setTime(familyMember.getBirthDate());
									Calendar tempDate1 = Calendar.getInstance();
									if (familyMember.getLifeExpectancy() != null) {
										tempDate1.add(Calendar.MONTH, (int) (familyMember.getLifeExpectancy() * 12));
										lifeExpClient.setTime(tempDate1.getTime());
									} else {
										FinexaBussinessException businessException = new FinexaBussinessException(
												"ClientIncome", "111",
												"Life Expectancy for the client needs to be entered to do Budget Analysis for the client.");
										throw businessException;
									}
								}

								tempDate.setTime(familyMember.getBirthDate());
								if (familyMember.getLifeExpectancy() != null) {
									tempDate.add(Calendar.MONTH, (int) (familyMember.getLifeExpectancy() * 12));
									if (maxDate.getTime().before(tempDate.getTime())) {
										maxDate.setTime(tempDate.getTime());
									}
								} /*else {
									FinexaBussinessException businessException = new FinexaBussinessException(
											"ClientIncome", "111",
											"Life Expectancy for the client needs to be entered to do Budget Analysis for the client.");
									throw businessException;
								}*/

							}
						}
					}

					// Fetching Each Income Growth Rate

					// Map<Integer, Double> incomeGrowthRateMap = new HashMap<>();
					Map<Byte, Double> incomeGrowthRateMap = new HashMap<>();
					for (ClientFamilyIncome income : incomeList) {
						MasterIncomeGrowthRate mDataGrRate = masterdataIncomeGrowthRateDAO
								.getIncomeGrowthRate(income.getIncomeType(), cInterimDate.getTime(), session);

						if (mDataGrRate != null) {
							// incomeGrowthRate = mDataGrRate.getCagr() /
							// (double) 100;
							// value will be in decimal form in database so no
							// need to divide by 100
							incomeGrowthRate = mDataGrRate.getCagr().doubleValue();
							incomeGrowthRateMap.put(income.getIncomeType(), incomeGrowthRate);
							clientIncomeMap.put((int) income.getClientFamilyMember().getId(), income);
						}

					}

					// boolean oneTimePaymentFlag = false;
					double oneTimePaymentAmount = 0.0;

					int yearCounter = 1;
					int startYearOfIncome = cInterimDate.get(Calendar.YEAR);
					if (cInterimDate.get(Calendar.MONTH) < 3) {
						startYearOfIncome = cInterimDate.get(Calendar.YEAR) - 1;
						cInterimDate.set(Calendar.YEAR, startYearOfIncome);
					}
					int day = cInterimDate.get(Calendar.DAY_OF_MONTH);
					if (financialPlanningFlag == 0) {
						cInterimDate.set(Calendar.MONTH, 3);
					} else {
						cInterimDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
					}
					int startMonthOfIncome = cInterimDate.get(Calendar.MONTH);
					if (day < FinexaConstant.MAX_DAY) {
						startMonthOfIncome = cInterimDate.get(Calendar.MONTH) + 1;
					}
					cInterimDate.set(Calendar.DAY_OF_MONTH, cInterimDate.getActualMaximum(Calendar.DAY_OF_MONTH));

					// fetching current age of client for Financial Planning
					int currentAge = getAgeForLifeExpectancy(bDate.getTime());

					for (int currentYear = startYearOfIncome; currentYear <= maxDate
							.get(Calendar.YEAR); currentYear++) {

						String currentFinYear = currentYear + "-" + (currentYear + 1);
						ClientFamilyIncomeOutput incomeOutputLookupTableYearly = new ClientFamilyIncomeOutput();
						double yearlysalaryIncome = 0;
						double yearlybonusIncome = 0;
						double yearlyproffee = 0;
						double yearlybussIncome = 0;
						double yearlyrentalIncome = 0;
						double yearlypension = 0;
						double yearlyotherIncome = 0;
						double yearlyInterestIncome = 0;
						double yearlyInTotalIncome = 0;
						double yearlytotalIncome = 0;

						// for lumpsumInflow Calculations in Financial Planning
						double yearlyLumpsumInflow = 0;

						do { // Calculations done against current financial year
							ClientFamilyIncomeOutput incomeOutputLookupTableMonthly = new ClientFamilyIncomeOutput();
							// System.out.println("For Month-------------------"
							// + startMonthOfIncome +
							// "--------------------------");
							double monthlySalary = 0;
							double monthlyBonus = 0;
							double monthlyProfFees = 0;
							double monthlyBusiness = 0;
							double monthlyRental = 0;
							double monthlyPension = 0;
							double monthlyOther = 0;
							double monthlyIndividualTotal = 0;
							double monthlyLumpsumInflow = 0;

							for (ClientFamilyMember familyMember : clientFamilyMember) {

								for (ClientFamilyIncome income : incomeList) {

									if (incomeGrowthRateMap.get(income.getIncomeType()) == null) {
										incomeGrowthRate = 0;
									} else {
										incomeGrowthRate = incomeGrowthRateMap.get(income.getIncomeType());
									}

									LookupIncomeExpenseDuration year = null;
									if (income.getClientFamilyMember().getId() == familyMember.getId()) {
										year = income.getLookupIncomeExpenseDuration();
										if (year.getDescription().matches("[0-9]+")) {
											endDate.set(Integer.parseInt(year.getDescription()), Calendar.DECEMBER, 31);
											// System.out.println("end time" +
											// endDate.getTime());
										} else {
											if (year.getDescription().equals("Upto Retirement")) {
												if (familyMember.getRetirementAge() != null) {
													// already retired
													endDate = this.getEndDate(familyMember.getRetirementAge(),
															familyMember.getBirthDate());
													// endDate.set(Calendar.MONTH,11);
													endDate.set(Calendar.DAY_OF_MONTH,
															endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
												}
											} else {
												if (year.getDescription().equals("Upto Life Expectancy")) {
													if (familyMember.getLifeExpectancy() != null) {
														endDate = this.getEndDate(familyMember.getLifeExpectancy(),
																familyMember.getBirthDate());
														endDate.set(Calendar.DAY_OF_MONTH,
																endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
														// endDate.set(Calendar.MONTH,11);
													}
												}
											}
										}
										endDate.set(Calendar.MILLISECOND, 0);
										endDate.set(Calendar.SECOND, 0);
										endDate.set(Calendar.MINUTE, 0);
										endDate.set(Calendar.HOUR, 12);
										// System.out.println("End Date" +
										// endDate.getTime());
										// System.out.println("Max Date" +
										// maxDate.getTime());
										// System.out.println("Current Date" +
										// cInterimDate.getTime());

										if (endDate.getTime().compareTo(cInterimDate.getTime()) >= 0) {
											// For Salary
											if (income.getIncomeType() == 1) {
												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {
													monthlySalary = monthlySalary
															+ income.getIncomeAmount().doubleValue() * Math
																	.pow((1 + incomeGrowthRate), (yearCounter - 1));

												}
											}

											// For Bonus

											if (income.getIncomeType() == 2) {

												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {
													monthlyBonus = monthlyBonus + income.getIncomeAmount().doubleValue()
															* Math.pow((1 + incomeGrowthRate), (yearCounter - 1));
												}
											}

											// for Business Income
											if (income.getIncomeType() == 3) {
												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {

													monthlyBusiness = monthlyBusiness
															+ income.getIncomeAmount().doubleValue() * Math
																	.pow((1 + incomeGrowthRate), (yearCounter - 1));
												}

											}

											// For Prof Fees

											if (income.getIncomeType() == 4) {

												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {
													monthlyProfFees = monthlyProfFees
															+ income.getIncomeAmount().doubleValue() * Math
																	.pow((1 + incomeGrowthRate), (yearCounter - 1));
												}

											}

											// For Rental Income
											if (income.getIncomeType() == 5) {

												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {

													monthlyRental = monthlyRental
															+ income.getIncomeAmount().doubleValue() * Math
																	.pow((1 + incomeGrowthRate), (yearCounter - 1));
												}

											}

											// For Pension
											if (income.getIncomeType() == 6) {

												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {

													monthlyPension = monthlyPension
															+ income.getIncomeAmount().doubleValue() * Math
																	.pow((1 + incomeGrowthRate), (yearCounter - 1));
												}

											}

											// For Other Income
											if (income.getIncomeType() == 7) {

												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {

													//oneTimePaymentAmount = income.getIncomeAmount().doubleValue();
													monthlyOther = monthlyOther + income.getIncomeAmount().doubleValue()
															* Math.pow((1 + incomeGrowthRate), (yearCounter - 1));
												}
											}

											// For Total Income Category
											if (income.getIncomeType() == 8) {
												
												if (valueToBeAdded(startMonthOfIncome, income.getLookupMonth().getId(),
														12 / income.getLookupFrequency().getId(), endDate, maxDate,
														familyMember.getId(), income.getIncomeType())) {

													monthlyIndividualTotal = monthlyIndividualTotal
															+ income.getIncomeAmount().doubleValue() * Math
																	.pow((1 + incomeGrowthRate), (yearCounter - 1));
												}
											}
										}
									}
								}
							}

							// omitted according to discussion with Neha M
							// adding lumpsumInflow in Financial Planning
							/*
							 * if (financialPlanningFlag == 1) {
							 * 
							 * for (ClientLumpsumInflow obj : lumpsumInflowList) { Date dt =
							 * obj.getExpectedInflowDate(); Calendar calLumpsum = Calendar.getInstance();
							 * calLumpsum.setTime(dt); int lumpYear = calLumpsum.get(Calendar.YEAR); if
							 * (calLumpsum.get(Calendar.MONTH) < 3) { lumpYear = lumpYear - 1; } int
							 * lumpMonth = calLumpsum.get(Calendar.MONTH) + 1; if (currentYear == lumpYear
							 * && startMonthOfIncome == lumpMonth) { monthlyLumpsumInflow =
							 * monthlyLumpsumInflow + obj.getExpectedInflow().doubleValue(); } } }
							 */

							Double interestAmount = 0.0;
							// System.out.println(cInterimDate.getTime() + " " +
							// dateInterestMap.get(cInterimDate.getTime()));
							Calendar cInterimDateCopy = cInterimDate;
							cInterimDateCopy.set(Calendar.MILLISECOND, 0);
							cInterimDateCopy.set(Calendar.SECOND, 0);
							cInterimDateCopy.set(Calendar.MINUTE, 0);
							cInterimDateCopy.set(Calendar.HOUR_OF_DAY, 12);
							cInterimDateCopy.set(Calendar.DAY_OF_MONTH,
									cInterimDate.getActualMaximum(Calendar.DAY_OF_MONTH));
							// System.out.println("Cinterim Copy" +
							// cInterimDateCopy.getTime());
							// if (currentYear == startYearOfIncome) {
							if (dateInterestMap != null && dateInterestMap.get(cInterimDateCopy.getTime()) != null) {
								interestAmount = dateInterestMap.get(cInterimDateCopy.getTime());
								// System.out.println(cInterimDate.getTime() + "
								// " + interestAmount);
							}
							yearlyInterestIncome = interestAmount + yearlyInterestIncome;

							// }
							Double pensionAmt = 0.0;
							// Adding Pension
							if (datePensionMap.get(cInterimDateCopy.getTime()) != null) {
								pensionAmt = datePensionMap.get(cInterimDateCopy.getTime());
								// System.out.println(cInterimDate.getTime() + "
								// " + interestAmount);
							}
							monthlyPension = monthlyPension + pensionAmt;

							if (currentYear == startYearOfIncome) {
								yearlysalaryIncome = yearlysalaryIncome + monthlySalary;
								yearlybonusIncome = yearlybonusIncome + monthlyBonus;
								yearlyproffee = yearlyproffee + monthlyProfFees;
							} else {
								yearlysalaryIncome = yearlysalaryIncome + monthlySalary + monthlyBonus
										+ monthlyProfFees;

							}
							yearlybussIncome = yearlybussIncome + monthlyBusiness;
							yearlyrentalIncome = yearlyrentalIncome + monthlyRental;
							yearlypension = yearlypension + monthlyPension;
							yearlyInTotalIncome = yearlyInTotalIncome + monthlyIndividualTotal;
							yearlyLumpsumInflow = yearlyLumpsumInflow + monthlyLumpsumInflow;
							yearlyotherIncome = yearlyotherIncome + monthlyOther;
							// System.out.println("------Current Month--------"
							// + startMonthOfIncome);

							// rounding to two decimal places

							yearlysalaryIncome = round(yearlysalaryIncome, 2);
							yearlybonusIncome = round(yearlybonusIncome, 2);
							yearlyproffee = round(yearlyproffee, 2);
							yearlybussIncome = round(yearlybussIncome, 2);
							yearlyrentalIncome = round(yearlyrentalIncome, 2);
							yearlypension = round(yearlypension, 2);
							yearlyInterestIncome = round(yearlyInterestIncome, 2);
							yearlyInTotalIncome = round(yearlyInTotalIncome, 2);
							yearlyLumpsumInflow = round(yearlyLumpsumInflow, 2);
							yearlyotherIncome = round(yearlyotherIncome, 2);

							// rounding to two decimal places

							monthlySalary = round(monthlySalary, 2);
							monthlyBonus = round(monthlyBonus, 2);
							monthlyProfFees = round(monthlyProfFees, 2);
							monthlyBusiness = round(monthlyBusiness, 2);
							monthlyRental = round(monthlyRental, 2);
							monthlyPension = round(monthlyPension, 2);
							interestAmount = round(interestAmount, 2);
							monthlyLumpsumInflow = round(monthlyLumpsumInflow, 2);
							monthlyIndividualTotal = round(monthlyIndividualTotal, 2);
							monthlyOther = round(monthlyOther, 2);

							if (mode.equals("monthly")) {
								incomeOutputLookupTableMonthly
										.setSalaryIncome(monthlySalary + monthlyBonus + monthlyProfFees);
								// incomeOutputLookupTableMonthly.setBonousIncome(monthlyBonus);
								// incomeOutputLookupTableMonthly.setProfessionalFee(monthlyProfFees);
								incomeOutputLookupTableMonthly.setBussinessIncome(monthlyBusiness);
								incomeOutputLookupTableMonthly.setRentalIncome(monthlyRental);
								incomeOutputLookupTableMonthly.setPension(monthlyPension);
								incomeOutputLookupTableMonthly.setOtherIncome(monthlyOther);
								incomeOutputLookupTableMonthly.setInterestIncome(interestAmount);
								incomeOutputLookupTableMonthly.setLumpsumInflow(monthlyLumpsumInflow);
								incomeOutputLookupTableMonthly.setIndividualTotalIncome(monthlyIndividualTotal);
								/*double monthlyTotalIncome = monthlySalary + monthlyBonus + monthlyProfFees
										+ monthlyBusiness + monthlyRental + monthlyPension + monthlyOther + monthlyOther
										+ monthlyIndividualTotal;*/
								//added by Saheli on 18/01/2019
								double monthlyTotalIncome = monthlySalary + monthlyBonus + monthlyProfFees + monthlyBusiness + monthlyRental
										+ monthlyPension + interestAmount + monthlyLumpsumInflow + monthlyIndividualTotal + monthlyOther;
								if (monthlyTotalIncome > 0) {
									incomeOutputLookupTableMonthly.setTotalIncome(round(monthlyTotalIncome, 2));
									incomeOutputLookupTableMonthly.setYear(
											monthArray[cInterimDate.get(Calendar.MONTH)] + "," + currentFinYear);
									incomeOutputLookupTableMonthly.setAge(currentAge);
									incomeOutputLookupList.add(incomeOutputLookupTableMonthly);
								}
								incomeOutput.setClientFamilyList(incomeOutputLookupList);
							}

							if (startMonthOfIncome == 12) {
								startMonthOfIncome = 1; // M
							} else {
								startMonthOfIncome++;
								// System.out.println("Start Month incremented
								// to 1, new Month " + startMonthOfIncome);
							}
							cInterimDate.add(Calendar.MONTH, 1);
						} while (startMonthOfIncome != 4); // Until calculation
						// reaches April

						if (mode.equals("yearly") || mode.equals("monthly")) {
						} else {
							FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome",
									"111", "mode not found , Please select a valid mode");
							throw businessException;
						}

						if (!mode.equals("monthly")) {
							if (currentYear == startYearOfIncome) {

								incomeOutputCurrentYear.setSalaryIncome(yearlysalaryIncome);
								incomeOutputCurrentYear.setBonousIncome(yearlybonusIncome);
								incomeOutputCurrentYear.setProfessionalFee(yearlyproffee);
								incomeOutputCurrentYear.setBussinessIncome(yearlybussIncome);
								incomeOutputCurrentYear.setRentalIncome(yearlyrentalIncome);
								incomeOutputCurrentYear.setPension(yearlypension);
								incomeOutputCurrentYear.setOtherIncome(yearlyotherIncome);
								incomeOutputCurrentYear.setLumpsumInflow(yearlyLumpsumInflow);
								incomeOutputCurrentYear.setIndividualTotalIncome(yearlyInTotalIncome);
								incomeOutputCurrentYear.setInterestIncome(yearlyInterestIncome);
								incomeOutputCurrentYear.setYear(currentFinYear);
								incomeOutputCurrentYear.setAge(currentAge);
								yearlytotalIncome = yearlysalaryIncome + yearlybonusIncome + yearlyproffee
										+ yearlybussIncome + yearlyrentalIncome + yearlypension + yearlyotherIncome
										+ yearlyInterestIncome + yearlyInTotalIncome;
								incomeOutputCurrentYear.setTotalIncome(round(yearlytotalIncome, 2));

								incomeOutput.setSalaryIncome(incomeOutputCurrentYear.getSalaryIncome());
								incomeOutput.setBonousIncome(incomeOutputCurrentYear.getBonousIncome());
								incomeOutput.setProfessionalFee(incomeOutputCurrentYear.getProfessionalFee());
								incomeOutput.setBussinessIncome(incomeOutputCurrentYear.getBussinessIncome());
								incomeOutput.setRentalIncome(incomeOutputCurrentYear.getRentalIncome());
								incomeOutput.setPension(incomeOutputCurrentYear.getPension());
								incomeOutput.setOtherIncome(incomeOutputCurrentYear.getOtherIncome());
								incomeOutput
										.setIndividualTotalIncome(incomeOutputCurrentYear.getIndividualTotalIncome());
								incomeOutput.setLumpsumInflow(incomeOutputCurrentYear.getLumpsumInflow());
								incomeOutput.setInterestIncome(incomeOutputCurrentYear.getInterestIncome());
								incomeOutput.setYear(incomeOutputCurrentYear.getYear());
								incomeOutput.setAge(incomeOutputCurrentYear.getAge());
								incomeOutput.setTotalIncome(incomeOutputCurrentYear.getTotalIncome());
								incomeOutput.setClientFamilyList(incomeOutputLookupList);

								incomeOutputCurrentYear
										.setSalaryIncome(yearlysalaryIncome + yearlybonusIncome + yearlyproffee);

								incomeOutputLookupList.add(incomeOutputCurrentYear);

							} else {

								if (mode.equals("yearly")) {
									incomeOutputLookupTableYearly.setSalaryIncome(yearlysalaryIncome);
									incomeOutputLookupTableYearly.setBonousIncome(yearlybonusIncome);
									incomeOutputLookupTableYearly.setProfessionalFee(yearlyproffee);
									incomeOutputLookupTableYearly.setBussinessIncome(yearlybussIncome);
									incomeOutputLookupTableYearly.setRentalIncome(yearlyrentalIncome);
									incomeOutputLookupTableYearly.setPension(yearlypension);
									incomeOutputLookupTableYearly.setOtherIncome(yearlyotherIncome);
									incomeOutputLookupTableYearly.setLumpsumInflow(yearlyLumpsumInflow);
									incomeOutputLookupTableYearly.setIndividualTotalIncome(yearlyInTotalIncome);
									incomeOutputLookupTableYearly.setInterestIncome(yearlyInterestIncome);
									incomeOutputLookupTableYearly.setYear(currentFinYear);
									incomeOutputLookupTableYearly.setAge(currentAge);
									yearlytotalIncome = yearlysalaryIncome + yearlybonusIncome + yearlyproffee
											+ yearlybussIncome + yearlyrentalIncome + yearlypension + yearlyotherIncome
											+ yearlyInterestIncome + yearlyInTotalIncome;
									incomeOutputLookupTableYearly.setTotalIncome(round(yearlytotalIncome, 2));
									incomeOutputLookupList.add(incomeOutputLookupTableYearly);
								}
							}
						}
						yearCounter++;
						if (cInterimDate.after(lifeExpClient)) {
							currentAge = 0;
						} else {
							currentAge++;
						}
						// System.out.println("CInterim Date get time" +
						// cInterimDate.getTime());

					}
					familyIncomeMap.clear();

					return incomeOutput;
				} else {
					FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
							"Client details not found for given client");
					throw businessException;
				}
		} catch (

		FinexaDaoException e) {
			e.printStackTrace();
			throw new FinexaBussinessException("", "", "");
		}

	}

	public int getAgeForLifeExpectancy(Date birthDate) {
		// log.debug("f id "+lifeExpectancyDTO.getFamilyMemberId());
		// ClientFamilyMember clientFamilyMember =
		// clientFamilyMemberRepository.findOne(familymenberId);
		int age = 0;
		try {
			Date dob1 = birthDate;

			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dob = formatter.format(dob1);

			// TAKE SUBSTRINGS OF THE DOB SO SPLIT OUT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			int yearDOB = Integer.parseInt(dob.substring(0, 4));
			int monthDOB = Integer.parseInt(dob.substring(5, 7));
			int dayDOB = Integer.parseInt(dob.substring(8, 10));

			// CALCULATE THE CURRENT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			java.util.Date date = new java.util.Date();
			int thisYear = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("MM");
			date = new java.util.Date();
			int thisMonth = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("dd");
			date = new java.util.Date();
			int thisDay = Integer.parseInt(dateFormat.format(date));

			// CREATE AN AGE VARIABLE TO HOLD THE CALCULATED AGE
			// TO START WILL SET THE AGE EQUEL TO THE CURRENT YEAR MINUS THE YEAR
			// OF THE DOB
			age = thisYear - yearDOB;

			// IF THE CURRENT MONTH IS LESS THAN THE DOB MONTH
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth < monthDOB) {
				age = age - 1;
			}

			// IF THE MONTH IN THE DOB IS EQUAL TO THE CURRENT MONTH
			// THEN CHECK THE DAY TO FIND OUT IF THEY HAVE HAD THEIR
			// BIRTHDAY YET. IF THE CURRENT DAY IS LESS THAN THE DAY OF THE DOB
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth == monthDOB && thisDay < dayDOB) {
				age = age - 1;
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return age;
	}

	/*
	 * private int getCurrentAgeOfClient(Calendar bDate, Calendar instance) { //
	 * TODO Auto-generated method stub return instance.get(Calendar.YEAR) -
	 * bDate.get(Calendar.YEAR); }
	 */

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

	public boolean valueToBeAdded(int currentMonthIndex, int referenceMonthInDB, int frequency, Calendar incomeEndDate,
			Calendar maxDate, long familyMemberId, int incomeId) {

		boolean toBeAdded = false;
		HashMap<Long, Integer> alreadyPresentMap = new HashMap<Long, Integer>();
		alreadyPresentMap.put(familyMemberId, incomeId);
		if (frequency == 1) {
			toBeAdded = true;
		} else {
			if (familyIncomeMap.size() == 0 || familyIncomeMap.get(alreadyPresentMap) == null) {
				// if (incomeEndDate.getTime().compareTo(maxDate.getTime()) <=
				// 0) {
				ArrayList<Integer> monthsToBeConsidered = new ArrayList<Integer>();
				monthsToBeConsidered.add(referenceMonthInDB);
				int i = referenceMonthInDB;
				for (int j = 0; j < frequency; j++) {
					if (i == 12) {
						i = 1;
					} else {
						i++;
					}

				}
				while (i != referenceMonthInDB) {
					monthsToBeConsidered.add(i);
					for (int j = 0; j < frequency; j++) {
						if (i == 12) {
							i = 1;
						} else {
							i++;
						}

					}
				}
				if (monthsToBeConsidered.contains(currentMonthIndex)) {
					toBeAdded = true;
				}
				if (!familyIncomeMap.containsKey(alreadyPresentMap)) {
					familyIncomeMap.put(alreadyPresentMap, monthsToBeConsidered);
				}
				// }
			} else {
				if (familyIncomeMap.get(alreadyPresentMap).contains(currentMonthIndex)) {
					toBeAdded = true;
				}
			}
		}
		return toBeAdded;
	}

	public Calendar getEndDate(int years, Date dob) {

		endDate.setTime(dob);
		endDate.add(Calendar.MONTH, years * 12);

		return endDate;
	}

	public Calendar getNearestMonth(Date planDate, int refMonth, int freq) {

		Calendar cal = Calendar.getInstance();
		Calendar planCalDate = Calendar.getInstance();
		planCalDate.setTime(planDate);
		// System.out.println("planCalDate" + planCalDate.getTime());
		cal.setTime(planDate);
		if (refMonth > 0) {
			cal.set(Calendar.MONTH, refMonth - 1);
			// System.out.println("cal" + cal.getTime());
			while (true) {
				if ((cal.getTime().compareTo(planCalDate.getTime()) > 0)
						|| (cal.get(Calendar.YEAR) == planCalDate.get(Calendar.YEAR)
								&& cal.get(Calendar.MONTH) == planCalDate.get(Calendar.MONTH))) {
					// System.out.println("cal" + cal.getTime());
					return cal;
				} else {
					cal.add(Calendar.MONTH, freq);
				}

			}
		} else {
			return cal;
		}
	}

	@Override
	public Map<Date, Double> getInterestRateFromProductCal(int clientId, Session session) {

		datePensionMap.clear();
		Map<Date, Double> dateInterestMap = new HashMap<>();
		ClientMasterDAO clientMasterDao = new ClientMasterDAOImpl();
		try {
			List<ClientFixedIncome> masterIncomeList = clientMasterDao.getClientIncomeListById(clientId, session);

			// SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat lookupdateFormat = new SimpleDateFormat("dd-MMM-yyyy");

			List<BankFDTDRLookup> bankFDTDRLookupList = new ArrayList<>();
			List<BankBondDebenturesLookup> bondLookupList = new ArrayList<>();
			List<PerpetualBondLookup> perpetualBondList = new ArrayList<>();
			List<PostOfficeMonthlyIncomeSchemeLookup> postMISLookupList = new ArrayList<>();
			List<SeniorCitizenSavingSchemeLookup> seniorCitLookupList = new ArrayList<>();
			List<Annuity2ProductLookup> annuity2ProdLookupList = new ArrayList<>();
			List<AtalPensionYojanaLookup> atpProdLookupList = new ArrayList<>();
			List<POTimeDepositLookup> postOfficeTimeList = new ArrayList<>();
			try {

				for (ClientFixedIncome fixedIncome : masterIncomeList) {
					if (fixedIncome.getMasterProductClassification().getId() == FinexaConstant.BANK_FIXED_DEPOSITS_ID) {
						if (fixedIncome.getLookupFrequency2() != null) {
							BankFDTDRService fdtdrService = new BankFDTDRService();
							BankFDTDRPC fdtdrc = fdtdrService.getFDTDROutput(fixedIncome.getAmount().doubleValue(),
									fixedIncome.getInterestCouponRate().doubleValue(), fixedIncome.getTenureYearsDays(),
									fixedIncome.getTenure(), fixedIncome.getLookupFrequency2().getId(),
									fixedIncome.getInvestmentDepositDate());

							if (bankFDTDRLookupList.size() > 0) {
								bankFDTDRLookupList.addAll(fdtdrc.getBankFdTdrLookupList());
							} else {
								bankFDTDRLookupList = fdtdrc.getBankFdTdrLookupList();
							}
						}
						// System.out.println(bankFDTDRLookupList.size());

					}

					if (fixedIncome.getMasterProductClassification().getId() == FinexaConstant.BONDS_DEBENTURES_ID) {
						// Converting tenure into years if it is entered in Days
						int tenureYears = 0;
						if (fixedIncome.getTenure() != null) {
							tenureYears = fixedIncome.getTenure();
						}
						/*
						 * if (fixedIncome.getTenureYearsDays().equals("D")) { // tenure in Days if
						 * (fixedIncome.getTenure() != null) { Date cDate = new Date(); long[]
						 * tenureYearsLong = new FinexaDateUtil().getYearCountByDay(cDate,
						 * fixedIncome.getTenure()); tenureYears = (int) tenureYearsLong[0]; }
						 * 
						 * } else { if (fixedIncome.getTenure() != null) { tenureYears =
						 * fixedIncome.getTenure(); } }
						 */
						// for Bond with Coupon
						if (fixedIncome.getLookupBondType().getId() == FinexaConstant.BOND_TYPE_BOND_WITH_COUPON_ID) {
							BondDebenturesService bondService = new BondDebenturesService();

							BondDebentures dbentureReturn = bondService.calculateDebenturesValue(
									fixedIncome.getTenureYearsDays(), fixedIncome.getInvestmentDepositDate(),
									String.valueOf(tenureYears), fixedIncome.getLookupFrequency2().getId(),
									fixedIncome.getInterestCouponRate().doubleValue(),
									fixedIncome.getBondFaceValue().doubleValue(), fixedIncome.getBondPurchased(),
									fixedIncome.getBondCurrentYield().doubleValue());

							List<BankBondDebenturesLookup> bbDLookupList = new BondDebenturesService()
									.getBondDebentures(fixedIncome.getBondPurchased(),
											fixedIncome.getBondFaceValue().doubleValue(),
											fixedIncome.getLookupFrequency2().getId(),
											fixedIncome.getInvestmentDepositDate(), dbentureReturn.getDaysToMaturity(),
											dbentureReturn.getCouponReceived(), dbentureReturn.getTotalMonths());

							if (bondLookupList.size() > 0) {
								bondLookupList.addAll(bbDLookupList);
							} else {
								bondLookupList = bbDLookupList;
							}
						}

						// for Perpetual Bond
						if (fixedIncome.getLookupBondType().getId() == FinexaConstant.BOND_TYPE_PERPETUAL_BOND_ID) {
							PerpetualBondService perpetualBondService = new PerpetualBondService();

							PerpetualBond perpetualBond = perpetualBondService.calculateDebenturesValue(
									fixedIncome.getTenureYearsDays(), fixedIncome.getInvestmentDepositDate(),
									String.valueOf(tenureYears), fixedIncome.getLookupFrequency2().getId(),
									fixedIncome.getInterestCouponRate().doubleValue(),
									fixedIncome.getBondFaceValue().doubleValue(), fixedIncome.getBondPurchased(),
									fixedIncome.getBondCurrentYield().doubleValue());
							List<PerpetualBondLookup> pbLokUpList = new PerpetualBondService().getBondDebentures(
									fixedIncome.getBondFaceValue().doubleValue(),
									fixedIncome.getLookupFrequency2().getId(), fixedIncome.getInvestmentDepositDate(),
									perpetualBond.getDaysToMaturity(), perpetualBond.getCouponReceived(),
									perpetualBond.getTotalMonths());
							// System.out.println(bondLookupList.size());
							if (perpetualBondList.size() > 0) {
								perpetualBondList.addAll(pbLokUpList);
							} else {
								perpetualBondList = pbLokUpList;
							}
						}

					}

				}
				List<ClientSmallSaving> masterSmallSavingsList = clientMasterDao.getClientSmallSavingsListById(clientId,
						session);

				for (ClientSmallSaving clientSmallSaving : masterSmallSavingsList) {

					// for PO Time Deposit
					if (clientSmallSaving.getMasterProductClassification()
							.getId() == FinexaConstant.PO_TIME_DEPOSIT_ID) {
						// need to check again
						POTimeDeposit POTimeDepositScheme = new POTimeDespositService().getTimeDepositCalculatedList(
								clientSmallSaving.getInvestmentAmount().doubleValue(),
								clientSmallSaving.getDepositTenure(), clientSmallSaving.getLookupFrequency1().getId(),
								clientSmallSaving.getLookupFrequency2().getId(), clientSmallSaving.getStartDate(),
								clientSmallSaving.getInterestRate().doubleValue());

						if (postOfficeTimeList.size() > 0) {
							postOfficeTimeList.addAll(POTimeDepositScheme.getPoTimeDepositLookupList());
						} else {
							postOfficeTimeList = POTimeDepositScheme.getPoTimeDepositLookupList();
						}

						// System.out.println(bankFDTDRLookupList.size());

					}

					// for POMIS
					if (clientSmallSaving.getMasterProductClassification().getId() == FinexaConstant.PO_MIS_ID) {
						// need to check again
						PostOfficeMonthlyIncomeScheme postOffMonScheme = new PostOfficeMonthlyIncomeSchemeService()
								.getPostOfficeMISCal(clientSmallSaving.getInvestmentAmount().doubleValue(),
										clientSmallSaving.getDepositTenure(),
										clientSmallSaving.getLookupFrequency2().getId(),
										clientSmallSaving.getStartDate());
						if (postMISLookupList.size() > 0) {
							postMISLookupList.addAll(postOffMonScheme.getPostOfficeMISLookupList());
						} else {
							postMISLookupList = postOffMonScheme.getPostOfficeMISLookupList();
						}

						// System.out.println(bankFDTDRLookupList.size());

					}

					// for SCSS
					if (clientSmallSaving.getMasterProductClassification().getId() == FinexaConstant.SCSS_ID) {
						SeniorCitizenSavingScheme seniorCit = new SeniorCitizenSavingSchemeService()
								.getSeniorCitizenSaingSchemeCal(clientSmallSaving.getInvestmentAmount().doubleValue(),
										clientSmallSaving.getDepositTenure(),
										clientSmallSaving.getLookupFrequency2().getId(),
										clientSmallSaving.getStartDate());
						if (seniorCitLookupList.size() > 0) {
							seniorCitLookupList.addAll(seniorCit.getSeniorCitizenSavingSchemeLookupsList());
						} else {
							seniorCitLookupList = seniorCit.getSeniorCitizenSavingSchemeLookupsList();
						}
					}
				}

				double clientlifeExpectancy = 0;
				ClientFamilyMemberDAO clientFamilyMemberDao = new ClientFamilyMemberDAOImpl();
				List<ClientFamilyMember> clientFamilyList = clientFamilyMemberDao
						.getAllClientFamilyMemberByClientId(clientId, session);
				for (ClientFamilyMember familyMember : clientFamilyList) {
					if (familyMember.getLookupRelation().getId() == 0) {
						clientlifeExpectancy = familyMember.getLifeExpectancy();
					}
				}
				List<ClientAnnuity> clientAnnuityList = clientMasterDao.getClientAnnuityListById(clientId, session);

				for (ClientAnnuity annuity : clientAnnuityList) {

					double clientFamlifeExpectancy = 0;
					if (annuity.getClientFamilyMember() != null
							&& annuity.getClientFamilyMember().getLifeExpectancy() != null) {
						clientFamlifeExpectancy = annuity.getClientFamilyMember().getLifeExpectancy();
					}
					double pensionableCorpus = 0.0;
					double annuityRate = 0.0;
					if (annuity.getLookupAnnuityType().getId() != FinexaConstant.EPS_ANNUITY) {
						pensionableCorpus = annuity.getPensionableCorpus().doubleValue();
						annuityRate = annuity.getAnnuityRate().doubleValue();
					}
					double growthRate = 0.0;
					if (annuity.getGrowthRate() != null) {
						growthRate = annuity.getGrowthRate().doubleValue();
					}
					Annuity2ProductCalculator annuity2 = new Annuity2ProductService().getAnnuityProductValues(
							annuity.getClientMaster().getBirthDate(), pensionableCorpus, annuityRate,
							clientlifeExpectancy, clientFamlifeExpectancy, annuity.getLookupFrequency().getId(),
							annuity.getAnnuityStartDate(), annuity.getLookupAnnuityType().getId(), growthRate,
							annuity.getId());

					if (annuity2ProdLookupList.size() > 0) {
						annuity2ProdLookupList.addAll(annuity2.getAnnuityLookupList());
					} else {
						annuity2ProdLookupList = annuity2.getAnnuityLookupList();
					}

				}

				List<ClientAtalPensionYojana> clientATPList = clientMasterDao.getClientATPListById(clientId, session);
				for (ClientAtalPensionYojana atp : clientATPList) {

					int clientFamlifeExpectancy = 0;
					int clientRetirementAge = 0;
					if (atp.getClientFamilyMember() != null
							&& atp.getClientFamilyMember().getLifeExpectancy() != null) {
						clientFamlifeExpectancy = atp.getClientFamilyMember().getLifeExpectancy();
						if (atp.getClientFamilyMember().getRetirementAge() != null)
							clientRetirementAge = atp.getClientFamilyMember().getRetirementAge();
					}
					if (clientFamlifeExpectancy > 0 && clientRetirementAge > 0) {
						AtalPensionYojana atpServ = new AtalPensionYojanaService().getAtalPensionYojanaCal(
								atp.getClientFamilyMember().getBirthDate(), atp.getLookupFrequency().getId(),
								atp.getMonthlyPensionRequired().doubleValue(), FinexaConstant.APY_RETIREMENT_AGE,
								atp.getApyStartDate(), clientFamlifeExpectancy);

						if (atpProdLookupList.size() > 0) {
							atpProdLookupList.addAll(atpServ.getAtalPensionYojanaLookupList());
						} else {
							atpProdLookupList = atpServ.getAtalPensionYojanaLookupList();
						}
					}
				}

				// System.out.println("BankFDTDRLookup");

				for (BankFDTDRLookup bankFdTdrLook : bankFDTDRLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(bankFdTdrLook.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateInterestMap.size() != 0) {
						Double interestAmount = 0.0;
						if (dateInterestMap.get(cal1.getTime()) != null) {
							interestAmount = dateInterestMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						bankFdTdrLook.setInterestReceived(bankFdTdrLook.getInterestReceived() + interestAmount);
						dateInterestMap.put(cal1.getTime(), bankFdTdrLook.getInterestReceived());
						// System.out.println(cal1.getTime() + " " +
						// dateInterestMap.get(cal1.getTime()));
					} else {
						dateInterestMap.put(cal1.getTime(), bankFdTdrLook.getInterestReceived());
					}
				}
				// System.out.println(" ");

				// 2
				// System.out.println("BankBondDebenturesLookup");
				for (BankBondDebenturesLookup bondDebenLook : bondLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(bondDebenLook.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateInterestMap.size() != 0) {
						Double interestAmount = 0.0;
						if (dateInterestMap.get(cal1.getTime()) != null) {
							interestAmount = dateInterestMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						bondDebenLook.setCouponReceived(bondDebenLook.getCouponReceived() + interestAmount);
						dateInterestMap.put(cal1.getTime(), bondDebenLook.getCouponReceived());
						// System.out.println("BankBondDebenturesLookup " +
						// cal1.getTime() + " "
						// + dateInterestMap.get(cal1.getTime()));
					} else {
						dateInterestMap.put(cal1.getTime(), bondDebenLook.getCouponReceived());
					}
				}

				for (PerpetualBondLookup perpetualBondLook : perpetualBondList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(perpetualBondLook.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateInterestMap.size() != 0) {
						Double interestAmount = 0.0;
						if (dateInterestMap.get(cal1.getTime()) != null) {
							interestAmount = dateInterestMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						perpetualBondLook.setCouponReceived(perpetualBondLook.getCouponReceived() + interestAmount);
						dateInterestMap.put(cal1.getTime(), perpetualBondLook.getCouponReceived());
						// System.out.println("BankBondDebenturesLookup " +
						// cal1.getTime() + " "
						// + dateInterestMap.get(cal1.getTime()));
					} else {
						dateInterestMap.put(cal1.getTime(), perpetualBondLook.getCouponReceived());
					}
				}

				// System.out.println(" ");
				// 3
				// System.out.println("PostOfficeMonthlyIncomeSchemeLookup");
				for (PostOfficeMonthlyIncomeSchemeLookup postOffIncShemeLookup : postMISLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(postOffIncShemeLookup.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateInterestMap.size() != 0) {
						Double interestAmount = 0.0;
						if (dateInterestMap.get(cal1.getTime()) != null) {
							interestAmount = dateInterestMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						postOffIncShemeLookup
								.setInterestReceived(postOffIncShemeLookup.getInterestReceived() + interestAmount);
						dateInterestMap.put(cal1.getTime(), postOffIncShemeLookup.getInterestReceived());
						// System.out.println(cal1.getTime() + " " +
						// postOffIncShemeLookup.getInterestReceived());
					} else {
						dateInterestMap.put(cal1.getTime(), postOffIncShemeLookup.getInterestReceived());
					}
				}

				for (POTimeDepositLookup poTimeDepositLookup : postOfficeTimeList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(poTimeDepositLookup.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateInterestMap.size() != 0) {
						Double interestAmount = 0.0;
						if (dateInterestMap.get(cal1.getTime()) != null) {
							interestAmount = dateInterestMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						poTimeDepositLookup.setInterestPaid(poTimeDepositLookup.getInterestPaid() + interestAmount);
						dateInterestMap.put(cal1.getTime(), poTimeDepositLookup.getInterestPaid());
						// System.out.println(cal1.getTime() + " " +
						// postOffIncShemeLookup.getInterestReceived());
					} else {
						dateInterestMap.put(cal1.getTime(), poTimeDepositLookup.getInterestPaid());
					}
				}

				// System.out.println(" ");

				// 4
				// System.out.println("SeniorCitizenSavingSchemeLookup");
				for (SeniorCitizenSavingSchemeLookup seniotCitSavSchemeLookup : seniorCitLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(seniotCitSavSchemeLookup.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateInterestMap.size() != 0) {
						Double interestAmount = 0.0;
						if (dateInterestMap.get(cal1.getTime()) != null) {
							interestAmount = dateInterestMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						seniotCitSavSchemeLookup
								.setInterestReceived(seniotCitSavSchemeLookup.getInterestReceived() + interestAmount);
						dateInterestMap.put(cal1.getTime(), seniotCitSavSchemeLookup.getInterestReceived());
						// System.out.println(cal1.getTime() + " " +
						// seniotCitSavSchemeLookup.getInterestReceived());
					} else {
						dateInterestMap.put(cal1.getTime(), seniotCitSavSchemeLookup.getInterestReceived());
					}
				}
				// 6
				// System.out.println(" Annuity2ProductLookup");
				for (Annuity2ProductLookup annuity2Look : annuity2ProdLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime((annuity2Look.getRefDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);

					if (datePensionMap.size() != 0) {
						Double pensionAmount = 0.0;
						if (datePensionMap.get(cal1.getTime()) != null) {
							pensionAmount = datePensionMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						// double totalPension = 7500 + interestAmount;
						double totalPension = pensionAmount + annuity2Look.getAnnuityT1PensionRec()
								+ annuity2Look.getAnnuityT2PensionRec() + annuity2Look.getAnnuityT3PensionRec()
								+ annuity2Look.getAnnuityT4PensionRec() + annuity2Look.getAnnuityT5PensionRec()
								+ annuity2Look.getEpsAnnuityPensionRec();
						datePensionMap.put(cal1.getTime(), totalPension);
					} else {
						double pension = annuity2Look.getAnnuityT1PensionRec() + annuity2Look.getAnnuityT2PensionRec()
								+ annuity2Look.getAnnuityT3PensionRec() + annuity2Look.getAnnuityT4PensionRec()
								+ annuity2Look.getAnnuityT5PensionRec() + annuity2Look.getEpsAnnuityPensionRec();
						datePensionMap.put(cal1.getTime(), pension);
						// dateInterestMap.put(cal1.getTime(), 7500.0);
					}
				}

				for (AtalPensionYojanaLookup atpLook : atpProdLookupList) {
					Calendar cal1 = Calendar.getInstance();
					Date refDate = lookupdateFormat.parse(atpLook.getReferenceDate());
					cal1.setTime(refDate);
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);

					if (datePensionMap.size() != 0) {
						Double pensionAmount = 0.0;
						if (datePensionMap.get(cal1.getTime()) != null) {
							pensionAmount = datePensionMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						// double totalInterest = 7500 + interestAmount;
						double totalPension = pensionAmount + atpLook.getAnnuityAmount();
						datePensionMap.put(cal1.getTime(), totalPension);
					} else {
						double pensionAmount = atpLook.getAnnuityAmount();
						datePensionMap.put(cal1.getTime(), pensionAmount);
						// dateInterestMap.put(cal1.getTime(), 7500.0);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println(fixedIncome.toString());

		} catch (

		FinexaDaoException e) {
			e.printStackTrace();
		}
		// System.out.println("size" + dateInterestMap.size());
		return dateInterestMap;

	}

	@Override
	public List<FamilyMemberIncomeDetailOutput> getIncomeOfFamilyMember(int memberId, Session session)
			throws FinexaBussinessException {

		ClientFamilyIncomeDAO clientFamilyDaoImpl = new ClientFamilyIncomeDAOImpl();
		List<ClientFamilyIncome> familyMemberOutputList = null;
		List<FamilyMemberIncomeDetailOutput> familyMemberList = new ArrayList<FamilyMemberIncomeDetailOutput>();
		MasterdataIncomeGrowthRateDAO masterdataIncomeGrowthRateDAO = new MasterdataIncomeGrowthRateDAOImpl();
		try {
			familyMemberOutputList = clientFamilyDaoImpl.getFamilyMemberIncome(memberId, session);
			if (familyMemberOutputList != null && !familyMemberOutputList.isEmpty()) {

				for (ClientFamilyIncome obj : familyMemberOutputList) {
					FamilyMemberIncomeDetailOutput output = new FamilyMemberIncomeDetailOutput();
					output.setMemberId(memberId);

					output.setIncome(round(obj.getIncomeAmount().doubleValue(), 2));
					output.setContinueUpto(obj.getLookupIncomeExpenseDuration().getDescription());

					MasterIncomeGrowthRate growthrate = masterdataIncomeGrowthRateDAO
							.getIncomeGrowthRateByIncomeHead(obj.getIncomeType(), session);
					if (growthrate == null) {
						output.setAnnualIncomeGrowthRate(0.0);
						output.setIncomeCategory("");
					} else {
						output.setAnnualIncomeGrowthRate(growthrate.getCagr().doubleValue());
						output.setIncomeCategory(growthrate.getIncomeCategory());
					}
					output.setFrequency(obj.getLookupFrequency().getDescription());
					output.setReferenceMonth(obj.getLookupMonth().getDescription());
					familyMemberList.add(output);
				}
			}

		} catch (Exception e) {
		}

		return familyMemberList;
	}
}