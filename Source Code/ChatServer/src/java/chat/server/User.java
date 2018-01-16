package chat.server;

import java.util.ArrayList;

/**
 * User class, represents data that a user should hold. All fields are public
 *
 * @author DisSys_Discard
 */
public class User {
    /* name for the user, should be made unique */

    public String username;

    /* List of all messages that this user has accumilated */
    public final ArrayList<StatusMess> messages = new ArrayList<>();
   

    /* boolean used to check if user has been made an admin */
    public boolean isAdmin;
    
    public boolean isAgroup;
    
    public boolean isOnline = true;
}
