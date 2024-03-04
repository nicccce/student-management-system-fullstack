package org.fatmansoft.teach.thread;


import org.fatmansoft.teach.RunJApplet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Counter2 extends JApplet {
	private class SeparateSubTask extends Thread {
		private int count = 0;
		private boolean runFlag = true;

		SeparateSubTask() {
			start();
		}

		void invertFlag() {
			runFlag = !runFlag;
		}

		public void run() {
			while (true) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					System.err.println("Interrupted");
				}
				if (runFlag)
					t.setText(Integer.toString(count++));
			}
		}
	}

	private SeparateSubTask sp = null;
	private JTextField t = new JTextField(10);
	private JButton start = new JButton("Start"),
			onOff = new JButton("Toggle");

	class StartL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (sp == null)
				sp = new SeparateSubTask();
		}
	}

	class OnOffL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (sp != null)
				sp.invertFlag();
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
		RunJApplet.run(new Counter2());
	}

}
