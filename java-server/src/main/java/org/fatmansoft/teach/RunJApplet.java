package org.fatmansoft.teach;

import javax.swing.*;

public class RunJApplet {
    public static void run(JApplet c) {
        JFrame f = new JFrame();
        c.init();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(c);
        f.setBounds(300,300, 400,400);
        f.pack();
        f.setVisible(true);
    }

}
