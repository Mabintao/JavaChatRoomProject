package UILayer;

/*
 * �ܽ����
 * 2017-05-03 00:40:54
 */
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.*;

import BLLayer.UsersBL;
import BLLayer.UsersEntity;

import java.awt.*;

public class ChatFrame extends JFrame {//ChatFrame��һ��JFrame
	
	//��ȡ��Ļ�ߴ�
	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();
	int screenHeight = screenSize.height;
	int screenWidth = screenSize.width;
	private static final long serialVersionUID = 1L;
	private static final int DEFAULTWIDTH = 800;
	private static final int DEFAULTHEIGHT = 400;
	//������Ϣ
	private JLabel nameLable;
	private JComboBox<Object> statusCombo;
	private UsersEntity user = null;
	JTextArea chatTextArea;//������Ϣ������
	JTextField sendArea;//������Ϣ������ 
	JButton sendButton ;
	Thread  readerThread ;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public ChatFrame(){//��ʼ���ͻ��˴��ڽ���
		setTitle("This is a simple ChatClient_V2.0");
		setSize(DEFAULTWIDTH, DEFAULTHEIGHT);
		setLocation((screenWidth - DEFAULTWIDTH) / 2, (screenHeight - DEFAULTHEIGHT) / 2);
		setJMenuBar(initMenuBar());//�˵�����ʼ��
		JPanel northPanel = initNorthPanel(); //������Ϣ��ʾ����ʼ��
		JPanel mainPanel = initMainPanel();//��������ʼ��
		JPanel southPanel = initSouthPanel();//��Ϣ��������ʼ��
		//������������
		getContentPane().add(BorderLayout.NORTH,northPanel );
		getContentPane().add(BorderLayout.CENTER,mainPanel);
		getContentPane().add(BorderLayout.SOUTH,southPanel);
		//�ж��û��Ƿ��½
		if(user == null){
			statusCombo.setEnabled(false);//����״̬
			chatFrameLogin();
		}
	}

