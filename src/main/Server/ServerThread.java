package Main.Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
	private Server server = null;
	private Socket sock = null;
	private int ID = -1;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	
	public ServerThread(Server server, Socket sock) {
		super();
		this.server = server;
		this.sock = sock;
		ID = this.sock.getPort();
	}
	
	public void send(String msg) throws IOException {
		streamOut.writeUTF(msg);
		streamOut.flush();
	}
	
	public int getID() {
		return ID;
	}
	
	public void run() {
		System.out.println("Server thread " + ID + " running...");
		while (true) {
			try {
				server.handle(ID, streamIn.readUTF());
			} catch (IOException e) {
				try {
					server.remove(ID);
				} catch (IOException | InterruptedException e2) {
					e2.printStackTrace();
				}
				try {
					join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
	}
	
	public void close() throws IOException {
		if (sock != null) {
			sock.close();
		}
		if (streamIn != null) {
			streamIn.close();
		}
		if (streamOut != null) {
			streamOut.close();
		}
	}
}
