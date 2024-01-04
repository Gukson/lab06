package org.github.gukson.lab06;

import java.io.IOException;
import java.net.ServerSocket;

public class closeSocket {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        serverSocket.close();
    }
}
