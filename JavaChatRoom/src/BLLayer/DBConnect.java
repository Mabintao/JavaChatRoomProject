package BLLayer;
/*
 * �������ݿ�
 * 2017-05-04 21:57:57
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	public static String DRIVER_MAYSQL = "com.mysql.jdbc.Driver";//MySQL JDBC�����ַ���
	//���ݿ�URL,������ʶҪ���ӵ����ݿ⣬�������ݿ������û����������Ǹ������ݿ�����趨
	public static String URL = "jdbc:mysql://localhost:3306/chatroom?"
							 + "user=root&password=123456&useUnicode=true&charcterEncoding=UTF8" ;
	private static Connection conn = null;
	static{//��̬��ʼ������
		try {
			Class.forName(DRIVER_MAYSQL);//����JDBC����
			System.out.println("Driver Load Success.");
			//�������ݿ����Ӷ���
			 conn = DriverManager.getConnection(URL);
			System.out.println("GetConnectionToDB");
        } catch (SQLException e){
        	e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		return conn;
	}
	public static void closeDB()throws SQLException{
		conn.close();
	}
	
}
