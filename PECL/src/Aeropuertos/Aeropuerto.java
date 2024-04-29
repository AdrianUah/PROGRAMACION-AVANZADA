package Aeropuertos;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aeropuerto {
    private int id,nviajeros;
    private Queue<String> colaA =new ConcurrentLinkedQueue<String>();
    private Semaphore em1=new Semaphore(1);
    private Semaphore em2=new Semaphore(1);
    private Semaphore lleno=new Semaphore(0);
    public Aeropuerto(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public int getNviajeros() {
        return nviajeros;
    }
    
    public void conducir(){
        int sleep=(new Random().nextInt(6)+5)*1000;
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public int recogerPasajeros(String id){
        int npasajeros=0;
        
        try {
            em1.acquire();
            int sleep=(new Random().nextInt(4)+2)*1000;
            npasajeros=new Random().nextInt(51);
            System.out.println("P1-"+id+" hay x pasajeros en la estacion "+npasajeros);
            Thread.sleep(sleep);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            em1.release();
        }
        System.out.println("P1-"+id+" han subido x pasajeros en el bus "+npasajeros);
        return npasajeros;
    }
    
    public synchronized void llevarPasajeros(int npasajeros, String id){
        //int sleep=(new Random().nextInt(6)+5)*1000;
        try {
            //Thread.sleep(sleep);       
            em2.acquire();          
            System.out.println("P2-"+id+" han llegado x pasajeros al aero y sin sumar z viajeros "+npasajeros+","+nviajeros);
            nviajeros=nviajeros+npasajeros;
            System.out.println("P2-"+id+" han llegado x pasajeros al aero y sumados son z "+npasajeros+","+nviajeros);          
            
//lleno.release();           
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em2.release();
            notifyAll();
        }
    }
    
    public synchronized int recogerPasajeros2(String id) throws InterruptedException{
        
        while(nviajeros==0){
            System.out.println("P2-"+id+"no hay viajeros que recoger en la estacion");
            wait();
        }
        int sleep,npasajeros=0;
        try{         
            /*do{
                npasajeros=new Random().nextInt(51);           
            }while (npasajeros>getNviajeros());
            */
            sleep=(new Random().nextInt(4)+2)*1000;
            em2.acquire();
            //sleep=(new Random().nextInt(4)+2)*1000;
            npasajeros=new Random().nextInt(51);       
            if (npasajeros>getNviajeros()){
                npasajeros=getNviajeros();
            }
            System.out.println("P2-"+id+" voy a recojer x pasajeros al aero y sin restar z viajeros "+npasajeros+","+nviajeros);
            Thread.sleep(sleep);
            nviajeros=nviajeros-npasajeros;
            System.out.println("P2-"+id+" he recogido x viajeros y ahora hay un total de y en elaeropuerto "+npasajeros+","+nviajeros);
        }catch(InterruptedException ex){         
        }
        finally{
            em2.release();
        }
        return npasajeros;
    }
    
    public void llevarPasajeros2(int npasajeros, String id){
        int sleep=(new Random().nextInt(6)+5)*1000;
        try {
            Thread.sleep(sleep);
            em1.acquire();
            System.out.println("P1-"+id+" han llegado x pasajeros a la estacion"+npasajeros);
            npasajeros=0;
            System.out.println("P1-"+id+" vacia ");
            //lleno.release();           
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            em1.release();
        }
    }
    public void Hangar(){
        
    }
}
