package com.fx.service.inout;

import com.fx.service.MessageParser;
import com.fx.service.kafka.KafkaProducerService;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

public class OutputServerHandler extends Thread {
    private final BlockingQueue<String> qu;
    private Map<Integer, Socket> userMap;
    private final KafkaProducerService kafksService = new KafkaProducerService("localhost:9092");

    public OutputServerHandler(BlockingQueue<String> qu, Map<Integer, Socket> userMap) {
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
                var userTo = userMap.get(Integer.parseInt(messagDto.to()));
                if (userTo == null) {
                    kafksService.sendMessage(messagDto.to(), messagDto.toString());
                } else {
                    var outTo = userTo.getOutputStream();
                    outTo.write((messagDto + "\n").getBytes());
                    outTo.flush();
                }
            } catch (InterruptedException | IOException | ExecutionException e) {
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
