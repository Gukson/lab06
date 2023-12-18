package org.github.gukson.lab06;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        GreetServer server=new GreetServer();
        server.start(8888);
    }
}
