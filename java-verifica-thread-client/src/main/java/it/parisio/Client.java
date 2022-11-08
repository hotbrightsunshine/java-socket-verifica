package it.parisio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ConnectIOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client 
{
    Socket s;
    static ObjectMapper om = new ObjectMapper();
    BufferedReader reader;
    DataOutputStream writer;
    
    public Client(int port) throws UnknownHostException, IOException{
        s = new Socket("localhost", port);
        reader = new BufferedReader(
            new InputStreamReader(s.getInputStream()));
        writer = new DataOutputStream(s.getOutputStream());
    }
    
    // Il client non ha bisogno di essere multi-thread
    public void start() throws IOException{

        // Invio della lista vuota
        writer.writeBytes(Message.toJSON(Message.empty()) + "\n");
        System.out.println("Invio la lista vuota...");

        // Lettura dal client (Riceve i biglietti disponibili)
        String str = reader.readLine();
        Message mesg = Message.fromJSON(str);

        System.out.println("Biglietti disponibili: ");
        int k = 0;
        for(Ticket t : mesg.tickets){
            System.out.println("- (Digita " + k + ") " + t.toString());
            k++;
        }

        // Manda al server i biglietti che vuole comprare
        System.out.println("Utente! Scrivi i biglietti che vuoi comprare (uno per linea).\nFinisci digitando `basta`.");
        ArrayList<Ticket> wantToBuy = inputBiglietti(mesg.tickets);
        Message mesgWantToBuy = new Message(wantToBuy);
        if(Message.isEmpty(mesgWantToBuy)){
            System.out.println("Non sono stati inviati messaggi da comprare.");
            return;
        }
        writer.writeBytes(Message.toJSON(mesgWantToBuy) + "\n");

        // Riceve dal client i biglietti venduti
        str = reader.readLine();
        System.out.println(str);
        Message mesgServerReply = Message.fromJSON(str);
        System.out.println("Sono stati venduti questi messaggi: ");
        for(Ticket t : mesgServerReply.tickets){
            System.out.println(" - " + t);
        }

        // Si chiude
        s.close();

    }

    public ArrayList<Ticket> inputBiglietti(ArrayList<Ticket> tickets){
        boolean stop = false;
        Scanner scan = new Scanner(System.in);
        ArrayList<Ticket> asked = new ArrayList<>();
        while(!stop){
            System.out.print("Inserisci un numero oppure `basta`: ");
            String inp = scan.nextLine();
            if(inp.equals("basta")){
                stop = false;
                break;
            } else {
                int n;
                try{
                    n = Integer.parseInt(inp);
                    if(n < 0 || n >= tickets.size()){
                        System.out.println("Numero non valido.");
                        continue;
                    } else {
                        asked.add(tickets.get(n));
                        System.out.println("Biglietto aggiunto.");
                    }
                } catch (Exception e){
                    System.out.println("Input non valido.");
                    continue;
                }

                
            }

        }
        return asked;
    }
}

