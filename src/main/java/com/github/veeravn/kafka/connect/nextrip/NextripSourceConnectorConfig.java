package com.github.veeravn.kafka.connect.nextrip;

import com.github.veeravn.kafka.connect.nextrip.config.InstanceOfValidator;
import com.github.veeravn.kafka.connect.nextrip.config.MethodRecommender;
import com.github.veeravn.kafka.connect.nextrip.config.MethodValidator;
import com.github.veeravn.kafka.connect.nextrip.config.ServiceProviderInterfaceRecommender;
import com.github.veeravn.kafka.connect.nextrip.executor.RequestExecutor;
import com.github.veeravn.kafka.connect.nextrip.executor.RequestExecutorImpl;
import com.github.veeravn.kafka.connect.nextrip.handler.ResponseHandler;
import com.github.veeravn.kafka.connect.nextrip.handler.ResponseHandlerImpl;
import com.github.veeravn.kafka.connect.nextrip.payload.PayloadGenerator;
import com.github.veeravn.kafka.connect.nextrip.payload.PayloadGeneratorImpl;
import com.github.veeravn.kafka.connect.nextrip.selector.TopicSelector;
import com.github.veeravn.kafka.connect.nextrip.selector.TopicSelectorImpl;
import lombok.EqualsAndHashCode;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

@SuppressWarnings("DefaultAnnotationParam")
@EqualsAndHashCode(callSuper=false)
public class NextripSourceConnectorConfig extends AbstractConfig {

    public static final String SOURCE_POLL_INTERVAL_CONFIG = "nextrip.poll.interval.ms";
    private static final String SOURCE_POLL_INTERVAL_DOC = "How often to poll the source URL.";
    private static final String SOURCE_POLL_INTERVAL_DISPLAY = "Polling interval";
    private static final Long SOURCE_POLL_INTERVAL_DEFAULT = 30000L;

    public static final String SOURCE_METHOD_CONFIG = "nextrip.method";
    private static final String SOURCE_METHOD_DOC = "The HTTP method for REST source connector.";
    private static final String SOURCE_METHOD_DISPLAY = "Source method";
    private static final String SOURCE_METHOD_DEFAULT = "GET";

    public static final String SOURCE_URL_CONFIG = "nextrip.url";
    public static final String SOURCE_URL_DEFAULT = "https://svc.metrotransit.org/NexTrip/VehicleLocations/0";
    private static final String SOURCE_URL_DOC = "The URL for REST source connector.";
    private static final String SOURCE_URL_DISPLAY = "URL for REST source connector.";

    public static final String SOURCE_PAYLOAD_GENERATOR_CONFIG = "nextrip.data.generator";
    private static final String SOURCE_PAYLOAD_GENERATOR_DOC = "The payload generator class which will produce the HTTP " +
            "request payload to be sent to the REST endpoint.  The payload may be sent as request parameters in the case of a " +
            "GET request, or as the request body in the case of POST";
    private static final String SOURCE_PAYLOAD_GENERATOR_DISPLAY = "Payload Generator class for REST source connector.";
    private static final Class<? extends PayloadGenerator> SOURCE_PAYLOAD_GENERATOR_DEFAULT = PayloadGeneratorImpl.class;

    public static final String SOURCE_TOPIC_SELECTOR_CONFIG = "nextrip.topic.selector";
    private static final String SOURCE_TOPIC_SELECTOR_DOC = "The topic selector class for REST source connector.";
    private static final String SOURCE_TOPIC_SELECTOR_DISPLAY = "Topic selector class for REST source connector.";
    private static final Class<? extends TopicSelector> SOURCE_TOPIC_SELECTOR_DEFAULT = TopicSelectorImpl.class;

    public static final String SOURCE_REQUEST_EXECUTOR_CONFIG = "nextrip.executor.class";
    private static final String SOURCE_REQUEST_EXECUTOR_DISPLAY = "HTTP request executor";
    private static final String SOURCE_REQUEST_EXECUTOR_DOC = "HTTP request executor. Default is OkHttpRequestExecutor";
    private static final String SOURCE_REQUEST_EXECUTOR_DEFAULT = RequestExecutorImpl.class.getName();


