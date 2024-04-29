package Aeropuertos;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Avion implements Runnable{
    
private String id;
private Aeropuerto aeropuerto;
private int npasajeros;

    public Avion(int id, Aeropuerto aeropuerto){
        char letra1 = (char) ('A' + new Random().nextInt(26));
        char letra2 = (char) ('A' + new Random().nextInt(26));
        
        this.id = String.format(letra1+""+letra2+"-%04d", id);
        this.aeropuerto=aeropuerto;
    }
    
    public void run(){
        System.out.println("avion "+id+" del aeropuerto "+aeropuerto.getId());
        while(true){
            
        }
    }
}
