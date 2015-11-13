package main.Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow extends JFrame {

	private JTextArea textArea;
	private JTextField msgField;
	private JButton sendBtn;
	
	private Client client;
	private JPanel contentPane;
	private RectPanel rectPanel;	// panel that will contain rectangle interaction
	
	public ClientWindow() {
		init();
		createClient();
		setSize(new Dimension(1280, 720));
		setTitle("Client Demo");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void createComponents() {
		contentPane = new JPanel();
		rectPanel = new RectPanel();
		textArea = new JTextArea();
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.CYAN);
		textArea.setEditable(false);
		
		msgField = new JTextField(10);
		msgField.setBorder(new LineBorder(Color.CYAN));
		msgField.setBackground(Color.BLACK);
		msgField.setForeground(Color.CYAN);
		msgField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					sendMessage();
				}
			}
			
			// unused
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			
		});
		
		
		sendBtn = new JButton("Send");
		sendBtn.setBorder(new LineBorder(Color.CYAN));
		sendBtn.setBackground(Color.BLACK);
		sendBtn.setForeground(Color.CYAN);
		sendBtn.addActionListener(e -> {
			sendMessage();
		});
	}
	
	private void init() {
		createComponents();
		
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// text area
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		contentPane.add(textArea, c);
		
		// msg text field
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.ipady = 20;
		contentPane.add(msgField, c);
		
		// send button
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 0.2;
		c.weighty = 0.0;
		c.ipady = 20;
		contentPane.add(sendBtn, c);
		
		// rect panel
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 1.0;
		c.ipady = 0;
		contentPane.add(rectPanel, c);
		
		add(contentPane);	// add to main client window
	}
	
	private void sendMessage() {
		String text = msgField.getText();
		if (!text.isEmpty()) {
			if (parseCommand(text)) {
				// send basic text message
				System.out.println("Attempting to send msg: " + text);
				String msgArea = textArea.getText();
				msgArea += text + "\n";
				textArea.setText(msgArea);
				msgField.setText("");	// clear out
				// send to client
				client.send(text.getBytes());
			}
		}
	}
	
	private boolean parseCommand(String text) {
		if (text.equalsIgnoreCase("cls")) {
			textArea.setText("");
			msgField.setText("");
			return false;
		} else if (text.startsWith("moveRect")) {
//			Rectangle r = rectPanel.getRect();
			rectPanel.getRect().x += 5;
			rectPanel.getRect().y += 10;
			rectPanel.repaint();
		}
		return true;
	}
	
	private void createClient() {
		client = new Client("test", "localhost");
	}
	
	public static void main(String[] args) {
		ClientWindow w = new ClientWindow();
	}
}
