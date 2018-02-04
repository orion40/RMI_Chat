/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author whoami
 */
public class ChatServer {
    static private ArrayList<ClientHandler> clientList;
    static private ArrayList<String> messageList;
    
    public static void main(String [] args) {
        boolean isRunning = true;
        
        clientList = new ArrayList<>();
        messageList = new ArrayList<>();
        
        try {
            // Create log file
            FileWriter serverLogs = new FileWriter("chat.log", true);
            serverLogs.write("[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + "Server started.\n");
            
            // Create interrupt handler for closing the server with ^C
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Interrupt detected, kicking users...");
                try {
                    serverLogs.write("[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + "Server shutdown.\n");
                    serverLogs.close();
                    // TODO
                } catch (IOException ex) {
                    System.out.println("Error closing log file.");
                }
            }));
            
            // Create a Hello remote object
            ServerHandlerImpl serverHandler = new ServerHandlerImpl(clientList, messageList, serverLogs);
            ServerHandler serverHandlerStub = (ServerHandler) UnicastRemoteObject.exportObject(serverHandler, 0);
            
            // Register the remote object in RMI registry with a given identifier
            Registry registry= LocateRegistry.getRegistry();
            registry.bind("ServerHandler", serverHandlerStub);
            System.out.println ("Server ready");
            
            /*
            while(isRunning){
            
            }
            */
            
        } catch (FileNotFoundException ex) {
            System.out.println("Unabled to open log file!");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error on server :" + e) ;
            e.printStackTrace();
        }
    }
}
