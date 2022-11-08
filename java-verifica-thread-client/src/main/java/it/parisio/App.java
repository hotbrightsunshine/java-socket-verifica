package it.parisio;

import java.io.IOException;
import java.net.UnknownHostException;

public class App {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Client c = new Client(12321);
        c.start();
    }
}
