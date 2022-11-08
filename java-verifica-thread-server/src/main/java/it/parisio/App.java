package it.parisio;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Server s = new Server(12321);
        s.start();

        //System.out.println(Ticket.generate_r());
        
    }
}
