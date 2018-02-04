/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/


import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHandlerImpl implements ServerHandler {
    
    static private ArrayList<ClientHandler> clientList;
    static private ArrayList<String> messageList;
    static private FileWriter logFile;
    
    public ServerHandlerImpl(ArrayList<ClientHandler> clients, ArrayList<String> messages, FileWriter file) {
        clientList = clients;
        messageList = messages;
        logFile = file;
    }
    
    @Override
    public boolean connect(ClientHandler client) throws RemoteException {
        if (!usernameIsTaken(client.getUsername())){
            String formatted_message = "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + client.getUsername() + " has joined.";
            System.out.println(formatted_message);
            messageList.add(formatted_message);
            writeToLogs(formatted_message);
            clientList.add(client);
            // TODO: limit client maximum ? Check if client is banned ?
            return true;
        } else {
            System.out.println("Username is already taken.");
            return false;
        }
    }
    
    @Override
    public boolean disconnect(ClientHandler client) throws RemoteException {
        String formatted_message = "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + client.getUsername() + " has left.";
        //System.out.println(formatted_message);
        messageList.add(formatted_message);
        writeToLogs(formatted_message);
        clientList.remove(client);
        return true;
    }
    
    @Override
    public boolean sendMessage(ClientHandler client, String message) throws RemoteException {
        String formatted_message = "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + client.getUsername() + ": " + message;
        messageList.add(formatted_message);
        writeToLogs(formatted_message);
        System.out.println(formatted_message);
        for (ClientHandler currentClient : clientList) {
            if (!currentClient.equals(client))
                currentClient.printMessage(formatted_message);
        }
        return true;
    }
    
    private boolean usernameIsTaken(String username) {
        for (ClientHandler currentClient : clientList){
            try {
                if (currentClient.getUsername().equals(username))
                    return true;
            } catch (RemoteException ex) {
                System.out.println("Error joining client (" + ex.getMessage() + "), ejecting him...");
                clientList.remove(currentClient);
            }
        }
        return false;
    }
    
    private void writeToLogs(String message){
        message += "\n";
        try {
            logFile.write(message);
        } catch (IOException ex) {
            System.out.println("Error writing to logfile !");
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public ArrayList<String> getHistory() throws RemoteException {
        return messageList;
    }
    
    @Override
    public ArrayList<String> getConnectedUsers() throws RemoteException {
        ArrayList<String> usernames = new ArrayList<>();
        for (ClientHandler client : clientList){
            usernames.add(client.getUsername());
        }
        
        return usernames;
    }
    
    
}
