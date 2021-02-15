package com.github.veeravn.kafka.connect.nextrip.handler;

import com.github.veeravn.kafka.connect.nextrip.ExecutionContext;
import com.github.veeravn.kafka.connect.nextrip.Response;

import java.util.List;

public interface ResponseHandler {

    List<String> handle(Response response, ExecutionContext ctx);
}
