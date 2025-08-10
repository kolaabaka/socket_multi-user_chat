package com.fx.service.inout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputClientService extends Thread{

    private final InputStream inputStream;

    public InputClientService(InputStream inputStream){
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));
                String message = buf.readLine();
                System.out.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
