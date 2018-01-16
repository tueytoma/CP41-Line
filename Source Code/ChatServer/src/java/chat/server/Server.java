package chat.server;

import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Server WebService ServerService. Server contains all the methods required for
 * a basic public chat, users may join, leave, send a message, send a private
 * message, a message may also contain an image. A list of connected users can
 * be returned and users maybe made an admin as long as they admit the correct
 * password.
 *
 * @author DisSys_Discard
 */
@WebService(serviceName = "ServerService")
public class Server {

    private static final String BROADCAST = "BROADCAST";
    private final String ADMIN_PASS = "pass";
    private final ArrayList<User> userListA;
    private final ArrayList<User> userListB;

    /**
     * ChatService constructor.
     */
    public Server() {
        this.userListA = new ArrayList<>();
        this.userListB = new ArrayList<>();
    }

    /**
     * Add a user, first checks if another user is using the same username and
     * if no other user has the same username then add a new user with the
     * candidate username. Username may not already exist. Username may not be
     * whitespace. Username may not be less than 1 in length.
     *
     * @param userName Candidate username.
     * @return Success True if user joined, false otherwise.
     */
    @WebMethod
    public boolean join(String userName, boolean group) {
        userName = userName.trim();
        User user = resolveUser(userName, group);
        
        if(group) {
            synchronized (userListA) {
                if (user != null
                        || userName.equals(BROADCAST)
                        || userName.length() < 1) {
                    //User exists or Username = BROADCAST
                    return false;
                } else {
                    //Add user, it doesn't yet exist
                    User u = new User();
                    u.username = userName;
                    userListA.add(u);
                    return true;
                }
            } 
        }  else {
            synchronized (userListB) {
                if (user != null
                        || userName.equals(BROADCAST)
                        || userName.length() < 1) {
                    //User exists or Username = BROADCAST
                    return false;
                } else {
                    //Add user, it doesn't yet exist
                    User u = new User();
                    u.username = userName;
                    userListB.add(u);
                    return true;
                }
            }
        }
    }

