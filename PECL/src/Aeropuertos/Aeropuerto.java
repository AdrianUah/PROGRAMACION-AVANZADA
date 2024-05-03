package Aeropuertos;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import Log.Log;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aeropuerto {
    private static final org.apache.log4j.Logger LOG = Log.getLogger(Main.class);
    private int id,nviajeros,nviajerosvuelta;
    private int npistaslibres=4;
    private Semaphore em1=new Semaphore(1);
    private Semaphore lleno=new Semaphore(0);
    private Semaphore vacio=new Semaphore(1);
    private Semaphore lleno2=new Semaphore(0);
    private Semaphore vacio2=new Semaphore(1);
    private Semaphore em2=new Semaphore(1);
    private Semaphore em22=new Semaphore(1);
    private Boolean[] puertasEmbarque= new Boolean[6];
    private Boolean[] pistas= new Boolean[4];
    private Semaphore em3=new Semaphore(6,true);
    private Semaphore em4=new Semaphore(1);
    private Semaphore em5=new Semaphore(1,true);
    private Semaphore em6=new Semaphore(20,true);
    
    private ConcurrentHashMap<String,Boolean> hmHangar= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Integer> hmTransfersA= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Integer> hmTransfersC= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Boolean> hmTaller= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Boolean> hmAreaEstacionamiento= new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer,Avion> hmGates= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Integer> hmAreaRodaje= new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer,Avion> hmPistas= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Integer> hmAerovia= new ConcurrentHashMap<>();
    //LOG.info("Se ha creado un nuevo Bus : " +id);

    public Aeropuerto(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public int getNviajerosvuelta() {
        return nviajerosvuelta;
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
            hmTransfersA.put(id, npasajeros);
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
    
    public void llevarPasajeros(int npasajeros, String id){
        try {
            em2.acquire();
            System.out.println("Aeropuertos.Aeropuerto.llevarPasajeros()");
            nviajeros=nviajeros+npasajeros;
            hmTransfersA.remove(id);
            if(nviajeros>0){
                lleno.release();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em2.release();
            System.out.println("hay "+nviajeros);
        }
    }
    
    public int recogerPasajeros2(String id) throws InterruptedException{
        
        
        int npasajeros=0;
        try{        
            
            lleno2.acquire();
            em22.acquire();
            npasajeros=new Random().nextInt(51);
            hmTransfersC.put(id, npasajeros);
            Thread.sleep((new Random().nextInt(4)+2)*1000);
            if (npasajeros>getNviajerosvuelta()){
                npasajeros=getNviajerosvuelta();
            }else{
                lleno2.release();
            }          
            nviajerosvuelta=nviajerosvuelta-npasajeros;
        }catch(InterruptedException ex){
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            em22.release();
        }
        return npasajeros;
    }
    
    public void llevarPasajeros2(int npasajeros, String id){
            npasajeros=0;
            hmTransfersC.remove(id);
    }
    
    
    public void hangar(String id,boolean aterrizaje){
        try {
            hmHangar.put(id,true);
            
            if(aterrizaje){
                Thread.sleep((new Random().nextInt(16)+15)*1000);
            }
            hmHangar.remove(id);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int areaDeEstacionamiento(String id,boolean aterrizaje){
        int operacion=-1;
        try {
            hmAreaEstacionamiento.put(id, true);
            if(aterrizaje){          
                Thread.sleep((new Random().nextInt(5)+1)*1000);
                setTaller();
            }else{
                //operacion=setPuertaDeEmbarque(aterrizaje);
                operacion=setpuertasDeEmbarque2222(aterrizaje);
            }
            hmAreaEstacionamiento.remove(id);
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
    public void actualizarhmGates(int npuerta,Avion avion ){
        hmGates.put(npuerta, avion);
        mostrarCampos();      
    }
    
    public int puertasDeEmbarque(int aforo,boolean aterrizaje){
        int npasajeros=0;
        try {
                     
            if(aterrizaje){
                Thread.sleep((new Random().nextInt(5)+1)*1000);
                em22.acquire();
                nviajerosvuelta=nviajerosvuelta+aforo;
                lleno2.release();
                em22.release();
                
            }else{
                Thread.sleep((new Random().nextInt(3)+1)*1000);
                
                lleno.acquire();
                em2.acquire();
                
                
                System.out.println("\n"+nviajeros+" quieren entrar en "+aforo+ " del avion ");
                if(nviajeros<=aforo){
                    npasajeros=nviajeros;
                    nviajeros=0;
                    
                }else{
                    npasajeros=aforo;
                    nviajeros=nviajeros-aforo;
                    lleno.release();
                }
                em2.release();
            }           
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("al final entran "+npasajeros+ " en el avion "+"\n");
        return npasajeros;    
    }
    
    public void liberarPuertasDeEmbarque(int puerta){
        try {
            em4.acquire();
            puertasEmbarque[puerta]=false;
            
            hmGates.remove(puerta);
            //mostrarCampos();
            
            em3.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em4.release();
        }
    }
    
    public int areaDeRodaje(String id,int npasajeros,boolean aterrizaje){
        
        int operacion=-1;
        try {
            hmAreaRodaje.put(id, npasajeros);
            if(aterrizaje){
               operacion=setPuertaDeEmbarque(aterrizaje);
               Thread.sleep((new Random().nextInt(3)+3)*1000);
            }else{
                Thread.sleep((new Random().nextInt(5)+1)*1000);
                operacion=setPista(aterrizaje);        
            }
            hmAreaRodaje.remove(id);
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
    
    public void pista(int npista, Avion avion,boolean aterrizaje){
        try {
            hmPistas.put(npista, avion);
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
        hmPistas.remove(npista);
        notifyAll();
    }
    
    public int aerovia(String id, int npasajeros,boolean aterrizaje){
        int npista=-1;
        try {
            hmAerovia.put(id, npasajeros);
            Thread.sleep((new Random().nextInt(16)+15)*1000);
            npista=setPista(aterrizaje);
            hmAerovia.remove(id);
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
    
    public void taller(String id,int nviajes){     
        try {
            hmTaller.put(id,true);
            if(nviajes%15==0){
                Thread.sleep((new Random().nextInt(6)+5)*1000);
            }else{
                Thread.sleep((new Random().nextInt(5)+1)*1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    public void liberarTaller(String id){       
        try {
            em5.acquire();
            Thread.sleep(1000);
            hmTaller.remove(id);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em5.release();
            em6.release();           
        }
    }
    
    /*public void addID(ConcurrentHashMap hm,String id) {
        hm.putIfAbsent(id, new AtomicBoolean(true));
    }
    public void removeID(ConcurrentHashMap hm,String id) {
        hm.remove(id);        
    }*/
    public void displayIDs(ConcurrentHashMap hm) {
        System.out.println("IDs presentes en el ConcurrentHashMap:");
        for (Object id : hm.keySet()) {
            System.out.println(id);
        }
    }
    public void mostrarCampos() {
        System.out.println("\nCampos del GATE:"+id);
        for (Map.Entry<Integer, Avion> entry : hmGates.entrySet()) {
            Integer clave = entry.getKey();
            Avion avion = entry.getValue();
            System.out.println("gate: " + clave + ", id: " + avion.getId()+ ", Personas: " + avion.getNpasajeros());
        }
    }
}
