package com.github.veeravn.kafka.connect.nextrip.executor;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class RequestExecutorConfig extends AbstractConfig {

    public static final String HTTP_CONNECTION_TIMEOUT_CONFIG = "nextrip.connection.connection.timeout";
    private static final String HTTP_CONNECTION_TIMEOUT_DISPLAY = "HTTP connection timeout in milliseconds";
    private static final String HTTP_CONNECTION_TIMEOUT_DOC = "HTTP connection timeout in milliseconds";
    private static final long HTTP_CONNECTION_TIMEOUT_DEFAULT = 2000;

    public static final String HTTP_READ_TIMEOUT_CONFIG = "nextrip.connection.read.timeout";
    private static final String HTTP_READ_TIMEOUT_DISPLAY = "HTTP read timeout in milliseconds";
    private static final String HTTP_READ_TIMEOUT_DOC = "HTTP read timeout in milliseconds";
    private static final long HTTP_READ_TIMEOUT_DEFAULT = 2000;

    public static final String HTTP_KEEP_ALIVE_DURATION_CONFIG = "nextrip.connection.keep.alive.ms";
    private static final String HTTP_KEEP_ALIVE_DURATION_DISPLAY = "Keep alive in milliseconds";
    private static final String HTTP_KEEP_ALIVE_DURATION_DOC = "For how long keep HTTP connection should be keept alive in milliseconds";
    private static final long HTTP_KEEP_ALIVE_DURATION_DEFAULT = 300000; // 5 minutes

    public static final String HTTP_MAX_IDLE_CONNECTION_CONFIG = "nextrip.connection.max.idle";
    private static final String HTTP_MAX_IDLE_CONNECTION_DISPLAY = "Number of idle connections";
    private static final String HTTP_MAX_IDLE_CONNECTION_DOC = "How many idle connections per host can be keept opened";
    private static final int HTTP_MAX_IDLE_CONNECTION_DEFAULT = 5;


    protected RequestExecutorConfig(ConfigDef config, Map<String, ?> parsedConfig) {
        super(config, parsedConfig);
    }

    public RequestExecutorConfig(Map<String, ?> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        String group = "NEXTRIP";
        int orderInGroup = 0;
        return new ConfigDef()
                .define(HTTP_CONNECTION_TIMEOUT_CONFIG,
                        ConfigDef.Type.LONG,
                        HTTP_CONNECTION_TIMEOUT_DEFAULT,
                        ConfigDef.Importance.LOW,
                        HTTP_CONNECTION_TIMEOUT_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.NONE,
                        HTTP_CONNECTION_TIMEOUT_DISPLAY)

                .define(HTTP_READ_TIMEOUT_CONFIG,
                        ConfigDef.Type.LONG,
                        HTTP_READ_TIMEOUT_DEFAULT,
                        ConfigDef.Importance.LOW,
                        HTTP_READ_TIMEOUT_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.NONE,
                        HTTP_READ_TIMEOUT_DISPLAY)

                .define(HTTP_KEEP_ALIVE_DURATION_CONFIG,
                        ConfigDef.Type.LONG,
                        HTTP_KEEP_ALIVE_DURATION_DEFAULT,
                        ConfigDef.Importance.LOW,
                        HTTP_KEEP_ALIVE_DURATION_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.NONE,
                        HTTP_KEEP_ALIVE_DURATION_DISPLAY)

                .define(HTTP_MAX_IDLE_CONNECTION_CONFIG,
                        ConfigDef.Type.INT,
                        HTTP_MAX_IDLE_CONNECTION_DEFAULT,
                        ConfigDef.Importance.LOW,
                        HTTP_MAX_IDLE_CONNECTION_DOC,
                        group,
                        ++orderInGroup,
                        ConfigDef.Width.NONE,
                        HTTP_MAX_IDLE_CONNECTION_DISPLAY)
                ;
    }

    public long getReadTimeout() {
        return this.getLong(HTTP_READ_TIMEOUT_CONFIG);
    }

    public long getConnectionTimeout() {
        return this.getLong(HTTP_CONNECTION_TIMEOUT_CONFIG);
    }

    public long getKeepAliveDuration() {
        return this.getLong(HTTP_KEEP_ALIVE_DURATION_CONFIG);
    }

    public int getMaxIdleConnections() {
        return this.getInt(HTTP_MAX_IDLE_CONNECTION_CONFIG);
    }
}
