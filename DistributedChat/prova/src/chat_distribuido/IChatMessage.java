package chat_distribuido;

// CSD 2013, Pablo Galdámez
import java.rmi.*;

//
// For our Chat, messages are objects. We could simplify the chat so that messages are simple 
// string, but having them as objects allows us to explore interesting invocation patterns.
//
public interface IChatMessage extends Remote{
   String getText() throws RemoteException;    // The message itself
   IChatUser getSender() throws RemoteException; // allways the user who sends
   Remote getDestination() throws RemoteException; // actual class depends on private/pub
   boolean isPrivate() throws RemoteException; // actual class depends on private/pub
}
