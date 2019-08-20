package com.train4game.munoon.utils.exceptions;

import java.util.Collections;
import java.util.List;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final List<String> details;

    public ErrorInfo(CharSequence url, ErrorType type, List<String> details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }

    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url.toString();
        this.type = type;
        this.details = Collections.singletonList(detail);
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public List<String> getDetails() {
        return details;
    }
}
