package com.github.veeravn.kafka.connect.nextrip;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class NextripSourceConnector extends SourceConnector {

    private NextripSourceConnectorConfig config;
    private Map<String, String> settings;

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        config = new NextripSourceConnectorConfig(props);
        this.settings = props;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return NextripSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        Map<String, String> taskProps = new HashMap<>(config.originalsStrings());
        List<Map<String, String>> taskConfigs = new ArrayList<>(maxTasks);
        for (int i = 0; i < maxTasks; ++i) {
            taskConfigs.add(taskProps);
        }
        return taskConfigs;
    }

    @Override
    public void stop() {
    }

    @Override
    public ConfigDef config() {
        return NextripSourceConnectorConfig.conf();
    }
}
