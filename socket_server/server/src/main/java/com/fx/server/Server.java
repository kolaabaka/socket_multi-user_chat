package com.fx.server;

import com.fx.service.inout.InputServerHandler;
import com.fx.service.inout.OutputServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server extends Thread {

    private static final Integer PORT = 4444;

    private final Map<Integer, Socket> userMap = new HashMap<>();
    private final BlockingQueue<String> qu = new LinkedBlockingQueue<>();

    public Server() {
        this.start();
    }

    @Override
    public void run() {

        try (ServerSocket socket = new ServerSocket(PORT)) {

            System.out.println("SERVER STARTED; PORT " + PORT);

            OutputServerHandler thOut = new OutputServerHandler(qu, userMap);
            thOut.start();

            while (true) {
                Socket client = socket.accept();
                var id = client.getInputStream().read();

                Thread thIn = new InputServerHandler(client.getInputStream(), id, qu);
                thIn.start();

                userMap.put(id, client);
                System.out.println(userMap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
