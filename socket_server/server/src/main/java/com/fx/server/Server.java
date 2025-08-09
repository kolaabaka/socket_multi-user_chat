package com.fx.server;

import com.fx.service.inout.InputHandler;
import com.fx.service.inout.OutputHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server extends Thread {

    private static final Integer PORT = 4444;

    private final Map<Integer, Socket> userMap = new HashMap<>();
    private final Random randomizer = new Random();
    private final BlockingQueue<String> qu = new LinkedBlockingQueue<>();

    public Server() {
        this.start();
    }

    @Override
    public void run() {
        try (ServerSocket socket = new ServerSocket(PORT)) {

            System.out.println("SERVER STARTED; PORT " + PORT);
            int randomId;

            OutputHandler thOut = new OutputHandler(qu, userMap);
            thOut.start();

            while (true) {
                Socket client = socket.accept();

                randomId = randomizer.nextInt(32);

                Thread thIn = new InputHandler(client.getInputStream(), randomId, qu);
                thIn.start();

                userMap.put(randomId, client);
                var inp = client.getOutputStream();
                inp.write((randomId + "\n").getBytes());
                inp.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
