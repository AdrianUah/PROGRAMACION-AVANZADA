package Aeropuertos;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Avion implements Runnable{
    
private String id;
private Aeropuerto aeropuerto,aeropuertoOrigen,aeropuertoDestino;
private int puertaEmbarque, capacidad,npasajeros,npista, nviajes=0;

private int vmax;

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

    public String getId() {
        return id;
    }

    public int getNpasajeros() {
        return npasajeros;
    }
    
    public void run(){
        while(true){
            try {
                aeropuerto.hangar(id,aterrizaje);
                puertaEmbarque=aeropuerto.areaDeEstacionamiento(id,aterrizaje);
                //System.out.println("Avion de despegue "+id+"tiene la puerta "+puertaEmbarque+ "del aeropuerto "+ aeropuerto.getId());
                int i=0;
                int aforo=capacidad;
                aeropuerto.actualizarhmGates(puertaEmbarque, this);
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
                //System.out.println("avion "+id+" aterriza en la puerta de embarque "+puertaEmbarque+" con pasajeros: "+npasajeros+ " ...del aeropuerto "+ aeropuerto.getId()+" hay estos viajeros aqui "+aeropuerto.getNviajeros() );
                npasajeros=aeropuerto.puertasDeEmbarque(npasajeros,aterrizaje);
                aeropuerto.liberarPuertasDeEmbarque(puertaEmbarque);
                //System.out.println("avion "+id+" deja la puerta de embarque "+puertaEmbarque+" y se queda sin pasajeros:: "+npasajeros+ " en el aeropuerto "+ aeropuerto.getId()+" ahora deberian haber mas viajeros aqui mas o menos "+aeropuerto.getNviajeros() );

                aeropuerto.areaDeEstacionamiento(id,aterrizaje);
                
                nviajes++;
                //aeropuerto.setTaller();
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
