package com.fx.service.inout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class InputServerHandler extends Thread {

    private final InputStream input;
    private final int idUser;
    private final BlockingQueue qu;

    public InputServerHandler(InputStream input, int idUser, BlockingQueue qu) {
        this.input = input;
        this.idUser = idUser;
        this.qu = qu;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedReader buf = new BufferedReader(new InputStreamReader(input));
                var bufStr = buf.readLine();
                qu.add(bufStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
