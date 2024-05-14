package myapps;

import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;

public class KafkaTopologyTest {

    private TopologyTestDriver testDriver;
    private KeyValueStore<String, Long> store;

    private StringDeserializer stringDeserializer = new StringDeserializer();
    private LongDeserializer longDeserializer = new LongDeserializer();
    private TestInputTopic<String, Long> inputTopic;

    private TestOutputTopic<String, Long> outputTopic;

    @Before
    public void setup() {
        Topology topology = new Topology();
        topology.addSource("sourceProcessor", "input-topic");
        topology.addProcessor("aggregator", new CustomMaxAggregatorSupplier(), "sourceProcessor");
        topology.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.inMemoryKeyValueStore("aggStore"),
                        Serdes.String(),
                        Serdes.Long()).withLoggingDisabled(), // need to disable logging to allow store pre-populating
                "aggregator");
        topology.addSink("sinkProcessor", "result-topic", "aggregator");

        // setup test driver
        Properties props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "maxAggregation");
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        testDriver = new TopologyTestDriver(topology, props);

        // pre-populate store
        store = testDriver.getKeyValueStore("aggStore");
        store.put("a", 21L);

        inputTopic = testDriver.createInputTopic("input-topic", new StringSerializer(), new LongSerializer());
        outputTopic = testDriver.createOutputTopic("result-topic", new StringDeserializer(), new LongDeserializer());
    }

    @After
    public void tearDown() {
        testDriver.close();
    }

    @Test
    public void shouldFlushStoreForFirstInput() {
        inputTopic.pipeInput( "a", 1L, 9999L);
        Assert.assertEquals(KeyValue.pair("a", 21L), outputTopic.readKeyValue());
        Assert.assertTrue(outputTopic.isEmpty());
    }

    @Test
    public void shouldNotUpdateStoreForSmallerValue() {
        inputTopic.pipeInput("a", 1L, 9999L);
        Assert.assertThat(store.get("a"), equalTo(21L));
        Assert.assertEquals(KeyValue.pair("a", 21L), outputTopic.readKeyValue());
        Assert.assertTrue(outputTopic.isEmpty());
    }

    @Test
    public void shouldNotUpdateStoreForLargerValue() {
        inputTopic.pipeInput("a", 42L, 9999L);
        Assert.assertThat(store.get("a"), equalTo(42L));
        Assert.assertEquals(KeyValue.pair("a", 42L), outputTopic.readKeyValue());
        Assert.assertTrue(outputTopic.isEmpty());
    }

    @Test
    public void shouldUpdateStoreForNewKey() {
        inputTopic.pipeInput("b", 21L, 9999L);
        Assert.assertThat(store.get("b"), equalTo(21L));
        Assert.assertEquals(KeyValue.pair("a", 21L), outputTopic.readKeyValue());
        Assert.assertEquals(KeyValue.pair("b", 21L), outputTopic.readKeyValue());
        Assert.assertTrue(outputTopic.isEmpty());
    }

    @Test
    public void shouldPunctuateIfEvenTimeAdvances() {
        inputTopic.pipeInput("a", 1L, 9999L);
        Assert.assertEquals(KeyValue.pair("a", 21L), outputTopic.readKeyValue());

        inputTopic.pipeInput("a", 1L, 9999L);
        Assert.assertTrue(outputTopic.isEmpty());


        inputTopic.pipeInput("a", 1L, 10000L);
        Assert.assertEquals(KeyValue.pair("a", 21L), outputTopic.readKeyValue());
                Assert.assertTrue(outputTopic.isEmpty());

    }

    @Test
    public void shouldPunctuateIfWallClockTimeAdvances() {
        testDriver.advanceWallClockTime(Duration.ofMillis(60000));
        Assert.assertEquals(KeyValue.pair("a", 21L), outputTopic.readKeyValue());
                Assert.assertTrue(outputTopic.isEmpty());


    }

    public class CustomMaxAggregatorSupplier implements ProcessorSupplier<String, Long, String, Long> {
        @Override
        public Processor<String, Long, String, Long> get() {
            return new CustomMaxAggregator();
        }
    }

    public class CustomMaxAggregator implements Processor<String, Long, String, Long> {
        ProcessorContext context;
        private KeyValueStore<String, Long> store;

        @SuppressWarnings("unchecked")
        @Override
        public void init(ProcessorContext context) {
            this.context = context;
            context.schedule(Duration.ofMillis(60000), PunctuationType.WALL_CLOCK_TIME, time -> flushStore());
            context.schedule(Duration.ofMillis(10000), PunctuationType.STREAM_TIME, time -> flushStore());
            store = (KeyValueStore<String, Long>) context.getStateStore("aggStore");
        }

        @Override
        public void process(Record<String, Long> record) {
            Long oldValue = store.get(record.key());
            if (oldValue == null || record.value() > oldValue) {
                store.put(record.key(), record.value());
            }
        }

        private void flushStore() {
            KeyValueIterator<String, Long> it = store.all();
            while (it.hasNext()) {
                KeyValue<String, Long> next = it.next();
                context.forward(new Record(next.key, next.value, context.currentSystemTimeMs()));
            }
        }

        @Override
        public void close() {}
    }


}
