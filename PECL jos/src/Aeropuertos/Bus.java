package Aeropuertos;

import Log.Log;

public class Bus extends Thread{

private String id;
private Aeropuerto aeropuerto;
private int npasajeros;
private static final org.apache.log4j.Logger LOG = Log.getLogger(Main.class);
 
    public Bus(int id, Aeropuerto aeropuerto){
        this.id = String.format("B-%04d", id);
        this.aeropuerto=aeropuerto;
        LOG.info("Bus "+this.id+" es creado");
    }
    
    public void run(){
        while (true){
            try {
                npasajeros=aeropuerto.recogerPasajeros(id);
                aeropuerto.conducir();
                aeropuerto.llevarPasajeros(npasajeros,id);
                
                npasajeros=aeropuerto.recogerPasajeros2(id);
                aeropuerto.conducir();
                aeropuerto.llevarPasajeros2(npasajeros,id);
            } catch (InterruptedException ex) {
            }
        }
        
    }
}
