package org.github.gukson.lab06.model.world;

import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.Information;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class World extends WorldHelper {
    private Field[][] fieldArea;
    private Information[] harvesters;
    private Information[] seeders;
    private ServerSocket serverSocket;
    private Socket clientSocket, responseSocket;
    private BufferedWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        harvesters = new Information[5];
        seeders = new Information[5];
        fieldArea = new Field[5][5];
        setup();

        serverSocket = new ServerSocket(port);


        while (true) {
            System.out.println("waiting");
            clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = in.readLine();

            System.out.println(greeting);
            String cmd = greeting.split(" ")[0];
            Integer id;
            String role;
            switch (cmd) {
                //register localhost 8081 Harvester
                case "register":
                    // register, host, port, role
                    role = greeting.split(" ")[3];
                    id = rejestracja(role);
                    int responsePort = Integer.parseInt(greeting.split(" ")[2]);

                    if (id != -1) {
                        if (Objects.equals(role, "Harvester")) {
                            harvesters[id] = new Information(0, id, "down", Integer.parseInt(greeting.split(" ")[2]));
                        } else if (Objects.equals(role, "Seeder")) {
                            seeders[id] = new Information(id, 0, "left", Integer.parseInt(greeting.split(" ")[2]));
                        }
                    }
                    response(String.format("registration %d", id), responsePort);
                    //move id rola
                case "move":
                    id = Integer.parseInt(greeting.split(" ")[1]);
                    role = greeting.split(" ")[2];
                    Integer[] pos;
                    if (role == "Harvester") {
                        pos = newPosition(harvesters[id],harvesters,seeders);
                    } else {
                        pos = newPosition(seeders[id],harvesters,seeders);
                    }

                    //get field from map
                    //pack to json -> send back to machine

            }
        }
    }

    private Integer rejestracja(String role) {
        //Sprawdzanie, czy port nie jest juz zajęty
        if (Objects.equals(role, "Harvester")) {
            for (int i = 0; i < harvesters.length; i++) {
                if (harvesters[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < seeders.length; i++) {
                if (seeders[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void setup() {
        //generate Field
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                fieldArea[x][y] = new Field(100);
            }
        }
    }
}