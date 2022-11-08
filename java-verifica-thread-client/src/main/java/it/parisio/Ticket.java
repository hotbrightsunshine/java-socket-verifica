package it.parisio;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket {

    private static int _id = 0;
    private static String[] strings = {"palco", "parterre", "tribuna", "prato"};

    private String numero;
    private int id;

    /*
     * Il programmatore utilizza questo metodo per 
     * creare un oggetto, in modo da crearne uno con 
     * ID sempre diverso.
     */
    public static Ticket create(String numero){
        _id++;
        return new Ticket(_id, numero);
    }

    // Genera un Biglietto Random
    public static Ticket generate_r(){
        Random rnd = new Random(System.currentTimeMillis());

        String str = strings[rnd.nextInt(4)];
        int number = rnd.nextInt(50);
        return create(str + "-" + number);
    }

    /* 
     * Se avessi utilizzato un costruttore con solo il numero
     * incrementando l'ID ogni volta che creo l'oggetto,
     * avevo paura che quando l'ObjectMapper avvesse creato un nuovo
     * oggetto dalla stringa ne creasse uno con ID diverso. 
     * 
     * Ho fatto un costruttore privato per inserire manualmente id e numero
     */
    private Ticket(
        @JsonProperty("id") int id,
        @JsonProperty("number") String numero){
        this.id = id;
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ticket " + id + ", numero " + numero;
    }
    
}
