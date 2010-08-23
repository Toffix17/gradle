/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.logging;

import org.gradle.api.logging.LogLevel;

/**
 * Provides streaming of styled text, that is, a stream of text and styling information. Implementations are not
 * required to be thread-safe.
 */
public interface StyledTextOutput {
    /**
     * Appends text with the default style.
     *
     * @param text The text
     * @return this
     */
    StyledTextOutput text(CharSequence text);

    /**
     * Appends text with the style for the given log level.
     *
     * @param text The text
     * @param level The log level.
     * @return this
     */
    StyledTextOutput text(LogLevel level, CharSequence text);

    /**
     * Starts a new line.
     *
     * @return this
     */
    StyledTextOutput endLine();
}
