package com.github.veeravn.kafka.connect.nextrip.executor;

import com.github.veeravn.kafka.connect.nextrip.Request;
import com.github.veeravn.kafka.connect.nextrip.Response;
import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.apache.kafka.common.Configurable;
import org.apache.kafka.connect.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestExecutorImpl implements RequestExecutor, Configurable {
    private static Logger log = LoggerFactory.getLogger(RequestExecutorImpl.class);

    private OkHttpClient client;

    @Override
    public void configure(Map<String, ?> props) {
        final RequestExecutorConfig config = new RequestExecutorConfig(props);

        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(config.getMaxIdleConnections(), config.getKeepAliveDuration(), TimeUnit.MILLISECONDS))
                .connectTimeout(config.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
    @Override
    public Response execute(Request request) throws Exception {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .url(createUrl(request.getUrl(), request.getParameters()));

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            builder.get();
        } else {
            builder.method(request.getMethod(), RequestBody.create(
                    MediaType.parse(request.getHeaders().getOrDefault("Content-Type", "")),
                    request.getBody())
            );
        }

        okhttp3.Request okRequest = builder.build();
        log.trace("Making request to: " + request);

        try (okhttp3.Response okResponse = client.newCall(okRequest).execute()) {

            if (okResponse.code() == 200) {
                return new Response(
                        okResponse.code(),
                        okResponse.headers().toMultimap(),
                        okResponse.body() != null ? okResponse.body().string() : null
                );
            } else {
                log.info(okResponse.toString());
                return null;
            }
        } catch (IOException e) {
            throw new RetriableException(e.getMessage(), e);
        }
    }
    private String createUrl(String url, Map<String, String> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return url;
        }

        String format = url.endsWith("?") ? "%s&%s" : "%s?%s";
        return String.format(format, url, parametersToString(parameters));
    }
    private String parametersToString(final Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(e -> parameterToString(e.getKey(), e.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String parameterToString(final String key, final String value) {
        try {
            return key.trim() + "=" + URLEncoder.encode(value.trim(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // This should never happen!
            log.warn("Unable to encode URL parameter as UTF-8 (UTF-8 not supported)");
            return key.trim() + "=" + value.trim();
        }
    }

    private String[] headersToArray(final Map<String, String> headers) {
        return headers.entrySet()
                .stream()
                .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
                .toArray(String[]::new);
    }
}
