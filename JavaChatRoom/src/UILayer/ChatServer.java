package UILayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;


public class ChatServer {

	ArrayList<PrintWriter> clientOutputStream;
	
	public class ClientHander implements Runnable{
		BufferedReader reader;
		Socket sock;
		public ClientHander(Socket clientSocket){
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void run(){
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read:" + message);
					tellEveryone(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}//�ڲ���ClientHander
	
	public static void main(String[] args){
		new ChatServer().go();
	}
	
	public void go(){
		clientOutputStream = new ArrayList<PrintWriter>();
		try {
			ServerSocket serverSocket = new ServerSocket(50000);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStream.add(writer);
				
				Thread t= new Thread(new ClientHander(clientSocket));
				t.start();
				System.out.println("got a connection");
			//	serverSocket.close();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tellEveryone(String message) {
		Iterator<PrintWriter> it = clientOutputStream.iterator();
		while (it.hasNext()) {
			try {
				PrintWriter writer = it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}