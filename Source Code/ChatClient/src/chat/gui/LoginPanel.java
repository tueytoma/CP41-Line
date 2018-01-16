package chat.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Login panel for ClientGUI
 *
 * @author DisSys_Discard
 */
public class LoginPanel extends JPanel {

    /**
     *
     */
    public ClientGUI parent;

    private JLabel loginErrorLabel;
    private RoundPanel loginButton;
    private JTextField loginTextField;
    private JTextField addressTextField;
    private JTextField portTextField;
    private JPanel container;
    private JLabel logo;
    private JComboBox group;
    
    private boolean groupA;

    private final int COMPONENT_WIDTH = 200;
    private final int IMAGE_SCALE = 4;
    private final Color BUTTON_COLOR = new Color(0, 160, 0);
    private final Color BUTTON_TEXT_COLOR = new Color(255, 255, 255);
    private final Color ERROR_TEXT_COLOR = new Color(255, 255, 255);
    private final Color BACKGROUND_COLOR = new Color(0, 194, 0);
    private final Dimension BUTTON_SIZE = new Dimension(COMPONENT_WIDTH, 35);
    private final Dimension BUTTON_SIZE2 = new Dimension(COMPONENT_WIDTH - 75, 25);
    private final Dimension BUTTON_ARC = new Dimension(35, 35);
    private final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font DEFAULT_FONT2 = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private final Insets TEXT_FIELD_INSETS = new Insets(5, 8, 5, 8);
    private final Insets LAYOUT_INSETS = new Insets(10, 0, 10, 0);
    private final String LOGIN_BUTTON_TEXT = "Login";
    private final String LOGIN_ERROR_TEXT = "Login failed!";

    /**
     *
     * @param parent
     */
    public LoginPanel(JFrame parent) {
        this.parent = (ClientGUI) parent;

        init();
        addListeners();
    }

    /**
     * Init GUI components of this panel.
     */
    private void init() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = LAYOUT_INSETS;

        setLayout(gridbag);
        setBackground(BACKGROUND_COLOR);


        //Error message label
        loginErrorLabel = new JLabel(LOGIN_ERROR_TEXT);
        loginErrorLabel.setFont(DEFAULT_FONT);
        loginErrorLabel.setForeground(ERROR_TEXT_COLOR);
        loginErrorLabel.setVisible(false);

        //Get and set image icon to display.
        try {
            URL url = getClass().getResource(ClientGUI.IMAGE_LOC3);
            Image img = Toolkit.getDefaultToolkit().getImage(url);

            //Image img = ImageIO.read(new File(IMAGE_LOC));
            img = img.getScaledInstance(
                    150, -1, Image.SCALE_SMOOTH);
            logo = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        logo.setToolTipText("You can click for watch \"About\"");
        
        //logo = new JLabel(new ImageIcon(ClientGUI.IMAGE_LOC));

        //Login button
        loginButton = new RoundPanel(BUTTON_ARC, new GridBagLayout());
        loginButton.setOpaque(false);
        loginButton.setForeground(BUTTON_COLOR);
        loginButton.setBackground(BUTTON_COLOR);
        JLabel jb = new JLabel(LOGIN_BUTTON_TEXT);
        jb.setFont(DEFAULT_FONT);
        jb.setForeground(BUTTON_TEXT_COLOR);
        loginButton.setPreferredSize(BUTTON_SIZE);
        loginButton.add(jb);

        //Username text field
        loginTextField = new JTextField("Username");
        loginTextField.setFont(DEFAULT_FONT);
        loginTextField.setPreferredSize(BUTTON_SIZE);
        loginTextField.setHorizontalAlignment(JTextField.CENTER);
        EmptyBorder eb = new EmptyBorder(TEXT_FIELD_INSETS);
        loginTextField.setBorder(eb);
        loginTextField.addFocusListener(new FocusAdapter(){
        @Override
            public void focusGained(FocusEvent fEvt) {
                JTextField component = (JTextField) fEvt.getSource();
                component.selectAll();
            }
        });  
        
        //Address text field
        addressTextField = new JTextField("localhost");
        addressTextField.setEditable(false);
        addressTextField.setEnabled(false);
        addressTextField.setBackground(BUTTON_COLOR);
        addressTextField.setForeground(BUTTON_COLOR);
        addressTextField.setFont(DEFAULT_FONT2);
        addressTextField.setPreferredSize(BUTTON_SIZE2);
        addressTextField.setHorizontalAlignment(JTextField.CENTER);
        EmptyBorder eb2 = new EmptyBorder(TEXT_FIELD_INSETS);
        addressTextField.setBorder(eb2);
        addressTextField.addFocusListener(new FocusAdapter(){
        @Override
            public void focusGained(FocusEvent fEvt) {
                JTextField component = (JTextField) fEvt.getSource();
                component.selectAll();
            }
        });  
        
        //Port text field
        portTextField = new JTextField("28081");
        portTextField.setEditable(false);
        portTextField.setBackground(BUTTON_COLOR);
        portTextField.setEnabled(false);
        portTextField.setFont(DEFAULT_FONT2);
        portTextField.setPreferredSize(BUTTON_SIZE2);
        portTextField.setHorizontalAlignment(JTextField.CENTER);
        EmptyBorder eb3 = new EmptyBorder(TEXT_FIELD_INSETS);
        portTextField.setBorder(eb3);
        portTextField.addFocusListener(new FocusAdapter(){
        @Override
            public void focusGained(FocusEvent fEvt) {
                JTextField component = (JTextField) fEvt.getSource();
                component.selectAll();
            }
        });  
        
        //Select Group
        String[] gr = {"Group A", "Group B"};
        group = new JComboBox(gr);
        group.setFont(DEFAULT_FONT2);
        
        
        container = new JPanel();
        container.setBackground(BACKGROUND_COLOR);
        container.setForeground(BACKGROUND_COLOR);
        container.add(addressTextField);
        container.add(portTextField);
        container.add(group);
        container.setBorder(null);

        c.gridx = 1;
        c.gridy = 0;
        add(logo, c);
        

        c.gridx = 1;
        c.gridy = 1;
        add(loginTextField, c);
        
        c.gridx = 1;
        c.gridy = 2;
        add(container, c);

        c.gridx = 1;
        c.gridy = 5;
        add(loginButton, c);

        c.gridx = 1;
        c.gridy = 6;
        add(loginErrorLabel, c);
    }

