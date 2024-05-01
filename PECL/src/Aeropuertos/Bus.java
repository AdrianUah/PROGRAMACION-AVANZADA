package Aeropuertos;

import log.Logg;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class Bus extends Thread{
private static final org.apache.log4j.Logger LOG = Logg.getLogger(Main.class);
private String id;
private Aeropuerto aeropuerto;
private int npasajeros;
//private Log log;
 
    public Bus(int id, Aeropuerto aeropuerto){
        this.id = String.format("B-%04d", id);
        this.aeropuerto=aeropuerto;
        //this.log=log;
        
    }
    
    public String getid()
    {
        return id;
    }
    
    public void run(){
        LOG.info("Se ha creado un nuevo Bus : " +getid());
        while (true){
            try {
                npasajeros=aeropuerto.recogerPasajeros(id);
                aeropuerto.llevarPasajeros(npasajeros,id);
                aeropuerto.conducir(id);
                npasajeros=aeropuerto.recogerPasajeros2(id);
                aeropuerto.llevarPasajeros2(npasajeros,id);
            } catch (InterruptedException ex) {
            }
        }
        
    }
}
