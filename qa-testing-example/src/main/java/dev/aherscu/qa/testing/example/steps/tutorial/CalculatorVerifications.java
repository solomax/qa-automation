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

package dev.aherscu.qa.testing.example.steps.tutorial;

import static io.appium.java_client.MobileBy.*;

import org.hamcrest.*;

import dev.aherscu.qa.jgiven.commons.utils.*;
import dev.aherscu.qa.jgiven.webdriver.steps.*;

public class CalculatorVerifications<SELF extends CalculatorVerifications<SELF>>
    extends WebDriverVerifications<SELF> {

    @AttachesScreenshot
    public SELF the_result(final Matcher<String> matcher) {
        return eventually_assert_that(
            () -> element(AccessibilityId("CalculatorResults")).getText(),
            matcher);
    }
}