    private void addListeners() {
        //User pressed enter in the username textfield, try to login
        loginTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    String selectedBook = (String) group.getSelectedItem();
                    if (selectedBook.equals("Group A")) {
                        groupA = true;
                    } else if (selectedBook.equals("Group B")) {
                        groupA = false;
                    }
                    attemptLogin();
                }
            }
            

        });

        //User pressed the login button, try to login.
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String selectedBook = (String) group.getSelectedItem();
                if (selectedBook.equals("Group A")) {
                    groupA = true;
                } else if (selectedBook.equals("Group B")) {
                    groupA = false;
                }
                attemptLogin();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                //loginButton.setPreferredSize();
                //loginButton.setSize(new Dimension(150, 27));
                loginButton.setForeground(new Color(255,255,255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                //loginButton.setPreferredSize();
                //loginButton.setSize(new Dimension(150, 27));
                loginButton.setForeground(BUTTON_COLOR);
            }
        });
        
        logo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Image img;
                //open Credit
                String message = "Credit - developed by: DisSys_Discard";
                JFrame frame = new JFrame(message);
                frame.setPreferredSize(new Dimension(800,600));
                
                try {
                    URL url = getClass().getResource(ClientGUI.IMAGE_LOC);
                    img = Toolkit.getDefaultToolkit().getImage(url);
                    frame.setIconImage(img);
                    url = getClass().getResource(ClientGUI.IMAGE_LOC4);
                    img = Toolkit.getDefaultToolkit().getImage(url);

                    //Image img = ImageIO.read(new File(IMAGE_LOC));
                    
                    img = img.getScaledInstance(
                        -1, -1, Image.SCALE_SMOOTH);
                    logo = new JLabel(new ImageIcon(img));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                frame.setLayout(new FlowLayout(FlowLayout.CENTER));
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.add(logo);

                frame.pack();
                frame.setVisible(true);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                //nothing
            }
            @Override
            public void mouseExited(MouseEvent e) {
                //nothing
            }
        });
                
    }

    /**
     * Method tries to login to the server and handles exceptions. If login is
     * successful display the Messaging panel so the user can send message,
     * otherwise the login panel is shown again.
     */
    public void attemptLogin() {
        try {
            if (parent.login(loginTextField.getText(), groupA)) {
                loginErrorLabel.setVisible(false);
            } else {
                loginErrorLabel.setVisible(true);
                loginErrorLabel.setText(LOGIN_ERROR_TEXT);
                loginButton.setForeground(ERROR_TEXT_COLOR);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            loginErrorLabel.setVisible(true);
            loginErrorLabel.setText("...Connection Error...");
            loginErrorLabel.setForeground(Color.RED);
            System.out.println(getClass() + ".attemptLogin():ConnectionError");
        }
    }
}
