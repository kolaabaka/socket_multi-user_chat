package com.fx.service.inout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import static com.fx.server.Server.deleteConnection;

public class InputServerHandler extends Thread {

    private final InputStream inputStream;
    private final int idUser; //use for deleting from usermap, make delete method in Server
    private final BlockingQueue qu;
    private boolean connectionClose = false;

    public InputServerHandler(InputStream inputStream, int idUser, BlockingQueue qu) {
        this.inputStream = inputStream;
        this.idUser = idUser;
        this.qu = qu;
    }

    @Override
    public void run() {
        while (!connectionClose) {
            try {
                BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));
                var bufStr = buf.readLine();
                qu.add(bufStr);
            } catch (Exception e) {
                connectionClose = true;
                deleteConnection(idUser);
            }
        }
    }
}
