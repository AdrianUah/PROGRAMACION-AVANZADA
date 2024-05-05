package Aeropuertos;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import Log.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aeropuerto {
    private static final org.apache.log4j.Logger LOG = Log.getLogger(Main.class);
    private static final String SERVER_IP = "127.0.0.1"; // Dirección IP del servidor
    private static final int SERVER_PORT = 8081; // Puerto del servidor
    private static Socket socket;
    private int id,nviajeros,nviajerosvuelta;
    private int npistaslibres=4;
    private Semaphore em1=new Semaphore(1);
    private Semaphore lleno=new Semaphore(0);
    private Semaphore lleno2=new Semaphore(0);
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
    
    private StringBuilder sb = new StringBuilder();
    
    

    public Aeropuerto(int id){
        this.id=id;
        establecerConexion();
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

    public ConcurrentHashMap<String, Boolean> getHmHangar() {
        return hmHangar;
    }

    public ConcurrentHashMap<String, Integer> getHmTransfersA() {
        return hmTransfersA;
    }

    public ConcurrentHashMap<String, Integer> getHmTransfersC() {
        return hmTransfersC;
    }

    public ConcurrentHashMap<String, Boolean> getHmTaller() {
        return hmTaller;
    }

    public ConcurrentHashMap<String, Boolean> getHmAreaEstacionamiento() {
        return hmAreaEstacionamiento;
    }

    public ConcurrentHashMap<Integer, Avion> getHmGates() {
        return hmGates;
    }

    public ConcurrentHashMap<String, Integer> getHmAreaRodaje() {
        return hmAreaRodaje;
    }

    public ConcurrentHashMap<Integer, Avion> getHmPistas() {
        return hmPistas;
    }

    public ConcurrentHashMap<String, Integer> getHmAerovia() {
        return hmAerovia;
    }
    
    public int getIdAeropuerto()
    {
        return id;
    }
    
    private void establecerConexion() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void enviarDatosPorSocket(String data) {
        if (socket == null || socket.isClosed()) {
            System.out.println("No hay una conexión establecida con el servidor.");
            return;
        }

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(data); // Envía los datos al servidor
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    
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
            nviajeros=nviajeros+npasajeros;
            LOG.info("Bus "+id+" deja "+npasajeros+" en el aeropuerto");
            if (getIdAeropuerto() == 1)
                enviarDatosPorSocket("PasajerosB:"+nviajeros);//pon esta parte en el metodo se encarga de mandar los datos
            else
                enviarDatosPorSocket("PasajerosM:"+nviajeros);
            hmTransfersA.remove(id);
            if(nviajeros>0){
                lleno.release();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            em2.release();
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
            if (getIdAeropuerto() == 1)//copiar esto
                enviarDatosPorSocket("PasajerosB:-"+npasajeros);
            else
                enviarDatosPorSocket("PasajerosM:-"+npasajeros);
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
            LOG.info("Avion "+id+ " esta en el hangar");
            hmHangar.put(id,true);
            if (getIdAeropuerto() == 1)//copiar esto
                enviarDatosPorSocket("HangarB:"+displayIDs(hmHangar));
            else
                enviarDatosPorSocket("HangarM:"+displayIDs(hmHangar));
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
            LOG.info("Avion "+id+ " esta en el area de estacionamiento");
            hmAreaEstacionamiento.put(id, true);
            if (getIdAeropuerto() == 1)//cpiar esto
                enviarDatosPorSocket("EstacionamientoB:"+displayIDs(hmAreaEstacionamiento));
            else
                enviarDatosPorSocket("EstacionamientoM:"+displayIDs(hmAreaEstacionamiento));
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
            LOG.info("Avion "+id+ " esta en el area de rodaje");
            hmAreaRodaje.put(id, npasajeros);
            if (getIdAeropuerto() == 1)//copiar esto
                enviarDatosPorSocket("RodajeB:"+displayIDs(hmAreaRodaje));
            else
                enviarDatosPorSocket("RodajeM:"+displayIDs(hmAreaRodaje));
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
                LOG.info("Avion "+avion.getid()+" ("+avion.getNpasajeros()+" pasajeros)"+ " accede a pista "+npista+" para despegue");
                Thread.sleep((new Random().nextInt(3)+1)*1000);
            }else{              
                LOG.info("Avion "+avion.getid()+" ("+avion.getNpasajeros()+" pasajeros)"+ " accede a pista "+npista+" para atterizaje");
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
            if (getIdAeropuerto() == 1){//copiar esto
                LOG.info("Avion "+id+" ("+npasajeros+" pasajeros)"+ " accede a aerovia Madrid - Barcelona");
                enviarDatosPorSocket("AeroviaB_M:"+displayIds(hmAerovia));
            }else{
                LOG.info("Avion "+id+" ("+npasajeros+" pasajeros)"+ " accede a aerovia Barcelona - Madrid");
                enviarDatosPorSocket("AeroviaM_B:"+displayIds(hmAerovia));
            }
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
            LOG.info("Avion "+id+ " esta en el taller");
            hmTaller.put(id,true);
            if (getIdAeropuerto() == 1)//copiar esto
                enviarDatosPorSocket("TallerB:"+displayIDs(hmTaller));
            else
                enviarDatosPorSocket("TallerM:"+displayIDs(hmTaller));
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
    public int displayIDs(ConcurrentHashMap hm) {
        System.out.println("IDs presentes en el ConcurrentHashMap:");
        int contador = 0;
        for (Object id : hm.keySet()) {
            contador++;
            System.out.println(id);
        }
        return contador;
    }
    public String displayIds(ConcurrentHashMap hm) {
    StringBuilder concatenatedIDs = new StringBuilder();
    for (Object id : hm.keySet()) {
        concatenatedIDs.append(id).append(",");
    }
    // Eliminamos la última coma si hay elementos en el mapa
    if (!hm.isEmpty()) {
        concatenatedIDs.deleteCharAt(concatenatedIDs.length() - 1);
    }
    return concatenatedIDs.toString();
}
    public void mostrarCampos() {
        System.out.println("\nCampos del GATE:"+id);
        for (Map.Entry<Integer, Avion> entry : hmGates.entrySet()) {
            Integer clave = entry.getKey();
            Avion avion = entry.getValue();
            System.out.println("gate: " + clave + ", id: " + avion.getid()+ ", Personas: " + avion.getNpasajeros());
        }
    }
    
    public String textTransfers(ConcurrentHashMap hm){
        for (Object clave : hm.keySet()) {
            Integer valor = (Integer) hm.get(clave);
            sb.append(clave).append("(").append(valor).append(") ");
        }
        String contenido = sb.toString();
        sb.setLength(0);
        return contenido;
    }

    public String textHangar(ConcurrentHashMap hm) {
        for (Object clave : hm.keySet()) {           
            sb.append(clave).append(" ");
        }
        String contenido = sb.toString();
        sb.setLength(0);
        return contenido;
    }

    public String textGate(int i) {
        Avion avion=hmGates.get(i);
        String contenido="";
        if(avion!=null){
            if(avion.isAterrizaje()){
                sb.append("Desembarque: ");
            }else{
                sb.append("Embarque: ");
            }
                sb.append(avion.getid());
                contenido = sb.toString();
                sb.setLength(0);
        }       
        return contenido;
    }

    public String textPista(int i) {
        Avion avion=hmPistas.get(i);
        String contenido="";
        if(avion!=null){
            if(avion.isAterrizaje()){
                sb.append("Aterrizaje: ");
            }else{
                sb.append("Despegue: ");
            }
                sb.append(avion.getid()).append("(").append(avion.getNpasajeros()).append(")");
                contenido = sb.toString();
                sb.setLength(0);
        }       
        return contenido;
    }
}
