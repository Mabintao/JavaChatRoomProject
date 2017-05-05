package DALayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import BLLayer.DBConnect;
import BLLayer.UsersEntity;

/*
 *  2017-05-04 18:34:55
 *  usersʵ��������ݷ���
 *  �������ݿ���users��ġ���ɾ������
 */
public class UsersDA {
	//��½��֤����ȡ�û���Ϣ
	public UsersEntity findByNamePassword(String name,String password) throws SQLException{
		Connection connection = DBConnect.getConnection();
		Statement statement = connection.createStatement();
		//����SQL���Ŀո�
		String sql = "SELECT  * FROM users WHERE name='"+name+"' AND password='"+password+"'";
		ResultSet rs = statement.executeQuery(sql);
		UsersEntity user = null;
		System.out.println("12312312");
		if (rs.next()) {
			user = new UsersEntity();
			user.setID(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setSex(rs.getString("sex"));
			user.setStatus(rs.getInt("status"));	
			System.out.println(user.toString());
		}
		rs.close();//�رս����
		statement.close(); 
		return user;
	}
    public boolean insert(UsersEntity user) throws SQLException {
        Connection conn = DBConnect.getConnection();
        String sql = "insert into users values(NULL,?,?,?,?)";
        // Ԥ����
        PreparedStatement ptmt = conn.prepareStatement(sql);
        // ����
        ptmt.setString(1, user.getName());
        ptmt.setString(2, user.getPassword());
        ptmt.setString(3, user.getSex());
        ptmt.setInt(4, user.getStatus());
        // ִ��
        ptmt.execute();
        return true;
    }
    public UsersEntity findByName(String name) throws SQLException {
        Connection conn = DBConnect.getConnection();
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(
                "SELECT id,name,password,sex,status FROM users u WHERE u.name='"+name+"'");
        UsersEntity user = null;
        if (rs.next()) {
            user = new UsersEntity();
            user.setID(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setSex(rs.getString("sex"));
            user.setStatus(rs.getInt("status"));
        }
        return user;
    }
    public UsersEntity findById(int id) throws SQLException {
        Connection conn = DBConnect.getConnection();
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(
                "SELECT id,name,password,sex,status FROM users u WHERE u.id="+id);
        UsersEntity user = null;
        if (rs.next()) {
            user = new UsersEntity();
            user.setID(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setSex(rs.getString("sex"));
            user.setStatus(rs.getInt("status"));
        }
        return user;
    }
    public void offline(int id) throws SQLException {//�û�����status=0
        Connection conn = DBConnect.getConnection();
        Statement state = conn.createStatement();
        String sql = "UPDATE users SET status = 0 WHERE id="+id;
        state.execute(sql);
    }
	public void online(int id) throws SQLException {//�û�����status=1
        Connection conn = DBConnect.getConnection();
        Statement state = conn.createStatement();
        String sql = "UPDATE users SET status = 1 WHERE id="+id;
        state.execute(sql);
    }
    public void hide(int id) throws SQLException {//�û�����status=2
        Connection conn = DBConnect.getConnection(); 
        Statement state = conn.createStatement();
        String sql = "UPDATE users SET status = 2 WHERE id="+id;
        state.execute(sql);
    }
	
	
}
