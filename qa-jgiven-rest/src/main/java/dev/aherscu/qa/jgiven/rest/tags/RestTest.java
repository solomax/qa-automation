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
package dev.aherscu.qa.jgiven.rest.tags;

import java.lang.annotation.*;

import com.tngtech.jgiven.annotation.*;

/**
 * Self test tagging annotation.
 * 
 * @author aherscu
 *
 */
@IsTag(name = "REST Test",
    description = "Tests interacting with REST interfaces")
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RestTest {
    // no parameter to declare
}
