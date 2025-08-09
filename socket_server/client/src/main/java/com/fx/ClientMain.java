package com.fx;


//import com.fx.dto.MessageDTO;

import com.fx.dto.MessageDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

public class ClientMain {

    private static Calendar calendar = Calendar.getInstance();
    public static int id = 0;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4444);
            var in = socket.getInputStream();
            var out = socket.getOutputStream();

            Thread thIn = new Thread(() -> {
                while (true) {
                    try {
                        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
                        String message = buf.readLine();
                        if (id == 0) {
                            id = Integer.parseInt(message);
                        }
                        System.out.println(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread thOut = new Thread(() -> {
                Scanner reader = new Scanner(System.in);
                while (true) {
                    System.out.println("ENTER YOUR MESSAGE");
                    try {
                        var text = reader.nextLine();
                        MessageDTO message = new MessageDTO(String.valueOf(id), String.valueOf(15), text, String.valueOf(calendar.getTime()));
                        out.write(message.toString().getBytes());
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thOut.start();
            thIn.start();
            thOut.join();
            thIn.join();

        } catch (Exception e) {

        }
    }
}
