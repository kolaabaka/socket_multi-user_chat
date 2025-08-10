package com.fx.service.inout;

import com.fx.dto.MessageDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Scanner;

public class OutputClientService extends Thread{

    private final OutputStream outputStream;
    private final Calendar calendar = Calendar.getInstance();
    private final int id;

    public OutputClientService(OutputStream outputStream, int id){
        this.outputStream = outputStream;
        this.id = id;
    }

    @Override
    public void run() {
        Scanner reader = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("ENTER RECIPIENT");
                var recipient = reader.nextLine();

                System.out.println("ENTER YOUR MESSAGE");
                var text = reader.nextLine();

                MessageDTO message = new MessageDTO(String.valueOf(id), recipient, text, String.valueOf(calendar.getTime()));
                outputStream.write(message.toString().getBytes());
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
