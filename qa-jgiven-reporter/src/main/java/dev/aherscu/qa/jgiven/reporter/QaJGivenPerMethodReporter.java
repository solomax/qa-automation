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

package dev.aherscu.qa.jgiven.reporter;

import static com.google.common.collect.Maps.*;
import static com.google.common.collect.Multimaps.*;
import static dev.aherscu.qa.testing.utils.FileUtilsExtensions.*;
import static dev.aherscu.qa.testing.utils.StringUtilsExtensions.*;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.io.FilenameUtils.*;

import java.io.*;
import java.text.*;
import java.util.*;

import org.jooq.lambda.*;
import org.testng.xml.*;

import com.google.common.collect.*;
import com.tngtech.jgiven.report.json.*;
import com.tngtech.jgiven.report.model.*;

import lombok.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

/**
 * Per test method reporter.
 */
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(force = true)
@Slf4j
@ToString(callSuper = true)
public class QaJGivenPerMethodReporter
    extends AbstractQaJgivenReporter<ScenarioModel, QaJGivenPerMethodReporter> {
    public static final String DEFAULT_TEMPLATE_RESOURCE =
        "/qa-jgiven-permethod-reporter.html";

    @SneakyThrows
    public static Map<String, String> readAttributesOf(final File reportFile) {
        try (val attributesReader = fileReader(
            new File(reportFile.getAbsolutePath() + ".attributes"))) {
            val p = new Properties();
            p.load(attributesReader);
            return fromProperties(p);
        }
    }

    /**
     * Builds a new reporter configured with additional TestNG XML suite
     * parameters. Currently, only {@code templateResource} is recognized.
     *
     * @param xmlSuite
     *            TestNG XML suite
     * @return reporter configured
     */
    @Override
    protected QaJGivenPerMethodReporter with(final XmlSuite xmlSuite) {
        return ((QaJGivenPerMethodReporter) super.with(xmlSuite))
            .toBuilder()
            .templateResource(templateResourceParamFrom(xmlSuite,
                DEFAULT_TEMPLATE_RESOURCE))
            .build();
    }

    @SneakyThrows
    protected void applyAttributesFor(
        final ScenarioModel scenarioModel,
        final File reportFile) {
        log.info("setting attributes for " + reportFile.getName());

        try (val attributesWriter = fileWriter(
            new File(reportFile.getAbsolutePath() + ".attributes"))) {
            val p = new Properties();
            p.putAll(scenarioModel.getTagIds().stream()
                // TODO apply the mapping here
                .map(tag -> immutableEntry(substringBefore(tag, DASH),
                    substringAfter(tag, DASH)))
                // NOTE there might be multiple
                // DeviceName/PlatformName/PlatformVersion tags
                .collect(toMultimap(Map.Entry::getKey, Map.Entry::getValue,
                    MultimapBuilder.hashKeys().arrayListValues()::build))
                .asMap().entrySet().stream()
                // NOTE here we merge them all under one key
                .map(e -> immutableEntry(e.getKey(),
                    String.join(COMMA, e.getValue())))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)));
            p.store(attributesWriter,
                "generated by qa-jgiven-reporter-maven-plugin");
        }
    }

    /**
     * Generates a report for each test method (scenario in JGiven terms).
     */
    @Override
    @SneakyThrows
    public void generate() {
        listJGivenReports()
            .parallelStream()
            .peek(reportModelFile -> log
                .debug("reading " + reportModelFile.getName()))
            .flatMap(reportModelFile -> new ReportModelFileReader()
                .apply(reportModelFile).model
                    .getScenarios()
                    .stream()
                    .filter(scenarioModel -> scenarioModel
                        .getTagIds()
                        .stream()
                        .anyMatch(tagId -> tagId.contains(referenceTag))))
            .peek(scenarioModel -> log
                .debug("processing " + targetNameFor(scenarioModel)))
            .forEach(Unchecked.consumer(scenarioModel -> {
                val targetReportFile = new File(outputDirectory,
                    targetNameFor(scenarioModel)
                        + EXTENSION_SEPARATOR_STR
                // TODO make it read .mustache files and drop the extension here
                        + getExtension(templateResource));
                try (val reportWriter = fileWriter(targetReportFile)) {
                    template()
                        .execute(reportModel(targetReportFile)
                            .toBuilder()
                            .jgivenReport(scenarioModel)
                            .screenshotScale(screenshotScale)
                            .datePattern(datePattern)
                            .build(),
                            reportWriter);
                    applyAttributesFor(scenarioModel, targetReportFile);
                }

                // FIXME should work only with html reports
                // if (pdf) {
                // renderToPDF(
                // reportFile(reportModelFile, ".html"),
                // reportFile(reportModelFile, ".pdf")
                // .getAbsolutePath());
                // }

                reportGenerated(scenarioModel, targetReportFile);
            }));
    }

    protected void reportGenerated(
        final ScenarioModel scenarioModel,
        final File reportFile) {
        log.debug("report generated for {} into {}",
            scenarioModel.getClassName(), reportFile.getName());
    }

    protected String targetNameFor(final ScenarioModel scenarioModel) {
        return MessageFormat.format("{0}-{1}-{2}",
            scenarioModel.getExecutionStatus(), scenarioModel.getClassName(),
            scenarioModel.getTestMethodName());
    }
}
