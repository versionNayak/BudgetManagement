package com.finlabs.finexa.util;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FinanceUtil {

	public static double pmt(double r, double n, double p, double f, boolean t) {
		double retval = 0;
		if (r == 0) {
			retval = -1 * (f + p) / n;
		} else {
			double r1 = r + 1;
			retval = (f + p * Math.pow(r1, n)) * r / ((t ? r1 : 1) * (1 - Math.pow(r1, n)));
		}
		return retval;
	}

	public static double roundUpAmount(double amount) {
		double roundUpAmount = Math.round((amount * 100)) / 100.0;
		return roundUpAmount;
	}

	public static double STDEV(List<Double> medianList) {

		double stdev = 0;

		// Calculate mean
		double sum = 0.0;
		double mean = 0.0;
		for (Double a : medianList) {
			sum += a;

		}
		mean = sum / medianList.size();

		// Calculate Variance
		double temp = 0;
		double variance = 0;
		for (Double a : medianList)
			temp += (a - mean) * (a - mean);
		variance = temp / (medianList.size() - 1);

		stdev = Math.sqrt(variance);
		return stdev;

	}

	public static double AVERAGE(List<Double> listOfValues) {

		double average = 0.0;

		// Calculate average
		double sum = 0.0;
		int size = listOfValues.size();
		for (Double a : listOfValues) {
			sum += a;
		}
		average = sum / size;

		return average;

	}

	public static double YEARFRAC(Date startDate, Date maturityDate, int basis) {
		long diff = maturityDate.getTime() - startDate.getTime();

		long daysTotal = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		double interestReceived = 0.0;
		if (basis == 1) {
			interestReceived = (daysTotal / (double) 365.3310);
		}

		return interestReceived;
	}

	public static double SLOPE(Double[] x, Double[] y) {
		Double intercept, slope;
		Double r2;
		Double svar0, svar1;
		if (x.length != y.length) {
			throw new IllegalArgumentException("array lengths are not equal");
		}
		int n = x.length;

		// first pass
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
		for (int i = 0; i < n; i++) {
			sumx += x[i];
			sumx2 += x[i] * x[i];
			sumy += y[i];
		}
		double xbar = sumx / n;
		double ybar = sumy / n;

		// second pass: compute summary statistics
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = 0; i < n; i++) {
			xxbar += (x[i] - xbar) * (x[i] - xbar);
			yybar += (y[i] - ybar) * (y[i] - ybar);
			xybar += (x[i] - xbar) * (y[i] - ybar);
		}
		slope = xybar / xxbar;
		intercept = ybar - slope * xbar;

		// more statistical analysis
		double rss = 0.0; // residual sum of squares
		double ssr = 0.0; // regression sum of squares
		for (int i = 0; i < n; i++) {
			double fit = slope * x[i] + intercept;
			rss += (fit - y[i]) * (fit - y[i]);
			ssr += (fit - ybar) * (fit - ybar);
		}

		int degreesOfFreedom = n - 2;
		r2 = ssr / yybar;
		double svar = rss / degreesOfFreedom;
		svar1 = svar / xxbar;
		svar0 = svar / n + xbar * xbar * svar1;
		return slope;
	}

}
