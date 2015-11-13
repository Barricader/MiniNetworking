package Main.Server;

import java.io.IOException;

import Main.Server.Window.NewFrame;

// PORT: 64837
public class Main implements Runnable {
	private Server server;
	private NewFrame window;
	private boolean running;
	private Thread t;

	public Main() {
		try {
			server = new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
		window = new NewFrame();
		t = new Thread(this);
		t.start();
		running = true;
	}

	public static void main(String[] args) {
		// Init window stuff here
		Main m = new Main();
		m.getWindow().setVisible(true);
	}

	public void run() {
		while (running) {
			try {
				server.addThread(server.getServerSock().accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public NewFrame getWindow() {
		return window;
	}
}
