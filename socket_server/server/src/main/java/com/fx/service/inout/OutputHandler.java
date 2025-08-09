package com.fx.service.inout;

import com.fx.service.MessageParser;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class OutputHandler extends Thread {
    private final BlockingQueue<String> qu;
    private Map<Integer, Socket> userMap;

    public OutputHandler(BlockingQueue<String> qu, Map<Integer, Socket> userMap) {
        this.qu = qu;
        this.userMap = userMap;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = qu.take();
                System.out.println(message + " FROM SERVER");
                var messagDto = MessageParser.parseMessage(message);
                var userTo = userMap.getOrDefault(messagDto.to(), null);
                if (userTo == null) {
                    throw new AccountNotFoundException();
                } else {
                    var outTo = userTo.getOutputStream();
                    outTo.write(messagDto.Message().getBytes());
                    outTo.flush();
                }
            } catch (InterruptedException | AccountNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int broadcast() {
        int amount = 0;
        for (var client : userMap.keySet()) {
            var connectionSoc = userMap.get(client);
            try {
                var out = connectionSoc.getOutputStream();
                out.write(new String("broadcast " + client + "\n").getBytes());
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return amount;
    }
}
