package Aeropuertos;

import Log.Log;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class Bus extends Thread{

private String id;
private Aeropuerto aeropuerto;
private int npasajeros;
private Log log;
 
    public Bus(Log log,int id, Aeropuerto aeropuerto){
        this.id = String.format("B-%04d", id);
        this.aeropuerto=aeropuerto;
        this.log=log;
        
    }
    
    public void run(){
        
        //System.out.println("bus "+id+" del aeropuerto "+aeropuerto.getId());
        while (true){
            try {
                log.writeLog("hola");
                npasajeros=aeropuerto.recogerPasajeros(id);
                aeropuerto.llevarPasajeros(npasajeros,id);
                aeropuerto.conducir();
                npasajeros=aeropuerto.recogerPasajeros2(id);
                aeropuerto.llevarPasajeros2(npasajeros,id);
            } catch (InterruptedException ex) {
            }
        }
        
    }
}
