/*
 * Copyright 2023 Adrian Herscu
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
package dev.aherscu.qa.jgiven.webdriver.tags;

import java.lang.annotation.*;

import com.tngtech.jgiven.annotation.*;

/**
 * User interface testing annotation.
 * 
 * @author aherscu
 *
 */
@IsTag(
    name = "UI Test",
    description = "Tests interacting with user interface")
@Retention(RetentionPolicy.RUNTIME)
@Inherited
// DELETEME -- replace with WebDriverTest
public @interface UITest {
    // no parameter to declare
}
