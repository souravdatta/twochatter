
package com.sourav.apps;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author sourdatt
 */
public class PiChatter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        final UserBean bean = new UserBean();
        PiChatterLoginWindow login = new PiChatterLoginWindow(bean);
        login.setVisible(true);
        
        /* validation!!! */
        if ((bean.getName() == null) || (bean.getName().length() <= 0)) {
            System.exit(1);
        }
            
        PiChatterWindow.getWindow().setUserBean(bean);
        
        SwingUtilities.invokeAndWait(new Runnable () {
            @Override
            public void run() {
                new PiChatterMenu(bean);
            }
        });
    }
    
}
