package com.github.veeravn.kafka.connect.nextrip.payload;

import com.github.veeravn.kafka.connect.nextrip.Request;
import com.github.veeravn.kafka.connect.nextrip.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

public class PayloadGeneratorImpl implements PayloadGenerator {

    private String requestBody;
    private Map<String, String> requestHeaders;

    @Override
    public void configure(Map<String, ?> props) {
        final PayloadGeneratorConfig config = new PayloadGeneratorConfig(props);

        requestBody = config.getRequestBody();
        requestHeaders = config.getRequestHeaders();
    }

    @Override
    public boolean update(Request request, Response response) {
        // False = Wait for the next poll cycle before calling again.
        return false;
    }

    @Override
    public String getRequestBody() {
        return requestBody;
    }

    @Override
    public Map<String, String> getRequestParameters() {
        return new HashMap<>();
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public Map<String, Object> getOffsets() {
        return Collections.singletonMap("timestamp", currentTimeMillis());
    }

    @Override
    public void setOffsets(Map<String, Object> offsets) {
        // do nothing.
    }
}
