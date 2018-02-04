/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author whoami
 */
public class ChatClient {
    private static ServerHandler serverHandler;
    private static ClientHandler client_stub;
    private static boolean run = true;
    public static void main(String [] args) {
        if (args.length < 2){
            System.out.println("Usage: java ChatClient <server ip> <username>");
            System.exit(0);
        }
        String host = args[0];
        
        try {
            ClientHandlerImpl client = new ClientHandlerImpl(args[1]);
            client_stub = (ClientHandler) UnicastRemoteObject.exportObject(client, 0);
            
            
            // Get remote object reference
            Registry registry = LocateRegistry.getRegistry(host);
            serverHandler = (ServerHandler) registry.lookup("ServerHandler");
            
            // Remote method invocation
            if (serverHandler.connect(client_stub) == true){
                System.out.println("Connection to " + host + " succeded.");
            } else {
                System.out.println("Error trying to reach server.");
                System.exit(1);
            }
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Interrupt detected, disconnecting...");
                try {
                    serverHandler.disconnect(client_stub);
                } catch (RemoteException ex) {
                    System.out.println("Error trying to reach server.");
                    System.exit(1);
                }
            }));
            
            // Read-eval loop
            while(run){
                Scanner sc = new Scanner(System.in);
                System.out.print(args[1] + ": ");
                String input = sc.nextLine().trim();
                
                if (input.length() > 0){
                    parseInput(input);
                }
            }
            
            System.out.println("Exiting...");
            serverHandler.disconnect(client_stub);
            System.exit(0);
            
        } catch (Exception e)  {
            System.err.println("Error on client: " + e);
            System.exit(1);
        }
    }
    
    private static void parseInput(String input) {
        if (input.startsWith("/")){
            executeCommand(input);
        }else{
            try {
                serverHandler.sendMessage(client_stub, input);
            } catch (RemoteException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void executeCommand(String input) {
        // Remove leading /
        input = input.substring(1).toLowerCase();
        switch (input){
            case "help":
                printHelp();
                break;
            case "logout":
            case "quit":
                logout();
                break;
            case "history":
                getHistory();
                break;
            case "users":
                getUsers();
                break;
            default:
                System.out.println("Command unknwon.");
                break;
        }
    }
    
    private static void printHelp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static void logout() {
        run = false;
        try {
            serverHandler.disconnect(client_stub);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Disconnected.");
    }
    
    private static void getHistory() {
        ArrayList<String> history;
        try {
            history = serverHandler.getHistory();
            
            for (String line : history){
                System.out.println(line);
            }
            System.out.println("");
        } catch (RemoteException ex) {
            System.out.println("Unable to get history.");
        }
    }
    
    private static void getUsers() {
        ArrayList<String> users;
        try {
            users = serverHandler.getConnectedUsers();
            
            for (String user : users){
                System.out.println(user);
            }
            System.out.println("");
        } catch (RemoteException ex) {
            System.out.println("Unable to get user list.");
        }
    }
}
