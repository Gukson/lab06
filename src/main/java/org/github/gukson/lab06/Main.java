package org.github.gukson.lab06;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Creator creator = new Creator();
        try {
            creator.run();
        }catch (IOException e2){
            e2.printStackTrace();
        }

    }
}
