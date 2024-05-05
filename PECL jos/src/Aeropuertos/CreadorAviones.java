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
        int i=0;
        while(true){
            try {
                i++;
                Aeropuerto aeropuertoOrigen;
                Aeropuerto aeropuertoDestino;
                if(i%2==0){
                    aeropuertoOrigen=this.aeropuertomadrid;
                    aeropuertoDestino=this.aeropuertobarcelona;
                }else{
                    aeropuertoOrigen=this.aeropuertobarcelona;
                    aeropuertoDestino=this.aeropuertomadrid;
                }
                pool.execute(new Avion(i,aeropuertoOrigen,aeropuertoDestino));
                int sleep=(new Random().nextInt(3)+1)*1000;
                Thread.sleep(sleep);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(CreadorAviones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
