package com.finlabs.finexa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class FinexaDateUtil {

	public static String getLastDayOfTheMonth(String date) {
		String lastDayOfTheMonth = "";

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			java.util.Date dt = formatter.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dt);

			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DATE, -1);

			java.util.Date lastDay = calendar.getTime();

			lastDayOfTheMonth = formatter.format(lastDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lastDayOfTheMonth;
	}

	public static String getFiscalYear(Date refDate) {
		Calendar cRefDate = Calendar.getInstance();
		int y1 = 0;
		int y2 = 0;
		cRefDate.setTime(refDate);
		int varMonth = cRefDate.get(Calendar.MONTH);
		int varYear = cRefDate.get(Calendar.YEAR);
		y1 = varMonth < 3 ? varYear - 1 : varYear;
		y2 = y1 + 1;
		String fiscalYear = Integer.toString(y1) + "-" + Integer.toString(y2);
		return fiscalYear;
	}

	public static int getFrequencyCount(int dbValue) {
		int interestFreqConstantLookup = 0;
		switch (dbValue) {
		case 1:
			interestFreqConstantLookup = 1;
			break;
		case 2:
			interestFreqConstantLookup = 3;
			break;
		case 3:
			interestFreqConstantLookup = 6;
			break;
		case 4:
			interestFreqConstantLookup = 12;
			break;
		}

		return interestFreqConstantLookup;
	}

	public static double getMutualFundFrequencyCount(int dbValue) {
		double interestFreqConstantLookup = 0;
		switch (dbValue) {
		case 12:
			interestFreqConstantLookup = 1;
			break;
		case 4:
			interestFreqConstantLookup = 3;
			break;
		case 52:
			interestFreqConstantLookup = 0.230769231;
			break;
		case 365:
			interestFreqConstantLookup = 0;
			break;
		}

		return interestFreqConstantLookup;
	}

	public static int getFrequencyPeriodCount(int dbValue) {
		int interestFreqConstantLookup = 0;
		switch (dbValue) {
		case 1:
			interestFreqConstantLookup = 12;
			break;
		case 2:
			interestFreqConstantLookup = 4;
			break;
		case 3:
			interestFreqConstantLookup = 2;
			break;
		case 4:
			interestFreqConstantLookup = 1;
		case 5:
			interestFreqConstantLookup = 52;
			break;
		case 6:
			interestFreqConstantLookup = 365;
			break;
		}

		return interestFreqConstantLookup;
	}

	public long[] getYearCountByDay(Date startDate, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		// cal.set(2015, 5, 10, 10, 00, 00);
		cal.setTime(startDate);
		System.out.println(cal.getTime());
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(0);
		// cal2.set(2015, 5, 10, 10, 00, 00);
		cal2.setTime(startDate);
		cal2.add(Calendar.DAY_OF_YEAR, days);
		System.out.println(cal2.getTime());
		LocalDate dateTo = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
		LocalDate dateFrom = LocalDate.of(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1,
				cal2.get(Calendar.DATE));
		Period intervalPeriod = Period.between(dateTo, dateFrom);
		long years = intervalPeriod.getYears();
		long months = intervalPeriod.getMonths();
		System.out.println(years + " " + months);

		return new long[] { years, months };
	}

	public static int getEquivalentFinMonthCounter(int originalMonthCounter) {
		int returnVal = 0;

		if (originalMonthCounter == 1) // Jan
			returnVal = 10;
		if (originalMonthCounter == 2)
			returnVal = 11;
		if (originalMonthCounter == 3)
			returnVal = 12;
		if (originalMonthCounter == 4)
			returnVal = 1;
		if (originalMonthCounter == 5)
			returnVal = 2;
		if (originalMonthCounter == 6)
			returnVal = 3;
		if (originalMonthCounter == 7)
			returnVal = 4;
		if (originalMonthCounter == 8)
			returnVal = 5;
		if (originalMonthCounter == 9)
			returnVal = 6;
		if (originalMonthCounter == 10)
			returnVal = 7;
		if (originalMonthCounter == 11)
			returnVal = 8;
		if (originalMonthCounter == 12) // Dec
			returnVal = 9;
		return returnVal;
	}

	public static int getCurrentYear() {

		Calendar cal = Calendar.getInstance();
		return cal.get(cal.YEAR);
	}

	public static int getCurrentMonth() {

		Calendar cal = Calendar.getInstance();
		return cal.get(cal.MONTH) + 1;
	}

	public static Calendar getBackDateFromNow(String YEARS_DAYS, int interval) {

		if (YEARS_DAYS != null) {
			Calendar backDate = Calendar.getInstance();

			if (YEARS_DAYS.equals("YEARS")) {
				backDate.add(Calendar.YEAR, -interval);
				backDate.set(Calendar.HOUR_OF_DAY, 0);
				backDate.set(Calendar.MINUTE, 0);
				backDate.set(Calendar.SECOND, 0);
				backDate.set(Calendar.MILLISECOND, 0);
			} else {
				if (YEARS_DAYS.equals("MONTHS")) {
					backDate.add(Calendar.MONTH, -interval);
					backDate.set(Calendar.HOUR_OF_DAY, 0);
					backDate.set(Calendar.MINUTE, 0);
					backDate.set(Calendar.SECOND, 0);
					backDate.set(Calendar.MILLISECOND, 0);
				}
			}
			return backDate;
		} else {
			return Calendar.getInstance();
		}
	}
}