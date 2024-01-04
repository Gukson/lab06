package org.github.gukson.lab06.model.world;

import org.github.gukson.lab06.model.Information;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class WorldHelper {

    public Integer[] newPosition(Information info, Information[] harvesters, Information[] seeders) {
        System.out.println(info.getFacing());
        switch (info.getFacing()) {
            case "up":
                if (info.getY() == 0 && Objects.equals(info.getFacing(), "up")) {
                    info.setFacing("down");
                } else if (isThisFieldEmpty(info.getX(), info.getY() - 1, harvesters, seeders)) {
                    info.setY(info.getY() - 1);
                }
                break;
            case "down":
                if (info.getY() == 4 && Objects.equals(info.getFacing(), "down")) {
                    info.setFacing("up");
                } else if (isThisFieldEmpty(info.getX(), info.getY() + 1, harvesters, seeders)) {
                    info.setY(info.getY() + 1);
                }
                break;
            case "left":
                if (info.getX() == 0 && Objects.equals(info.getFacing(), "left")) {
                    info.setFacing("right");
                } else if (isThisFieldEmpty(info.getX() - 1, info.getY(), harvesters, seeders)) {
                    info.setY(info.getX() - 1);
                }
                break;
            case "right":
                if (info.getX() == 4 && Objects.equals(info.getFacing(), "right")) {
                    info.setFacing("left");
                } else if (isThisFieldEmpty(info.getX() + 1, info.getY(), harvesters, seeders)) {
                    info.setY(info.getX() + 1);
                }
                break;
        }

        Integer[] res = new Integer[2];
        res[0] = info.getX();
        res[1] = info.getY();

        return res;

    }

    public Boolean isThisFieldEmpty(Integer x, Integer y, Information[] harvesters, Information[] seeders) {
        for (int i = 0; i < 5; i++) {
            if (harvesters[i] != null && Objects.equals(harvesters[i].getX(), x) && Objects.equals(harvesters[i].getY(), y)) {
                System.out.println("field not empty");
                return false;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (seeders[i] != null && Objects.equals(seeders[i].getX(), x) && Objects.equals(seeders[i].getY(), y)) {
                System.out.println("field not empty");
                return false;
            }
        }
        System.out.println("field is empty");
        return true;
    }

    public void response(String response, Integer responsePort) {
        try (Socket responseSocket = new Socket("localhost", responsePort);
             BufferedWriter responseWriter = new BufferedWriter(new OutputStreamWriter(responseSocket.getOutputStream()))) {

            responseWriter.write(response);
            responseWriter.flush();
            responseSocket.close();
            System.out.println(" " + String.format("Wysłano do klienta na porcie: %d odpowedź %s", responsePort, response));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
