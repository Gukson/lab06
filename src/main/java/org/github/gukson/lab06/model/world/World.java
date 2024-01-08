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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class World extends WorldHelper {
    private Field[][] fieldArea;
    private List<Information> harvesters, seeders;
    private ServerSocket serverSocket;
    private Socket clientSocket, responseSocket;
    private BufferedWriter out;
    private BufferedReader in;
    private List harvestersLabels, seedersLabels;
    private JPanel machinePanel;

    public void start(int port) throws IOException {
        harvesters = Collections.synchronizedList(Arrays.asList(new Information[5]));
        seeders = Collections.synchronizedList(Arrays.asList(new Information[5]));
        fieldArea = new Field[5][5];
        harvestersLabels = Collections.synchronizedList(Arrays.asList(new JLabel[5]));
        seedersLabels = Collections.synchronizedList(Arrays.asList(new JLabel[5]));
        setup();


        WorldGui worldGui = new WorldGui(fieldArea);
        Time time = new Time(fieldArea, worldGui.getFieldlabels(), worldGui.getContentPane());
        Thread timer = new Thread(time);
        timer.start();
        machinePanel = worldGui.getMachinePanel();
        worldGui.setVisible(true);
        serverSocket = new ServerSocket(port);

        while (true) {
            clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = in.readLine();
            WorldThread worldThread = new WorldThread(greeting,harvesters,seeders,harvestersLabels,seedersLabels,worldGui,fieldArea, machinePanel);
            Thread thread = new Thread(worldThread);
            thread.start();

        }
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