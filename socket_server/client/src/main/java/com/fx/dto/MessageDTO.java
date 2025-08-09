package com.fx.dto;

public record MessageDTO(String from, String to, String Message, String timeStamp) {
    @Override
    public String toString() {
        return from + "." + to + "." + Message + "." + timeStamp + "\n";
    }
}
