package test;

import dao.StationDAO;
import dao.impl.StationDAOImpl;

public class StationTest {
	public static void main(String[] args) {
		StationDAO dao = new StationDAOImpl();
//		dao.addFile_number(20);
		System.out.println(dao.select());
	}
}
