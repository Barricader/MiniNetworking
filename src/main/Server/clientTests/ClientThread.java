package main.server.clientTests;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
	private Socket sock = null;
	private Client client = null;
	private DataInputStream streamIn = null;
	
	public ClientThread(Client client, Socket sock) throws IOException {
		this.client = client;
		this.sock = sock;
		open();
		start();
	}
	
	public void open() throws IOException {
		streamIn = new DataInputStream(sock.getInputStream());
	}
	
	public void close() throws IOException {
		if (streamIn != null) {
			streamIn.close();
		}
	}
	
	public void run() {
		while (true) {
			try {
				client.handle(streamIn.readUTF());
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				client.stop();
			}
		}
	}
}
