package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dao.SeekDAO;
import dao.UserDAO;
import dao.impl.SeekDAOImpl;
import dao.impl.UserDAOImpl;

public class UserDAOTest {
	public static void main(String[] args) throws Exception {
		UserDAO dao = new UserDAOImpl();
		System.out.println(dao.selectById("160400426@stu.hit.edu.cn"));
		
		SeekDAO seekdao = new SeekDAOImpl();
//		Seek seek = new Seek();
//		seek.setSeek_id(0);
//		seek.setId("160400426@stu.hit.edu.cn");
//		seek.setTitle("华为手机");
//		seek.setDescription("华为手机，便宜甩卖");
//		seek.setTime(new Date(new java.util.Date().getTime()));
//		seek.setIs_buy(0);
//		seek.setDelete(0);
//		seekdao.insert(seek);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("title","华为手机");
//		map.put("title","为");
		System.out.println(Arrays.toString(seekdao.select("title", "华为")));
		System.out.println(Arrays.toString(seekdao.selectByCond(map)));
		
	}
}
