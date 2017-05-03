package UILayer;


import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.swing.*;



import java.awt.*;

public class ChatClient {
	
	JTextArea chatTextArea;//������Ϣ�ı���
	JTextField sendArea;//��Ϣ������ 
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	static int userID;
	
	public static void main(String[] args){
		ChatClient client = new ChatClient();
		userID = (int)(Math.random()*10000000);
		client.go();
	}
	
	public void go(){
		JFrame frame = new JFrame("This is a simple ChatClient_V1.0");
		JPanel mainpanel = new JPanel();
		//��������ʼ��
		chatTextArea = new JTextArea(15,50);//15��50����
		chatTextArea.setLineWrap(true);//�����Զ�����
		chatTextArea.setEditable(false);//���ɱ༭
			//����Ϊ�ɴ�ֱ������
		JScrollPane qScroller = new JScrollPane(chatTextArea);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//��Ϣ��������ʼ��
		sendArea = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		
		mainpanel.add(qScroller);
		mainpanel.add(BorderLayout.SOUTH,sendArea);
		mainpanel.add(BorderLayout.SOUTH,sendButton);
		
		 setUpNetworking();
		
		//�����̳߳����ػ�ȡ������Ϣ
		Thread  readerThread  =  new Thread(new ChatTextReader());
		readerThread.start();
		
		//��ʾ����
		frame.getContentPane().add(BorderLayout.CENTER,mainpanel);
		frame.setSize(800, 400);
		frame.setVisible(true);	
		
	}

	public class SendButtonListener implements ActionListener{
		//����actionPerformed
		public void actionPerformed(ActionEvent ev){
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String IDandTime = userID + "    " + dateFormat.format(new Date());
				writer.println(IDandTime);
				writer.println(sendArea.getText());//��ȡ���������ֶ�
				writer.flush();	//����������������ݣ���ʹδ�������͵����������
			} catch (Exception e) {
				e.printStackTrace();
			}
			sendArea.setText("");
			sendArea.requestFocus();//�����ʾ�ڱ༭����
		}
	}
	
	private void setUpNetworking() {//�ڲ���
		try {
			sock = new Socket("127.0.0.1",50000);//��������Ϊ����
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());//��ȡ���봮�������ͻ��ˣ�
			reader = new BufferedReader(streamReader);//Buffer�������봮��
			writer = new PrintWriter(sock.getOutputStream());//��ȡ�������������������
			System.out.println("networking established");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

		
	public class ChatTextReader implements Runnable {
		public void run(){
			String message;
			try {
				while((message = reader.readLine())!= null){
					System.out.println("read:"+message);
					chatTextArea.append(message+"\n");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}