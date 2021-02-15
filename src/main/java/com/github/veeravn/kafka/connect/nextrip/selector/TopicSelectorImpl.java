package com.github.veeravn.kafka.connect.nextrip.selector;

import org.apache.kafka.common.Configurable;

import java.util.Map;

public class TopicSelectorImpl implements TopicSelector, Configurable {

    private String topic;

    @Override
    public void configure(Map<String, ?> props) {
        final TopicSelectorConfig config = new TopicSelectorConfig(props);

        // Always use the first topic in the list
        topic = config.getTopics().get(0);
    }

    @Override
    public String getTopic(Object data) {
        return topic;
    }
}