    private final TopicSelector topicSelector;
    private final PayloadGenerator payloadGenerator;
    private RequestExecutor requestExecutor;


    protected NextripSourceConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
        topicSelector = this.getConfiguredInstance(SOURCE_TOPIC_SELECTOR_CONFIG, TopicSelector.class);
        requestExecutor = this.getConfiguredInstance(SOURCE_REQUEST_EXECUTOR_CONFIG, RequestExecutor.class);
        payloadGenerator = this.getConfiguredInstance(SOURCE_PAYLOAD_GENERATOR_CONFIG, PayloadGenerator.class);
    }

    public NextripSourceConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        String group = "NEXTRIP_REST";
        int orderInGroup = 0;
        return new ConfigDef()
                .define(SOURCE_POLL_INTERVAL_CONFIG,
                        ConfigDef.Type.LONG, SOURCE_POLL_INTERVAL_DEFAULT,
                        ConfigDef.Importance.LOW, SOURCE_POLL_INTERVAL_DOC,
                        group, ++orderInGroup,
                        ConfigDef.Width.SHORT,
                        SOURCE_POLL_INTERVAL_DISPLAY)

                .define(SOURCE_METHOD_CONFIG,
                        ConfigDef.Type.STRING,
                        SOURCE_METHOD_DEFAULT,
                        new MethodValidator(),
                        ConfigDef.Importance.HIGH,
                        SOURCE_METHOD_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.SHORT,
                        SOURCE_METHOD_DISPLAY,
                        new MethodRecommender())

                .define(SOURCE_URL_CONFIG,
                        ConfigDef.Type.STRING,
                        SOURCE_URL_DEFAULT,
                        ConfigDef.Importance.HIGH,
                        SOURCE_URL_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.SHORT,
                        SOURCE_URL_DISPLAY)

                .define(SOURCE_PAYLOAD_GENERATOR_CONFIG,
                        ConfigDef.Type.CLASS,
                        SOURCE_PAYLOAD_GENERATOR_DEFAULT,
                        new InstanceOfValidator(PayloadGenerator.class),
                        ConfigDef.Importance.HIGH,
                        SOURCE_PAYLOAD_GENERATOR_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.SHORT,
                        SOURCE_PAYLOAD_GENERATOR_DISPLAY,
                        new ServiceProviderInterfaceRecommender<>(PayloadGenerator.class))

                .define(SOURCE_TOPIC_SELECTOR_CONFIG,
                        ConfigDef.Type.CLASS,
                        SOURCE_TOPIC_SELECTOR_DEFAULT,
                        new InstanceOfValidator(TopicSelector.class),
                        ConfigDef.Importance.HIGH,
                        SOURCE_TOPIC_SELECTOR_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.SHORT,
                        SOURCE_TOPIC_SELECTOR_DISPLAY,
                        new ServiceProviderInterfaceRecommender<>(TopicSelector.class))

                .define(SOURCE_REQUEST_EXECUTOR_CONFIG,
                        ConfigDef.Type.CLASS,
                        SOURCE_REQUEST_EXECUTOR_DEFAULT,
                        new InstanceOfValidator(RequestExecutor.class),
                        ConfigDef.Importance.LOW,
                        SOURCE_REQUEST_EXECUTOR_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.NONE,
                        SOURCE_REQUEST_EXECUTOR_DISPLAY,
                        new ServiceProviderInterfaceRecommender<>(RequestExecutor.class))
                ;
    }

    public ResponseHandler getResponseHandler() {
        return new ResponseHandlerImpl();
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    public long getPollInterval() {
        return this.getLong(SOURCE_POLL_INTERVAL_CONFIG);
    }

    public String getMethod() {
        return this.getString(SOURCE_METHOD_CONFIG);
    }

    public String getUrl() {
        return this.getString(SOURCE_URL_CONFIG);
    }

    public TopicSelector getTopicSelector() {
        return topicSelector;
    }

    public PayloadGenerator getPayloadGenerator() {
        return payloadGenerator;
    }

}
