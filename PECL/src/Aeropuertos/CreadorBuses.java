package Aeropuertos;
import log.Logg;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreadorBuses extends Thread{
    ExecutorService pool=Executors.newFixedThreadPool(10);
    Aeropuerto aeropuertomadrid;
    Aeropuerto aeropuertobarcelona;
    
    public CreadorBuses(Aeropuerto aeropuertomadrid,Aeropuerto aeropuertobarcelona){
        this.aeropuertomadrid=aeropuertomadrid;
        this.aeropuertobarcelona=aeropuertobarcelona;
        //this.log=log;
    }
    
    public void run(){
        int i=0;      
        while (true){
            try {
                i++;
                Aeropuerto aeropuerto;
                if(i%2==0){
                    aeropuerto=this.aeropuertomadrid;
                }else{
                    aeropuerto=this.aeropuertobarcelona;
                }
                pool.execute(new Bus(i,aeropuerto));
                int sleep=(new Random().nextInt(5)+5)*100;
                Thread.sleep(sleep);
            
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
        }
    }
}
