package cook.sys.ftd.messenger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server(){
		super("MV and Cedrics Chat Server"); // creates constructor
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener( 
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
				);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300, 150);
		setVisible(true);
		
	}
	public void strartRunning(){ //setup and runs this damn server
		try{
			server = new ServerSocket(667, 50);
			while(true){
				try{
					waitForConnection();
					setupStreams();
					whileChatting();
				}
				catch(EOFException eofException){
					showMessage("\n Oh Damn your connected!");
				}finally{
					closeShit();
				}
			} 
		} catch(IOException ioException){
			ioException.printStackTrace();
		}
		
	}
	
	private void waitForConnection() throws IOException{ //waits for connection and displays connection info
	showMessage(" Be patient for someone to connect...\n");
	connection = server.accept();
	showMessage("Your finally connected to " + connection.getInetAddress().getHostName());
	
	}
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n YO these streams are now setup! \n");
		
	}
		private void whileChatting() throws IOException{
			String message = "You are now connected!\n";
			sendMessage(message);
			ableToType(true);
			do{
				try{
					message = (String) input.readObject();
					showMessage("\n" + message);
				}catch(ClassNotFoundException classNotFoundException){
					showMessage("\n WTF i have no clue what that user sent!");
					
				}
			}while(!message.equals("CLIENT - END"));
		}
		private void closeShit() {
			showMessage("\n Closing Connection... \n");
			ableToType(false);
			try{
				output.close();
				input.close();
				connection.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
		private void sendMessage(String message){
			try{
				output.writeObject("\nSERVER - " + message);
				output.flush();
			}catch(IOException ioException){
				chatWindow.append("\n ERROR: YO YOU CANT SEND THAT MESSAGE!!");
			}
		}
		private void showMessage(final String text){ // updates chat window
			SwingUtilities.invokeLater( 
					new Runnable(){
						public void run(){
							chatWindow.append(text);
						}
					}
					);
		}
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater( 
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}
				);
	}

}
