package com.finlabs.finexa.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Session;

import com.finlabs.finexa.dto.AnnualExpensesDetailed;
import com.finlabs.finexa.dto.ClientDetailedOutput;
import com.finlabs.finexa.dto.CommittedOutFlowOutput;
import com.finlabs.finexa.dto.FamilyMemberIncomeDetailOutput;
import com.finlabs.finexa.dto.MasterExpenseIndustryStandardDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientLifeInsurance;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.LookupIncomeExpenseDuration;
import com.finlabs.finexa.model.MasterExpenseGrowthRate;
import com.finlabs.finexa.model.MasterExpenseIndustryStandard;
import com.finlabs.finexa.repository.ClientFamilyExpenseDAO;
import com.finlabs.finexa.repository.ClientLifeInsuranceDAO;
import com.finlabs.finexa.repository.ClientMasterDAO;
import com.finlabs.finexa.repository.ClientNonLifeInsuranceDAO;
import com.finlabs.finexa.repository.MasterdataExpenseGrowthRateDAO;
import com.finlabs.finexa.repository.impl.ClientFamilyExpenseDAOImpl;
import com.finlabs.finexa.repository.impl.ClientLifeInsuranceDAOmpl;
import com.finlabs.finexa.repository.impl.ClientMasterDAOImpl;
import com.finlabs.finexa.repository.impl.ClientNonLifeInsuranceDAOmpl;
import com.finlabs.finexa.repository.impl.MasterdataExpenseGrowthRateDAOImpl;
import com.finlabs.finexa.resources.model.AtalPensionYojana;
import com.finlabs.finexa.resources.model.AtalPensionYojanaLookup;
import com.finlabs.finexa.resources.model.BankRecurringDeposit;
import com.finlabs.finexa.resources.model.BankRecurringDepositLookup;
import com.finlabs.finexa.resources.model.PORecurringDeposit;
import com.finlabs.finexa.resources.model.PORecurringDepositLookup;
import com.finlabs.finexa.resources.model.PPFFixedAmountDeposit;
import com.finlabs.finexa.resources.model.PPFFixedAmountLookup;
import com.finlabs.finexa.resources.model.SukanyaSamriddhiScheme;
import com.finlabs.finexa.resources.model.SukanyaSamriddhiSchemeLookup;
import com.finlabs.finexa.resources.service.AtalPensionYojanaService;
import com.finlabs.finexa.resources.service.BankRecurringDespositService;
import com.finlabs.finexa.resources.service.PORecurringDespositService;
import com.finlabs.finexa.resources.service.PPFFixedAmountService;
import com.finlabs.finexa.resources.service.SukanyaSamriddhiSchemeService;
import com.finlabs.finexa.service.ClientExpenseService;
import com.finlabs.finexa.util.FinexaConstant;

//@Transactional
//@Service
public class ClientExpenseServiceImpl implements ClientExpenseService {

	// @Autowired
	// private ClientFamilyExpenseDAO clientFamilyExpenseDAO;
	// @Autowired
	// private ClientLifeInsuranceDAO clientLifeInsuranceDAO;

	// @Autowired
	// private ClientNonLifeInsuranceDAO clientNonLifeInsuranceDAO;
	private Calendar endDate = Calendar.getInstance();
	// @Autowired
	// private ClientMasterDAO clientMasterDao;

//	@Autowired
//	private MasterdataExpenseGrowthRateDAO masterdataExpenseGrowthRateDAO;
	private HashMap<Integer, ArrayList<Integer>> familyExpenseMap = new HashMap<Integer, ArrayList<Integer>>();
	private HashMap<Integer, ArrayList<Integer>> familyLifeInsuranceMap = new HashMap<Integer, ArrayList<Integer>>();
	private HashMap<Integer, ArrayList<Integer>> familyNonLifeInsuranceMap = new HashMap<Integer, ArrayList<Integer>>();
	private String monthArray[] = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public Calendar getEndDate(int years, Date dob) {

		endDate.setTime(dob);
		endDate.add(Calendar.MONTH, years * 12);

		return endDate;
	}

