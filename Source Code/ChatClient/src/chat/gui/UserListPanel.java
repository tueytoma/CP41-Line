package chat.gui;

import chat.server.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * User list panel, displays connected users, Current username and Logout button
 *
 * @author DisSys_Discard
 */
public class UserListPanel extends JPanel {

    /**
     * Parent frame ClientGUI
     */
    public final ClientGUI parent;
    
    public boolean request = false;
    private int sum = 0;

    private RoundPanel userNameLabel;
    private RoundPanel logoutButton;
    private RoundPanel offlineButton;
    private RoundPanel spam;
    private RoundPanel space;
    private RoundPanel space2;
    private JPanel listPanel;
    private JPanel commandPanel;
    private JLabel logo;
    private JScrollPane scrollPane;
    
    private JLabel jb;
    private JLabel jb2;
    private JLabel jb3;
    private JLabel jb4;
    
    private int online = 1;
    
    private final Color BACKGROUND = new Color(246,246,245);
    private final Color BACKGROUND_default_textArea = new Color(255, 255, 255);
    private final Color BUTTON_COLOR = new Color(0, 194, 0);
    private final Color BUTTON_COLOR_ON_HOVER = new Color(0, 160, 0);
    private final Color OFFline_COLOR = new Color(210,73,18);
    private final Color OFFline_COLOR_ON_HOVER = new Color(190,50,20);
    private final Color USER_COLOR = new Color(65,72,88);
    private final Color USER_TEXT_COLOR = new Color(255, 255, 255);
    private final Color BUTTON_TEXT_COLOR = new Color(255,255,255);
    private final Dimension SIZE = new Dimension(200, 0);
    private final Dimension USER_NAME_LABEL_SIZE = new Dimension(100, 40);
    private final Dimension SCROLL_PANE_SIZE = new Dimension(0, 50);
    private final Dimension USER_ARC = new Dimension(30, 30);
    private final Dimension COMMAND_BUTTON_ARC = new Dimension(30, 30);
    private final Dimension COMMAND_BUTTON_SIZE = new Dimension(60, 30);
    private final Dimension ARC = new Dimension(20, 20);
    private final Font USER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 17);
    private final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Insets SCROLL_PANE_INSETS = new Insets(8, 8, 8, 8);
    private final Insets BUTTON_INSETS = new Insets(8, 8, 8, 8);
    private final EmptyBorder EMPTY_BORDER = new EmptyBorder(SCROLL_PANE_INSETS);
    private final String LOGOUT_BUTTON_TEXT = "Logout";
    private final String OFFLINE_BUTTON_TEXT = "Click to Offline";
    private final String ONLINE_BUTTON_TEXT = "Click to Online";
    private final String SPAM_BUTTON_TEXT = "All user spam message";

    /**
     * Init UserListPanel
     * @param parent Parent frame ClientGUI
     */
    public UserListPanel(JFrame parent) {
        this.parent = (ClientGUI) parent;

        init();
        addListeners();
    }

    /**
     * Init GUI components of this panel
     */
    private void init() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        setLayout(gridbag);
        setBackground(BACKGROUND);
        setPreferredSize(SIZE);
        
        //Get and set image icon to display.
        try {
            URL url = getClass().getResource(ClientGUI.IMAGE_LOC2);
            Image img = Toolkit.getDefaultToolkit().getImage(url);

            //Image img = ImageIO.read(new File(IMAGE_LOC));
            img = img.getScaledInstance(
                    150, -1, Image.SCALE_SMOOTH);
            logo = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Component that displays this logged in user
        userNameLabel = new RoundPanel(USER_ARC, new GridBagLayout());
        userNameLabel.setOpaque(false);
        userNameLabel.setForeground(USER_COLOR);
        userNameLabel.setBackground(USER_COLOR);
        jb = new JLabel("");
        jb.setFont(USER_FONT);
        jb.setForeground(USER_TEXT_COLOR);
        userNameLabel.add(jb);
        userNameLabel.setPreferredSize(USER_NAME_LABEL_SIZE);

        //Component used to offline of chat
        offlineButton = new RoundPanel(COMMAND_BUTTON_ARC, new GridBagLayout());
        offlineButton.setOpaque(false);
        offlineButton.setForeground(BUTTON_COLOR);
        offlineButton.setBackground(BUTTON_COLOR);
        jb3 = new JLabel(OFFLINE_BUTTON_TEXT);
        jb3.setFont(DEFAULT_FONT);
        jb3.setForeground(BUTTON_TEXT_COLOR);
        offlineButton.add(jb3);
        offlineButton.setPreferredSize(COMMAND_BUTTON_SIZE);
        
        //Component used to logout of chat
        logoutButton = new RoundPanel(COMMAND_BUTTON_ARC, new GridBagLayout());
        logoutButton.setOpaque(false);
        logoutButton.setForeground(BUTTON_COLOR);
        logoutButton.setBackground(BUTTON_COLOR);
        jb2 = new JLabel(LOGOUT_BUTTON_TEXT);
        jb2.setFont(DEFAULT_FONT);
        jb2.setForeground(BUTTON_TEXT_COLOR);
        logoutButton.add(jb2);
        logoutButton.setPreferredSize(COMMAND_BUTTON_SIZE);
        
        //Component used to request message of chat when you offline
        spam = new RoundPanel(COMMAND_BUTTON_ARC, new GridBagLayout());
        spam.setOpaque(false);
        spam.setForeground(BUTTON_COLOR);
        spam.setBackground(BUTTON_COLOR);
        jb4 = new JLabel(SPAM_BUTTON_TEXT);
        jb4.setFont(DEFAULT_FONT);
        jb4.setForeground(BUTTON_TEXT_COLOR);
        spam.add(jb4);
        spam.setPreferredSize(COMMAND_BUTTON_SIZE);
        
        space = new RoundPanel(COMMAND_BUTTON_ARC, new GridBagLayout());
        space.setForeground(BACKGROUND);
        space.setBackground(BACKGROUND);
        space.setPreferredSize(new Dimension(60, 2));
        
        space2 = new RoundPanel(COMMAND_BUTTON_ARC, new GridBagLayout());
        space2.setForeground(BACKGROUND);
        space2.setBackground(BACKGROUND);
        space2.setPreferredSize(new Dimension(60, 2));
        
        commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));
        commandPanel.setBackground(BACKGROUND);
        commandPanel.setPreferredSize(new Dimension(100,100));
        commandPanel.add(offlineButton);
        commandPanel.add(space);
        commandPanel.add(spam);
        commandPanel.add(space2);
        commandPanel.add(logoutButton);

        //List panel is used to contain all connected users
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BACKGROUND);
        
        //Allow scrolling for the list panel
        scrollPane = new JScrollPane(
                listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.setBorder(EMPTY_BORDER);
        scrollPane.setPreferredSize(SCROLL_PANE_SIZE);
        
        c.gridx = 0;
        c.gridy = 0;
        add(logo, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = BUTTON_INSETS;
        add(userNameLabel, c);
        
        c.gridx = 0;
        c.gridy = 2;
        add(commandPanel, c);

        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = GridBagConstraints.RELATIVE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(scrollPane, c);
    }

    private void addListeners() {
        //User notification of mouse events for the logout button
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                              
                logoutButton.setBackground(BUTTON_COLOR_ON_HOVER);
                logoutButton.setForeground(BUTTON_COLOR_ON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(BUTTON_COLOR);
                logoutButton.setForeground(BUTTON_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    online = 1;
                    offlineButton.setBackground(BUTTON_COLOR);
                    offlineButton.setForeground(BUTTON_COLOR);
                    parent.messagePanel.inputPanel.inputField.setEnabled(true);
                    parent.messagePanel.inputPanel.inputField.setEditable(true);
                    parent.messagePanel.inputPanel.inputField.setBackground(BACKGROUND_default_textArea);
                    parent.messagePanel.inputPanel.scroller.setBackground(BACKGROUND_default_textArea);
                    parent.logout();
                } catch (Exception ex) {
                    ((MessagePanel) getParent()).getChatPanel().addServerMessage("LOGOUT FAILED");
                    System.out.println(getClass() + ".logoutPanel.mousePressed():ConnectionError");
                }
            }
        });
        
        //User notification of mouse events for the offline button
        offlineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(online == 1) { 
                    offlineButton.setBackground(BUTTON_COLOR_ON_HOVER);
                    offlineButton.setForeground(BUTTON_COLOR_ON_HOVER);
                } else { 
                    offlineButton.setBackground(OFFline_COLOR_ON_HOVER);
                     offlineButton.setForeground(OFFline_COLOR_ON_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(online == 1) { 
                    offlineButton.setBackground(BUTTON_COLOR);
                    offlineButton.setForeground(BUTTON_COLOR);
                } else { 
                    offlineButton.setBackground(OFFline_COLOR);
                     offlineButton.setForeground(OFFline_COLOR);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(online == 1) { 
                    jb3.setText(ONLINE_BUTTON_TEXT); 
                    online = 0; 
                    offlineButton.setBackground(OFFline_COLOR);
                    offlineButton.setForeground(OFFline_COLOR);
                } else { 
                    jb3.setText(OFFLINE_BUTTON_TEXT ); 
                    online = 1; 
                    offlineButton.setBackground(BUTTON_COLOR);
                    offlineButton.setForeground(BUTTON_COLOR);
                }
                parent.toggleStatus();
                parent.messagePanel.inputPanel.updateArea();
            }
        });
        
        spam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                spam.setBackground(BUTTON_COLOR_ON_HOVER);
                spam.setForeground(BUTTON_COLOR_ON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                spam.setBackground(BUTTON_COLOR);
                spam.setForeground(BUTTON_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    for (int i = 0 ; i < parent.client.getUserList(true).size() ;i++){
                        String usr = parent.client.getUserList(true).get(i).getUsername();
                        boolean t = parent.group;
                        if(!parent.client.getStatus_isOnline(usr, t)) continue;
                        
                        parent.client.broadcast(usr, "Hello everybody" + usr, null, t);
                    }
                    
                } catch (ConnectException ex) {
                    Logger.getLogger(UserListPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
            
        });
        
        
    }
    
    
    

    /**
     * Method is used to find any user that aren't connected anymore but the
     * list of users is still displaying they are connected. In which case
     * remove them.
     */
    private boolean isInArray(String user, Component[] array) {
        for (int i = 0; i < array.length; i++) {
            String oldUser = ((UserListItem) array[i]).getUsername();

            if (user.equals(oldUser)) {
                //Found match!
                return true;
            }
        }

        //Wasn't found
        return false;
    }

    /**
     * Method used to find users that are connected but aren't in the local 
     * list of users, in which case add a new user label.
     */
    private boolean isInList(String user, List<User> list) {
        for (int i = 0; i < list.size(); i++) {
            String newUser = list.get(i).getUsername();

            if (user.equals(newUser)) {
                //Found match!
                return true;
            }
        }

        //Wasn't found
        return false;
    }

    /**
     * Use this to update the username component.
     *
     * @param userName Username to replace previous.
     */
    public void updateUserName(String userName) {
        ((JLabel) userNameLabel.getComponent(0)).setText(userName);
    }

    /**
     * Add and remove users from the list of connected users to match the
     * servers connected users.
     * @param newList New list of connected users.
     */
    public void updateListPanel(List<User> newList) {
        Component[] oldList = listPanel.getComponents();

        //Remove users who have logged out
        for (Component oldLabel : oldList) {
            String user = ((UserListItem) oldLabel).getUsername();
            if (isInList(user, newList)) {
                //User is contained in newU do nothing
            } else {
                //Remove JLabel
                listPanel.remove(oldLabel);
                ((MessagePanel) getParent()).getChatPanel().addServerMessage(((UserListItem) oldLabel).getUsername() + " left.");
            }
        }

        //Add users who have recently joined
        for (User newUser : newList) {
            String user = newUser.getUsername();
            if (isInArray(user, oldList)) {
                //User is contained in newU do nothing
            } else if (!user.equals(parent.getUserName())) {
                //Add new user
                addUserItem(user);
            }
        }

        scrollPane.invalidate();
        scrollPane.validate();
        scrollPane.repaint();
    }

    /**
     * Get the list of local connected users.
     * @return Panel containing all connected user items.
     */
    protected JPanel getListPanel() {
        return listPanel;
    }

    /**
     * Add a GUI component that shows that a user is connected to the server.
     */
    private void addUserItem(String user) {
        UserListItem item = new UserListItem(ARC, user,parent);

        listPanel.add(item);
        ((MessagePanel) getParent()).getChatPanel().addServerMessage(user + " joined.");
    }

    /**
     * Get the selected users of the user list panel.
     * @return All selected users.
     */
    protected ArrayList<String> getSelectedUsers() {
        ArrayList<String> arr = new ArrayList<>();

        for (Component item : listPanel.getComponents()) {
            if (((UserListItem) item).isSelected()) {
                arr.add(((UserListItem) item).getUsername());
            }
        }

        return arr;
    }
}
