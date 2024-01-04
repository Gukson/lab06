package org.github.gukson.lab06.model.machine;

import com.google.gson.Gson;
import org.github.gukson.lab06.model.Field;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Machine {

    public String register(String host, int port, String role, BufferedWriter out) throws IOException {
        out.write(String.format("register %s %d %s", host,port,role));
        out.flush();
        System.out.println("wysłano zapytanie do serwera o rejestrację");
        out.close();

        String response = "";
        try (ServerSocket responseServerSocket = new ServerSocket(port);
             Socket responseSocket = responseServerSocket.accept();
             BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseSocket.getInputStream()))) {

            // Odczyt odpowiedzi od serwera
            response = responseReader.readLine();
            System.out.printf("Odebrano odpowiedź od serwera na porcie %d : %s%n", port,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

//    public String unregister(Integer id,BufferedWriter out, BufferedReader in) throws IOException {
//        writer.write(responseData);
//        writer.newLine();
//        writer.flush();
//        out.println();
//        String resp = in.readLine();
//        return resp;
//    }
    public Field move(Integer id, String role, BufferedWriter out, Integer port) throws IOException {
        Socket worldSocket = new Socket("localhost", 8080);
        out = new BufferedWriter(new OutputStreamWriter(worldSocket.getOutputStream()));
        out.write(String.format("move %d %s", id,role));
        out.flush();
        System.out.println("wysłano zapytanie do serwera o move");
        out.close();

        String response = "";
        try (ServerSocket responseServerSocket = new ServerSocket(port);
             Socket responseSocket = responseServerSocket.accept();
             BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseSocket.getInputStream()))) {

            // Odczyt odpowiedzi od serwera
            response = responseReader.readLine();
            System.out.printf("Odebrano odpowiedź od serwera na porcie %d : %s%n", port,response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();

        return gson.fromJson(response,Field.class);
    }

}
