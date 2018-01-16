package chat.gui;

import chat.server.Message;
import chat.server.StatusMess;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Chat panel, displays messages
 *
 * @author DisSys_Discard
 */
public class ChatPanel extends JPanel {

    private final ClientGUI parent;

    private int msgCount = 0;

    private JPanel msgsPanel;
    private JPanel jp;
    private JPanel container;
    public JScrollPane scroller;
    private ArrayList<RoundPanel> panelMessages;
    private final Color BACKGROUND = new Color(136,163,191);
    private final Color MSG_FROM_COLOR = new Color(254,254,254);
    private final Color MSG_ME_COLOR = new Color(180,237,136);
    private final Color MSG_PRIV_FROM = new Color(180, 220, 140);
    private final Color MSG_PRIV_ME = new Color(200, 247, 160);
    private final Color MSG_SERVER = new Color(243,243,243);
    private final Color GLOBAL_MSG_COLOR = new Color(243,243,243);
    private final Color GLOBAL_MSG_COLOR2 = new Color(63,71,92,50);
    private final Dimension SIZE = new Dimension(0, 80);
    private final Dimension ARC = new Dimension(20, 20);
    private final Dimension INPUT_FIELD_SIZE = new Dimension(0, 50);
    private final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font GLOBAL_MSG_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    private final Font USER_MSG_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    private final Insets TEXT_FIELD_INSETS = new Insets(5, 8, 5, 8);
    private final Insets ME_INSETS = new Insets(5, 40, 5, 5);
    private final Insets FROM_INSETS = new Insets(5, 5, 5, 40);
    private final Insets SERVER_MESSAGE_INSETS = new Insets(5, 40, 5, 40);
    private final EmptyBorder EMPTY_BORDER = new EmptyBorder(TEXT_FIELD_INSETS);

    /**
     * Init ChatPanel.
     *
     * @param parent Parent frame ClientGUI.
     */
    public ChatPanel(JFrame parent) {
        this.parent = (ClientGUI) parent;

        init();
        addListeners();
    }

