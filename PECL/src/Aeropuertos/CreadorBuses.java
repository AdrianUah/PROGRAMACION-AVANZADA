package Aeropuertos;
import Log.Log;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreadorBuses extends Thread{
    private Log log;
    ExecutorService pool=Executors.newFixedThreadPool(10);
    Aeropuerto aeropuertomadrid;
    Aeropuerto aeropuertobarcelona;
    
    public CreadorBuses(Log log,Aeropuerto aeropuertomadrid,Aeropuerto aeropuertobarcelona){
        this.aeropuertomadrid=aeropuertomadrid;
        this.aeropuertobarcelona=aeropuertobarcelona;
        this.log=log;
    }
    
    public void run(){
        int i=0;      
        //for (int i=1;i<=4000;i++){
        while (true){
            try {
                i++;
                Aeropuerto aeropuerto;
                if(i%2==0){
                    aeropuerto=this.aeropuertomadrid;
                }else{
                    aeropuerto=this.aeropuertobarcelona;
                }
                pool.execute(new Bus(log,i,aeropuerto));
                //pool.execute(new Bus(i,aeropuertomadrid));
                int sleep=(new Random().nextInt(5)+5)*100;
                Thread.sleep(sleep);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(CreadorAviones.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
        }
        //pool.shutdown();
    }
}
