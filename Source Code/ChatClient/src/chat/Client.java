package chat;

import chat.server.Server;
import chat.server.ServerService;
import chat.server.StatusMess;
import chat.server.User;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/**
 * Client, This is used as a middle-man between the GUI and ServerService.
 * Client simply places all WebMethods of ServerService into java methods that
 * have error detection.
 *
 * @author DisSys_Discard
 */



public class Client {

    // Main Server
    ServerService service; 
    Server port;
    
    //Backup Server
    ServerService service2; 
    Server port2;
    
    public boolean isServerMain = true;

    /**
     * Constructor connects to a new ServerService and gets the ServerPort.
     *
     * @throws WebServiceException
     */
    public Client() throws WebServiceException, MalformedURLException {
        URL pri = new URL("http://localhost:28081/ChatServer/ServerService?wsdl");
        service = new ServerService(pri);
        port = service.getServerPort();
        System.out.println(port.toString());
        
        URL bc = new URL("http://localhost:28082/ChatServer/ServerService?wsdl");
        service2 = new ServerService(bc);
        port2 = service2.getServerPort();
        System.out.println(port2.toString());
        
        
    }

    /**
     * Add a candidate user with username to the list of connected users, this
     * allows this user to send and receive messages.
     * @param username Candidate username.
     * @return True if the candidate user joined the connected user list.
     * @throws ConnectException Thrown if it could not connect to service.
     */
    public boolean join(String username, boolean group) throws ConnectException {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false)); 
            return port.join(username, group);
        } catch (Exception e) {
            isServerMain = false;
            System.out.println(e);
            return port2.join(username, group);
        }
        
        
    }

    /**
     * Candidate user to leave the server.
     * @param username Candidate username.
     * @throws ConnectException Thrown if it could not connect to service
     */
    public void leave(String username, boolean group) throws ConnectException {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false)); 
            port.leave(username, group); 
        } catch (Exception e) {
            isServerMain = false;
            port2.leave(username, group);
        }
    }

    /**
     * Send a message to all users from the candidate sender.
     * @param sender Username of sender.
     * @param message Message to send to all users.
     * @param image Image to send to all users.
     * @return True if message was sent to all users.
     * @throws ConnectException Thrown if it could not connect to service.
     */
    public boolean broadcast(String sender,
            String message, byte[] image, boolean group) throws ConnectException {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            }  
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            return port.broadcast(sender, message, image, group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.broadcast(sender, message, image, group);
        }
    }

    /**
     * Send a message and image from the sender to the receiver
     * @param username Candidate sender.
     * @param receiver Candidate receiver.
     * @param message Message to be sent to receiver.
     * @param image Image to be sent to receiver.
     * @return Server result, true if message was sent
     * @throws ConnectException Thrown if it could not connect to service.
     */
    public boolean privateMessage(String username, String receiver,
            String message, byte[] image, boolean group) throws ConnectException {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            return port.privateMessage(username, receiver, message, image, group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.privateMessage(username, receiver, message, image, group);
        }
    }

    /**
     * Make a candidate user admin.
     * Note: This is not encrypted, nor is it encoded.
     * @param username Candidate username.
     * @param pass Candidate password.
     * @return Server response, true if candidate was made admin.
     * @throws ConnectException Thrown if it could not connect to service.
     */
    public boolean adminLogon(
            String username, String pass, boolean group) throws ConnectException {

        try {
           /* if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false));*/
            return port.adminLogon(username, pass, group); 
        } catch (Exception e) {
            isServerMain = false;
            return port2.adminLogon(username, pass, group);
        }
    }

    /**
     * Logout a candidate user.
     * @param username Candidate username.
     * @return Server response, true if candidate user is no longer admin.
     * @throws ConnectException Thrown if it could not connect to service.
     */
    public boolean adminLogoff(String username, boolean group) throws ConnectException {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            return port.adminLogoff(username, group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.adminLogoff(username, group);
        }
    }

    /**
     * Get all the connected users of the ServerService.
     * @param username Candidate username.
     * @return List of all users connected to the server.
     * @throws ConnectException Thrown if it could not connect to service.
     */
    public List<User> connectedUsers(
            String username, boolean group) throws ConnectException {

        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            }  
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            return port.connectedUsers(username, group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.connectedUsers(username, group);
        }
    }

    /**
     * Collect all message for a candidate user.
     * @param username Candidate username.
     * @return List of Message objects that have been sent to the candidate user.
     */
    public List<StatusMess> listen(String username, boolean group) {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            return port.listen(username, group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.listen(username, group);
        }
    }
    
    public void setToggleStatus(String username, boolean group) {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            port.setToggleStatus(username, group);
        } catch (Exception e) {
            isServerMain = false;
            port2.setToggleStatus(username, group);
        }
    }
    
    public boolean getStatus_isOnline(String username, boolean group) {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            return port.getStatusIsOnline(username, group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.getStatusIsOnline(username, group);
        }
    }
    
    public List<StatusMess> setAllonline(String username, boolean group) {
        try {
            if(!isServerMain) {
                port.setUserList(port2.getUserList(true), port2.getUserList(false));
                isServerMain = true;
            } 
            port2.setUserList(port.getUserList(true), port.getUserList(false));
            return port.setAllonline(username, group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.setAllonline(username, group);
        }
    }
    
    public List<User> getUserList(boolean group) {
        try {
            return port.getUserList(group);
        } catch (Exception e) {
            isServerMain = false;
            return port2.getUserList(group);
        }
    }
    
    public void setUserList(List<User> a, List<User> b) {
        try {
            port.setUserList(a,b);
        } catch (Exception e) {
            isServerMain = false;
            port2.setUserList(a,b);
        }
    }
}
