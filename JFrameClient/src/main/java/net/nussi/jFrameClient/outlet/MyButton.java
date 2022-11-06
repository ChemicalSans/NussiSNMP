package net.nussi.jFrameClient.outlet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class MyButton extends JButton implements MyButtonInterface, Runnable {
    private Thread thread;

    public MyButton() {
        this.setVisible(true);
//        this.setFont(new Font("Courier New", Font.PLAIN, 14));

        this.thread = new Thread(this);
        this.addActionListener(new MyActionListener(this));
    }

    public Thread getThread() {
        return thread;
    }

    private class MyActionListener implements ActionListener {
        MyButton button;

        MyActionListener(MyButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.button.thread.interrupt();
            this.button.thread = new Thread(button);
            this.button.thread.start();
        }
    }
}
