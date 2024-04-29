package Aeropuertos;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreadorAviones extends Thread{
    ExecutorService pool=Executors.newFixedThreadPool(8000);
    Aeropuerto aeropuertomadrid;
    Aeropuerto aeropuertobarcelona;
    
    public CreadorAviones(Aeropuerto aeropuertomadrid,Aeropuerto aeropuertobarcelona){
        this.aeropuertomadrid=aeropuertomadrid;
        this.aeropuertobarcelona=aeropuertobarcelona;
    }
    
    public void run(){
              
        for (int i=1;i<=8000;i++){         
            try {
                Aeropuerto aeropuerto;
                if(i%2==0){
                    aeropuerto=this.aeropuertomadrid;
                }else{
                    aeropuerto=this.aeropuertobarcelona;
                }
                pool.execute(new Avion(i,aeropuerto));
                int sleep=(new Random().nextInt(3)+1)*1000;
                Thread.sleep(sleep);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(CreadorAviones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        pool.shutdown();
    }
}
