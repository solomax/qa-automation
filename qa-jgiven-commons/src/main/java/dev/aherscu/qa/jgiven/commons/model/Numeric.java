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
package dev.aherscu.qa.jgiven.commons.model;

import java.io.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

import edu.umd.cs.findbugs.annotations.*;
import lombok.*;

/**
 * Represents an identifier.
 * 
 * @param <T>
 *            type of indentifier's value
 * 
 * @author aherscu
 *
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@JsonSerialize(using = Numeric.Serializer.class)
@SuppressFBWarnings(value = "USBR_UNNECESSARY_STORE_BEFORE_RETURN",
    justification = "hashcode implemented by lombok")
public abstract class Numeric<T extends Number> {
    /**
     * The value of this identifier.
     */
    public final T value;

    /**
     * Use this method to get a textual representation of this identifier.
     *
     * @return the textual representation of {@link #value}
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * JSON serialization logic for {@link Numeric}s.
     *
     * @author aherscu
     * @param <T>
     *            type of numeric
     *
     */
    public static class Serializer<T extends Numeric<?>>
        extends JsonSerializer<T> {
        @Override
        public void serialize(
            final T numeric,
            final JsonGenerator gen,
            final SerializerProvider provider)
            throws IOException {
            gen.writeNumber(numeric.value.toString());
        }
    }
}
