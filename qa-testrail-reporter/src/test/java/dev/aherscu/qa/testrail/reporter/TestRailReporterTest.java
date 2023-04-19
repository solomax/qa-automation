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

package dev.aherscu.qa.testrail.reporter;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static dev.aherscu.qa.tester.utils.WireMockServerUtils.*;
import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.io.FileMatchers.*;

import java.io.*;

import javax.ws.rs.core.*;

import org.testng.annotations.*;
import org.testng.xml.*;

import com.github.tomakehurst.wiremock.*;
import com.google.common.collect.*;

import dev.aherscu.qa.tester.utils.rest.*;
import lombok.*;

public class TestRailReporterTest {
    public static final File     REPORTING_INPUT  = new File(
        "target/test-classes");
    public static final File     REPORTING_OUTPUT = new File(
        "target/test-classes/reporting-output");
    private final WireMockServer wireMockServer   =
        wireMockServerOnDynamicPort();

    @BeforeClass
    protected void startMockRestServer() {
        // TODO maybe should move into some base class
        // -- see AbstractMockedServiceTest
        // then, should depend on qa-jgiven-commons, making the build slower
        wireMockServer.start();
        wireMockServer.stubFor(
            get(urlEqualTo("/self-test"))
                .willReturn(ok()));
        wireMockServer.stubFor(
            post(urlEqualTo("/index.php?/api/v2/add_result_for_case/123/68"))
                .willReturn(okJson("{\"id\":321,\"test_id\":321}")));
    }

    @AfterClass(alwaysRun = true)
    protected void stopMockRestServer() {
        wireMockServer.stop();
    }

    @Test
    public void selfTest() {
        try (val request = LoggingClientBuilder
            .newClient()
            .target(wireMockServer.baseUrl())
            .path("self-test")
            .request()
            .get()) {
            assertThat(request.getStatusInfo().getFamily(),
                is(Response.Status.Family.SUCCESSFUL));
        }
    }

    @Test
    @SneakyThrows
    public void shouldGenerateReport() {
        val xmlSuite = new XmlSuite();
        xmlSuite.setParameters(ImmutableMap.<String, String> builder()
            .put("templateResourceTestRailReporter",
                "/permethod-reporter.testrail")
            .put("testRailUrl", wireMockServer.baseUrl())
            .put("testRailRunId", "123")
            .build());
        TestRailReporter.builder()
            .sourceDirectory(REPORTING_INPUT)
            .outputDirectory(REPORTING_OUTPUT)
            .build()
            .generateReport(
                singletonList(xmlSuite),
                emptyList(),
                null);

        assertThat(REPORTING_OUTPUT, anExistingDirectory());
        // TODO verify that an HTML file was generated at least
    }
}
