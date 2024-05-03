package Aeropuertos;

import Interfaz.Pantalla;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    
    
    public static void main(String[] args) throws InterruptedException {
        
        
        
        AtomicBoolean pausa=new AtomicBoolean(false);
        
        Aeropuerto aeropuertomadrid=new Aeropuerto(2);
        Aeropuerto aeropuertobarcelona=new Aeropuerto(1);
        CreadorBuses cb = new CreadorBuses(aeropuertomadrid,aeropuertobarcelona);
        CreadorAviones ca=new CreadorAviones(aeropuertomadrid,aeropuertobarcelona);
        Pantalla pantalla=new Pantalla(aeropuertomadrid,aeropuertobarcelona);
        Thread threadPantalla= new Thread(pantalla);
        threadPantalla.start();
        cb.start();
        ca.start();
    }
}
