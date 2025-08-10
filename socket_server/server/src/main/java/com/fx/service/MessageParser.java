package com.fx.service;

import com.fx.dto.MessageDTO;

public class MessageParser {

    public static MessageDTO parseMessage(String message) {
        String[] messageArgs = message.split("\\.");
        if(messageArgs.length != 4){
            throw new RuntimeException("Message size out of bounds");
        }
        return new MessageDTO(messageArgs[0], messageArgs[1], messageArgs[2], messageArgs[3]);
    }
}
