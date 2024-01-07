package org.github.gukson.lab06.model.world;

import com.google.gson.Gson;
import org.github.gukson.lab06.gui.WorldGui;
import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.Information;
import org.github.gukson.lab06.model.Plant;

import javax.swing.*;
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
    private JLabel[] harvestersLabels, seedersLabels;
    private JPanel machinePanel;

    public void start(int port) throws IOException {
        harvesters = new Information[5];
        harvestersLabels = new JLabel[5];
        seeders = new Information[5];
        seedersLabels = new JLabel[5];
        fieldArea = new Field[5][5];
        setup();


        WorldGui worldGui = new WorldGui(fieldArea);
        Time time = new Time(fieldArea, worldGui.getFieldlabels(), worldGui.getContentPane());
        Thread timer = new Thread(time);
        timer.start();
        machinePanel = worldGui.getMachinePanel();
        worldGui.setVisible(true);
        serverSocket = new ServerSocket(port);

        while (true) {
            System.out.println("waiting");
            clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = in.readLine();
//            System.out.println(greeting);
            String cmd = greeting.split(" ")[0];
            Integer id;
            String role;
            int responsePort;
            int x,y,fieldID;
            Field requestedField;
            switch (cmd) {
                //register localhost 8081 Harvester
                case "register":
                    // register, host, port, role
                    role = greeting.split(" ")[3];
                    id = rejestracja(role);
                    responsePort = Integer.parseInt(greeting.split(" ")[2]);

                    if (id != -1) {
                        if (Objects.equals(role, "Harvester")) {
                            harvesters[id] = new Information(0, id, "down", Integer.parseInt(greeting.split(" ")[2]));
                            harvestersLabels[id] = worldGui.newHarvester(id);
                        } else if (Objects.equals(role, "Seeder")) {
                            seeders[id] = new Information(id, 0, "right", Integer.parseInt(greeting.split(" ")[2]));
                            seedersLabels[id] = worldGui.newSeeder(id);
                        }
                    }
                    response(String.format("registration %d", id), responsePort);
                    //move id rola
                    break;
                case "move":
//                    System.out.println("Przyszło zapytanie move");
//                    System.out.println(greeting);
                    id = Integer.parseInt(greeting.split(" ")[1]);
                    role = greeting.split(" ")[2].strip();
                    Integer[] pos;

                    if (Objects.equals(role, "Harvester")) {
                        pos = newPosition(id, harvesters[id], harvesters, seeders, harvestersLabels, machinePanel);
                        responsePort = harvesters[id].getPort();

                    } else {
                        pos = newPosition(id, seeders[id], harvesters, seeders, seedersLabels, machinePanel);
                        responsePort = seeders[id].getPort();
                    }

                    //pos = x y
                    Field responseField = fieldArea[pos[1]][pos[0]];
                    Gson gson = new Gson();
                    response(gson.toJson(responseField), responsePort);
//                    System.out.println("wysłano odpowiedź");
                    break;
                //seed y x id
                case "seed":
                    System.out.println("otrzymano request o zasianie");
                    y = Integer.parseInt(greeting.split(" ")[1]);
                    x = Integer.parseInt(greeting.split(" ")[2]);
                    fieldID = Integer.parseInt(greeting.split(" ")[3]);
                    id = Integer.parseInt(greeting.split(" ")[4]);

                    requestedField = fieldArea[y][x];
                    if(requestedField.getSeedsQueue()[0] != null){
                        System.out.println("Rzecyzwiście jest coś do zasiania");
                        if (requestedField.getPlants()[fieldID] == null){
                            System.out.println("wskazane pole jest puste");
                            requestedField.getPlants()[fieldID] = new Plant(1,requestedField.getSeedsQueue()[0].getName());
                            System.out.println("zasiano wskazaną roślinę");
                            requestedField.getSeedsQueue()[0] = null;
                            System.out.println("usunięto roślinę z kolejki");
                            response("seed Success",seeders[id].getPort());
                        }
                        else {
                            response("seed Failed",seeders[id].getPort());
                        }
                    }
                    else {
                        response("seed Failed",seeders[id].getPort());
                    }
                    break;
                case "harvest":
                    System.out.println("otrzymano request o ścięcie");
                    y = Integer.parseInt(greeting.split(" ")[1]);
                    x = Integer.parseInt(greeting.split(" ")[2]);
                    fieldID = Integer.parseInt(greeting.split(" ")[3]);
                    id = Integer.parseInt(greeting.split(" ")[4]);

                    requestedField = fieldArea[y][x];

                    if(requestedField.getPlants()[fieldID] != null && requestedField.getPlants()[fieldID].getAge() == 10){
                        requestedField.getPlants()[fieldID] = null;
                        worldGui.getFieldlabels()[y][x].getLabels()[fieldID].setIcon(new ImageIcon("./src/main/resources/data/emptyfield.png"));
                        response("harvest Success",harvesters[id].getPort());
                    }
                    else {
                        response("harvest Failed",harvesters[id].getPort());
                    }
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
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                fieldArea[y][x] = new Field(100, y, x);
            }
        }
    }
}