package org.gw.commons.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.gw.commons.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

@Aspect
public abstract class ProfilingAspect {

	private static Logger logger = LoggerFactory
			.getLogger(ProfilingAspect.class);

	private int slowTimeInMillis = 5;
	private int potentialIssueTimeInMillis = 100;
	private int needsAttentionTimeInMillis = 500;

	@Pointcut
	public void profile() {
	}

	/**
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "profile()", argNames = "pjp")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			/* Run the operation */
			return pjp.proceed();
		} finally {

			long time = System.currentTimeMillis() - start;

			if (pjp.getTarget() != null) {
				Class<?> targetClass = AopUtils.getTargetClass(pjp.getTarget());
				if (targetClass == null) {
					targetClass = pjp.getTarget().getClass();
				}
				if (slowTimeInMillis < 0 || time == 0) {
					if (logger.isTraceEnabled()) {
						String timeTaken = StringUtils.convertMillisToString(
                                time, true, true);
						String msg = timeTaken + " to execute ["
								+ targetClass.getName() + "."
								+ pjp.getSignature().getName() + "(..)";
						logger.trace(msg);
					}
				} else if (time < slowTimeInMillis) {
					if (logger.isDebugEnabled()) {
						String timeTaken = StringUtils.convertMillisToString(
								time, true, true);
						String msg = "* SLOW METHOD * " + timeTaken
								+ " to execute [" + targetClass.getName() + "."
								+ pjp.getSignature().getName() + "(..)";
						logger.debug(msg);
					}
				} else if (time < potentialIssueTimeInMillis) {
					if (logger.isInfoEnabled()) {
						String timeTaken = StringUtils.convertMillisToString(
								time, true, true);
						String msg = "** POTENTIAL ISSUE ** " + timeTaken
								+ " to execute [" + targetClass.getName() + "."
								+ pjp.getSignature().getName() + "(..)";
						logger.info(msg);
					}
				} else if (time < needsAttentionTimeInMillis) {
					if (logger.isWarnEnabled()) {
						String timeTaken = StringUtils.convertMillisToString(
								time, true, true);
						String msg = "*** NEEDS ATTENTION *** " + timeTaken
								+ " to execute [" + targetClass.getName() + "."
								+ pjp.getSignature().getName() + "(..)";
						logger.warn(msg);
					}
				} else if (time >= needsAttentionTimeInMillis) {
					String timeTaken = StringUtils.convertMillisToString(time,
							true, true);
					String msg = "*!*!* THIS IS EMBARRISSING *!*!* "
							+ timeTaken + " to execute ["
							+ targetClass.getName() + "."
							+ pjp.getSignature().getName() + "(..)";
					logger.error(msg);
				}
			}
		}

	}
	public int getSlowTimeInMillis() {
		return slowTimeInMillis;
	}

	public void setSlowTimeInMillis(int slowTimeInMillis) {
		this.slowTimeInMillis = slowTimeInMillis;
	}

	public int getPotentialIssueTimeInMillis() {
		return potentialIssueTimeInMillis;
	}

	public void setPotentialIssueTimeInMillis(int potentialIssueTimeInMillis) {
		this.potentialIssueTimeInMillis = potentialIssueTimeInMillis;
	}

	public int getNeedsAttentionTimeInMillis() {
		return needsAttentionTimeInMillis;
	}

	public void setNeedsAttentionTimeInMillis(int needsAttentionTimeInMillis) {
		this.needsAttentionTimeInMillis = needsAttentionTimeInMillis;
	}
}
