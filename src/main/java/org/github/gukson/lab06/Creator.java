package org.github.gukson.lab06;

import org.github.gukson.lab06.model.machine.Harvester;
import org.github.gukson.lab06.model.world.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Creator {

    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj jaki obiekt chcesz zarejestrować? World / Seeder / Harvester");
        String obiekt = scanner.nextLine();

        switch (obiekt){
            case "World":
                World world = new World();
                world.start(8080);
            case "Harvester":
                System.out.println("Podaj numer portu");
                String port = scanner.nextLine();
                if(isPortAvailable(Integer.parseInt(port))){
                    new Harvester("localhost", Integer.parseInt(port));
                }
        }
        scanner.close();
    }

    private static boolean isPortAvailable(int port) {
        try {
            // Próbuj utworzyć ServerSocket na danym porcie
            ServerSocket serverSocket = new ServerSocket(port);

            // Jeżeli utworzenie ServerSocket się powiedzie, oznacza to, że port jest dostępny
            serverSocket.close();
            System.out.println("port jest ds†epny");
            return true;
        } catch (IOException e) {
            System.out.println("socket jest niedostepny");
            // Rzucenie IOException oznacza, że port jest już zajęty
            return false;
        }
    }
}
