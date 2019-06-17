package test;

import dao.FileStationDAO;
import dao.impl.FileStationDAOImpl;

public class StationTest {
	public static void main(String[] args) {
		FileStationDAO dao = new FileStationDAOImpl();
//		dao.addFile_number(20);
		System.out.println(dao.select());
	}
}