    /**
     * Init GUI components.
     */
    private void init() {
        //setLayout(new BorderLayout());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BACKGROUND);
        setPreferredSize(SIZE);

        //List of messages components
        panelMessages = new ArrayList<>();

        //Panel that displays message components
        msgsPanel = new JPanel();
        msgsPanel.setLayout(new GridBagLayout());
        
        //msgsPanel.setLayout(new BoxLayout(msgsPanel, BoxLayout.Y_AXIS));
        //msgsPanel.setBounds(100, 100, 450, 300);
        
        msgsPanel.setBackground(BACKGROUND);
        
        jp = new JPanel();
        jp.setBackground(BACKGROUND);
        jp.setLayout(new BorderLayout());
        jp.add(msgsPanel, BorderLayout.NORTH);
        

        //Allow message panel to be scrollable
        scroller = new JScrollPane(
                jp,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setBackground(BACKGROUND);
        scroller.setBorder(EMPTY_BORDER);
        scroller.setPreferredSize(INPUT_FIELD_SIZE);
        scroller.setBackground(BACKGROUND);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        
        scroller.setPreferredSize(new Dimension(80,100));

        add(scroller, 0);
    }

    private void addListeners() {
        //NONE
    }

    /**
     * Remove all message components from the list of message components.
     */
    protected void clear() {
        msgsPanel.removeAll();
    }

    /**
     * Method updates and creates the GUI required to show messages to and from
     * users. Selected user messages are shown only if at least one has been
     * selected.
     *
     * @param newMsgs List of message for the client user.
     */
    protected void updateTextArea(List<StatusMess> newMsgs, String usr) {
        String sender;
        String receiver; //Default BROADCAST
        String message;
        byte[] image;

        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 0; i < newMsgs.size(); i++) {
            sender = newMsgs.get(i).getMess().getSender();
            receiver = newMsgs.get(i).getMess().getReceiver();
            message = newMsgs.get(i).getMess().getMessage();
            image = newMsgs.get(i).getMess().getImage();
            
            String temp ="";
            int x = 0;
            if(message.length()>60){
                while(message.length() - x > 60){
                    temp += message.substring(x, x + 60) + "\n";
                    x += 60;
                }
                temp = temp + message.substring(x);
                message = temp;
            }
   
            
            //if(newMsgs.get(i).isOnline()) {
                RoundPanel msgPanel;
                JTextArea msgLabel = new JTextArea();
                msgLabel.setFont(DEFAULT_FONT);
                msgLabel.setBorder(EMPTY_BORDER);
                msgLabel.setEditable(false);
                msgLabel.setOpaque(false);

                //Create timestamp of when I received the message
                String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());

                //Add time stamp
                JLabel timeLabel = new JLabel(timeStamp);
                timeLabel.setFont(GLOBAL_MSG_FONT);
                timeLabel.setForeground(GLOBAL_MSG_COLOR);

                //Messges I sent
                if (parent.getUserName().equals(sender)) {
                    msgLabel.setText(message);

                    msgPanel = new RoundPanel(ARC, new FlowLayout(FlowLayout.RIGHT));
                    msgPanel.setOpaque(false);

                    if (receiver.equals(ClientGUI.BROADCAST)) {
                        //Public message I sent to everyone
                        msgPanel.username = ClientGUI.BROADCAST;

                        msgLabel.setText(message);
                        msgPanel.setBackground(MSG_ME_COLOR);
                        msgPanel.setForeground(MSG_ME_COLOR);
                    } else {
                        //Private message I sent to receiver
                        msgPanel.username = receiver;

                        //Set the colour of the private messge to the same colour
                        // as the connected user colour when selected.
                        Color privMsgColor = null;
                        UserListItem item = getUserListItem(receiver);
                        if (item != null) {
                            privMsgColor = item.getItemColor();
                        }
                        //msgLabel.setForeground(BACKGROUND);
                        msgPanel.setBackground(privMsgColor.darker());
                        msgPanel.setForeground(privMsgColor.darker());
                    }
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    gbc.weightx = 1;
                    //gbc.gridy = msgCount;
                    gbc.insets = ME_INSETS;
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                } else {

                    //Messages to me
                    msgLabel.setText(message);
                    //msgLabel.setForeground(MSG_SERVER);
                    msgPanel = new RoundPanel(ARC, new FlowLayout(FlowLayout.LEFT));
                    msgPanel.setOpaque(false);

                    if (receiver.equals(ClientGUI.BROADCAST)) {
                        //Public messages send to everyone, give default colour
                        msgPanel.username = sender;
                        msgPanel.setBackground(MSG_FROM_COLOR);
                        msgPanel.setForeground(MSG_FROM_COLOR);
                    } else {
                        //Private message, set colour to the user who sent it
                        msgPanel.username = sender;
                        Color privMsgColor = null;
                        UserListItem item = getUserListItem(sender);
                        if (item != null) {
                            privMsgColor = item.getItemColor();
                        }
                        msgPanel.setBackground(privMsgColor);
                        msgPanel.setForeground(privMsgColor);
                    }
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    gbc.weightx = 1;
                    //gbc.gridy = msgCount;
                    gbc.insets = FROM_INSETS;
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                }
                
                msgPanel.isOnline = parent.getStatus();

                msgPanel.add(msgLabel, 0);

                //Add image
                if (image != null) {
                    JLabel imageLabel = new JLabel();
                    imageLabel.addMouseListener(imageLabelListener());
                    imageLabel.setIcon(processImageBytes(image));                
                    msgPanel.add(imageLabel);
                }



                //Message component formatting
                container = new JPanel();
                JLabel pm = null;
                if (parent.getUserName().equals(sender)) {
                    //Messages I sent, put on the right side of panel
                    container.setLayout(new FlowLayout(FlowLayout.RIGHT));

                    if (receiver.equals(ClientGUI.BROADCAST)) {
                        //Do nothing
                    } else {
                        //Private message, add who the message is sent to on the right
                        pm = new JLabel("to " + receiver);
                        pm.setFont(USER_MSG_FONT);
                        pm.setForeground(GLOBAL_MSG_COLOR);
                    }
                    //container.add(timeLabel, 0);
                } else {
                    //Message sent to me, put ont the left side of panel
                    container.setLayout(new FlowLayout(FlowLayout.LEFT));

                    if (receiver.equals(ClientGUI.BROADCAST)) {
                        //Broadcast message
                        JLabel jb = new JLabel(sender);
                        jb.setFont(USER_MSG_FONT);
                        jb.setForeground(GLOBAL_MSG_COLOR);
                        container.add(jb);
                    } else {
                        //Private message, add PM to the left
                        JLabel jb = new JLabel("[PM] " + sender);
                        jb.setFont(USER_MSG_FONT);
                        jb.setForeground(GLOBAL_MSG_COLOR);
                        container.add(jb);
                    }
                    //container.add(timeLabel);
                }

                //Add msgPanel reference to list of messages
                panelMessages.add(msgPanel);

                //Add message content plus private message label
                container.add(msgPanel);
                if (pm != null) {
                    container.add(pm);
                }
                //Messges I sent
                if (parent.getUserName().equals(sender)) {
                    container.add(timeLabel, 0);
                } else { // Messges to me
                    container.add(timeLabel, -1);
                }

                container.setOpaque(false);


                //Add message containter to scrollpane
                msgsPanel.add(container, gbc, -1);
                //msgsPanel.add(container, gbc);

                Timer t = new Timer();
                t.schedule(new TimerTask () {
                @Override
                public void run() {
                    scroller.getVerticalScrollBar().setValue(scroller.getVerticalScrollBar().getMaximum());
                    revalidate();
                    repaint();
                    }           
                },5);
                //msgCount++;
                //Need to refresh scrolling

                revalidate();
                repaint();
            //} else ;
            
            
                //int height = (int)msgsPanel.getPreferredSize().getHeight();
                //scroller.getVerticalScrollBar().setValue(height);
        }

