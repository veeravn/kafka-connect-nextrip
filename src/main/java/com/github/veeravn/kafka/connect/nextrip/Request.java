package com.github.veeravn.kafka.connect.nextrip;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String url;
    private String body;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String method;


    public Request(String url, String method, String body, Map<String, String> parameters, Map<String, String> headers) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.parameters = parameters;
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", parameters=" + parameters +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    public static class RequestFactory {

        private String url;
        private String method;
        private Map<String, String> headers;

        public RequestFactory(String url, String method) {
            this.url = url;
            this.method = method;
            headers = new HashMap<>();
        }

        public Request createRequest(String body, Map<String, String> parameters, Map<String, String> headers) {
            return new Request(url, method, body, parameters, headers);
        }

        public Request createRequest(String payload, Map<String, String> headers) {
            if("GET".equalsIgnoreCase(method)) {
                return new Request(url, method, null, Collections.singletonMap("format", payload), headers);
            } else {
                return new Request(url, method, payload, Collections.emptyMap(), headers);
            }
        }
    }
}
