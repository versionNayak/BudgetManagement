/**
 * 
 */
package com.finlabs.finexa.util;

/**
 * @author vishwajeet
 *
 */
public class FinexaConstant {

	/***************** LookupRelationConstants ******************/
	public static final int LOOKUP_RELATION_SELF_ID = 0;
	public static final int LOOKUP_RELATION_SPOUSE_ID = 1;
	public static final int LOOKUP_RELATION_SON_ID = 2;
	public static final int LOOKUP_RELATION_DAUGHTER_ID = 3;
	public static final int LOOKUP_RELATION_FATHER_ID = 4;
	public static final int LOOKUP_RELATION_MOTHER_ID = 5;
	public static final int LOOKUP_RELATION_BROTHER_ID = 6;
	public static final int LOOKUP_RELATION_SISTER_ID = 7;
	public static final int LOOKUP_RELATION_OTHER_ID = 8;
	/***************** LookupRelationConstants ******************/

	/*************** FamilyIncomeCatID Constant ***************/
	public static final int LOOKUP_INCOME_SALARY_ID = 1;
	public static final int LOOKUP_INCOME_BONOUS_VAR_ID = 2;
	public static final int LOOKUP_INCOME_BUSSINESS_ID = 3;
	public static final int LOOKUP_INCOME_PROFESSIONAL_ID = 4;
	public static final int LOOKUP_INCOME_RENTAL_ID = 5;
	public static final int LOOKUP_INCOME_PENSION_ID = 6;
	public static final int LOOKUP_INCOME_OTHER_ID = 7;
	/*************** FamilyIncomeCatID Constant ***************/

	/**************************
	 * Budget Management Constants
	 **************************/
	public static final int BANK_FIXED_DEPOSITS_ID = 22;
	public static final int BANK_RECURRING_DEPOSITS_ID = 23;
	public static final int CP_CD_ID = 27;
	public static final int PO_RECURRING_DEPOSITS_ID = 28;
	public static final int BONDS_DEBENTURES_ID = 24;
	public static final int PO_TIME_DEPOSIT_ID = 29;
	public static final int PO_MIS_ID = 30;
	public static final int SCSS_ID = 31;
	public static final int SUKANYA_SAMRIDDHI_SCHEME_ID = 32;
	public static final int SUKANYA_SAMRIDDHI_PAYMENT_TENURE = 15;
	public static final int SUKANYA_SAMRIDDHI_MATURITY_TENURE = 21;
	public static final int BOND_TYPE_BOND_WITH_COUPON_ID = 3;
	public static final int BOND_TYPE_PERPETUAL_BOND_ID = 2;
	public static final int BOND_TYPE_ZERO_COUPONL_BOND_ID = 1;
	public static final byte GENERAL_INSURANCE_ID = 2;
	public static final byte HEALTH_INSURANCE_ID = 3;
	public static final byte EPS_ANNUITY = 6;
	public static final byte ANNUITY_ID = 34;
	public static final byte ATAL_PENSION_YOJANA_ID = 33;
	public static final byte FD_TYPE_INTEREST_PAYOUT = 2;
	public static final byte FD_TYPE_CUMULATIVE = 1;
	public static final int  KVP_ID = 26;
	public static final int  NSC_ID = 25;
	public static final int  PPF_ID = 12;
	public static final int  EPF_ID = 13;
	public static final int  LUMPSUM_INFLOW = 38;
	

	/******************************* Cache Constant **************************/
	public static final String CALCULATION_TYPE_CONSTANT = "CALCULATION";
	public static final String MASTER_TYPE_CONSTANT = "MASTER";
	public static final String FINANCIAL_ITERATION_SERVICE_TOTAL_GOALS = "FINANCIAL_ITERATION_SERVICE_TOTAL_GOALS";

	public static final String CALCULATION_SUB_TYPE_PORTFOLIO_CONSTANT = "PORTFOLIO-CAL";
	public static final String CALCULATION_SUB_TYPE_FUTURELOAN_CONSTANT = "FUTURELOAN-CAL";
	// PRO RATA = from current month
	public static final String CALCULATION_SUB_TYPE_NETSURPLUS_CONSTANT_PRO_RATA = "NETSURPLUS-CAL-PRO-RATA";
	public static final String CALCULATION_SUB_TYPE_NETSURPLUS_CONSTANT = "NETSURPLUS-CAL";
	// Atal Pension Yojana Retirement Age
	public static final int APY_RETIREMENT_AGE = 60;
	public static final int MAX_DAY=31;
	
	public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
