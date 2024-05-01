package Aeropuertos;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aeropuerto {
    private int id,nviajeros;
    private int npistaslibres=4;
    private Semaphore em1=new Semaphore(1);
    private Semaphore em2=new Semaphore(1);
    private Boolean[] puertasEmbarque= new Boolean[6];
    private Boolean[] pistas= new Boolean[4];
    private Semaphore em3=new Semaphore(6,true);
    private Semaphore em4=new Semaphore(1);
    private Semaphore em5=new Semaphore(1,true);
    private Semaphore em6=new Semaphore(20,true);

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
        try {
            Thread.sleep((new Random().nextInt(6)+5)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public int recogerPasajeros(String id){
        int npasajeros=0;
        
        try {
            em1.acquire();
            npasajeros=new Random().nextInt(51);
            //System.out.println("P1-"+id+" hay x pasajeros en la estacion "+npasajeros);
            Thread.sleep((new Random().nextInt(4)+2)*1000);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            em1.release();
        }
        return npasajeros;
    }
    
    public synchronized void llevarPasajeros(int npasajeros, String id){
        try {
            em2.acquire();          
            nviajeros=nviajeros+npasajeros;          
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em2.release();
            notifyAll();
        }
    }
    
    public synchronized int recogerPasajeros2(String id) throws InterruptedException{
        
        while(nviajeros==0){
            wait();
        }
        int npasajeros=0;
        try{                  
            em2.acquire();
            npasajeros=new Random().nextInt(51);       
            if (npasajeros>getNviajeros()){
                npasajeros=getNviajeros();
            }
            Thread.sleep((new Random().nextInt(4)+2)*1000);
            nviajeros=nviajeros-npasajeros;
        }catch(InterruptedException ex){         
        }
        finally{
            em2.release();
        }
        return npasajeros;
    }
    
    public void llevarPasajeros2(int npasajeros, String id){       
        try {
            Thread.sleep((new Random().nextInt(6)+5)*1000);
            em1.acquire();
            npasajeros=0;     
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            em1.release();
        }
    }
    
    
    public void hangar(boolean aterrizaje){
        try {
            if(aterrizaje){
                Thread.sleep((new Random().nextInt(16)+15)*1000);
            }
            //System.out.println("AVION "+id+" esta en el hangar");
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int areaDeEstacionamiento(boolean aterrizaje){
        int operacion=-1;
        try {
            if(aterrizaje){          
                Thread.sleep((new Random().nextInt(5)+1)*1000);       
            }else{
                //operacion=setPuertaDeEmbarque(aterrizaje);
                operacion=setpuertasDeEmbarque2222(aterrizaje);
            }
            
        }catch (InterruptedException ex) {
        Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    return operacion;
    }
    
    public int setPuertaDeEmbarque(boolean aterrizaje){
        
        return 0;
        
    }
    
    public int setpuertasDeEmbarque2222(boolean aterrizaje) throws InterruptedException{
        
        em3.acquire();
        em4.acquire();
        
        int puerta;
        //System.out.println("avion"+id+" - "+puertasEmbarque[0]+puertasEmbarque[1]+puertasEmbarque[2]+puertasEmbarque[3]+puertasEmbarque[4]+puertasEmbarque[5]);
        /*if(op==0 && (puertasEmbarque[0]==null || puertasEmbarque[0]==false)){
           puerta=0;
           puertasEmbarque[0]=true;
           
        }else if(op==1 && (puertasEmbarque[1]==null || puertasEmbarque[1]==false)){
            puerta=1;
            puertasEmbarque[1]=true;
        }else{
        */    
            int i=0;
            
            //int i=2;
            while(puertasEmbarque[i]!=null && puertasEmbarque[i]){                
                i++;
                if(i>puertasEmbarque.length){
                    //falta terminar, no consigo que una puerta sea de embarque y otra de desembarque
                }
            }
            
            puerta=i;
            puertasEmbarque[i]=true;       
        em4.release();
        return puerta;       
    }
    
    public int puertasDeEmbarque(int aforo,boolean aterrizaje){
        int npasajeros=0;
        try {
        if(aterrizaje){
            Thread.sleep((new Random().nextInt(5)+1)*1000);
            em2.acquire();
            nviajeros=nviajeros+aforo;          
        }else{
            Thread.sleep((new Random().nextInt(3)+1)*1000);
            em2.acquire();          
            if(nviajeros<=aforo){
                npasajeros=nviajeros;
                nviajeros=nviajeros-npasajeros;
            }else{
                npasajeros=aforo;
                nviajeros=nviajeros-aforo;
            }           
        }           
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em2.release();
        }
        return npasajeros;    
    }
    
    public void liberarPuertasDeEmbarque(int puerta){
        try {
            em4.acquire();
            puertasEmbarque[puerta]=false;
            em3.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em4.release();
        }
    }
    
    public int areaDeRodaje(boolean aterrizaje){
        int operacion=-1;
        try {
            if(aterrizaje){
               operacion=setPuertaDeEmbarque(aterrizaje);
               Thread.sleep((new Random().nextInt(3)+3)*1000);
            }else{
                Thread.sleep((new Random().nextInt(5)+1)*1000);
                operacion=setPista(aterrizaje);        
            }           
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operacion;
    }
    
    public synchronized int setPista(boolean aterrizaje){
        int i=-1;     
        try {                 
            while(npistaslibres==0){
                if(aterrizaje){
                   Thread.sleep((new Random().nextInt(5)+1)*1000);
                }else wait();                
            }
            
            npistaslibres=npistaslibres-1;
            i=0;
            while(pistas[i]!=null && pistas[i]){
                i++;
            }
            pistas[i]=true;
                           
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
    
    public void pista(boolean aterrizaje){
        try {
            if (!aterrizaje){
                Thread.sleep((new Random().nextInt(3)+1)*1000);
            }
            Thread.sleep((new Random().nextInt(5)+1)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    public synchronized void liberarPista(int npista){
        pistas[npista]=false;
        npistaslibres++;
        notifyAll();
    }
    
    public int aerovia(boolean aterrizaje){
        int npista=-1;
        try {
            Thread.sleep((new Random().nextInt(16)+15)*1000);
            npista=setPista(aterrizaje);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return npista;
    }
    
    public void setTaller(){
        try {
            em6.acquire();
            em5.acquire();
            Thread.sleep(1000);          
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em5.release();
        }
    }
    
    public void taller(int nviajes){     
        try {
            if(nviajes%15==0){
                Thread.sleep((new Random().nextInt(6)+5)*1000);
            }else{
                Thread.sleep((new Random().nextInt(5)+1)*1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    public void liberarTaller(){       
        try {
            em5.acquire();
            Thread.sleep(1000);          
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em5.release();
            em6.release();           
        }
    }
}
