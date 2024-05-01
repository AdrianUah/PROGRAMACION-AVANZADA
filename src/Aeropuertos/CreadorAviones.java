package Aeropuertos;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.Logg;

public class CreadorAviones extends Thread{
    //private static final org.apache.log4j.Logger LOG = Logg.getLogger(Main.class);
    ExecutorService pool=Executors.newFixedThreadPool(5);
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
                //LOG.info("Se ha creado un nuevo avion : " + i);
                int sleep=(new Random().nextInt(3)+1)*1000;
                Thread.sleep(sleep);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(CreadorAviones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