	@Override
	public AnnualExpensesDetailed getAnnualExpensesDetailed(int clientId, String mode, int financialPlanningFlag,
			Session session) throws FinexaBussinessException {

		AnnualExpensesDetailed expenseOutput = new AnnualExpensesDetailed();
		ArrayList<AnnualExpensesDetailed> expenseOutputLookupList = new ArrayList<AnnualExpensesDetailed>();
		Calendar cInterimDate = Calendar.getInstance();
		ClientFamilyExpenseDAO clientFamilyExpenseDAO = new ClientFamilyExpenseDAOImpl();
		MasterdataExpenseGrowthRateDAO masterdataExpenseGrowthRateDAO = new MasterdataExpenseGrowthRateDAOImpl();
		try {
			ClientMaster client = clientFamilyExpenseDAO.getClientById(clientId, session);
			List<ClientExpense> clientExpenseList = client.getClientExpenses();
			if (clientExpenseList != null && !clientExpenseList.isEmpty()) {

				Date planRefDate = new Date();// Getting current date
				double expenseGrowthRate = 0;
				cInterimDate.setTime(planRefDate);
				cInterimDate.set(Calendar.MILLISECOND, 0);
				cInterimDate.set(Calendar.SECOND, 0);
				cInterimDate.set(Calendar.MINUTE, 0);
				cInterimDate.set(Calendar.HOUR, 0);

				Calendar maxDate = Calendar.getInstance();
				Calendar tempDate = Calendar.getInstance();

				// Fetching Life Expectancy of Client

				ClientFamilyMember clientFamilyMember = null;
				try {
					clientFamilyMember = clientFamilyExpenseDAO.getClientFromFamilyMemberById(clientId, session);
				} catch (Exception e) {
				}

				int lifeExp = clientFamilyMember.getLifeExpectancy();

				if (lifeExp > 0) {
					tempDate.setTime(clientFamilyMember.getBirthDate());
					if (clientFamilyMember.getLifeExpectancy() != null) {
						tempDate.add(Calendar.MONTH, (int) (clientFamilyMember.getLifeExpectancy() * 12));
						maxDate.setTime(tempDate.getTime());
					} else {
						FinexaBussinessException businessException = new FinexaBussinessException("ClientExpense",
								"111",
								"Life Expectancy for the client needs to be entered to do Budget Analysis for the client.");
						throw businessException;
					}
				}

				// Fetching Each Expense Growth Rate

				Map<Byte, Double> expenseGrowthRateMap = new HashMap<>();
				for (ClientExpense expense : clientExpenseList) {
					MasterExpenseGrowthRate masterdataExpenseGrowthRate = masterdataExpenseGrowthRateDAO
							.getExpenseGrowthRateByIncomeHead(expense.getExpenseType(), session);

					if (masterdataExpenseGrowthRate != null) {
						// expense will be calculated from database
						// expenseGrowthRate =
						// masterdataExpenseGrowthRate.getCagr() / (double)
						// 100;
						expenseGrowthRate = masterdataExpenseGrowthRate.getCagr().doubleValue();
						expenseGrowthRateMap.put(expense.getExpenseType(), expenseGrowthRate);
					}

				}

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

				for (int currentYear = startYearOfIncome; currentYear <= maxDate.get(Calendar.YEAR); currentYear++) {
					AnnualExpensesDetailed expenseYearly = new AnnualExpensesDetailed();
					String currentFinYear = currentYear + "-" + (currentYear + 1);
					// System.out.println("For Year---------------" +
					// currentFinYear + "-----------------------");

					double yearlygroceriesExpense = 0;
					double yearlyUtilitiesExpense = 0;
					double yearlyTransportExpense = 0;
					double yearlyHouseholsPersonalExpense = 0;
					double yearlyHousingMaintainanceExpense = 0;
					double yearlyCommunicationExpense = 0;
					double yearlyLifeStyleExpense = 0;
					double yearlyApparelExpense = 0;
					double yearlyChildrenFeeExpense = 0;
					double yearlyHealthcareExpense = 0;
					double yearlyOtherExpense = 0;
					double yearlyTotalFamilyExpense = 0;
					double yearlyLivingExpense = 0;
					double yearlyDiscretionaryExpense = 0;
					double yearlyTotalExpense = 0;

					do { // Calculations done against current financial year
						AnnualExpensesDetailed expenseMonthly = new AnnualExpensesDetailed();
						// System.out.println("For Month-------------------"
						// + startMonthOfIncome +
						// "--------------------------");
						double monthlygroceriesExpense = 0;
						double monthlyUtilitiesExpense = 0;
						double monthlyTransportExpense = 0;
						double monthlyHouseholsPersonalExpense = 0;
						double monthlyHousingMaintainanceExpense = 0;
						double monthlyCommunicationExpense = 0;
						double monthlyLifeStyleExpense = 0;
						double monthlyApparels = 0;
						double monthlyChildrenFeeExpense = 0;
						double monthlyHealthcareExpense = 0;
						double monthlyOtherExpense = 0;
						double monthlyTotalFamilyExpense = 0;
						double monthlyLivingExpense = 0;
						double monthlyDiscretionaryExpense = 0;
						double monthlyTotalExpense = 0;

						for (ClientExpense expense : clientExpenseList) {

							if (expenseGrowthRateMap.get(expense.getExpenseType()) == null) {
								expenseGrowthRate = 0;
							} else {
								expenseGrowthRate = expenseGrowthRateMap.get(expense.getExpenseType());
							}

							LookupIncomeExpenseDuration year = null;
							year = expense.getLookupIncomeExpenseDuration();
							if (year.getDescription().matches("[0-9]+")) {
								endDate.set(Integer.parseInt(year.getDescription()), Calendar.DECEMBER, 31);
								// System.out.println("end time" +
								// endDate.getTime());
							} else {
								if (year.getDescription().equals("Upto Retirement")) {
									endDate = this.getEndDate(clientFamilyMember.getRetirementAge(),
											clientFamilyMember.getBirthDate());
									endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));

								} else {
									if (year.getDescription().equals("Upto Life Expectancy")) {
										endDate = this.getEndDate(clientFamilyMember.getLifeExpectancy(),
												clientFamilyMember.getBirthDate());
										endDate.set(Calendar.DAY_OF_MONTH,
												endDate.getActualMaximum(Calendar.DAY_OF_MONTH));

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

							if (endDate.getTime().compareTo(cInterimDate
									.getTime()) >= 0/*
													 * endDate.getTime(). compareTo(maxDate. getTime()) <=0 &&
													 * endDate.getTime(). compareTo( cInterimDate.getTime( )) > 0
													 */) {
								// For Groceries
								if (expense.getExpenseType() == 1) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {

										monthlygroceriesExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlygroceriesExpense = yearlygroceriesExpense + monthlygroceriesExpense;

										/*
										 * System.out. println("Monthly monthlygroceriesExpense"
										 * +monthlygroceriesExpense); System.out.
										 * println("Yearly yearlygroceriesExpense" + yearlygroceriesExpense);
										 * System.out. println("Monthly Living" + monthlyLivingExpense);
										 */
									}
								}

								// For Utilities
								if (expense.getExpenseType() == 2) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyUtilitiesExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyUtilitiesExpense = yearlyUtilitiesExpense + monthlyUtilitiesExpense;

										/*
										 * System.out. println("Monthly monthlyUtilitiesExpense" +
										 * monthlyUtilitiesExpense); System.out. println("Yearly yearlyUtilitiesExpense"
										 * + yearlyUtilitiesExpense); System.out. println("Monthly Living" +
										 * monthlyLivingExpense);
										 */
									}
								}

								// For Transport
								if (expense.getExpenseType() == 3) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyTransportExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyTransportExpense = yearlyTransportExpense + monthlyTransportExpense;

										/*
										 * System.out. println("Monthly Utilities" + monthlyTransportExpense);
										 * System.out. println("Yearly Utilities" + yearlyTransportExpense); System.out.
										 * println("Monthly Living" + monthlyLivingExpense);
										 */
									}
								}

								// Household & Personal Care
								if (expense.getExpenseType() == 4) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyHouseholsPersonalExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyHouseholsPersonalExpense = yearlyHouseholsPersonalExpense
												+ monthlyHouseholsPersonalExpense;

										/*
										 * System.out. println("Monthly Utilities" + monthlyHouseholsPersonalExpense);
										 * System.out. println("Yearly Utilities" + yearlyHouseholsPersonalExpense);
										 * System.out. println("Monthly Living" + monthlyLivingExpense);
										 */
									}
								}
								// Housing & Maintenance
								if (expense.getExpenseType() == 5) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyHousingMaintainanceExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyHousingMaintainanceExpense = yearlyHousingMaintainanceExpense
												+ monthlyHousingMaintainanceExpense;

										/*
										 * System.out. println("Monthly monthlyHouseholsPersonalExpense" +
										 * monthlyHouseholsPersonalExpense); System.out.
										 * println("Yearly yearlyHousingMaintainanceExpense" +
										 * yearlyHousingMaintainanceExpense) ; System.out. println("Monthly Living" +
										 * monthlyLivingExpense);
										 */
									}
								}
								// Communication
								if (expense.getExpenseType() == 6) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyCommunicationExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyCommunicationExpense = yearlyCommunicationExpense
												+ monthlyCommunicationExpense;

										/*
										 * System.out. println("Monthly monthlyCommunicationExpense" +
										 * monthlyCommunicationExpense); System.out.
										 * println("Yearly yearlyCommunicationExpense" + yearlyCommunicationExpense);
										 * System.out. println("Monthly Living" + monthlyLivingExpense);
										 */

									}
								}
								// Life Style
								if (expense.getExpenseType() == 7) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyLifeStyleExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyLifeStyleExpense = yearlyLifeStyleExpense + monthlyLifeStyleExpense;

										/*
										 * System.out. println("Monthly monthlyLifeStyleExpense" +
										 * monthlyLifeStyleExpense); System.out. println("Yearly yearlyLifeStyleExpense"
										 * + yearlyLifeStyleExpense); System.out. println("Monthly Living" +
										 * monthlyLivingExpense);
										 */
									}
								}

								// Apparels
								if (expense.getExpenseType() == 8) {

									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyApparels = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyApparelExpense = monthlyApparels + yearlyApparelExpense;

										/*
										 * System.out. println("Monthly monthlyApparels" + monthlyApparels); System.out.
										 * println("Yearly yearlyApparelExpense" + yearlyApparelExpense); System.out.
										 * println("Monthly Living" + monthlyLivingExpense);
										 */
									}
								}

								// Children Fee
								if (expense.getExpenseType() == 9) {

									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyChildrenFeeExpense = monthlyChildrenFeeExpense
												+ expense.getExpenseAmount().doubleValue()
														* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyChildrenFeeExpense = yearlyChildrenFeeExpense + monthlyChildrenFeeExpense;

										/*
										 * System.out. println("Monthly monthlyChildrenFeeExpense" +
										 * monthlyChildrenFeeExpense); System.out.
										 * println("Yearly yearlyChildrenFeeExpense" + yearlyChildrenFeeExpense);
										 * System.out. println("Monthly Living" + monthlyLivingExpense);
										 */

									}

								}

								// Healthcare Expenses
								if (expense.getExpenseType() == 10) {

									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyHealthcareExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyHealthcareExpense = yearlyHealthcareExpense + monthlyHealthcareExpense;

										/*
										 * System.out. println("Monthly monthlyHealthcareExpense" +
										 * monthlyHealthcareExpense); System.out.
										 * println("Yearly yearlyHealthcareExpense" + yearlyHealthcareExpense);
										 * System.out. println("Monthly Living" + monthlyLivingExpense);
										 */

									}
								}

								// Others
								if (expense.getExpenseType() == 11) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyOtherExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyOtherExpense = yearlyOtherExpense + monthlyOtherExpense;

										/*
										 * System.out. println("Monthly monthlyOtherExpense" + monthlyOtherExpense);
										 * System.out. println("Yearly yearlyOtherExpense" + yearlyOtherExpense);
										 */
									}
								}

								// Total expense Category
								if (expense.getExpenseType() == 12) {
									if (valueToBeAdded(startMonthOfIncome, expense.getLookupMonth().getId(),
											12 / expense.getLookupFrequency().getId(), endDate, maxDate,
											expense.getExpenseType())) {
										monthlyTotalFamilyExpense = expense.getExpenseAmount().doubleValue()
												* Math.pow((1 + expenseGrowthRate), (yearCounter - 1));
										yearlyTotalFamilyExpense = yearlyTotalFamilyExpense + monthlyTotalFamilyExpense;

										/*
										 * System.out. println("Monthly monthlyOtherExpense" + monthlyOtherExpense);
										 * System.out. println("Yearly yearlyOtherExpense" + yearlyOtherExpense);
										 */
									}
								}

								/*
								 * System.out.println("MonthlyLiving" + monthlyLivingExpense);
								 * System.out.println("MonthlyDiscretionary" + monthlyDiscretionaryExpense);
								 */

								// System.out.println("Monthly Total" +
								// monthlyTotalExpense);

							}

						}

						monthlyLivingExpense = monthlygroceriesExpense + monthlyUtilitiesExpense
								+ monthlyTransportExpense + monthlyHouseholsPersonalExpense
								+ monthlyHousingMaintainanceExpense + monthlyCommunicationExpense
								+ +monthlyChildrenFeeExpense + monthlyHealthcareExpense + monthlyTotalFamilyExpense;

						monthlyDiscretionaryExpense = monthlyLifeStyleExpense + monthlyApparels + monthlyOtherExpense;

						monthlyTotalExpense = monthlyDiscretionaryExpense + monthlyLivingExpense;

						yearlyLivingExpense = yearlyLivingExpense + monthlyLivingExpense;
						yearlyDiscretionaryExpense = monthlyDiscretionaryExpense + yearlyDiscretionaryExpense;
						yearlyTotalExpense = yearlyLivingExpense + yearlyDiscretionaryExpense;
						/*
						 * System.out.println("yearlyLivingExpense" + yearlyLivingExpense);
						 * System.out.println("yearlyDiscretionaryExpense" +
						 * yearlyDiscretionaryExpense); System.out.println("Yearly Total" +
						 * yearlyTotalExpense);
						 */

						if (mode.equals("monthly")) {

							// System.out.println("For Year---------------"
							// + currentFinYear +
							// "-----------------------");
							expenseMonthly
									.setFinYear(monthArray[cInterimDate.get(Calendar.MONTH)] + "," + currentFinYear);
							expenseMonthly.setGroceries_amt(round(monthlygroceriesExpense, 2));
							expenseMonthly.setUtilities_amt(round(monthlyUtilitiesExpense, 2));
							expenseMonthly.setTransport_amt(round(monthlyTransportExpense, 2));
							expenseMonthly.setHouseHoldPersonal_amt(round(monthlyHouseholsPersonalExpense, 2));
							expenseMonthly.setHousing_amt(round(monthlyHousingMaintainanceExpense, 2));
							expenseMonthly.setCommunication_amt(round(monthlyCommunicationExpense, 2));
							expenseMonthly.setLifeStyle_amt(round(monthlyLifeStyleExpense, 2));
							expenseMonthly.setApparels_amt(round(monthlyApparels, 2));
							expenseMonthly.setChildrenFees_amt(round(monthlyChildrenFeeExpense, 2));
							expenseMonthly.setHealthCare_amt(round(monthlyHealthcareExpense, 2));
							expenseMonthly.setOthers_amt(round(monthlyOtherExpense, 2));
							expenseMonthly.setTotalFamilyExpense(round(monthlyTotalFamilyExpense, 2));
							expenseMonthly.setLivingExpense(round(monthlyLivingExpense, 2));
							expenseMonthly.setDiscretionaryExpense(round(monthlyDiscretionaryExpense, 2));
							expenseMonthly.setTotalExpense(round(monthlyTotalExpense, 2));
							expenseMonthly.setExpenseProjectionList(null);
							if (monthlyTotalExpense > 0) {
								expenseOutputLookupList.add(expenseMonthly);
							}
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
						FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
								"mode not found , Please select a valid mode");
						throw businessException;
					}

					/*
					 * System.out.println("yearlyLivingExpense" + yearlyLivingExpense);
					 * System.out.println("yearlyDiscretionaryExpense" +
					 * yearlyDiscretionaryExpense);
					 */
					if (mode.equals("yearly")) {
						expenseYearly.setFinYear(currentFinYear);
						expenseYearly.setGroceries_amt(round(yearlygroceriesExpense, 2));
						expenseYearly.setUtilities_amt(round(yearlyUtilitiesExpense, 2));
						expenseYearly.setTransport_amt(round(yearlyTransportExpense, 2));
						expenseYearly.setHouseHoldPersonal_amt(round(yearlyHouseholsPersonalExpense, 2));
						expenseYearly.setHousing_amt(round(yearlyHousingMaintainanceExpense, 2));
						expenseYearly.setCommunication_amt(round(yearlyCommunicationExpense, 2));
						expenseYearly.setLifeStyle_amt(round(yearlyLifeStyleExpense, 2));
						expenseYearly.setApparels_amt(round(yearlyApparelExpense, 2));
						expenseYearly.setChildrenFees_amt(round(yearlyChildrenFeeExpense, 2));
						expenseYearly.setHealthCare_amt(round(yearlyHealthcareExpense, 2));
						expenseYearly.setOthers_amt(round(yearlyOtherExpense, 2));
						expenseYearly.setTotalFamilyExpense(round(yearlyTotalFamilyExpense, 2));
						expenseYearly.setLivingExpense(round(yearlyLivingExpense, 2));
						expenseYearly.setDiscretionaryExpense(round(yearlyDiscretionaryExpense, 2));
						expenseYearly.setTotalExpense(round(yearlyTotalExpense, 2));
						expenseYearly.setExpenseProjectionList(null);

						expenseOutputLookupList.add(expenseYearly);
					}

					yearCounter++;

				}
				familyExpenseMap.clear();
				expenseOutput.setFinYear(expenseOutputLookupList.get(0).getFinYear());
				expenseOutput.setGroceries_amt(expenseOutputLookupList.get(0).getGroceries_amt());
				expenseOutput.setUtilities_amt(expenseOutputLookupList.get(0).getUtilities_amt());
				expenseOutput.setTransport_amt(expenseOutputLookupList.get(0).getTransport_amt());
				expenseOutput.setHouseHoldPersonal_amt(expenseOutputLookupList.get(0).getHouseHoldPersonal_amt());
				expenseOutput.setHousing_amt(expenseOutputLookupList.get(0).getHousing_amt());
				expenseOutput.setCommunication_amt(expenseOutputLookupList.get(0).getCommunication_amt());
				expenseOutput.setLifeStyle_amt(expenseOutputLookupList.get(0).getLifeStyle_amt());
				expenseOutput.setApparels_amt(expenseOutputLookupList.get(0).getApparels_amt());
				expenseOutput.setChildrenFees_amt(expenseOutputLookupList.get(0).getChildrenFees_amt());
				expenseOutput.setHealthCare_amt(expenseOutputLookupList.get(0).getHealthCare_amt());
				expenseOutput.setOthers_amt(expenseOutputLookupList.get(0).getOthers_amt());
				expenseOutput.setTotalFamilyExpense(expenseOutputLookupList.get(0).getTotalFamilyExpense());
				expenseOutput.setLivingExpense(expenseOutputLookupList.get(0).getLivingExpense());
				expenseOutput.setDiscretionaryExpense(expenseOutputLookupList.get(0).getDiscretionaryExpense());
				expenseOutput.setTotalExpense(expenseOutputLookupList.get(0).getTotalExpense());

				expenseOutput.setExpenseProjectionList(expenseOutputLookupList);
				return expenseOutput;

			} else {
				expenseOutput = new AnnualExpensesDetailed();
				return expenseOutput;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaBussinessException("", "", "", e);
		}
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

	@Override
	public CommittedOutFlowOutput getClientCommitedOutFlows(int clientId, String mode, int financialPlanningFlag,
			Session session) throws FinexaBussinessException {
		CommittedOutFlowOutput overAllcommitedOutFlow = new CommittedOutFlowOutput();
		List<CommittedOutFlowOutput> overAllCommitedOutflowList = new ArrayList<CommittedOutFlowOutput>();
		ClientLifeInsuranceDAO clientLifeInsuranceDAO = new ClientLifeInsuranceDAOmpl();
		ClientNonLifeInsuranceDAO clientNonLifeInsuranceDAO = new ClientNonLifeInsuranceDAOmpl();
		try {

			Calendar currentDate = Calendar.getInstance();

			Calendar maxDate = Calendar.getInstance();
			List<ClientLifeInsurance> lifeInsuranceList = clientLifeInsuranceDAO.getClientAllLifeInsurance(clientId,
					session);
			List<ClientNonLifeInsurance> nonLifeInsuranceList = clientNonLifeInsuranceDAO
					.getClientAllNonLifeInsurance(clientId, session);

			Map<Date, Double> getAmountMap = null;
			try {
				getAmountMap = this.getDepositAmountFromProductCal(clientId, session);
			} catch (Exception e) {

			}
			if (null != getAmountMap && !getAmountMap.isEmpty()) {
				Set<Date> allDates = getAmountMap.keySet();
				maxDate.setTime((Date) allDates.toArray()[allDates.size() - 1]);
			}

			for (ClientLifeInsurance obj : lifeInsuranceList) {
				Calendar policyStartDate = Calendar.getInstance();
				policyStartDate.setTime(obj.getPolicyStartDate());
				policyStartDate.add(Calendar.MONTH, (obj.getPremiumTenure() * 12));
				if (maxDate.getTime().compareTo(policyStartDate.getTime()) < 0) {
					maxDate = policyStartDate;
				}

			}

			for (ClientNonLifeInsurance obj : nonLifeInsuranceList) {
				Calendar policyStartDate = Calendar.getInstance();
				policyStartDate.setTime(obj.getPolicyEndDate());
				if (maxDate.getTime().compareTo(policyStartDate.getTime()) < 0) {
					maxDate = policyStartDate;
				}
			}
			//System.out.println("EndDate" + maxDate.getTime());

			// for pro rata

			int startYear = currentDate.get(Calendar.YEAR);
			if (currentDate.get(Calendar.MONTH) < 3) {
				// currentDate.set(Calendar.YEAR,startYear-1);
				startYear = startYear - 1;
			}
			int startMonth = currentDate.get(Calendar.MONTH) + 1;
			if (financialPlanningFlag == 0) {
				startMonth = 4;
				currentDate.set(Calendar.MONTH, startMonth - 1);
				currentDate.set(Calendar.YEAR, startYear);
			}
			currentDate.set(Calendar.DAY_OF_MONTH, 1);
			for (int currentYear = startYear; currentYear <= maxDate.get(Calendar.YEAR); currentYear++) {

				String fiscalYearCO = currentYear + "-" + (currentYear + 1);

				double yearlyLifePremium = 0.0;
				double yearlyGeneralPremium = 0.0;
				double yearlyHealthPremium = 0.0;
				double yearlyInvestmentAmount = 0.0;

				do {
					double monthlyLifePremium = 0.0;
					double monthlyGeneralPremium = 0.0;
					double monthlyHealthPremium = 0.0;
					double monthlyInvestmentAmount = 0.0;

					Calendar currentDateCpy = currentDate;
					currentDateCpy.set(Calendar.DAY_OF_MONTH, currentDateCpy.getActualMaximum(Calendar.DAY_OF_MONTH));
					currentDateCpy.set(Calendar.HOUR_OF_DAY, 12);
					currentDateCpy.set(Calendar.MINUTE, 0);
					currentDateCpy.set(Calendar.SECOND, 0);
					currentDateCpy.set(Calendar.MILLISECOND, 0);

					// System.out.println(cStartDate.getTime());

					Double depositAmount = getAmountMap.get(currentDateCpy.getTime());
					if (depositAmount == null) {
						depositAmount = 0.0;
					}
					monthlyInvestmentAmount = depositAmount;

					currentDate.set(Calendar.DAY_OF_MONTH, 1);
					// System.out.println("CurrentDate" + currentDate.getTime());
					for (ClientLifeInsurance lifeInsurance : lifeInsuranceList) {
						Calendar policyStartDate = Calendar.getInstance();
						policyStartDate.setTime(lifeInsurance.getPolicyStartDate());
						int referenceMonthInDB = policyStartDate.get(Calendar.MONTH) + 1;

						Calendar policyEndDate = Calendar.getInstance();
						policyEndDate.setTime(lifeInsurance.getPolicyStartDate());
						policyEndDate.add(Calendar.MONTH, (lifeInsurance.getPremiumTenure() * 12));

						if (valueTobeAddedCOLife(policyStartDate, currentDate, policyEndDate,
								12 / (int) lifeInsurance.getPremiumFrequency(), referenceMonthInDB,
								lifeInsurance.getId())) {
							monthlyLifePremium = monthlyLifePremium + (lifeInsurance.getPremiumAmount() == null ? 0
									: lifeInsurance.getPremiumAmount().doubleValue());
						}
					}

					for (ClientNonLifeInsurance nonLifeInsurance : nonLifeInsuranceList) {

						Calendar policyStartDate = Calendar.getInstance();
						policyStartDate.setTime(nonLifeInsurance.getPolicyStartDate());
						int referenceMonthInDB = policyStartDate.get(Calendar.MONTH) + 1;

						Calendar policyEndDate = Calendar.getInstance();
						policyEndDate.setTime(nonLifeInsurance.getPolicyEndDate());

						if (valueTobeAddedCONonLife(policyStartDate, currentDate, policyEndDate, 12, referenceMonthInDB,
								nonLifeInsurance.getId())) {
							if (nonLifeInsurance.getInsuranceTypeID() == FinexaConstant.GENERAL_INSURANCE_ID) {
								monthlyGeneralPremium = monthlyGeneralPremium
										+ (nonLifeInsurance.getPremiumAmount() == null ? 0
												: nonLifeInsurance.getPremiumAmount().doubleValue());
							} else {
								monthlyHealthPremium = monthlyHealthPremium
										+ (nonLifeInsurance.getPremiumAmount() == null ? 0
												: nonLifeInsurance.getPremiumAmount().doubleValue());
							}
						}
					}
					yearlyInvestmentAmount = yearlyInvestmentAmount + monthlyInvestmentAmount;
					yearlyLifePremium = yearlyLifePremium + monthlyLifePremium;
					yearlyGeneralPremium = yearlyGeneralPremium + monthlyGeneralPremium;
					yearlyHealthPremium = yearlyHealthPremium + monthlyHealthPremium;

					if (mode.equals("monthly")) {

						CommittedOutFlowOutput monthlyCO = new CommittedOutFlowOutput();
						monthlyCO.setProjectionYear(monthArray[currentDate.get(Calendar.MONTH)] + "," + fiscalYearCO);
						monthlyCO.setInvestmentAmount(monthlyInvestmentAmount);
						monthlyCO.setPremiumAMount(monthlyLifePremium);
						monthlyCO.setPremiumAmountGeneral(monthlyGeneralPremium);
						monthlyCO.setPremiumAmountHealth(monthlyHealthPremium);
						monthlyCO.setTotalOutFlow(monthlyInvestmentAmount + monthlyLifePremium + monthlyGeneralPremium
								+ monthlyHealthPremium);
						monthlyCO.setOutFlowList(null);
						overAllCommitedOutflowList.add(monthlyCO);

					}

					if (startMonth == 12) {
						startMonth = 1; // M
					} else {
						startMonth++;

					}
					currentDate.add(Calendar.MONTH, 1);

				} while (startMonth != 4);

				if (mode.equals("yearly")) {
					CommittedOutFlowOutput yearlyCO = new CommittedOutFlowOutput();
					yearlyCO.setProjectionYear(fiscalYearCO);
					yearlyCO.setInvestmentAmount(yearlyInvestmentAmount);
					yearlyCO.setPremiumAMount(yearlyLifePremium);
					yearlyCO.setPremiumAmountGeneral(yearlyGeneralPremium);
					yearlyCO.setPremiumAmountHealth(yearlyHealthPremium);
					yearlyCO.setTotalOutFlow(
							yearlyInvestmentAmount + yearlyGeneralPremium + yearlyHealthPremium + yearlyLifePremium);
					yearlyCO.setOutFlowList(null);
					if (financialPlanningFlag == 1) {
						if (overAllCommitedOutflowList.size() > 0) {
							if (!(yearlyInvestmentAmount == 0 && yearlyLifePremium == 0 && yearlyGeneralPremium == 0
									&& yearlyHealthPremium == 0)) {
								overAllCommitedOutflowList.add(yearlyCO);
							}
						} else {
							overAllCommitedOutflowList.add(yearlyCO);
						}
					} else {
						if (!(yearlyInvestmentAmount == 0 && yearlyLifePremium == 0 && yearlyGeneralPremium == 0
								&& yearlyHealthPremium == 0)) {
							overAllCommitedOutflowList.add(yearlyCO);
						}
					}
				}

			}
			familyLifeInsuranceMap.clear();
			familyNonLifeInsuranceMap.clear();
			if (overAllCommitedOutflowList.size() > 0) {
				overAllcommitedOutFlow.setInvestmentAmount(overAllCommitedOutflowList.get(0).getInvestmentAmount());
				overAllcommitedOutFlow.setPremiumAMount(overAllCommitedOutflowList.get(0).getPremiumAMount());
				overAllcommitedOutFlow
						.setPremiumAmountGeneral(overAllCommitedOutflowList.get(0).getPremiumAmountGeneral());
				overAllcommitedOutFlow
						.setPremiumAmountHealth(overAllCommitedOutflowList.get(0).getPremiumAmountHealth());
				overAllcommitedOutFlow.setTotalOutFlow(overAllCommitedOutflowList.get(0).getTotalOutFlow());
				overAllcommitedOutFlow.setProjectionYear(overAllCommitedOutflowList.get(0).getProjectionYear());
				overAllcommitedOutFlow.setOutFlowList(overAllCommitedOutflowList);
			}
		} catch (FinexaDaoException e) {
			e.printStackTrace();

		}
		return overAllcommitedOutFlow;
	}

	private boolean valueTobeAddedCOLife(Calendar policyStartDate, Calendar currentDate, Calendar maturityDate,
			int frequency, int referenceMonthInDB, int insuranceId) {
		policyStartDate.set(Calendar.DAY_OF_MONTH, 1);
		maturityDate.set(Calendar.DAY_OF_MONTH, 1);

		boolean toBeAdded = false;
		if (currentDate.compareTo(policyStartDate) >= 0 && currentDate.compareTo(maturityDate) <= 0) {
			if (frequency == 1) {
				toBeAdded = true;
			} else {
				if (familyLifeInsuranceMap.size() == 0 || familyLifeInsuranceMap.get(insuranceId) == null) {
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
					if (monthsToBeConsidered.contains(currentDate.get(Calendar.MONTH) + 1)) {
						toBeAdded = true;
					}
					if (!familyLifeInsuranceMap.containsKey(insuranceId)) {
						familyLifeInsuranceMap.put(insuranceId, monthsToBeConsidered);
					}
				} else {
					if (familyLifeInsuranceMap.get(insuranceId).contains(currentDate.get(Calendar.MONTH) + 1)) {
						toBeAdded = true;
					}
				}
			}
		}

		return toBeAdded;
	}

	private boolean valueTobeAddedCONonLife(Calendar policyStartDate, Calendar currentDate, Calendar maturityDate,
			int frequency, int referenceMonthInDB, int insuranceId) {

		policyStartDate.set(Calendar.DAY_OF_MONTH, 1);
		maturityDate.set(Calendar.DAY_OF_MONTH, 1);

		boolean toBeAdded = false;
		if (currentDate.compareTo(policyStartDate) >= 0 && currentDate.compareTo(maturityDate) <= 0) {
			if (frequency == 1) {
				toBeAdded = true;
			} else {
				if (familyNonLifeInsuranceMap.size() == 0 || familyNonLifeInsuranceMap.get(insuranceId) == null) {
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
					if (monthsToBeConsidered.contains(currentDate.get(Calendar.MONTH) + 1)) {
						toBeAdded = true;
					}
					if (!familyNonLifeInsuranceMap.containsKey(insuranceId)) {
						familyNonLifeInsuranceMap.put(insuranceId, monthsToBeConsidered);
					}
				} else {
					if (familyNonLifeInsuranceMap.get(insuranceId).contains(currentDate.get(Calendar.MONTH) + 1)) {
						toBeAdded = true;
					}
				}
			}
		}

		return toBeAdded;
	}

	public boolean valueToBeAdded(int currentMonthIndex, int referenceMonthInDB, int frequency, Calendar expenseEndDate,
			Calendar maxDate, int expenseType) {

		boolean toBeAdded = false;
		if (frequency == 1) {
			toBeAdded = true;
		} else {
			if (familyExpenseMap.size() == 0 || familyExpenseMap.get(expenseType) == null) {
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
				if (!familyExpenseMap.containsKey(expenseType)) {
					familyExpenseMap.put(expenseType, monthsToBeConsidered);
				}
			} else {
				if (familyExpenseMap.get(expenseType).contains(currentMonthIndex)) {
					toBeAdded = true;
				}
			}
		}
		return toBeAdded;
	}

	@Override
	public Map<Date, Double> getDepositAmountFromProductCal(int clientId, Session session)
			throws FinexaBussinessException {

		Map<Date, Double> dateAmountDepositMap = new TreeMap<Date, Double>();
		ClientMasterDAO clientMasterDao = new ClientMasterDAOImpl();
		try {

			List<ClientFixedIncome> masterIncomeList = clientMasterDao.getClientIncomeListById(clientId, session);
			List<ClientSmallSaving> masterSmallSavingsList = clientMasterDao.getClientSmallSavingsListById(clientId,
					session);
			SimpleDateFormat lookupdateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			List<BankRecurringDepositLookup> bankRDLookupList = new ArrayList<>();
			List<PORecurringDepositLookup> poRDLookupList = new ArrayList<>();
			List<SukanyaSamriddhiSchemeLookup> sukanyaSamScSLookupList = new ArrayList<>();
			List<PPFFixedAmountLookup> ppfFixedLookupList = new ArrayList<>();
			List<AtalPensionYojanaLookup> atalPensionYojanaLookupList = new ArrayList<>();

			try {
				for (ClientFixedIncome fixedIncome : masterIncomeList) {
					if (fixedIncome.getMasterProductClassification()
							.getId() == FinexaConstant.BANK_RECURRING_DEPOSITS_ID) {
						// From Client Info Tenure will be saved in days so not required to convert here
						// int tenureRDMonths = 0;
						// int tenureYears = 0;
						int tenureDays = fixedIncome.getTenure();
						/*
						 * if(fixedIncome.getTenureYearsDays().equals("D")){ // tenure in Days if
						 * (fixedIncome.getTenure() != null) { Date cDate = new Date(); long[]
						 * tenureYearsLong = new FinexaDateUtil().getYearCountByDay(cDate,
						 * fixedIncome.getTenure().intValue()); tenureYears = (int)tenureYearsLong[0];
						 * tenureRDMonths = (int)tenureYearsLong[1]; }
						 * 
						 * } else { if (fixedIncome.getTenure() != null) { tenureYears =
						 * fixedIncome.getTenure().intValue(); } if (fixedIncome.getTenureRDMonths() !=
						 * null) { tenureRDMonths = fixedIncome.getTenureRDMonths(); } }
						 */

						BankRecurringDespositService bankRDService = new BankRecurringDespositService();
						// parameter will be in tenureDays and month will be 0
						BankRecurringDeposit bankRD = bankRDService.getRecurringDepositCalculatedList(
								fixedIncome.getAmount().doubleValue(),
								fixedIncome.getInterestCouponRate().doubleValue(), tenureDays, 0,
								fixedIncome.getLookupFrequency3().getId(), fixedIncome.getLookupFrequency1().getId(),
								fixedIncome.getInvestmentDepositDate());
						if (bankRDLookupList.size() > 0) {
							bankRDLookupList.addAll(bankRD.getBankRecurringLookupList());
						} else {
							bankRDLookupList = bankRD.getBankRecurringLookupList();
						}

					}
				}

			} catch (Exception e) {
			}
			for (ClientSmallSaving clientSmallSaving : masterSmallSavingsList) {

				try {
					if (clientSmallSaving.getMasterProductClassification()
							.getId() == FinexaConstant.PO_RECURRING_DEPOSITS_ID) {

						PORecurringDespositService poRecurringDespositService = new PORecurringDespositService();
						PORecurringDeposit postOffRD = poRecurringDespositService.getRecurringDepositCalculatedList(
								clientSmallSaving.getInvestmentAmount().doubleValue(),
								clientSmallSaving.getDepositTenure(), clientSmallSaving.getLookupFrequency3().getId(),
								clientSmallSaving.getLookupFrequency1().getId(), clientSmallSaving.getStartDate());
						if (poRDLookupList.size() > 0) {
							poRDLookupList.addAll(postOffRD.getPoRecurringLookupList());
						} else {
							poRDLookupList = postOffRD.getPoRecurringLookupList();
						}

					}
				} catch (Exception e) {
				}
				try {
					if (clientSmallSaving.getMasterProductClassification()
							.getId() == FinexaConstant.SUKANYA_SAMRIDDHI_SCHEME_ID) {
						// hard coded payment term and maturity term
						SukanyaSamriddhiScheme suknayaSmScheme = new SukanyaSamriddhiSchemeService()
								.getSukanyaSamriddhiSchemeList(clientSmallSaving.getInvestmentAmount().doubleValue(),
										String.valueOf(clientSmallSaving.getLookupFrequency1().getId()),
										FinexaConstant.SUKANYA_SAMRIDDHI_PAYMENT_TENURE,
										clientSmallSaving.getLookupFrequency3().getId(),
										clientSmallSaving.getLookupFrequency1().getId(),
										clientSmallSaving.getStartDate(),
										FinexaConstant.SUKANYA_SAMRIDDHI_MATURITY_TENURE);
						if (sukanyaSamScSLookupList.size() > 0) {
							sukanyaSamScSLookupList.addAll(suknayaSmScheme.getSukanyaSamSchLookupList());
						} else {
							sukanyaSamScSLookupList = suknayaSmScheme.getSukanyaSamSchLookupList();
						}
					}
				} catch (Exception e) {
				}
			}
			List<ClientPPF> clientPpfList = clientMasterDao.getClientPPFList(clientId, session);

			try {
				for (ClientPPF clientPpf : clientPpfList) {
					PPFFixedAmountDeposit ppfFixDeposit = new PPFFixedAmountService()
							.getPPFFixedAmountCalculationDetails(clientPpf.getCurrentBalance().doubleValue(), clientPpf.getPlannedDepositAmount().doubleValue(), "Y",
									clientPpf.getPpfTenure(), clientPpf.getLookupFrequency1().getId(),
									clientPpf.getLookupFrequency2().getId(), clientPpf.getStartDate());
					if (ppfFixedLookupList.size() > 0) {
						ppfFixedLookupList.addAll(ppfFixDeposit.getPpfFixedAmountLookupList());
					} else {
						ppfFixedLookupList = ppfFixDeposit.getPpfFixedAmountLookupList();
					}
					if (clientPpf.getExtensionFlag().equals("Y")) {
						ppfFixDeposit = new PPFFixedAmountService().getPPFExtensionCalcuation(
								clientPpf.getDepositAmountExt().doubleValue(), "Y", clientPpf.getExtensionTenure(),
								clientPpf.getAmountDepositFrequencyExt(), clientPpf.getExtensionCompoundingFrequency(),
								clientPpf.getExtensionStartDate(), ppfFixDeposit.getMaturityAmount(),
								ppfFixDeposit.getTotalAmountDeposited());
						ppfFixedLookupList.addAll(ppfFixDeposit.getPpfFixedAmountLookupList());
					}

				}
			} catch (Exception e) {
			}
			List<ClientAtalPensionYojana> atalPensionYojanaList = clientMasterDao.getClientFamilyATPListById(clientId,
					session);
			try {
				for (ClientAtalPensionYojana clientAtalPensionYojana : atalPensionYojanaList) {
					AtalPensionYojanaService atalYService = new AtalPensionYojanaService();
					ClientFamilyMember cfm = clientAtalPensionYojana.getClientFamilyMember();
					AtalPensionYojana atalPension = atalYService.getAtalPensionYojanaCal(cfm.getBirthDate(),
							clientAtalPensionYojana.getLookupFrequency().getId(),
							clientAtalPensionYojana.getMonthlyPensionRequired().doubleValue(),
							FinexaConstant.APY_RETIREMENT_AGE, clientAtalPensionYojana.getApyStartDate(),
							cfm.getLifeExpectancy());
					atalPensionYojanaLookupList.addAll(atalPension.getAtalPensionYojanaLookupList());

				}
			} catch (Exception e) {
			}
			try {

				for (BankRecurringDepositLookup bankRDLookup : bankRDLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(bankRDLookup.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateAmountDepositMap.size() != 0) {
						Double amountDeposited = 0.0;
						if (dateAmountDepositMap.get(cal1.getTime()) != null) {
							amountDeposited = dateAmountDepositMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}

						if ((bankRDLookup.getAmountDeposited() > 0.0)) {
							bankRDLookup.setAmountDeposited(bankRDLookup.getAmountDeposited() + amountDeposited);
							dateAmountDepositMap.put(cal1.getTime(), bankRDLookup.getAmountDeposited());
						}
						// System.out.println(cal1.getTime() + " " +
						// dateInterestMap.get(cal1.getTime()));
					} else {
						dateAmountDepositMap.put(cal1.getTime(), bankRDLookup.getAmountDeposited());
					}
				}
				// System.out.println(" ");

				// 2
				// System.out.println("PORecurringDepositLookup");

				for (PORecurringDepositLookup poRDLookup : poRDLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(poRDLookup.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					if (dateAmountDepositMap.size() != 0) {
						Double amountDeposited = 0.0;
						if (dateAmountDepositMap.get(cal1.getTime()) != null) {
							amountDeposited = dateAmountDepositMap.get(cal1.getTime());
						}
						if ((poRDLookup.getAmountDeposited() > 0.0)) {
							poRDLookup.setAmountDeposited(poRDLookup.getAmountDeposited() + amountDeposited);
							dateAmountDepositMap.put(cal1.getTime(), poRDLookup.getAmountDeposited());
						} //
							// System.out.println("BankBondDebenturesLookup " +
							// cal1.getTime() + " "+
							// dateAmountDepositMap.get(cal1.getTime()));
					} else {
						dateAmountDepositMap.put(cal1.getTime(), poRDLookup.getAmountDeposited());
					}
				}

				// System.out.println(" ");
				// 3
				// System.out.println("SukanyaSamriddhiSchemeLookup");
				for (SukanyaSamriddhiSchemeLookup sukanyaSamScheLookup : sukanyaSamScSLookupList) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(lookupdateFormat.parse(sukanyaSamScheLookup.getReferenceDate()));
					cal1.set(Calendar.MILLISECOND, 0);
					cal1.set(Calendar.SECOND, 0);
					cal1.set(Calendar.MINUTE, 0);
					cal1.set(Calendar.HOUR_OF_DAY, 12);
					// System.out.println(bankFdTdrLook.getInterestReceived());
					if (dateAmountDepositMap.size() != 0) {
						Double amountDeposited = 0.0;
						if (dateAmountDepositMap.get(cal1.getTime()) != null) {
							amountDeposited = dateAmountDepositMap.get(cal1.getTime());
							// System.out.println(cal1.getTime() + " " +
							// interestAmount);
						}
						if ((sukanyaSamScheLookup.getAmountDeposited() > 0.0)) {
							sukanyaSamScheLookup
									.setAmountDeposited(sukanyaSamScheLookup.getAmountDeposited() + amountDeposited);
							dateAmountDepositMap.put(cal1.getTime(), sukanyaSamScheLookup.getAmountDeposited());
						}
						// System.out.println(cal1.getTime() + " " +
						// postOffIncShemeLookup.getInterestReceived());
					} else {
						dateAmountDepositMap.put(cal1.getTime(), sukanyaSamScheLookup.getAmountDeposited());
					}
				}
				// System.out.println(" ");

				// 4
				// System.out.println("SeniorCitizenSavingSchemeLookup");
				for (PPFFixedAmountLookup ppfFixLookup : ppfFixedLookupList) {

					if (!ppfFixLookup.getReferenceDate().isEmpty()) {
						Calendar cal1 = Calendar.getInstance();
						cal1.setTime(lookupdateFormat.parse(ppfFixLookup.getReferenceDate()));
						cal1.set(Calendar.MILLISECOND, 0);
						cal1.set(Calendar.SECOND, 0);
						cal1.set(Calendar.MINUTE, 0);
						cal1.set(Calendar.HOUR_OF_DAY, 12);
						// System.out.println(bankFdTdrLook.getInterestReceived());
						if (dateAmountDepositMap.size() != 0) {
							Double amountDeposited = 0.0;
							if (dateAmountDepositMap.get(cal1.getTime()) != null) {
								amountDeposited = dateAmountDepositMap.get(cal1.getTime());
								// System.out.println(cal1.getTime() + " " +
								// interestAmount);
							}

							if ((ppfFixLookup.getAmountDeposited() > 0.0)) {
								ppfFixLookup.setAmountDeposited(ppfFixLookup.getAmountDeposited() + amountDeposited);
								dateAmountDepositMap.put(cal1.getTime(), ppfFixLookup.getAmountDeposited());
							}
							// System.out.println(cal1.getTime() + " " +
							// seniotCitSavSchemeLookup.getInterestReceived());
						} else {
							dateAmountDepositMap.put(cal1.getTime(), ppfFixLookup.getAmountDeposited());
						}
					}
				}

				// 5
				// System.out.println("Atal Pension Yojana");
				for (AtalPensionYojanaLookup atalPensionYojanaLookup : atalPensionYojanaLookupList) {

					if (!atalPensionYojanaLookup.getReferenceDate().isEmpty()) {
						Calendar cal1 = Calendar.getInstance();
						cal1.setTime(lookupdateFormat.parse(atalPensionYojanaLookup.getReferenceDate()));
						cal1.set(Calendar.MILLISECOND, 0);
						cal1.set(Calendar.SECOND, 0);
						cal1.set(Calendar.MINUTE, 0);
						cal1.set(Calendar.HOUR_OF_DAY, 12);
						// System.out.println(bankFdTdrLook.getInterestReceived());
						if (dateAmountDepositMap.size() != 0) {
							Double amountInvested = 0.0;
							if (dateAmountDepositMap.get(cal1.getTime()) != null
									&& dateAmountDepositMap.get(cal1.getTime()) != 0.0) {
								amountInvested = dateAmountDepositMap.get(cal1.getTime());
								// System.out.println(cal1.getTime() + " " +
								// interestAmount);
							}
							if ((atalPensionYojanaLookup.getAmountInvested() > 0.0)) {
								atalPensionYojanaLookup.setAmountInvested(
										atalPensionYojanaLookup.getAmountInvested() + amountInvested);
								dateAmountDepositMap.put(cal1.getTime(), atalPensionYojanaLookup.getAmountInvested());
							}
						} else {
							dateAmountDepositMap.put(cal1.getTime(), atalPensionYojanaLookup.getAmountInvested());
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateAmountDepositMap;
	}

	@Override
	public ClientDetailedOutput getClientDetails(int clientId, Session session) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ClientDetailedOutput clientDetailedOutput = null;
		List<ClientFamilyMember> familyMemberOutputList = null;
		ClientFamilyMember clientFamilyMember = null;
		ClientMaster clientMaster = null;
		ArrayList<ClientDetailedOutput> detailedList = new ArrayList<ClientDetailedOutput>();
		ClientFamilyExpenseDAO clientFamilyExpenseDAO = new ClientFamilyExpenseDAOImpl();
		try {
			clientFamilyMember = clientFamilyExpenseDAO.getClientFromFamilyMemberById(clientId, session);
			// System.out.println("ClientFamilyMember" + clientFamilyMember.getFirstName());
		} catch (Exception e) {
			System.out.println("Error****************" + e.getMessage());
		}

		try {
			clientMaster = clientFamilyMember.getClientMaster();
			/*
			 * List<ClientMaster> clientMasterList =
			 * clientMasterDao.getAllClientsById(clientId); if (clientMasterList != null &&
			 * !clientMasterList.isEmpty()) { clientMaster = clientMasterList.get(0); }
			 */

		} catch (Exception e) {
			System.out.println("Error****************" + e.getMessage());
		}

		if (clientFamilyMember != null) {
			clientDetailedOutput = new ClientDetailedOutput();
			clientDetailedOutput.setClientId(clientId);
			if (clientFamilyMember.getMiddleName() == null || clientFamilyMember.getMiddleName().equals("")) {
				clientDetailedOutput
						.setName(clientFamilyMember.getFirstName() + " " + clientFamilyMember.getLastName());
			} else {
				clientDetailedOutput.setName(clientFamilyMember.getFirstName() + " "
						+ clientFamilyMember.getMiddleName() + " " + clientFamilyMember.getLastName());
			}

			clientDetailedOutput.setMemberId(clientFamilyMember.getId());
			if (clientFamilyMember.getLifeExpectancy() != null) {
				clientDetailedOutput.setLifeExp((int) clientFamilyMember.getLifeExpectancy());
			} else {
				clientDetailedOutput.setLifeExp(0);
			}
		}

		if (clientMaster != null) {
			String gender = clientMaster.getGender();
			clientDetailedOutput.setGenderString(gender);
		}

		try {
			familyMemberOutputList = clientFamilyExpenseDAO.getAllFamilyMembersOfClient(clientId, session);
			if (clientFamilyMember != null && !familyMemberOutputList.isEmpty()) {
				for (ClientFamilyMember obj : familyMemberOutputList) {
					if (obj.getClientFamilyIncomes() != null && obj.getClientFamilyIncomes().size() > 0) {// code added
																											// for bug
																											// fix Jira
																											// -91
																											// saying
																											// only
																											// those
																											// family
																											// members
																											// to be
																											// diplayed
																											// who has
																											// income
						ClientDetailedOutput familyMember = new ClientDetailedOutput();
						familyMember.setClientId(clientId);
						familyMember.setName(obj.getFirstName() + " " + obj.getMiddleName() + " " + obj.getLastName());
						// System.out.println("obj.getMemberId()" + obj.getId());
						familyMember.setMemberId(obj.getId());
						// System.out.println("obj.getRelation()" + obj.getId());
						familyMember.setRelation(obj.getLookupRelation().getId());
						// System.out.println("obj.getLifeExpectancy()" + obj.getLifeExpectancy());

						familyMember.setLifeExp((int) obj.getLifeExpectancy());
						detailedList.add(familyMember);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Could not get family members" + e.getMessage());
		}

		clientDetailedOutput.setOutputList(detailedList);
		return clientDetailedOutput;

	}

	@Override
	public List<FamilyMemberIncomeDetailOutput> getExpenseOfFamilyMember(int memberId, Session session)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<ClientExpense> familyMemberOutputList = null;
		List<FamilyMemberIncomeDetailOutput> familyMemberList = new ArrayList<FamilyMemberIncomeDetailOutput>();
		ClientFamilyExpenseDAO clientFamilyExpenseDAO = new ClientFamilyExpenseDAOImpl();
		 MasterdataExpenseGrowthRateDAO masterdataExpenseGrowthRateDAO = new MasterdataExpenseGrowthRateDAOImpl();
		try {
			familyMemberOutputList = clientFamilyExpenseDAO.getExpenseListOfClient(memberId, session);
			if (familyMemberOutputList != null && !familyMemberOutputList.isEmpty()) {

				for (ClientExpense obj : familyMemberOutputList) {
					FamilyMemberIncomeDetailOutput output = new FamilyMemberIncomeDetailOutput();
					output.setMemberId(memberId);

					output.setIncome(obj.getExpenseAmount().doubleValue());
					LookupIncomeExpenseDuration year = obj.getLookupIncomeExpenseDuration();
					output.setContinueUpto(year.getDescription());

					MasterExpenseGrowthRate growthrate = masterdataExpenseGrowthRateDAO
							.getExpenseGrowthRateByIncomeHead(obj.getExpenseType(), session);
					if (growthrate == null) {
						output.setAnnualIncomeGrowthRate(0.0);
						output.setIncomeCategory("");
					} else {
						output.setAnnualIncomeGrowthRate(growthrate.getCagr().doubleValue());
						output.setIncomeCategory(growthrate.getExpenseCategory());
					}
					output.setFrequency(obj.getLookupFrequency().getDescription());
					output.setReferenceMonth(obj.getLookupMonth().getDescription());
					familyMemberList.add(output);
				}
			}
			return familyMemberList;
		} catch (Exception e) {
			throw new FinexaBussinessException("ClientExpense", "", e.getMessage());
		}
	}

	@Override
	public List<MasterExpenseIndustryStandardDTO> getExpenseIndustryStandard(Session session)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<MasterExpenseIndustryStandardDTO> masterExpenseIndustryDTOList = new ArrayList<>();
		List<MasterExpenseIndustryStandard> masterExpenseIndustryStandardList = null;
		ClientFamilyExpenseDAO clientFamilyExpenseDAO = new ClientFamilyExpenseDAOImpl();
		double livingPerc = 0;
		double discrePerc = 0;
		try {
			masterExpenseIndustryStandardList = clientFamilyExpenseDAO.getExpenseIndustryStandard(session);
			if (masterExpenseIndustryStandardList != null && !masterExpenseIndustryStandardList.isEmpty()) {
				for (MasterExpenseIndustryStandard obj : masterExpenseIndustryStandardList) {
					MasterExpenseIndustryStandardDTO masterExpenseIndustryDTO = new MasterExpenseIndustryStandardDTO();
					masterExpenseIndustryDTO.setId(obj.getId());
					masterExpenseIndustryDTO.setDescription(obj.getExpenseCategory());
					// change by Debolina on 12thOct2017 for null handling
					// masterExpenseIndustryDTO.setPerc(obj.getPercentOfTotal().doubleValue());
					masterExpenseIndustryDTO.setPerc(obj.getPercentOfTotal() == null ? 0
							: round((obj.getPercentOfTotal().doubleValue() * 100), 2));
					masterExpenseIndustryDTOList.add(masterExpenseIndustryDTO);
					if (obj.getId() == 9 || obj.getId() == 10 || obj.getId() == 11) {
						discrePerc = discrePerc + (obj.getPercentOfTotal() == null ? 0
								: round((obj.getPercentOfTotal().doubleValue() * 100), 2));
					} else if (obj.getId() != 12) {
						livingPerc = livingPerc + (obj.getPercentOfTotal() == null ? 0
								: round((obj.getPercentOfTotal().doubleValue() * 100), 2));
					}
				}
				MasterExpenseIndustryStandardDTO living = new MasterExpenseIndustryStandardDTO();
				living.setId(13);
				living.setDescription("Living Expense");
				living.setPerc(livingPerc);
				MasterExpenseIndustryStandardDTO discre = new MasterExpenseIndustryStandardDTO();
				discre.setId(14);
				discre.setDescription("Discretionary Expense");
				discre.setPerc(discrePerc);

				masterExpenseIndustryDTOList.add(living);
				masterExpenseIndustryDTOList.add(discre);
			}
		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return masterExpenseIndustryDTOList;
	}
}