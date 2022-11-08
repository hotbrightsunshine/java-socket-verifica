package it.parisio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Server 
{
    ServerSocket ss;
    Random rnd = new Random(System.currentTimeMillis());

    ArrayList<Ticket> tickets = new ArrayList<>();
    ObjectMapper om = new ObjectMapper();

    // Costruttore
    public Server(int port) throws IOException{
        ss = new ServerSocket(port);
        
        // Generazione dei biglietti casuale
        
        int qty = 10;
        for(int i = 0; i < qty; ++i){
            Ticket t = Ticket.generate_r(rnd);
            tickets.add(t);
        }
    }
    
    // Mette in funzione il server
    public void start() throws IOException{
        while(true){
            Socket s = ss.accept();
            System.out.println("Nuova connessione.");
            Worker w = new Worker(s, this);
            Thread t = new Thread(w);
            t.start();
            System.out.println("Ho affidato il client al nuovo Thread.");
        }
    }
}
