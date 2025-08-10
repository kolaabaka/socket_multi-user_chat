package com.fx;

import com.fx.service.kafka.KafkaConsumerService;
import com.fx.service.inout.InputClientService;
import com.fx.service.inout.OutputClientService;

import java.net.Socket;

public class ClientMain {

    public static int id = 0;

    public static void main(String[] args) {

        id = Integer.parseInt(args[0]);
        KafkaConsumerService kaf = new KafkaConsumerService(id);
        kaf.start();

        try {
            Socket socket = new Socket("localhost", 4444);
            var in = socket.getInputStream();
            var out = socket.getOutputStream();

            //Logging client ID
            out.write(id);
            out.flush();

            InputClientService thIn = new InputClientService(in);
            OutputClientService thOut = new OutputClientService(out, id);

            thIn.start();
            thOut.start();

            thOut.join();
            thIn.join();

        } catch (Exception e) {

        }
    }
}
