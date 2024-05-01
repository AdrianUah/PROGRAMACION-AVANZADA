package Aeropuertos;
import log.Logg;
import static java.lang.Math.log;
import java.util.logging.Logger;
import jdk.jpackage.internal.Log;

public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        Aeropuerto aeropuertomadrid=new Aeropuerto(1);
        Aeropuerto aeropuertobarcelona=new Aeropuerto(2);
        
        CreadorBuses cb = new CreadorBuses(aeropuertomadrid,aeropuertobarcelona);
        CreadorAviones ca=new CreadorAviones(aeropuertomadrid,aeropuertobarcelona);
        
        cb.start();
        ca.start();
    }
}
