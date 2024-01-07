package org.github.gukson.lab06.model.world;

import com.google.gson.Gson;
import org.github.gukson.lab06.gui.WorldGui;
import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.Information;
import org.github.gukson.lab06.model.Plant;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class WorldThread extends WorldHelper implements Runnable{

    private String greeting;
    private List<Information> harvesters, seeders;
    private List<JLabel> harvestersLabels, seedersLabels;
    private WorldGui worldGui;
    private Field[][] fieldArea;
    private JPanel machinePanel;

    public WorldThread(String greeting, List<Information> harvesters, List<Information> seeders, List<JLabel> harvestersLabels, List<JLabel> seedersLabels, WorldGui worldGui, Field[][] fieldArea, JPanel machinePanel) {
        this.greeting = greeting;
        this.harvesters = harvesters;
        this.seeders = seeders;
        this.harvestersLabels = harvestersLabels;
        this.seedersLabels = seedersLabels;
        this.worldGui = worldGui;
        this.fieldArea = fieldArea;
        this.machinePanel = machinePanel;
    }

    private Integer rejestracja(String role) {
        //Sprawdzanie, czy port nie jest juz zajęty
        if (Objects.equals(role, "Harvester")) {
            for (int i = 0; i < harvesters.size(); i++) {
                if (harvesters.get(i) == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < seeders.size(); i++) {
                if (seeders.get(i) == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void run() {

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
                        harvesters.set(id,new Information(0, id, "down", Integer.parseInt(greeting.split(" ")[2])));
                        harvestersLabels.set(id,worldGui.newHarvester(id));
                    } else if (Objects.equals(role, "Seeder")) {
                        seeders.set(id,new Information(id, 0, "right", Integer.parseInt(greeting.split(" ")[2])));
                        seedersLabels.set(id,worldGui.newSeeder(id));
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
                    pos = newPosition(id, harvesters.get(id), harvesters, seeders, harvestersLabels, machinePanel);
                    responsePort = harvesters.get(id).getPort();

                } else {
                    pos = newPosition(id, seeders.get(id), harvesters, seeders, seedersLabels, machinePanel);
                    responsePort = seeders.get(id).getPort();
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
                        response("seed Success",seeders.get(id).getPort());
                    }
                    else {
                        response("seed Failed",seeders.get(id).getPort());
                    }
                }
                else {
                    response("seed Failed",seeders.get(id).getPort());
                }
                break;
            case "harvest":
                System.out.println("otrzymano request o ścięcie");
                y = Integer.parseInt(greeting.split(" ")[1]);
                x = Integer.parseInt(greeting.split(" ")[2]);
                fieldID = Integer.parseInt(greeting.split(" ")[3]);
                id = Integer.parseInt(greeting.split(" ")[4]);

                synchronized (fieldArea){
                    requestedField = fieldArea[y][x];
                }

                if(requestedField.getPlants()[fieldID] != null && requestedField.getPlants()[fieldID].getAge() == 10){
                    requestedField.getPlants()[fieldID] = null;
                    worldGui.getFieldlabels()[y][x].getLabels()[fieldID].setIcon(new ImageIcon("./src/main/resources/data/emptyfield.png"));
                    response("harvest Success",harvesters.get(id).getPort());
                }
                else {
                    response("harvest Failed",harvesters.get(id).getPort());
                }
        }
        Thread.currentThread().interrupt();
    }
}
