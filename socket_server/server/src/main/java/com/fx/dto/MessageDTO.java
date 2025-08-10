package com.fx.dto;

public record MessageDTO(String from, String to, String message, String timeStamp) {
    @Override
    public String toString() {
        return from + "." + to + "." + message + "." + timeStamp + "\n";
    }
}
