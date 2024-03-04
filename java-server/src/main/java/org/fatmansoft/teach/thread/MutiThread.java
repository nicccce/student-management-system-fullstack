package org.fatmansoft.teach.thread;


public class MutiThread extends Thread {
	Hanoi m_Parent;

	MutiThread(Hanoi p) {
		m_Parent = p;
	}

	public void run() {
		Hanoi p = (Hanoi) m_Parent;
		p.reDraw();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		p.hanoi(p.n, 0, 1, 2);
		p.thread = null;
	}
}
