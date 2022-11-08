package it.parisio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Worker implements Runnable {

    Socket s;
    BufferedReader reader;
    DataOutputStream writer;
    ObjectMapper om;
    Server father;

    public Worker(Socket socket, Server father) throws IOException{
        om = new ObjectMapper();
        s = socket;
        reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        writer = new DataOutputStream(socket.getOutputStream());
        this.father = father;
    }

    @Override
    public void run() {
        while(true){
            // Leggo dal socket (Dal client verso il Server)
            try {
                String received = reader.readLine();
                // Non è possibile leggere => Il client non manda un messaggio valido/è disconnesso
                if(received == null){
                    break;
                }
                System.out.println("Leggo: " + received);

                Message msg = Message.fromJSON(received);

                if(Message.isEmpty(msg)){
                    // Se il messaggio è vuoto
                    sendWholeList();
                } else {
                    // Se il messaggio non è vuoto (Il client vuole acquistare un biglietto)
                    ArrayList<Ticket> toBeSold = sellTickets(msg);
                    System.out.println("Ho venduto: " + toBeSold.toString());
                    synchronized(father.tickets){
                        System.out.println("Mi rimangono: " + father.tickets);
                    }
                    Message sent = new Message(toBeSold);
                    writer.writeBytes(Message.toJSON(sent) + "\n");
                }
            } catch (Exception e) {
                break;
            }

        }
    }

    public void sendWholeList(){
        // I biglietti del server sono condivisi con tutti i Thread!
        synchronized (father.tickets){
            try {
                String sent = Message.toJSON(new Message(father.tickets));
                writer.writeBytes(sent+"\n");
                System.out.println("Scrivo: " + sent);
            } catch (Exception e){ }
        }
    }

    public ArrayList<Ticket> sellTickets(Message msg){
        ArrayList<Ticket> toBeSold = new ArrayList<>();
        for(Ticket t : msg.tickets){
            // I biglietti del server sono condivisi con tutti i Thread!
            synchronized(father.tickets){
                if(isIn(father.tickets, t)){
                    toBeSold.add(t);
                    father.tickets.remove(t);
                }
            }
        }
        return toBeSold;
    }

    public boolean isIn(ArrayList<Ticket> tickets, Ticket t){
        for(Ticket e : tickets){
            if(t.equals(e)){
                return true;
            }
        }
        return false;
    }
    
}
