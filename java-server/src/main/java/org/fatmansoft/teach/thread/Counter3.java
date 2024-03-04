package org.fatmansoft.teach.thread;

import org.fatmansoft.teach.RunJApplet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Counter3 extends JApplet implements Runnable {
	private int count = 0;
	private boolean runFlag = true;
	private Thread selfThread = null;
	private JButton start = new JButton("Start"),
			onOff = new JButton("Toggle");
	private JTextField t = new JTextField(10);

	public void run() {
		while (true) {
			try {
				selfThread.sleep(100);
			} catch (InterruptedException e) {
				System.err.println("Interrupted");
			}
			if (runFlag)
				t.setText(Integer.toString(count++));
		}
	}

	class StartL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (selfThread == null) {
				selfThread = new Thread(Counter3.this);
				selfThread.start();
				// ִ��Counter3.this��run����
			}
		}
	}

	class OnOffL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			runFlag = !runFlag;
		}
	}

	public void init() {
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(t);
		start.addActionListener(new StartL());
		cp.add(start);
		onOff.addActionListener(new OnOffL());
		cp.add(onOff);
	}
	public static void main(String args[]) {
		RunJApplet.run(new Counter3());
	}

}
