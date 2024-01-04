package org.github.gukson.lab06.socketexample;

import org.github.gukson.lab06.model.world.World;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
//        GreetServer server=new GreetServer();

        World server = new World();
        server.start(8888);
    }
}
