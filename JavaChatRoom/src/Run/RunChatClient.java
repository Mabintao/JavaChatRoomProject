package Run;

import java.awt.EventQueue;
import javax.swing.*;
import UILayer.ChatFrame;

/*
 * �ͻ���ִ�����
 * 2017-05-05 16:05:09
 */
public class RunChatClient {
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {//���ڴ�һ���µ�swing.fame���¼�����
			public void run() {
				ChatFrame chatClientFrame = new ChatFrame();
				chatClientFrame.setVisible(true);
				chatClientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
