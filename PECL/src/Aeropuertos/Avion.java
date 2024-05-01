package Aeropuertos;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Avion implements Runnable{
    
private String id;
private Aeropuerto aeropuerto,aeropuertoOrigen,aeropuertoDestino;
private int puertaEmbarque, capacidad,npasajeros,npista, nviajes=0;
private boolean aterrizaje=false;

    public Avion(int id, Aeropuerto aeropuertoOrigen, Aeropuerto aeropuertoDestino){
        char letra1 = (char) ('A' + new Random().nextInt(26));
        char letra2 = (char) ('A' + new Random().nextInt(26));
        
        this.id = String.format(letra1+""+letra2+"-%04d", id);
        this.aeropuertoOrigen=aeropuertoOrigen;
        this.aeropuertoDestino=aeropuertoDestino;
        aeropuerto=aeropuertoOrigen;
        capacidad=new Random().nextInt(201)+100;
        
    }
    
    public void run(){
        while(true){
            try {
                aeropuerto.hangar(aterrizaje);
                puertaEmbarque=aeropuerto.areaDeEstacionamiento(aterrizaje);
                
                int i=0;
                int aforo=capacidad;
                do{
                    if(i!=0){
                        Thread.sleep((new Random().nextInt(5)+1)*1000);
                    }
                    npasajeros=+aeropuerto.puertasDeEmbarque(aforo,aterrizaje);
                    aforo=aforo-npasajeros;
                    i++;                                   
                }while(npasajeros<capacidad && i<3);
                aeropuerto.liberarPuertasDeEmbarque(puertaEmbarque);
                
                npista=aeropuerto.areaDeRodaje(aterrizaje);              
                aeropuerto.pista(aterrizaje);
                aeropuerto.liberarPista(npista);
                
                aterrizaje=true;
                aeropuerto=aeropuertoDestino;
                npista=aeropuerto.aerovia(aterrizaje);
                               
                
                
                aeropuerto.pista(aterrizaje);
                aeropuerto.liberarPista(npista);
                
                puertaEmbarque=aeropuerto.areaDeRodaje(aterrizaje);
                aeropuerto.puertasDeEmbarque(npasajeros,aterrizaje);               
                aeropuerto.areaDeEstacionamiento(aterrizaje);
                
                nviajes++;
                aeropuerto.setTaller();
                aeropuerto.taller(nviajes);
                aeropuerto.liberarTaller();
                
                int decision=new Random().nextInt(2);
                if(decision==1){
                    aeropuerto.hangar(aterrizaje);
                }
                aterrizaje=false;
                aeropuertoDestino=aeropuertoOrigen;
                aeropuertoOrigen=aeropuerto;
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Avion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
