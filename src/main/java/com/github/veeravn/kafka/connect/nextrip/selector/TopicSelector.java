package com.github.veeravn.kafka.connect.nextrip.selector;

import java.util.Map;

public interface TopicSelector {
    String getTopic(Object data);


}
