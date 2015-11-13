package Main.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server wrapper
 * @author JoJones
 */
public class Server implements Runnable {
	private static int PORT = 64837;
	private ServerThread clients[] = new ServerThread[8];
	private ServerSocket server = null;
	private Thread t = null;
	private int clientCount = 0;
	
	public Server() throws IOException {
		server = new ServerSocket(PORT);
		System.out.println("Server started: " + server);
		t = new Thread(this);
		t.start();
	}
	
	/**
	 * Server loop, listens for clients and accepts them automatically
	 */
	public void run() {
		while (t != null) {
			try {
				System.out.println("Waiting for client...");
				addThread(server.accept());
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	/**
	 * Kills server
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public void stop() throws InterruptedException, IOException {
		if (t != null) {
			t.join();
			t = null;
			for (int i = 0; i < clients.length; i++) {
				clients[i].close();
			}
			server.close();
		}
	}
	
	/**
	 * Finds client by an ID
	 * @param ID - Uses this ID to search for a client
	 * @return
	 */
	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].getID() == ID) {
				return i;
			}
		}

		return -1;
	}
	
	/**
	 * Handles input from client
	 * @param ID - ID of client
	 * @param input - String of text that they want to input
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public synchronized void handle(int ID, String input) throws IOException, InterruptedException {
		// If a client types 'bye', they exit from the server
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
	
	/**
	 * Removes a client
	 * @param ID - Client ID to remove
	 * @throws IOException
	 * @throws InterruptedException
	 */
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
	
	/**
	 * Add a thread for each client
	 * @param sock - Socket of incoming client
	 * @throws IOException
	 */
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
	
	public static void main(String[] args) throws IOException {
		Server s = new Server();
	}
}
