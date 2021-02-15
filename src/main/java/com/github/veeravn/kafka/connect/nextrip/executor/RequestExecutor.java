package com.github.veeravn.kafka.connect.nextrip.executor;

import com.github.veeravn.kafka.connect.nextrip.Request;
import com.github.veeravn.kafka.connect.nextrip.Response;

public interface RequestExecutor {

    Response execute(Request request) throws Exception;
}
