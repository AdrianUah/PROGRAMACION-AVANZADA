/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextField;

/**
 *
 * @author Joseph
 */
public class ServerSocketExample {
    public static void main(String []args)
    {
        InterfazGrafica interfaz = crearInterfazGrafica();
        iniciarServidorSocket(interfaz);
        
    }
    private static InterfazGrafica crearInterfazGrafica() {
        // Crear e inicializar la interfaz gráfica aquí
        Map<String, JTextField> textFieldsMap = new HashMap<>();
        // Agregar los JTextField al mapa
        // Por ejemplo:
        textFieldsMap.put("HangarM", new JTextField());
        textFieldsMap.put("PasajerosM", new JTextField());
        textFieldsMap.put("RodajeM", new JTextField());
        textFieldsMap.put("TallerM", new JTextField());
        textFieldsMap.put("EstacionamientoM", new JTextField());
        textFieldsMap.put("HangarB", new JTextField());
        textFieldsMap.put("PasajerosB", new JTextField());
        textFieldsMap.put("RodajeB", new JTextField());
        textFieldsMap.put("TallerB", new JTextField());
        textFieldsMap.put("EstacionamientoB", new JTextField());
        textFieldsMap.put("AeroviaB_M", new JTextField());
        textFieldsMap.put("AeroviaM_B", new JTextField());
        // Crear la instancia de InterfazGrafica con el mapa de textFields
        return new InterfazGrafica(textFieldsMap);
    }
    
     private static void iniciarServidorSocket(InterfazGrafica interfaz) {
        try (ServerSocket serverSocket = new ServerSocket(8081)) {
            System.out.println("Server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);
                // Crear una instancia de ClientHandler pasándole el socket y la interfaz gráfica
                new ClientHandler(socket, interfaz).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
