/*
 * Copyright 2022 NAVER Corp.
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

package com.navercorp.pinpoint.it.plugin.kafka;

import com.navercorp.pinpoint.it.plugin.utils.AgentPath;
import com.navercorp.pinpoint.it.plugin.utils.TestcontainersOption;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointConfig;
import com.navercorp.pinpoint.test.plugin.PluginTest;
import com.navercorp.pinpoint.test.plugin.shared.SharedDependency;
import com.navercorp.pinpoint.test.plugin.shared.SharedTestLifeCycleClass;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import test.pinpoint.plugin.kafka.Kafka3UnitServer;
import test.pinpoint.plugin.kafka.TestProducer;

import java.util.Random;

import static test.pinpoint.plugin.kafka.KafkaITConstants.TRACE_TYPE_MULTI_RECORDS;

@PluginTest
@PinpointAgent(AgentPath.PATH)
@PinpointConfig("pinpoint-kafka-client.config")
@Dependency({
        "org.apache.kafka:kafka_2.12:[2.8.0]",
        "org.apache.kafka:kafka-clients:[2.8.0,2.max]"
})
@SharedDependency({"org.apache.kafka:kafka_2.12:2.8.0",
        TestcontainersOption.TEST_CONTAINER, TestcontainersOption.KAFKA})
@SharedTestLifeCycleClass(Kafka3UnitServer.class)
public class KafkaClient_2_8_x_3_IT extends KafkaClient3ITBase {
    Random random = new Random();
    @Test
    public void producerSendTest() throws NoSuchMethodException {
        int messageCount = random.nextInt(5) + 1;
        final TestProducer producer = new TestProducer(brokerUrl);

        producer.sendMessage(messageCount);
        KafkaClientITBase.verifyProducerSend(brokerUrl, messageCount);
    }

    @Disabled
    @Test
    public void recordEntryPointTest() throws NoSuchMethodException {
        final TestProducer producer = new TestProducer(brokerUrl);
        producer.sendMessage(1);
        KafkaClientITBase.verifySingleConsumerEntryPoint(brokerUrl, offset);
    }

    @Test
    public void recordMultiEntryPointTest() throws NoSuchMethodException {
        final TestProducer producer = new TestProducer(brokerUrl);
        producer.sendMessage(1, TRACE_TYPE_MULTI_RECORDS);
        KafkaClientITBase.verifyMultiConsumerEntryPoint(brokerUrl);
    }

}
