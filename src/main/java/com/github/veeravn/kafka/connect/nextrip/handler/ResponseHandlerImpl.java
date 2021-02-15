package com.github.veeravn.kafka.connect.nextrip.handler;

import com.github.veeravn.kafka.connect.nextrip.ExecutionContext;
import com.github.veeravn.kafka.connect.nextrip.Response;
import org.apache.kafka.connect.errors.RetriableException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseHandlerImpl implements ResponseHandler {

    private final Pattern allowedCodes;
    private final Pattern forbiddenCodes;

    public ResponseHandlerImpl() {
        this(null, null);
    }

    public ResponseHandlerImpl(String whitelist, String blacklist) {
        this.allowedCodes = createPattern(whitelist);
        this.forbiddenCodes = createPattern(blacklist);
    }

    @Override
    public List<String> handle(Response response, ExecutionContext ctx) {

        String code = String.valueOf(response.getStatusCode());


        if (allowedCodes != null) {
            checkCodeIsAllowed(code);
        }

        if (forbiddenCodes != null) {
            checkCodeIsForbidden(code);
        }

        ArrayList<String> records = new ArrayList<>();
        records.add(response.getPayload());
        return records;
    }

    private void checkCodeIsAllowed(String code) {
        Matcher allowed = allowedCodes.matcher(code);
        if (!allowed.find()) {
            throw new RetriableException("HTTP Response code is not whitelisted " + code);
        }
    }

    private void checkCodeIsForbidden(String code) {
        Matcher forbidden = forbiddenCodes.matcher(code);
        if (forbidden.find()) {
            throw new RetriableException("HTTP Response code is in blacklist " + code);
        }
    }

    private Pattern createPattern(String regex) {
        if (regex == null || regex.trim().isEmpty()) {
            return null;
        }
        return Pattern.compile(regex);
    }

}
