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
package dev.aherscu.qa.jgiven.commons.formatters;

import static dev.aherscu.qa.testing.utils.StringUtilsExtensions.*;

import java.lang.annotation.*;

import javax.annotation.concurrent.*;

import com.tngtech.jgiven.annotation.*;
import com.tngtech.jgiven.format.*;

/**
 * Formatter for {@link String}s.
 * 
 * @author aherscu
 *
 */
@ThreadSafe
public class StringFormatter implements
    AnnotationArgumentFormatter<StringFormatter.Annotation> {

    @Override
    public String format(final Object argumentToFormat,
        final StringFormatter.Annotation annotation) {
        return abbreviate(
            String.format(annotation.value(),
                argumentToFormat,
                annotation.value()),
            annotation.maxWidth());
    }

    /**
     * Formatter annotation for {@link String}s.
     *
     * @author aherscu
     *
     */
    @AnnotationFormat(StringFormatter.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface Annotation {
        /**
         * @return the maximum width
         */
        @SuppressWarnings("MagicNumber")
        int maxWidth() default 200;

        /**
         * @return the format as in {@link String#format(String, Object...)}
         */
        String value() default "%s";
    }
}
