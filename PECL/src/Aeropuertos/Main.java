package Aeropuertos;

public class Main {
    
    
    public static void main(String[] args) throws InterruptedException {
        
        Aeropuerto aeropuertomadrid=new Aeropuerto(1);
        Aeropuerto aeropuertobarcelona=new Aeropuerto(2);
        
        CreadorBuses cb = new CreadorBuses(aeropuertomadrid,aeropuertobarcelona);
        CreadorAviones ca=new CreadorAviones(aeropuertomadrid,aeropuertobarcelona);
        cb.start();
        ca.start();
    }
}
