package main.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client implements Runnable {

	private static final int port = 64837;
	private String name;
	private String address;
	private boolean running = false;
	private Thread run;
	private Thread send;
	private Thread receive;
	private DatagramSocket socket;
	private InetAddress ip;
	
	
	public Client(String name, String address) {
		this.name = name;
		this.address = address;
		
		// try to connect to server
		boolean connect = openConnection(address);
		if (connect) {
			System.out.println("Client connection successful!");
		} else {
			System.out.println("Client connection failed!");
		}
		run = new Thread(this, "Client");
		run.start();
	}
	
	public void run() {
		running = true;
		while (running) {
			
		}
	}
	
	public boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void send(byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
					System.out.println("Client attempting to send packet to server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public void receive() {
		receive = new Thread() {
			public void run() {
				System.out.println("Listening to receive packets!");
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				
				try {
					socket.receive(packet);
					String str = new String(packet.getData());
					System.out.println("Client received packet: " + packet);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Client failed to receive packet! :(");
				}
			}
		};
		receive.start();
	}
}
