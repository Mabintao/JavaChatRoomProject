package UILayer;

import BLLayer.UsersBL;
import BLLayer.UsersEntity;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/*
 * �û���¼����
 * 2017-05-03 16:39:20
 */
//��¼������һ��(Login)Panel��������ChatFrame�����
public class LoginUI extends JPanel{
	private static final long serialVersionUID = 6329954484003010244L;//�Զ����ɵİ汾��
	private static final int DEFAULTWIDTH = 240;
	private static final int DEFAULTHEIGHT = 150;
	private JTextField username;//����û����������
	private JPasswordField password;//�������������
	private JDialog dialog;//�Ի�����
	private JButton loginButton;
	
	private UsersEntity user;//null��½ʧ��
	private boolean ok;

	
	public LoginUI(){
		//������С����
		JLabel welcome = new JLabel("Welcome to ChatRoom!");
		Font font= new Font("Welcome", Font.HANGING_BASELINE, 20);
		welcome.setFont(font);
		//����Panelװ���˺š�����������
		JPanel panel = new JPanel();
		panel.setSize(200,150);
		LayoutManager panelMgr = new GridLayout(2, 2);
		panel.setLayout(panelMgr);
		panel.add(new JLabel("      User name:"));
		panel.add(username = new JTextField());
		panel.add(new JLabel("       Password:"));
		panel.add(password = new JPasswordField());
		panel.setOpaque(false);//���ÿؼ�͸��;���Ϊ true��������������߽��ڵ��������ء�
		//��¼��ť
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {//��¼��Ӧ
			public void actionPerformed(ActionEvent event) {
				String tname = username.getText();
				String tpassword = new String(password.getPassword());//char[]ת����String
				//��½��֤
				user = null;
				try {
					user = new UsersBL().loginCheckOut(tname,tpassword);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unknown error!Please try again" ,"Login failed", JOptionPane.ERROR_MESSAGE);
				}	
				if(user!=null){//��½�ɹ�������û���Ϣ
					ok = true;
					dialog.setVisible(false);//�رնԻ���
				}else{
					JOptionPane.showMessageDialog(null,"Invalid username/password!" ,"Login failed", JOptionPane.ERROR_MESSAGE);
					ok = false;
					dialog.setVisible(true);
				}
			}
		});
		//ע�ᰴť
		JButton signUpButton = new JButton("SignUp");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				 SignupUI signupUI = new SignupUI();
				 signupUI.showDialog(LoginUI.this, "Sign Up");
				
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(loginButton);
		buttonPanel.add(signUpButton);
		
		//Ϊ������(Login)panel���ò��ֹ���BorderLayout��
		LayoutManager loginMgr = new BorderLayout();
		setLayout(loginMgr);
		add(welcome, BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);
	}	//Login()
		
	public UsersEntity getUser() {
		return user;		
	}
	//��ʾ��½����
	public boolean showDialog(Component parent,String title){
		ok = false;
		//��ȡ��Ļ����
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		//ȷ������Frame����
		Frame owner = null;
		if(parent instanceof Frame) 
			owner = (Frame) parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
		 // if first time, or if owner has changed, make new dialog
		if(dialog == null || dialog.getOwner() != owner){
			dialog = new JDialog(owner, true);//true��ʾֻ���ȴ���ǰ����
			dialog.add(this);//���ص�½���浽�Ի�����
			dialog.getRootPane().setDefaultButton(loginButton);//����Ĭ�ϰ�ť����ʹ��JRootPane�����ṩ��setDefaultButton()
			dialog.setSize(DEFAULTWIDTH, DEFAULTHEIGHT);
			dialog.setLocation((screenWidth - DEFAULTWIDTH )/2,(screenHeight - DEFAULTHEIGHT)/2 );//�����Ͻ�Ϊԭ��
			dialog.addWindowListener(new WindowAdapter() {//�رմ��ڼ����ش���
				public void windowClosing(WindowEvent arg0) {
					setVisible(false);
				}	
			});
		}
		dialog.setTitle(title);
		dialog.setVisible(true);//��ʾ		
		return ok;
	}
	
}
