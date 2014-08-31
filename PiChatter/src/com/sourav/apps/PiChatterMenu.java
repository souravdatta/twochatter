package com.sourav.apps;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author sourdatt
 */
public class PiChatterMenu {
    private SystemTray sysTray;
    private TrayIcon trayIcon;
    final private Image icon;
    final private Image iconAttn;
    final private MenuItem open;
    final private MenuItem exit;
    final private MenuItem info;
    final private UserBean bean;
    final private PiMessageFetcher msgf;
    
    public static final String TT_NORMAL = "PiChatter 0.2";
    private static final String TT_NOTIFY = "You have new messages!";
    
    public PiChatterMenu(UserBean bean) {
        this.bean = bean;
        sysTray = SystemTray.getSystemTray();
        icon = (new ImageIcon(PiChatterMenu.class.getResource("icon.gif"))).getImage();
        iconAttn = (new ImageIcon(PiChatterMenu.class.getResource("attention.gif"))).getImage();
        trayIcon = new TrayIcon(icon, TT_NORMAL);
        
        PopupMenu pmenu = new PopupMenu();
        open = new MenuItem("Open");
        exit = new MenuItem("Exit");
        info = new MenuItem("Info");
        
        final PiChatterMenu that = this;
        
        pmenu.add(info);
        pmenu.add(open);
        pmenu.addSeparator();
        pmenu.add(exit);
    
        trayIcon.setPopupMenu(pmenu);
        try {
            sysTray.add(trayIcon);
        } catch (AWTException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        
        msgf = new PiMessageFetcher(this, bean);
        msgf.start();
        
        open.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openChatWindow();
            }
        });
        
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sysTray.remove(trayIcon);
                msgf.stop();
                System.exit(0);
            }
        });
        
        info.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Your are: " + that.bean.getName());
            }
        });
        
        trayIcon.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openChatWindow();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        trayIcon.displayMessage("PiChatter is now running", null, TrayIcon.MessageType.INFO);
    }
    
    public synchronized void setNotification() {
        if (!PiChatterWindow.getWindow().isAlreadyOpen()) {
            trayIcon.setImage(iconAttn);
            trayIcon.setToolTip(TT_NOTIFY);
            trayIcon.displayMessage("New messages!", "", TrayIcon.MessageType.INFO);
        }
    }
    
    public synchronized void hideNotification() {
        trayIcon.setImage(icon);
        trayIcon.setToolTip(TT_NORMAL);
    }
    
    private synchronized void openChatWindow() {
        PiChatterWindow window = PiChatterWindow.getWindow();
        window.open();
        hideNotification();
    }
}
