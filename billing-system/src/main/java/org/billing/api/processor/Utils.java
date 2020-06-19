package org.billing.api.processor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.codec.digest.DigestUtils;
import org.billing.api.data.TransactionException;

public class Utils {

	public static String getTransactionNumber() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddkkmmssSSS");
		return LocalDateTime.now().format(formatter);
	}

	public static String getCurrentDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDateTime.now().format(formatter);
	}

	public static String validateDate(String date) throws TransactionException {
		LocalDate fdate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
		return fdate.toString();
	}

	public static String formatTraceNumber(int memberID, String traceNo) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return LocalDateTime.now().format(formatter) + String.valueOf(memberID) + "-" + traceNo;
	}

	public static String getUID(Integer memberID, Integer voucherID, String email) {
		String md5Hex = DigestUtils.md5Hex(memberID + voucherID + Utils.getTransactionNumber()).toUpperCase();
		return md5Hex;
	}

	public static String getCurrentMonthFirstDate() {
		return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).withDayOfMonth(1).toString();
	}

	public static String getCurrentMonthLastDate() {
		return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).plusMonths(1).withDayOfMonth(1)
				.minusDays(1).toString();
	}
}
