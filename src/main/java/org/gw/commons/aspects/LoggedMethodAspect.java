package org.gw.commons.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.gw.commons.aspects.LoggedMethod.LEVEL;
import org.gw.commons.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import java.util.Arrays;

@Aspect
public class LoggedMethodAspect {

	private static Logger logger = LoggerFactory
			.getLogger(LoggedMethodAspect.class);

	private int slowTimeInMillis = 50;
	
	/**
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* *(..)) && @annotation(loggedMethod)")
	public Object logAround(ProceedingJoinPoint pjp, LoggedMethod loggedMethod)
			throws Throwable {
		Class<?> targetClass = AopUtils.getTargetClass(pjp.getTarget());
		if (targetClass == null) {
			targetClass = pjp.getTarget().getClass();
		}
		String inMsg = ">>>> In [" + pjp.getTarget().getClass().getName()
				+ "::" + pjp.getSignature().getName() + "("
				+ Arrays.toString(pjp.getArgs()) + ")]";
		if (logger.isDebugEnabled()) {
			if (loggedMethod.level() == LEVEL.DEBUG) {
				logger.debug(inMsg);
			}
		} else if (logger.isInfoEnabled()) {
			if (loggedMethod.level() == LEVEL.INFO) {
				logger.info(inMsg);
			}
		}

		long start = System.currentTimeMillis();
		try {
			/* Run the operation */
			return pjp.proceed();
		} finally {

			long time = System.currentTimeMillis() - start;

			String timeTaken = null;
			if (loggedMethod.timed()) {
				timeTaken = StringUtils.convertMillisToString(time, true, true);
			}
			String outMsg = "<<<< Out "
					+ ((timeTaken != null) ? " Time taken: " + timeTaken : "")
					+ " [" + pjp.getTarget().getClass().getName() + "::"
					+ pjp.getSignature().getName() + "("
					+ Arrays.toString(pjp.getArgs()) + ")]";

			if (logger.isDebugEnabled()) {
				if (loggedMethod.level() == LEVEL.DEBUG) {
					logger.debug(outMsg);
				}
			} else if (logger.isInfoEnabled()) {
				if (loggedMethod.level() == LEVEL.INFO) {
					logger.info(outMsg);
				}
			}
			if (time > loggedMethod.maxTimeInMillis()) {
				String msg = "Logged method took " + time
						+ "ms. Max expected: " + loggedMethod.maxTimeInMillis()
						+ "ms [" + pjp.getTarget().getClass().getName() + "::"
						+ pjp.getSignature().getName() + "("
						+ Arrays.toString(pjp.getArgs()) + ")]";
				logger.error(msg);
			}
		}

	}

}