        try {
            //Only show messages from selected users, if none selected then
            // show all messages.
            if(parent.getStatus()) filterMessages(true);
            else filterMessages(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        
        revalidate();
        repaint();
    }

    /**
     * Show messages from selected users, if no users selected then show all
     * messages.
     *
     * @throws NullPointerException No messages.
     */
    protected void filterMessages(boolean online) throws NullPointerException {
        ArrayList<String> userList = ((MessagePanel) getParent()).getUserListPanel().getSelectedUsers();
        Component[] msgs = msgsPanel.getComponents();
        
        if(online) {
            for(int i = 0 ; i < panelMessages.size(); i++) {
                panelMessages.get(i).isOnline = true;
            }
        }
        
        //For al mesages
        ArrayList<RoundPanel> toShow = new ArrayList<>();
        for (RoundPanel msg : panelMessages) {
            String username = msg.username; //receiver
            boolean isOnline = msg.isOnline;
            
            if(isOnline) {
                //For all selected users
                if (userList.size() > 0) {

                    int changed = 0;
                    for (String selUsername : userList) {

                        if (username.equals(parent.getUserName())) {
                            //Show your messages
                            toShow.add(msg);
                            changed++;
                        } else if (username.equals(selUsername)) {
                            //Show selected user messages
                            toShow.add(msg);
                            changed++;
                        }
                    }

                    if (changed == 0) {
                        msg.getParent().setVisible(false);
                    }

                } else {
                    //Show all messages if no selected users
                    msg.getParent().setVisible(true);
                }
            } else msg.getParent().setVisible(false);
        }

        for (RoundPanel comp : toShow) {
            comp.getParent().setVisible(true);
        }
        
        
    }

    /**
     * Resolve a string username to a UserListItem that represents the same user
     *
     * @param username Name of userListItem component to retreive.
     * @return
     */
    protected UserListItem getUserListItem(String username) {
        UserListItem userListItem = null;
        Component[] userList = ((MessagePanel) getParent()).getUserListPanel().getListPanel().getComponents();
        for (Component item : userList) {
            if (((UserListItem) item).getUsername().equals(username)) {
                //If it exists, return this...
                userListItem = (UserListItem) item;

                //Code should be this but I don't want to break anything
                //return (UserListItem) item;
            }
        }
        return userListItem;
    }

    /**
     * Method displays a specially formatted message that's added to the
     * messages panel. These cannot be hidden and are used to notify users of
     * important events.
     *
     * @param message Message to display as notification.
     */
    protected void addServerMessage(String message) {
        GridBagConstraints gbc = new GridBagConstraints();

        RoundPanel msgPanel;

        JLabel msgLabel = new JLabel();
        msgLabel.setFont(GLOBAL_MSG_FONT);
        msgLabel.setForeground(MSG_SERVER);
        msgLabel.setBorder(EMPTY_BORDER);

        msgLabel.setText(message);

        //Create timestamp of when I received the message
        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
        timeStamp += "\n";
            
        //Add time stamp
        JLabel timeLabel = new JLabel(timeStamp);
        timeLabel.setFont(GLOBAL_MSG_FONT);
        timeLabel.setForeground(GLOBAL_MSG_COLOR);
            
        msgPanel = new RoundPanel(ARC, new FlowLayout(FlowLayout.CENTER));
        msgPanel.setOpaque(false);

        msgLabel.setText(message);
        msgPanel.setBackground(GLOBAL_MSG_COLOR2);
        msgPanel.setForeground(GLOBAL_MSG_COLOR2);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
//        gbc.gridy = msgCount;
        gbc.insets = SERVER_MESSAGE_INSETS;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        msgPanel.add(timeLabel);
        msgPanel.add(msgLabel);
        msgsPanel.add(msgPanel, gbc, -1);
        //msgsPanel.add(msgPanel, gbc);

//        msgCount++;
        revalidate();
        repaint();
    }

    /**
     * Convert image bytes to a ImageIcon.
     *
     * @param imgBytes An image represented as a byte array.
     * @return ImageIcon made from imgBytes.
     */
    private MyImageIcon processImageBytes(byte[] imgBytes) {
        MyImageIcon icon = null;

        try {
            // convert byte array back to BufferedImage
            InputStream in = new ByteArrayInputStream(imgBytes);
            Image fullsize = ImageIO.read(in);

            //Save fullsize instance
            icon = new MyImageIcon();
            icon.fullSize = fullsize;

            //Scale image
            Image scaled;
            scaled = fullsize.getScaledInstance(45, -1, Image.SCALE_SMOOTH);
            icon.setImage(scaled);
        } catch (Exception e) {
            //Broken
        }

        return (MyImageIcon) icon;
    }

    /**
     * When an image in the chat is pressed a Frame is shown, this method
     * specifys this action. The Frame is used to display the full size version
     * of the image.
     *
     * @return Mouse listener object that does the above.
     */
    private MouseListener imageLabelListener() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String message = ((JTextArea) ((RoundPanel) e.getComponent().getParent()).getComponent(0)).getText();
                MyImageIcon icon = (MyImageIcon) ((JLabel) e.getComponent()).getIcon();
                JFrame frame = new JFrame(message);

                frame.setLayout(new FlowLayout(FlowLayout.CENTER));
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setIconImage(icon.getImage());
                frame.getContentPane().setBackground(MSG_ME_COLOR);

                JLabel label = new JLabel();
                label.setIcon(new ImageIcon(icon.fullSize));

                frame.add(label);
                frame.pack();
                frame.setVisible(true);
            }
        };
    }
}
