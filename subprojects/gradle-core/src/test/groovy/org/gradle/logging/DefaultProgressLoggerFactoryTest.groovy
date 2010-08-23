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
package org.gradle.logging

import org.gradle.logging.internal.ProgressListener
import spock.lang.Specification

class DefaultProgressLoggerFactoryTest extends Specification {
    private final ProgressListener progressListener = Mock()
    private final ProgressLoggerFactory factory = new DefaultProgressLoggerFactory(progressListener)

    def progressLoggerBroadcastsEvents() {
        when:
        def logger = factory.start('logger', 'description')

        then:
        logger != null
        1 * progressListener.started({it.category == 'logger' && it.description == 'description'})

        when:
        logger.progress('progress')

        then:
        1 * progressListener.progress({it.category == 'logger' && it.status == 'progress'})

        when:
        logger.completed('completed')

        then:
        1 * progressListener.completed({it.category == 'logger' && it.status == 'completed'})
    }

    def hasEmptyStatusOnStart() {
        when:
        def logger = factory.start('logger', 'description')

        then:
        logger.description == 'description'
        logger.status == ''
    }

    def hasMostRecentStatusOnProgress() {
        when:
        def logger = factory.start('logger', 'description')
        logger.progress('status')

        then:
        logger.status == 'status'
    }
    
    def hasMostRecentStatusOnComplete() {
        when:
        def logger = factory.start('logger', 'description')
        logger.completed('done')

        then:
        logger.status == 'done'
    }
}

