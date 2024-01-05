package org.github.gukson.lab06.model.machine;

import org.github.gukson.lab06.model.Field;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Harvester extends Machine {
    private Socket worldSocket, responseSocket;
    private ServerSocket responseServerSocket;
    private BufferedWriter out;
    private BufferedReader in;
    private final String role = "Harvester";
    private Integer serverPort = 8080;
    private Integer rowNumb;
    private String host;
    private int port;
    private Integer id;
    private Field actualField;

    public Harvester(String host, int port) {
        this.host = host;
        this.port = port;
        this.id = null;
        try {
            startConnection();
            registration();
        } catch (IOException e2) {
            System.out.println("=========");
            System.out.println(e2.getMessage());
        }
    }

    private void startConnection() throws IOException {
        this.worldSocket = new Socket("localhost", 8080);
        this.out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
    }

    private void registration() throws IOException {
        String response = register(host, port, role, out);
        String[] resp = response.split(" ");
        if (Objects.equals(resp[0], "registration") && Integer.parseInt(resp[1].strip()) != -1) {
            id = Integer.parseInt(resp[1]);
            work();
        } else {
            System.out.println("error");
        }
    }

    private void work() {
        System.out.println("working");
        System.out.println(id);
        System.out.println(role);
        while (true){
            try {
                actualField = move(id, role, out, port);
                System.out.println("Współrzędne pola: " +actualField.getX() + " " + actualField.getY());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }
}
