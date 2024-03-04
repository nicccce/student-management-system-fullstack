package org.fatmansoft.teach.thread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Hanoi extends JPanel  {
	Label times, prompt;
	TextField input;
	Button btn1;
	int n = 4, d, move_cnt = 0, abcn[] = new int[3];
	int abc[][];
	MutiThread thread;
	GraphPanel graphPanel = new GraphPanel();
	private class GraphPanel extends JPanel{
		public void paint(Graphics g) {
			super.paint(g);
			drawHanoi(g);
		}			
		
	}
	public void init() {
		initDisk();
		this.setLayout(new BorderLayout());
		JPanel p =  new JPanel(); 
		times = new Label("move count:" + "   ");
		prompt = new Label("Please input need plate number:");
		input = new TextField(6);
		input.setText(n + "");
		btn1 = new Button("start");
		p.add(prompt);
		p.add(input);
		p.add(btn1);
		p.add(times);
		btn1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				n = Integer.parseInt(input.getText());
				if (thread == null) {
					initDisk();
					thread = new MutiThread(Hanoi.this);
					thread.start();
				}
			}
		});
		this.add(p, BorderLayout.NORTH);
		this.add(graphPanel,BorderLayout.CENTER);
		thread = null;
	}

	void initDisk() {
		if (n < 1)
			n = 1;
		abc = new int[3][n];
		abcn[0] = n;
		abcn[1] = 0;
		abcn[2] = 0;
		for (int i = 0; i < n; i++) {
			abc[0][n - 1 - i] = i + 1;
		}
		move_cnt = 0;
	}

	public void drawHanoi(Graphics g) {
		int xo = 100, yo = 300, width = 200, dw = 250, h = 250, dh = 15, minw = 20, dd = 10;
		for (int i = 0; i < 3; i++) {
			g.drawLine(xo + i * dw - width / 2, yo, xo + i * dw + width / 2, yo);
			g.drawLine(xo + i * dw, yo, xo + i * dw, yo - h);
			for (int j = 0; j < abcn[i]; j++) {
				g.drawLine(xo + i * dw - (minw + abc[i][j] * dd), yo - dh
						* (j + 1), xo + i * dw + (minw + abc[i][j] * dd), yo
						- dh * (j + 1));
				g.drawString(abc[i][j] + "", xo + i * dw
						+ (minw + abc[i][j] * dd) + 5, yo - dh * (j + 1) + dh
						/ 2);
			}
		}
	}


	void hanoi(int n, int s1, int s2, int s3) {
		if (n == 1)
			moveone(s1, s3);
		else {
			hanoi(n - 1, s1, s3, s2);
			moveone(s1, s3);
			hanoi(n - 1, s2, s1, s3);
		}
	}

	void moveone(int from, int to) {
		abcn[from]--;
		abc[to][abcn[to]] = abc[from][abcn[from]];
		abcn[to]++;
		move_cnt++;
		times.setText("move count:" + move_cnt);
		repaint();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	public void reDraw(){
		graphPanel.repaint();
	}
}
