/**
 * TimeShiftAspectTest.java (c) Copyright 2013 Graham Webber
 */
package org.gw.commons.aspects;

import org.aspectj.lang.Aspects;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author gman
 * @since 1.0
 * @version 1.0
 * 
 */
public class TimeShiftAspectTest {

	private static final long millisInDay = 86400000;
	private static final long actual = System.currentTimeMillis();
	private static final long actualToDay = System.currentTimeMillis()
			/ millisInDay;

	private TimeShiftAspect timeAspect = Aspects
			.aspectOf(TimeShiftAspect.class);

	@Before
	public void init() {
		Assert.assertNotNull(timeAspect);
		timeAspect.resetTime();
		Assert.assertEquals(actualToDay, System.currentTimeMillis()
                / millisInDay);
	}

	@Test
	public void testSetDate() {

		// Pick a date 10 days ago
		Calendar shifted = Calendar.getInstance();
		shifted.add(Calendar.DATE, -10);

		// Shift the system time to that date
		timeAspect.setSystemTime(shifted.getTime());

		// Create a new date which should be very close to that date
		Date test = new Date();

		long difference = Math.abs(test.getTime() - shifted.getTimeInMillis());

		// Test this new date is very close to the shifted date - within 1000
		// millis
		Assert.assertTrue("Did not shift. Diff: " + difference, difference < 1000);

		// Create a calendar to test
		Calendar testCal = Calendar.getInstance();

		// Compare the days
		Assert.assertTrue("Date compare is wrong.",
				testCal.get(Calendar.DATE) == shifted.get(Calendar.DATE));

		// Compare the epoch times
		long epoch = System.currentTimeMillis() / 1000;
		long shiftedEpoch = shifted.getTimeInMillis() / 1000;
		long epochDiff = Math.abs(epoch - shiftedEpoch);

		Assert.assertTrue("Epoch compare is wrong. Diff: " + epochDiff,
				epochDiff < 5);

	}

	@Test
	public void testSetCalendar() {

		// Pick a date 10 days ago
		Calendar shifted = Calendar.getInstance();
		shifted.add(Calendar.DATE, -10);

		// Shift the system time to that date
		timeAspect.setSystemTime(shifted);

		// Create a new date which should be very close to that date
		Date test = new Date();

		long difference = Math.abs(test.getTime() - shifted.getTimeInMillis());

		// Test this new date is very close to the shifted date - within 1000
		// millis
		Assert.assertTrue("Did not shift. Diff: " + difference, difference < 1000);

		// Create a calendar to test
		Calendar testCal = Calendar.getInstance();

		// Compare the days
		Assert.assertTrue("Date compare is wrong.",
				testCal.get(Calendar.DATE) == shifted.get(Calendar.DATE));

		// Compare the epoch times
		long epoch = System.currentTimeMillis() / 1000;
		long shiftedEpoch = shifted.getTimeInMillis() / 1000;
		long epochDiff = Math.abs(epoch - shiftedEpoch);

		Assert.assertTrue("Epoch compare is wrong. Diff: " + epochDiff,
				epochDiff < 5);

	}

	@Test
	public void testSetIsoDate() {

		// Pick a date 10 days ago
		Calendar shifted = Calendar.getInstance();
		shifted.add(Calendar.DATE, -10);

		// Change the date to the iso date string
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		// Shift the system time to that date
		timeAspect.setSystemTime(format.format(shifted.getTime()));

		// Create a new date which should be very close to that date
		Date test = new Date();

		long difference = Math.abs(test.getTime() - shifted.getTimeInMillis());

		// Test this new date is very close to the shifted date - within 1000
		// millis
		Assert.assertTrue("Did not shift. Diff: " + difference,
				difference < 1000);

		// Create a calendar to test
		Calendar testCal = Calendar.getInstance();

		// Compare the days
		Assert.assertTrue("Date compare is wrong.",
				testCal.get(Calendar.DATE) == shifted.get(Calendar.DATE));

		// Compare the epoch times
		long epoch = System.currentTimeMillis() / 1000;
		long shiftedEpoch = shifted.getTimeInMillis() / 1000;
		long epochDiff = Math.abs(epoch - shiftedEpoch);

		Assert.assertTrue("Epoch compare is wrong. Diff: " + epochDiff,
				epochDiff < 5);

	}

}
