/**
 * TimeShiftAspect.java (c) Copyright 2013 Graham Webber
 */
package org.gw.commons.aspects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Aspect which enables an application's system time to be modified in code at
 * any point in the application lifecycle.
 * <p>
 * It does this by replacing all returned values of
 * {@code System.currentTimeMillis()}, {@code new Date()} and
 * {@code Calendar.getInstance(..)} with a respective time-shifted value.
 * <p>
 * This class can be updated on construction or throughout the lifecycle of the
 * application by setting the "current" system time using the various
 * {@code setSystemTime(..)} methods.
 * <p>
 * <strong>Note: </strong> Changing the "current" system time only works for
 * newly created {@link java.util.Date} and {@link java.util.Calendar} objects. Existing instances
 * will have their original times.
 * 
 * @author Gman
 * @since 1.0.0
 * @version 1.0.0
 * 
 */
@Aspect
public class TimeShiftAspect {

	/**
	 * Holds the machine startup time as millis since epoch
	 */
	private static final long initMillis = System.currentTimeMillis();

	/**
	 * Holds the machine startup time for nanos
	 */
	private static final long initNanos = System.nanoTime();

	/**
	 * Holds the offset from machine time
	 */
	private long offset = 0;

	/**
	 * Constructor setting the system time to the actual time on the server.
	 */
	public TimeShiftAspect() {
	}

	/**
	 * Constructor that sets the system time to the given {@link java.util.Date}
	 */
	public TimeShiftAspect(Date date) {
		setSystemTime(date);
	}

	/**
	 * Constructor that sets the system time to the given ISO date-time
	 * {@link String} (date+time, date or just time)
	 */
	public TimeShiftAspect(String isoDateStr) {
		setSystemTime(isoDateStr);
	}

	/**
	 * Replaces calls to {@code System.currentTimeMillis()} with a time-shifted
	 * value.
	 * 
	 * @param pjp
	 *            The {@link ProceedingJoinPoint}
	 * @return A {@link Long} that has been time-shifted
	 * @throws Throwable
	 */
	@Around("call(long java.lang.System.currentTimeMillis()) && !within(org.gw.commons.aspects.TimeShiftAspect)")
	public Object getCurrentTimeMillis(ProceedingJoinPoint pjp)
			throws Throwable {
		return currentTimeMillis();
	}

	/**
	 * Replaces calls to {@code new Date()} with a time-shifted {@link java.util.Date}
	 * 
	 * @param pjp
	 *            The {@link ProceedingJoinPoint}
	 * @return A {@link java.util.Date} that has been time-shifted
	 * @throws Throwable
	 */
	@Around("call(java.util.Date.new()) && !within(org.gw.commons.aspects.TimeShiftAspect)")
	public Object getNewDate(ProceedingJoinPoint pjp) throws Throwable {
		Date date = new Date(currentTimeMillis());
		return date;
	}

	/**
	 * Replaces calls to {@code new GregorianCalendar()} with a time-shifted
	 * {@link java.util.Date}
	 * 
	 * @param pjp
	 *            The {@link ProceedingJoinPoint}
	 * @return A {@link java.util.Date} that has been time-shifted
	 * @throws Throwable
	 */
	@Around("call(java.util.GregorianCalendar.new()) && !within(org.gw.commons.aspects.TimeShiftAspect)")
	public Object getNewGregorianCalendar(ProceedingJoinPoint pjp)
			throws Throwable {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(currentTimeMillis());
		return cal;
	}

	/**
	 * Replaces calls to {@code Calendar.getInstance()} with a time-shifted
	 * {@link java.util.Calendar}
	 * 
	 * @param pjp
	 *            The {@link ProceedingJoinPoint}
	 * @return A {@link java.util.Calendar} that has been time-shifted
	 * @throws Throwable
	 */
	@Around("call(* java.util.Calendar.getInstance()) && !within(org.gw.commons.aspects.TimeShiftAspect)")
	public Object getCalendarInstance(ProceedingJoinPoint pjp) throws Throwable {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(currentTimeMillis());
		return cal;
	}

	/**
	 * Replaces calls to {@code Calendar.getInstance(java.util.Locale)} with a
	 * time-shifted {@link java.util.Calendar}
	 * 
	 * @param pjp
	 *            The {@link ProceedingJoinPoint}
	 * @param locale
	 *            The {@link java.util.Locale} passed to the
	 *            {@code Calendar.getInstance(java.util.Locale)}
	 * @return A {@link java.util.Calendar} that has been time-shifted
	 * @throws Throwable
	 */
	@Around("call(* java.util.Calendar.getInstance(java.util.Locale)) && args(locale) && !within(org.gw.commons.aspects.TimeShiftAspect)")
	public Object getCalendarInstance(ProceedingJoinPoint pjp, Locale locale)
			throws Throwable {
		Calendar cal = Calendar.getInstance(locale);
		cal.setTimeInMillis(currentTimeMillis());
		return cal;
	}

