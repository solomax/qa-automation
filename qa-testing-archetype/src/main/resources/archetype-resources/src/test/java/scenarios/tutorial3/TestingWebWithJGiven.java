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

package ${package}.scenarios.tutorial3;

import static dev.aherscu.qa.testing.utils.StreamMatchersExtensions.*;
import static dev.aherscu.qa.testing.utils.StringUtilsExtensions.*;
import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.*;

import org.hamcrest.*;
import org.testng.annotations.*;

import dev.aherscu.qa.jgiven.webdriver.*;
import dev.aherscu.qa.jgiven.webdriver.model.*;
import dev.aherscu.qa.jgiven.commons.model.*;
import ${package}.*;
import ${package}.steps.tutorial.*;
import lombok.*;

public class TestingWebWithJGiven
    extends
    ApplicationPerMethodWebSessionTest<TestConfiguration, GoogleFixtures<?>, GoogleActions<?>, GoogleVerifications<?>> {

    protected TestingWebWithJGiven() {
        super(TestConfiguration.class);
    }

    @DataProvider
    private Object[][] data() {
        return new Object[][] {
            { new Text(wrap(randomAlphanumeric(40), DOUBLE_QUOTE)),
                counts(equalTo(0L)) },
            { new Text("testng"),
                allMatch(either(containsStringIgnoringCase("testng"))
                        .or(containsStringIgnoringCase("Try again"))
                        .or(containsStringIgnoringCase("More results"))) }
        };
    }

    @Test(dataProvider = INTERNAL_DATA_PROVIDER)
    public void shouldFind(
        final Text textToSearch,
        final Matcher<Stream<String>> titleRule) {
        given()
            .google(webDriver.get());

        when()
            .searching_for(textToSearch);

        then()
            .the_results(
                adaptedStream(googleResult -> googleResult.title.value,
                    titleRule));
    }

    @BeforeMethod
    @SneakyThrows
    @Override
    public void beforeMethodOpenWebDriver() {
        super.beforeMethodOpenWebDriver();
        webDriver.get().asRemote().manage().window().maximize();
    }
}
