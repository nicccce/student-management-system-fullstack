package org.fatmansoft.teach.socket;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//@Component
//@Order(1)
public class MultiJabberServer implements ApplicationListener<ApplicationReadyEvent> {
	static final int PORT = 7777;
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			ServerSocket s = new ServerSocket(PORT);
			System.out.println("Server Started");
			try {
				while (true) {
					Socket socket = s.accept();
					try {
						new ServeOneJabber(socket);
					} catch (IOException e) {
						socket.close();
					}
				}
			} finally {
				s.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
