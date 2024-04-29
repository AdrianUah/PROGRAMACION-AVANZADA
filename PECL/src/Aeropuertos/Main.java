package Aeropuertos;
import Log.Log;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
public class Main {
    
    
    public static void main(String[] args) throws InterruptedException {
        Log log=new Log();
        
        log.writeLog("hola 2");
        System.out.println("Aeropuertos.Main.main()");
        Aeropuerto aeropuertomadrid=new Aeropuerto(1);
        Aeropuerto aeropuertobarcelona=new Aeropuerto(2);
        
        CreadorBuses cb = new CreadorBuses(log,aeropuertomadrid,aeropuertobarcelona);
        //CreadorAviones ca=new CreadorAviones(aeropuertomadrid,aeropuertobarcelona);
        cb.start();
        //ca.start();
    }
}