	/*
	 * ��½
	 */
	private void chatFrameLogin(){
		if (user!= null) {
			JOptionPane.showMessageDialog(null, "Please logout first", "Login failed", JOptionPane.ERROR_MESSAGE);
		}else{
			LoginUI loginUI =new LoginUI();
			//��½�ɹ�
			if(loginUI.showDialog(ChatFrame.this, "Login")){//���븸���ڣ������࣬
				setUpNetworking();
				//�����̳߳����ػ�ȡ������Ϣ
				readerThread  =  new Thread(new ChatTextReader());
				readerThread.start();
				user = loginUI.getUser();
				nameLable.setText(user.getName());
				statusCombo.setEnabled(true);//���״̬
				sendButton.setEnabled(true);//������Ͱ�ť
				user.setStatus(1);
				new UsersBL().setOnline(user.getID());
				statusCombo.setSelectedIndex(1);//����Ϊ����״̬	
				
			}
		}
	}
	/*
	 * �û�ע��
	 */
	private void chatFrameSignup(){
		SignupUI signupUI =new SignupUI();
		//��½�ɹ�
		if(signupUI.showDialog(ChatFrame.this, "Sign Up")){//���븸���ڣ������࣬
			 chatFrameLogin();
		}
	}
	/*
	 * �û��ǳ�
	 */
	@SuppressWarnings("deprecation")
	private void ChatFrameLogout()  {
		if(user == null){
			JOptionPane.showMessageDialog(null, "Please login first", "Logout failed", JOptionPane.ERROR_MESSAGE);
		}else{
			nameLable.setText("UnLogin");
			statusCombo.setEnabled(false);
			sendButton.setEnabled(false); 
			tellServerOffline();
			user.setStatus(0);
			new UsersBL().setOffline(user.getID());
			user = null;
			readerThread.stop();
			try {
				sock.close();
				reader.close();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	/*
	 * �رտͻ���
	 */
	private void ChatFrameExit() {
		if (user != null) {
			ChatFrameLogout();	
		}
		System.exit(0);
	}
	/*
	 * ���ӷ�����
	 */
	private void setUpNetworking() {
		try {
			sock = new Socket("127.0.0.1",50000);//��������Ϊ����
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());//��ȡ���봮�������ͻ��ˣ�
			reader = new BufferedReader(streamReader);//Buffer�������봮��
			writer = new PrintWriter(sock.getOutputStream());//��ȡ�������������������
			System.out.println("Networking Established");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * �߳����񣺽�����Ϣ������������
	 */
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

	/*
	 * ������Ϣ��������˵������
	 */
	 private void tellServerOffline() {
		 if(user.getStatus() == 1){
            String message = user.getName() + "�뿪��������";
            writer.println(message);
            writer.flush();
		 }
    }
	 /*
	  * 
	  */
	 private void tellServerOnline() {
		 String message = "��ӭ"+user.getName()+"���������ң�";
		 writer.println(message);
		 writer.flush();
	 }
	/*
	 * �˵�����ʼ�� 
	 */
	private JMenuBar initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);
		JMenuItem loginItem = new JMenuItem("Login");
		JMenuItem signUpItem = new JMenuItem("Sign Up");
		JMenuItem logoutItem = new JMenuItem("Logout");
		JMenuItem exitItem = new JMenuItem("Exit");
		menu.add(loginItem);
		menu.add(logoutItem);
		menu.add(signUpItem);
		menu.add(exitItem);
		loginItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatFrameLogin();
			}
		});	 
		signUpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatFrameSignup();
			}
		});	 
		logoutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatFrameLogout();
			}
		});	
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatFrameExit();
			}
		});	
		return menuBar;
	}  
	/*
	 * ������Ϣ��ʾ����ʼ�� 
	 */
	private JPanel initNorthPanel() {
		JPanel northPanel = new JPanel();
		JLabel userLable = new JLabel("User��");
        nameLable = new JLabel("UnLogin");
    	JLabel statusLable = new JLabel("Status��");
        statusCombo = new JComboBox<>();
        statusCombo.addItem("Offline");
        statusCombo.addItem("Online");
        statusCombo.addItem("Hide");
      //״̬�ı��¼�
        statusCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int s = statusCombo.getSelectedIndex();
                if (s == 0) {
                	tellServerOffline();
                	user.setStatus(0);
                	new UsersBL().setOffline(user.getID());
                } else if (s == 1) {
                    new UsersBL().setOnline(user.getID());
                    tellServerOnline();
                    user.setStatus(1);
                } else {
                	tellServerOffline();
                	user.setStatus(2);
                    new UsersBL().setHide(user.getID());
                  
                }
            }
        });
        northPanel.add(userLable);
        northPanel.add(nameLable);
        northPanel.add(statusLable);
        northPanel.add(statusCombo);
        return northPanel;
	}
	/*
	 * ��������ʼ�� 
	 */
	private JPanel initMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		chatTextArea = new JTextArea();
		chatTextArea.setLineWrap(true);//�����Զ�����
		chatTextArea.setEditable(false);//���ɱ༭
		//����Ϊ�ɴ�ֱ������
		JScrollPane qScroller = new JScrollPane(chatTextArea);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(qScroller,BorderLayout.CENTER);
		return mainPanel;
	}
	/*
	 * ��Ϣ��������ʼ�� 
	 */
	private JPanel initSouthPanel() {
		JPanel southPanel = new JPanel();
		sendArea = new JTextField(20);
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {//��SEND��ť����¼�����
			public void actionPerformed(ActionEvent ev){
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String IDandTime = user.getName()+ "    " + dateFormat.format(new Date());
					writer.println(IDandTime);
					writer.println(sendArea.getText());//��ȡ���������ֶ�
					writer.flush();	//����������������ݣ���ʹδ�������͵����������
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendArea.setText("");
				sendArea.requestFocus();//�����ʾ�ڱ༭����
			}
		});
		southPanel.add(sendArea);
		southPanel.add(sendButton);
		return southPanel;
	}
}
