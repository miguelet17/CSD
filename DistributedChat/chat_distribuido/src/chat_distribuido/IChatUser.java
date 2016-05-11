package chat_distribuido;

// CSD 2013, Pablo Galdámez
import java.rmi.*;

//
// Users are objects. They receive messages when they are invoked their "sendMessage()" 
//
public interface IChatUser extends Remote {
   String getNick() throws RemoteException;
   void sendMessage(IChatMessage msg) throws RemoteException;
}
