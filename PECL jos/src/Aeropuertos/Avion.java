package Aeropuertos;

import Log.Log;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Avion extends Thread{
private static final org.apache.log4j.Logger LOG = Log.getLogger(Main.class);    
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
        LOG.info("Avión "+this.id+" es creado");
        
    }

    public String getid() {
        return id;
    }

    public boolean isAterrizaje() {
        return aterrizaje;
    }
    

    public int getNpasajeros() {
        return npasajeros;
    }
    
    public void run(){
        while(true){
            try {
                aeropuerto.hangar(id,aterrizaje);
                puertaEmbarque=aeropuerto.areaDeEstacionamiento(id,aterrizaje);
                int i=0;
                int aforo=capacidad;
                aeropuerto.actualizarhmGates(puertaEmbarque, this);
                LOG.info("Avion "+id+ " accede a puerta de Embarque "+puertaEmbarque+" para embarcar pasajeros");
                do{
                    
                    if(i!=0){
                        Thread.sleep((new Random().nextInt(5)+1)*1000);
                    }
                    
                    int nuevosPasajeros=aeropuerto.puertasDeEmbarque(aforo,aterrizaje);                    
                    npasajeros=npasajeros+nuevosPasajeros;
                    aforo=aforo-nuevosPasajeros;
                    i++;
                    aeropuerto.actualizarhmGates(puertaEmbarque, this);                  
                }while(aforo!=0 && i<3);
                aeropuerto.liberarPuertasDeEmbarque(puertaEmbarque);
                
                npista=aeropuerto.areaDeRodaje(id,npasajeros,aterrizaje);              
                aeropuerto.pista(npista,this,aterrizaje);
                aeropuerto.liberarPista(npista);
                
                aterrizaje=true;
                aeropuerto=aeropuertoDestino;
                npista=aeropuerto.aerovia(id,npasajeros,aterrizaje);
                               
                
                
                aeropuerto.pista(npista,this,aterrizaje);
                aeropuerto.liberarPista(npista);
                
                puertaEmbarque=aeropuerto.areaDeRodaje(id,npasajeros,aterrizaje);
                LOG.info("Avion "+id+ " accede a puerta de Embarque "+puertaEmbarque+" para desembarcar "+npasajeros+" pasajeros");

                npasajeros=aeropuerto.puertasDeEmbarque(npasajeros,aterrizaje);
                aeropuerto.liberarPuertasDeEmbarque(puertaEmbarque);

                aeropuerto.areaDeEstacionamiento(id,aterrizaje);
                
                nviajes++;
                aeropuerto.taller(id,nviajes);
                aeropuerto.liberarTaller(id);
                
                int decision=new Random().nextInt(2);
                if(decision==1){
                    aeropuerto.hangar(id,aterrizaje);
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
