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

        while (true){
            try {
                actualField = move(id, role, out, port);
                System.out.println("Współrzędne pola: " +actualField.getX() + " " + actualField.getY());
                tryToHarvest();
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
            if (actualField.getPlants()[id] != null &&  actualField.getPlants()[id].getAge() == 10) {
                try {
                    System.out.println("Wysłano request o ścięcie");
                    response = harvestRequest(actualField.getY(), actualField.getX(), id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (Objects.equals(response, "harvest Failed")){
                    System.out.println("Coś poszło nie tak podczas siania");
                    //TODO Czy maszyna ma coś zrobić, jeżeli wysłano by Failed?
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

}
