package Main.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server /*implements Runnable*/ {
	private ServerThread clients[] = new ServerThread[50];
	private ServerSocket serverSock = null;
	//private Thread t = null;
	private int clientCount = 0;
	
	public Server() throws IOException {
		serverSock = new ServerSocket(64837);
		System.out.println("Server started: " + serverSock);
		//start();
	}
	
//	public void run() {
//		while (t != null) {
//			try {
//				System.out.println("Waiting for client...");
//				addThread(serverSock.accept());
//			} catch (IOException e) {
//				e.printStackTrace();
//				break;
//			}
//		}
//	}
	
//	public void start() {
//		if (t == null) {
//			t = new Thread(this);
//			t.start();
//		}
//	}
//	
//	public void stop() throws InterruptedException {
//		if (t != null) {
//			t.join();
//			t = null;
//		}
//	}
	
	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].getID() == ID) {
				return i;
			}
		}

		return -1;
	}
	
	public synchronized void handle(int ID, String input) throws IOException, InterruptedException {
		if (input.equals("bye")) {
			clients[findClient(ID)].send("bye");
			remove(ID);
		}
		else {
			for (int i = 0; i < clientCount; i++) {
				clients[i].send(ID + ": " + input);
			}
		}
	}
	
	public synchronized void remove(int ID) throws IOException, InterruptedException {
		int pos = findClient(ID);
		if (pos >= 0) {
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			
			if (pos < clientCount - 1) {
				for (int i = pos + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
			}
			
			clientCount--;
			
			toTerminate.close();
			toTerminate.join();
		}
	}
	
	public void addThread(Socket sock) throws IOException {
		if (clientCount < clients.length) {
			System.out.println("Client accepted: " + sock);
			clients[clientCount] = new ServerThread(this, sock);
			
			clients[clientCount].open();
			clients[clientCount].start();
			clientCount++;
		}
		else {
			System.out.println("Client refused: max clients reached...");
		}
	}
	
	public ServerSocket getServerSock() {
		return serverSock;
	}
}
