// CSD 2013, Pablo Galdámez
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//
// Main Client process.
// It creates a user interface. On UI events that happen at the UI interface, this class
// methods are invoked to connect to the server, to retrieve its channels, to disconnect, etc.
//
// It also contains a basic message listener for one chat user.
//
//
public class ChatRobot
        implements MessageListener
{
    private ChatConfiguration conf;
    private IChatServer srv = null;   // We just connect to one single server
    private IChatUser myUser = null;  // Our own user


    public ChatRobot (ChatConfiguration conf) {
        this.conf = conf;

    }


    //
    // The first thing to do before chatting is to connect to a ChatServer!!
    //
    // For us, connect means to locate it, register a new user into it and retrieve its channel list.
    // On success, returns the server channel list.
    //
    public  void conectar (String serverName) throws Exception {

        // Locate server using the name service
        try {
            Registry reg = LocateRegistry.getRegistry (conf.getNameServiceHost(),
                    conf.getNameServicePort());

            srv = (IChatServer) reg.lookup (serverName);
            //System.out.println ("LOG==> ChatServer: " + srv);
        } catch (java.rmi.ConnectException e) {
            throw new Exception ("rmiregistry not found at '" +
                    conf.getNameServiceHost() + ":" + conf.getNameServicePort() + "'");
        } catch (java.rmi.NotBoundException e) {
            throw new Exception ("Server '" + serverName + "' not found.");
        }

        // Once we've got the server, we create a local user object and register it into the server
        myUser = new ChatUser ("robot", this);
        boolean done = srv.connectUser (myUser);
        if (!done) throw new Exception ("Nick already in use");

        // Once we've registered, retrieve the channel list




    }

    //
    // Disconnect allows server to free up resources and remove stale references.
    //



    public  void doJoinChannel (String channelName) throws Exception {

        IChatChannel ch = srv.getChannel (channelName);
        if (ch == null) {throw new Exception ("Channel not found");}

        ch.join (myUser); // join

        IChatUser [] users = ch.listUsers ();
        if (users == null || users.length == 0)
            throw new Exception ("BUG. Tell professor there are no users after joining");

        String [] userList = new String [users.length];
        for (int i=0; i<users.length; i++) {
            userList[i] = users[i].getNick();
        }




    }

    //
    // UI wants to send a message to a channel... lets do it creating a IChatMessage
    //
    public void doSendChannelMessage (String dst, String msg) throws Exception
    {
        try {
            IChatChannel c_dst = srv.getChannel (dst);
            IChatMessage c_msg = new ChatMessage(myUser, c_dst, msg);
            c_dst.sendMessage (c_msg);
        } catch (Exception e) {
            throw new Exception ("Cannot send message: " + e);
        }
    }

    //
    // UI wants to send a private message to some user... lets do it creating a IChatMessage
    //

    //
    // On window close, try to disconnect

    //
    // ISA MessageListener
    // Messages come from a channel or from a remote user.
    //
    public void messageArrived (IChatMessage msg) {
        try {
            IChatUser src = msg.getSender();
            Remote dst = msg.getDestination();
            String str = msg.getText();

            if (msg.isPrivate()) {
                IChatUser u_dst = (IChatUser) dst;
             //   ui.showPrivateMessage (src.getNick(), u_dst.getNick(), str);

            } else {
                IChatChannel c_dst = (IChatChannel) dst;
                if (src == null) { // Control message from the channel itself
                    String nick = null;

                   if (str.startsWith (ChatChannel.JOIN)) {

                        nick = str.substring (ChatChannel.JOIN.length() + 1);
                        doSendChannelMessage(c_dst.getName(),"hola "+nick);
                    }
                }

            }
        } catch (Exception e) {
          //  ui.showErrorMessage ("Error when receiving message: " + e.getMessage());
        }
    }

    //
    // Main program, just creates the Client object, the program frame and shows it.
    //
    public static void main (String [] args) throws Exception {
        ChatRobot cc = new ChatRobot (ChatConfiguration.parse (args));
    cc.conectar(cc.conf.getServerName());
        cc.doJoinChannel("#Friends");
    }

}