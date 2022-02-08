/*
 * Copyright 2022 Adrian Herscu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.aherscu.qa.jgiven.commons.utils;

import static dev.aherscu.qa.jgiven.commons.utils.DryRunAspect.*;
import static java.lang.Integer.*;
import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.lang.SuppressWarnings;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;

import edu.umd.cs.findbugs.annotations.*;
import lombok.extern.slf4j.*;

/**
 * Monitors execution of methods of {@link MayAttachScreenshots} derivatives
 * which are annotated by {@link AttachesScreenshot} and calls
 * {@link MayAttachScreenshots#attaching_screenshot()} just before method
 * returns even if there was a failure.
 */
// ISSUE: attachments are not always reported on failed steps
// see https://github.com/TNG/JGiven/issues/408
@Aspect
@Slf4j
@SuppressFBWarnings(value = "MS_SHOULD_BE_FINAL",
    justification = "static field generated by aspectj")
public class AttachesScreenshotAspect {
    public static final int     screenshotDelayMs =
        parseInt(getProperty("screenshotDelayMs", "0"));
    public static final boolean screenshots       =
        isNotBlank(getProperty("screenshots"));

    @DeclareWarning("screenshottingMethod() && !mayAttachScreenshots()")
    static final String         unappropriateType =
        "must be a subtype of MayAttachScreenshots";

    /**
     * Attaches a screenshot even if the advised method failed.
     * 
     * @param thisJoinPoint
     *            advised join point
     * @return forwarded from advised method
     * @throws Throwable
     *             re-thrown as received advised method
     * @see #mayAttachScreenshots()
     * @see #screenshottingMethod()
     */
    @Around("screenshottingMethod() && mayAttachScreenshots()")
    @SuppressWarnings("static-method")
    public Object aroundScreenshottingMethod(
        final ProceedingJoinPoint thisJoinPoint)
        throws Throwable {
        try {
            return thisJoinPoint.proceed(thisJoinPoint.getArgs());
        } finally {
            if (!dryRun && screenshots) {
                try {
                    log.trace("attaching screenshot for {}",
                        thisJoinPoint.getSignature().toShortString());
                    ((MayAttachScreenshots<?>) thisJoinPoint.getThis())
                        .attaching_screenshot(screenshotDelayMs);
                } catch (final Throwable t) {
                    log.error(t.getMessage());
                }
            } else {
                log.trace("running with dry-run mode {} and/or screenshots {}",
                    dryRun, screenshots);
            }
        }
    }

    /**
     * Matches the execution of any method within a sub-type of
     * {@link MayAttachScreenshots}.
     */
    @Pointcut("within("
        + "dev.aherscu.qa.jgiven.commons.utils.MayAttachScreenshots+)")
    public void mayAttachScreenshots() {
        // nothing to do here -- just defines a pointcut matcher
    }

    /**
     * Matches the execution of any method annotated with
     * {@link AttachesScreenshot}.
     */
    @Pointcut("execution("
        + "@dev.aherscu.qa.jgiven.commons.utils.AttachesScreenshot "
        + "* *(..))")
    public void screenshottingMethod() {
        // nothing to do here -- just defines a pointcut matcher
    }
}
