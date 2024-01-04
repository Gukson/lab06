package org.example;

import java.io.IOException;

import org.github.gukson.lab06.socketexample.GreetClient;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestClass {
    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() throws IOException {
        GreetClient client = new GreetClient();
        client.startConnection("localhost", 8888);
        String response = client.sendMessage("hello server");
        System.out.println(response);
        assertEquals("hello client",response);
    }
}

