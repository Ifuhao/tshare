package test;

import dao.UserDAO;
import dao.impl.UserDAOImpl;

public class UserDAOTest {
	public static void main(String[] args) throws Exception {
		UserDAO dao = new UserDAOImpl();
		System.out.println(dao.selectById("160400427@stu.hit.edu.cn"));
	}
}