    /**
     * Multicase a message to all users, including sender, if and only if the
     * sender exists AND if the message send is not null.
     * It is Multicast but we forget to change it. 
     * 
     *
     * @param sender username of the sender.
     * @param message Message to be sent to all users.
     * @param image Image to be sent to all users.
     * @return True if the message is not empty and username exists.
     */
    @WebMethod
    public boolean broadcast(String sender, String message, byte[] image, boolean group) {
        User sendingUser = resolveUser(sender, group);
        if(group) {
            synchronized (userListA) {

                //Loop though all users if the sender has joined
                if (sendingUser != null && message.length() > 0) {

                    //Add new message to all users
                    for (User user : userListA) {
                        Message msg = new Message();
                        msg.sender = sender;
                        msg.receiver = BROADCAST;
                        msg.message = message.trim();
                        msg.image = image;
                        
                        StatusMess sm = new StatusMess();
                        sm.mess = msg;
                        sm.online = user.isOnline;

                        user.messages.add(sm);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            synchronized (userListB) {

                //Loop though all users if the sender has joined
                if (sendingUser != null && message.length() > 0) {

                    //Add new message to all users
                    for (User user : userListB) {
                        Message msg = new Message();
                        msg.sender = sender;
                        msg.receiver = BROADCAST;
                        msg.message = message.trim();
                        msg.image = image;

                        StatusMess sm = new StatusMess();
                        sm.mess = msg;
                        sm.online = user.isOnline;

                        user.messages.add(sm);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Send a message from one user to another user if and only if they are both
     * in the chat.
     *
     * @param sender Username of sender.
     * @param receiver Username of receiver.
     * @param message Message to send to candidate receiver.
     * @param image Image to be sent to candidate receiver.
     * @return Success True if sender and receiver exists AND if message is not
     * empty.
     */
    @WebMethod
    public boolean privateMessage(
            String sender, String receiver, String message, byte[] image, boolean group) {
        User sendingUser = resolveUser(sender, group);
        User receivingUser = resolveUser(receiver, group);

        if(group) {
            synchronized (userListA) {
                if (sendingUser == null
                        || receivingUser == null || message.length() < 0) {
                    //Either too or from candidate is not in the chat
                    return false;
                } else {
                    //Both candidate users are present, send message...
                    Message msg1 = new Message();
                    msg1.sender = sender;
                    msg1.receiver = receiver;
                    msg1.message = message.trim();
                    msg1.image = image;
                    Message msg2 = new Message();
                    msg2.sender = sender;
                    msg2.receiver = receiver;
                    msg2.message = message.trim();
                    msg2.image = image;

                    StatusMess sm1 = new StatusMess();
                    sm1.mess = msg1;
                    sm1.online = sendingUser.isOnline;
                    
                    StatusMess sm2 = new StatusMess();
                    sm2.mess = msg2;
                    sm2.online = receivingUser.isOnline;

                    sendingUser.messages.add(sm1);
                    receivingUser.messages.add(sm2);

                    return true;
                }
            }
        } else {
            synchronized (userListB) {
            if (sendingUser == null
                    || receivingUser == null || message.length() < 0) {
                //Either too or from candidate is not in the chat
                return false;
            } else {
                //Both candidate users are present, send message...
                Message msg1 = new Message();
                msg1.sender = sender;
                msg1.receiver = receiver;
                msg1.message = message.trim();
                msg1.image = image;
                Message msg2 = new Message();
                msg2.sender = sender;
                msg2.receiver = receiver;
                msg2.message = message.trim();
                msg2.image = image;

                StatusMess sm1 = new StatusMess();
                    sm1.mess = msg1;
                    sm1.online = sendingUser.isOnline;
                    
                    StatusMess sm2 = new StatusMess();
                    sm2.mess = msg2;
                    sm2.online = receivingUser.isOnline;

                    sendingUser.messages.add(sm1);
                    receivingUser.messages.add(sm2);

                return true;
            }
        }
        }
    }

    /**
     * Apply admin on a user if the user exists
     *
     * @param userName Candidate username.
     * @param password Candidate password.
     * @return Success True if user exists and password matched.
     */
    @WebMethod
    public boolean adminLogon(String userName, String password, boolean group) {
        User user = resolveUser(userName, group);

        if(group) {
            synchronized (userListA) {
                if (user != null && password.equals(ADMIN_PASS)) {
                    user.isAdmin = (true);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            synchronized (userListB) {
            if (user != null && password.equals(ADMIN_PASS)) {
                user.isAdmin = (true);
                return true;
            } else {
                return false;
            }
        }
        }
    }

    /**
     * Revoke admin on a user if the user exists
     *
     * @param userName Candidate user to remove admin from.
     * @return Success True if candidate exists.
     */
    @WebMethod
    public boolean adminLogoff(String userName, boolean group) {
        User user = resolveUser(userName, group);

        if(group) {
            synchronized (userListA) {
                if (user != null) {
                    user.isAdmin = false;
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            synchronized (userListB) {
                if (user != null) {
                    user.isAdmin = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Return accumulated messages for a candidate user
     *
     * @param userName Candidate user.
     * @return List of message objects that have been send to the user.
     */
    @WebMethod
    public ArrayList<StatusMess> listen(String userName, boolean group) {
        User user = resolveUser(userName, group);

        if(group) {
            synchronized (userListA) {
                if (user == null) {
                    return null;
                } else {
                    //Get copy of user messages
                    ArrayList<StatusMess> msgs = (ArrayList) user.messages.clone();

                    //Cleear user messages
                    user.messages.clear();

                    //Return copy of user messages
                    return msgs;
                }
            }
        } else { 
            synchronized (userListB) {
                if (user == null) {
                    return null;
                } else {
                    //Get copy of user messages
                    ArrayList<StatusMess> msgs = (ArrayList) user.messages.clone();

                    //Cleear user messages
                    user.messages.clear();

                    //Return copy of user messages
                    return msgs;
                }
            }
        }
    }

    /**
     * Remove a candidate user from the user list
     *
     * @param userName Candidate user.
     */
    @WebMethod
    public void leave(String userName, boolean group) {
        User user = resolveUser(userName, group);

        if(group) {
            synchronized (userListA) {
                if (user != null) {

                    //Remove user
                    userListA.remove(user);
                }
            }
        } else {
            synchronized (userListB) {
                if (user != null) {

                    //Remove user
                    userListB.remove(user);
                }
            }
        }
    }

    /**
     * Returns userList if user exists
     *
     * @param userName User requesting the connected users.
     * @return List containing all connected users.
     */
    @WebMethod
    public ArrayList<User> connectedUsers(String userName, boolean group) {
        User user = resolveUser(userName, group);

        if(group) {
            synchronized (userListA) {
                if (user != null) {
                    //Return list of connected users
                    return userListA;
                } else {
                    return null;
                }
            }
        } else {
            synchronized (userListB) {
                if (user != null) {
                    //Return list of connected users
                    return userListB;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Given a userName, find and return a matching User object
     *
     * @param userName Search userList for a matching USer
     * @return null if user not found or User object with matching userName if
     * found
     */
    private User resolveUser(String userName, boolean group) {
        User resolvedUser = null;

        if(group) {
            synchronized (userListA) {
                for (User u : userListA) {
                    if ((u.username).equals(userName)) {
                        resolvedUser = u;
                    }
                }
            }
        } else {
            synchronized (userListB) {
                for (User u : userListB) {
                    if ((u.username).equals(userName)) {
                        resolvedUser = u;
                    }
                }
            }
        }
       
        return resolvedUser;
    }

    /**
     * Web service operation
     */
    @WebMethod
    public void setToggleStatus(String userName, boolean group) {
        //TODO write your implementation code here:
        User user = resolveUser(userName, group);
        user.isOnline = !(user.isOnline);
    }

    /**
     * Web service operation
     */
    @WebMethod
    public Boolean getStatus_isOnline(String userName, boolean group) {
        //TODO write your implementation code here:
        User user = resolveUser(userName, group);
        return user.isOnline;
    }

    /**
     * Web service operation
     */
    @WebMethod
    public ArrayList<StatusMess> setAllonline(String userName, boolean group) {
        //TODO write your implementation code here:
        User user = resolveUser(userName, group);

        if(group) {
            synchronized (userListA) {
                if (user == null) {
                    return null;
                } else {
                    //Get copy of user messages
                    ArrayList<StatusMess> msgs = (ArrayList) user.messages.clone();
                    for (int i = 0; i < msgs.size(); i++) {
                        msgs.get(i).online = true;
                    }

                    //Cleear user messages
                    user.messages.clear();

                    //Return copy of user messages
                    return msgs;
                }
            }
        } else { 
            synchronized (userListB) {
                if (user == null) {
                    return null;
                } else {
                    //Get copy of user messages
                    ArrayList<StatusMess> msgs = (ArrayList) user.messages.clone();
                    for (int i = 0; i < msgs.size(); i++) {
                        msgs.get(i).online = true;
                    }

                    //Cleear user messages
                    user.messages.clear();

                    //Return copy of user messages
                    return msgs;
                }
            }
        }
    }

    /**
     * Web service operation
     */
    @WebMethod
    public ArrayList<User> getUserList(boolean group) {
        //TODO write your implementation code here:
        if(group) return userListA;
        else return userListB;
    }

    /**
     * Web service operation
     */
    @WebMethod
    public void setUserList(ArrayList<User> userA, ArrayList<User> userB) {
        //TODO write your implementation code here:
        userListA.clear();
        for (int i = 0 ; i < userA.size() ;i++){
            userListA.add(userA.get(i));
        }
        
        userListB.clear();
        for (int i = 0 ; i < userB.size() ;i++){
            userListB.add(userB.get(i));
        }
        
    }
    
}
