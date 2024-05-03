package Aeropuertos;

public class Bus extends Thread{

private String id;
private Aeropuerto aeropuerto;
private int npasajeros;
//private Log log;
 
    public Bus(int id, Aeropuerto aeropuerto){
        this.id = String.format("B-%04d", id);
        this.aeropuerto=aeropuerto;      
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
