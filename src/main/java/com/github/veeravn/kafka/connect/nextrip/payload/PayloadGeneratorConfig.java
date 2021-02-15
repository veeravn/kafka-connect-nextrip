package com.github.veeravn.kafka.connect.nextrip.payload;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayloadGeneratorConfig extends AbstractConfig {

    public static final String REQUEST_BODY_CONFIG = "rest.source.body";
    private static final String REQUEST_BODY_DOC = "The HTTP request body that will be sent with each REST request. " +
            "This parameter is not appliccable to GET requests.";
    private static final String REQUEST_BODY_DISPLAY = "HTTP request body for REST source connector.";
    private static final String REQUEST_BODY_DEFAULT = "";

    public static final String REQUEST_HEADERS_CONFIG = "rest.source.headers";
    private static final String REQUEST_HEADERS_DISPLAY = "The HTTP request headers that will be sent with each REST " +
            "request. The headers should be of the form 'key:value'.";
    private static final String REQUEST_HEADERS_DOC = "HTTP request headers for REST source connector.";
    private static final List<String> REQUEST_HEADERS_DEFAULT = Arrays.asList("Content-Type:application/json", "Accept:application/json");

    private final Map<String, String> requestHeaders;


    protected PayloadGeneratorConfig(ConfigDef config, Map<String, ?> unparsedConfig) {
        super(config, unparsedConfig);

        requestHeaders = new HashMap<>();
        REQUEST_HEADERS_DEFAULT.forEach(header -> {
            String[] keyValues = header.split(":");
            requestHeaders.put(keyValues[0],keyValues[1]);
        });
    }

    public PayloadGeneratorConfig(Map<String, ?> unparsedConfig) {
        this(conf(unparsedConfig), unparsedConfig);
    }



    public static ConfigDef conf(Map<String, ?> unparsedConfig) {
        String group = "NEXTRIP";
        int orderInGroup = 0;
        ConfigDef config =  new ConfigDef()
                .define(REQUEST_BODY_CONFIG,
                        Type.STRING,
                        REQUEST_BODY_DEFAULT,
                        Importance.LOW,
                        REQUEST_BODY_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.LONG,
                        REQUEST_BODY_DISPLAY)

                .define(REQUEST_HEADERS_CONFIG,
                        Type.LIST,
                        REQUEST_HEADERS_DEFAULT,
                        Importance.HIGH,
                        REQUEST_HEADERS_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.SHORT,
                        REQUEST_HEADERS_DISPLAY)
                ;
        return(config);
    }

    public String getRequestBody() {
        return this.getString(REQUEST_BODY_CONFIG);
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }
}
