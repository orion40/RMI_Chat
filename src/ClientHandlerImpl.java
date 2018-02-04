/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.RemoteException;


public class ClientHandlerImpl implements ClientHandler {
    private String clientName;
    
    public ClientHandlerImpl(String name){
        clientName = name;
    }

    @Override
    public void printMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public String getUsername() throws RemoteException {
        return clientName;
    }
    
}
