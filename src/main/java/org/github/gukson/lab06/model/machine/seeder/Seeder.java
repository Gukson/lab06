package org.github.gukson.lab06.model.machine.seeder;

import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.machine.Machine;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Seeder extends Machine {
    private Socket worldSocket, responseSocket;
    private ServerSocket responseServerSocket;
    private BufferedWriter out;
    private BufferedReader in;
    private final String role = "Seeder";
    private Integer serverPort = 8080;
    private Integer rowNumb;
    private String host;
    private int port;
    private Integer id;
    private Field actualField;
    private SeederGUI seederGUI;
    private Boolean keepSeeding;

    public Seeder() {
        this.host = "localhost";
        this.id = null;
        seederGUI = new SeederGUI(this);
        seederGUI.setVisible(true);
    }


    public void registration() throws IOException {
        Socket worldSocket = new Socket("localhost", 8080);
        out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
        String response = register(host, port, role, out);
        String[] resp = response.split(" ");
        if (Objects.equals(resp[0], "registration") && Integer.parseInt(resp[1].strip()) != -1 && Integer.parseInt(resp[1].strip()) != -2) {
            id = Integer.parseInt(resp[1]);
            SeederInBack seeder = new SeederInBack();
            seeder.execute();
        }else if(Integer.parseInt(resp[1].strip()) == -2){
            seederGUI.portUsed();
        } else {
            System.out.println("error");
        }
    }

    public void unregistration() throws IOException{
        Socket worldSocket = new Socket("localhost", 8080);
        out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
        String response = unregister(id, port, out, role);
        String[] resp = response.split(" ");
        if (Objects.equals(resp[1], "Success")){
            System.out.println("Wyrejestrowano");
            out.close();
            in.close();
        }
    }

    private void work() {
        while (true) {
            try {
                actualField = move(id, role, out, port);
                seederGUI.updateInfo(actualField);
                System.out.println("Współrzędne pola: " + actualField.getX() + " " + actualField.getY());
                tryToSeed();
                if(!keepSeeding){
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

    private void tryToSeed() {
        if (actualField.getSeedsQueue()[0] == null) {
            System.out.println("Nie ma nic do zasiania");
        } else {
            String response;
            for (int id = 0; id < actualField.getPlants().length; id++) {
                if (actualField.getPlants()[id] == null) {
                    try {
                        response = seedRequest(actualField.getY(), actualField.getX(), id);
                        System.out.println("Wysłano request o zasianie");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (Objects.equals(response, "seed Failed")){
                        System.out.println("Coś poszło nie tak podczas siania");
                        //TODO Czy maszyna ma coś zrobić, jeżeli wysłano by Failed?
                    }
                    break;
                }
            }

        }
    }

    private String seedRequest(Integer y, Integer x, Integer fieldID) throws IOException {
        Socket worldSocket = new Socket("localhost", 8080);
        out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
        out.write(String.format("seed %d %d %d %d", y, x, fieldID, id));
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

    public void setKeepSeeding(Boolean keepSeeding) {
        this.keepSeeding = keepSeeding;
    }

    private class SeederInBack extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            work();
            return null;
        }
    }

}
