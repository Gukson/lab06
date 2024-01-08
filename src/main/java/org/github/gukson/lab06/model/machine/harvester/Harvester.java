package org.github.gukson.lab06.model.machine.harvester;

import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.machine.Machine;

import javax.sound.midi.Soundbank;
import javax.swing.*;
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
    private boolean keepHarvesting;
    private HarvesterGUI harvesterGUI;

    public Harvester() {
        keepHarvesting = true;
        this.id = null;
        harvesterGUI = new HarvesterGUI(this);
        harvesterGUI.setVisible(true);
    }

//    private void startConnection() throws IOException {
//        this.worldSocket = new Socket("localhost", 8080);
//        this.out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
//    }

    public void unregistration() throws IOException{
        Socket worldSocket = new Socket("localhost", 8080);
        out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
        String response = unregister(id, port, out, role);
        String[] resp = response.split(" ");
        if (Objects.equals(resp[1], "Success")){
            out.close();
            in.close();
        }
    }

    public void registration() throws IOException {
        Socket worldSocket = new Socket("localhost", 8080);
        out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
        String response = register(host, port, role, out);
        String[] resp = response.split(" ");
        System.out.println(response);
        if (Objects.equals(resp[0], "registration") && Integer.parseInt(resp[1].strip()) != -1 && Integer.parseInt(resp[1].strip()) != -2) {
            id = Integer.parseInt(resp[1]);
            HarvesterBackground harvesterBackground = new HarvesterBackground();
            try {
                harvesterBackground.execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if(Integer.parseInt(resp[1].strip()) == -2){
            harvesterGUI.portUsed();
        }
        else {
            System.out.println("error");
        }
    }

    private void work() {
        while (true) {
            try {
                actualField = move(id, role, out, port);
                harvesterGUI.updateInfo(actualField);
                System.out.println("Współrzędne pola: " + actualField.getX() + " " + actualField.getY());
                tryToHarvest();
                if(!keepHarvesting){
                    unregistration();
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(350);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    private void tryToHarvest() {
        String response;
        for (int id = 0; id < actualField.getPlants().length; id++) {
            if (actualField.getPlants()[id] != null && actualField.getPlants()[id].getAge() == 10) {
                try {
                    response = harvestRequest(actualField.getY(), actualField.getX(), id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (Objects.equals(response, "harvest Failed")) {
                }
                break;
            }
        }
    }

    private String harvestRequest(Integer y, Integer x, Integer fieldID) throws IOException {
        Socket worldSocket = new Socket("localhost", 8080);
        out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
        out.write(String.format("harvest %d %d %d %d", y, x, fieldID, id));
        out.flush();
        out.close();

        String response = "";
        try (ServerSocket responseServerSocket = new ServerSocket(port);
             Socket responseSocket = responseServerSocket.accept();
             BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseSocket.getInputStream()))) {

            // Odczyt odpowiedzi od serwera
            response = responseReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private class HarvesterBackground extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            work();
            return null;
        }
    }

    public void setKeepHarvesting(boolean keepHarvesting) {
        this.keepHarvesting = keepHarvesting;
    }
}
