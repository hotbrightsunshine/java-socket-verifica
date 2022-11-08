package it.parisio;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {
    ArrayList<Ticket> tickets;

    // Costruttore
    public Message(
        @JsonProperty("tickets") 
        ArrayList<Ticket> tickets){
        this.tickets = tickets;
    }

    public static Message fromJSON(String str) throws JsonMappingException, JsonProcessingException{
        return new ObjectMapper().readValue(str, Message.class);
    }

    public static String toJSON(Message m) throws JsonProcessingException{
        return new ObjectMapper().writeValueAsString(m);
    }

    public static Message empty(){
        return new Message(new ArrayList<Ticket>());
    }

    // Non posso avere un attributo "empty" perché potrebbe 
    // essere inconsistente con il messaggio.
    public static boolean isEmpty(Message msg){
        return msg.tickets.isEmpty();
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Message [tickets=" + tickets + "]";
    }
    
}
