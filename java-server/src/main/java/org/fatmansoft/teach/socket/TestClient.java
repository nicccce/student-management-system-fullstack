package org.fatmansoft.teach.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TestClient {
	public static void main(String[] args) throws IOException {
		InetAddress addr = InetAddress.getByName("202.194.14.73");
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, 27772);
		try {
			int data;
			System.out.println("socket = " + socket);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			for (int i = 0; i < 10; i++) {
				System.out.print(i);
				out.println("?##0077QN=20220301083948000;ST=22;CN=3023;PW=100000;MN=02005300001000;Flag=5;CP=&&&&3480----"+i);
				out.flush();
				while(true) {
					data = in.read();
					if(data < 0)
						break;
					System.out.println(data);
				}
			}
		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}
}
