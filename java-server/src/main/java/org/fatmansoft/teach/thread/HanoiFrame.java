package org.fatmansoft.teach.thread;

import javax.swing.*;
import java.awt.*;

public class HanoiFrame extends JFrame {
	public HanoiFrame(String title){
		super(title);
		Hanoi hanoi;
		hanoi = new Hanoi();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(hanoi,BorderLayout.CENTER);
		hanoi.init();
		this.setLocation(50,100);
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public static void main(String args[]){
		JFrame f = new JFrame("hanoi-1");
		Hanoi hanoi;
		hanoi = new Hanoi();
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(hanoi,BorderLayout.CENTER);
		hanoi.init();
		f.setLocation(700,100);
		f.setSize(600, 600);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