	/**
	 * Replaces calls to {@code Calendar.getInstance(java.util.TimeZone)} with a
	 * time-shifted {@link java.util.Calendar}
	 * 
	 * @param pjp
	 *            The {@link ProceedingJoinPoint}
	 * @param timezone
	 *            The {@link java.util.TimeZone} passed to the
	 *            {@code Calendar.getInstance(java.util.TimeZone)}
	 * @return A {@link java.util.Calendar} that has been time-shifted
	 * @throws Throwable
	 */
	@Around("call(* java.util.Calendar.getInstance(java.util.TimeZone)) && args(timezone) && !within(org.gw.commons.aspects.TimeShiftAspect)")
	public Object getCalendarInstance(ProceedingJoinPoint pjp, TimeZone timezone)
			throws Throwable {
		Calendar cal = Calendar.getInstance(timezone);
		cal.setTimeInMillis(currentTimeMillis());
		return cal;
	}

	/**
	 * Replaces calls to
	 * {@code Calendar.getInstance(java.util.TimeZone, java.util.Locale)} with a
	 * time-shifted {@link java.util.Calendar}
	 * 
	 * @param pjp
	 *            The {@link ProceedingJoinPoint}
	 * @param timezone
	 *            The {@link java.util.TimeZone} passed to the
	 *            {@code Calendar.getInstance(java.util.TimeZone, java.util.Locale)}
	 * @param locale
	 *            The {@link java.util.Locale} passed to the
	 *            {@code Calendar.getInstance(java.util.TimeZone, java.util.Locale)}
	 * @return A {@link java.util.Calendar} that has been time-shifted
	 * @throws Throwable
	 */
	@Around("call(* java.util.Calendar.getInstance(java.util.TimeZone, java.util.Locale)) && args(timezone, locale) && !within(org.gw.commons.aspects.TimeShiftAspect)")
	public Object getCalendarInstance(ProceedingJoinPoint pjp,
			TimeZone timezone, Locale locale) throws Throwable {
		Calendar cal = Calendar.getInstance(timezone, locale);
		cal.setTimeInMillis(currentTimeMillis());
		return cal;
	}

	/**
	 * Returns the millis since the class was initialised using
	 * {@code System.nanoTime()} for the most precise measurement.
	 * 
	 * @return
	 */
	private long preciseMillisSinceClassInit() {
		return (System.nanoTime() - initNanos) / 1000000;
	}

	/**
	 * Returns the shifted system time as described by this class. It basically
	 * equates to
	 * <p>
	 * {@code startup time + offset + millisSinceClassInit()}
	 * 
	 * @return Returns the system time as described by this class. May differ
	 *         from the machine time.
	 */
	private long currentTimeMillis() {
		return initMillis + offset + preciseMillisSinceClassInit();
	}

	/**
	 * Sets the system time to specified ISO time. It is possible to set exact
	 * time with the format {@code yyyy-MM-dd'T'HH:mm:ss} (no apostrophes around
	 * T in the actual string!) or one can set just time (then current date
	 * stays) or just date (then current time stays).
	 * <p>
	 * Note that milliseconds are preserved in any case.
	 * <p>
	 * If parse fails for whatever reason, nothing is changed.
	 * 
	 * @param isoDateStr
	 *            String with ISO date (date+time, date or just time)
	 */
	public void setSystemTime(String isoDateStr) {
		try {
			if (isoDateStr.indexOf('T') != -1) { // it's date and time (so
													// "classic" ISO timestamp)
				long wantedMillis = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss").parse(isoDateStr).getTime();
				offset = wantedMillis - preciseMillisSinceClassInit()
						- initMillis;
			} else if (isoDateStr.indexOf(':') != -1) { // it's just time we
														// suppose
				Calendar calx = Calendar.getInstance();
				calx.setTime(new SimpleDateFormat("HH:mm:ss").parse(isoDateStr));

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, calx.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, calx.get(Calendar.MINUTE));
				cal.set(Calendar.SECOND, calx.get(Calendar.SECOND));
				offset = cal.getTimeInMillis() - preciseMillisSinceClassInit()
						- initMillis;
			} else { // it must be just date then!
				Calendar calx = Calendar.getInstance();
				calx.setTime(new SimpleDateFormat("yyyy-MM-dd")
						.parse(isoDateStr));

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH, calx.get(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.MONTH, calx.get(Calendar.MONTH));
				cal.set(Calendar.YEAR, calx.get(Calendar.YEAR));
				offset = cal.getTimeInMillis() - preciseMillisSinceClassInit()
						- initMillis;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the system time to the given epoch time.
	 * 
	 * @param epoch
	 */
	public void setSystemTime(long epoch) {
		offset = epoch - preciseMillisSinceClassInit() - initMillis;
	}

	/**
	 * Sets the system time to the given {@link java.util.Calendar} time.
	 * 
	 * @param calendar
	 */
	public void setSystemTime(Calendar calendar) {
		offset = calendar.getTimeInMillis() - preciseMillisSinceClassInit() - initMillis;
	}

	/**
	 * Sets the system time to the given {@link java.util.Date} time.
	 * 
	 * @param date
	 */
	public void setSystemTime(Date date) {
		offset = date.getTime() - preciseMillisSinceClassInit() - initMillis;
	}

	/**
	 * Resets the system time to the servers time.
	 */
	public void resetTime() {
		offset = 0;
	}
}
