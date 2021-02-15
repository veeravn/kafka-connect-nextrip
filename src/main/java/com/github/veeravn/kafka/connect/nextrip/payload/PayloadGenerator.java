package com.github.veeravn.kafka.connect.nextrip.payload;

import com.github.veeravn.kafka.connect.nextrip.Request;
import com.github.veeravn.kafka.connect.nextrip.Response;

import java.util.Map;

public interface PayloadGenerator {

    /**
     * Update the generator with the request/response that just happened.
     *
     * @param request  The request just made
     * @param response The response just received
     * @return True if another call should be made immediately, false otherwise.
     */
    boolean update(Request request, Response response);

    /**
     * Get the HTTP request body that should be sent with the next request.
     * This is not used for GET requests.
     *
     * @return The body content to be sent to the REST service.
     */
    String getRequestBody();

    /**
     * Get the HTTP request parameters that should be sent with the next request.
     *
     * @return The parameters to be sent to the REST service.
     */
    Map<String, String> getRequestParameters();

    /**
     * Get the HTTP request headers that should be sent with the next request.
     *
     * @return The headers to be sent to the REST service.
     */
    Map<String, String> getRequestHeaders();

    /**
     * Get the input stream offsets for the current payload.
     *
     * @return The offsets.
     */
    Map<String, Object> getOffsets();

    /**
     * Set the input stream offsets.
     *
     * @param offsets The offsets.
     */
    void setOffsets(Map<String, Object> offsets);

    void configure(Map<String, ?> props);
}

