package web.socket;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import json.JSONArray;
import utils.JSONParser;
import utils.form.BeanOperator;
import dao.PerGroupDAO;
import dao.PerNoticeDAO;
import dao.SysNoticeAcceptDAO;
import dao.SysNoticeDAO;
import dao.UserDAO;
import dao.impl.PerGroupDAOImpl;
import dao.impl.PerNoticeDAOImpl;
import dao.impl.SysNoticeAcceptDAOImpl;
import dao.impl.SysNoticeDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.PerGroup;
import dao.vo.PerNotice;
import dao.vo.SysNotice;
import dao.vo.SysNoticeAccept;
import dao.vo.User;

/**
 * webSocket的服务器端
 * @author fuhao
 */
@ServerEndpoint("/msgServer/{id}")
public class WebSocketServer {
	private UserDAO user_dao = new UserDAOImpl();
	private SysNoticeDAO sn_dao = new SysNoticeDAOImpl();
	private SysNoticeAcceptDAO sna_dao = new SysNoticeAcceptDAOImpl();
	private PerNoticeDAO pn_dao = new PerNoticeDAOImpl();
	private PerGroupDAO pg_dao = new PerGroupDAOImpl();
	
	/**
	 * 保存所有连接到websocket的客户端的id(用户账号)
	 */
	private static HashMap<String, Session> users = new HashMap<>();
	
	/**
	 * 客户端连接成功时调用的方法
	 * @param id 用户账号
	 * @param session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(@PathParam("id") String id, Session session) throws IOException {
		// 先判断这个账号是否有效
		if(id == null || id.equals("null")) {
			// 不进行连接
			JSONArray json = new JSONArray();
			json.set("code", 0);
			json.set("msg", "您已掉线");
			session.getBasicRemote().sendText(JSONParser.json_encode(json));
			return;
		}
		
		if(!users.containsKey(id)) {
			users.put(id, session);
			System.out.println(id+" 用户登录");
			
			JSONArray json = new JSONArray();
			json.set("system", sna_dao.hasNew(id)?1:0);
			
			PerGroup[] person = pg_dao.getNew(id, PerGroup.TYPE_PERSONAL);
			if(person != null) {
				JSONArray person_json = new JSONArray(false);
				for(int i=0;i<person.length;i++) {
					person_json.set(person[i].getGroup_id());
				}
				json.set("person", person_json);
			}
			
			PerGroup[] trade = pg_dao.getNew(id, PerGroup.TYPE_TRANSACTION);
			if(trade != null) {
				JSONArray trade_json = new JSONArray(false);
				for(int i=0;i<trade.length;i++) {
					trade_json.set(trade[i].getGroup_id());
				}
				json.set("trade", trade_json);
			}
			
			json.set("other", 0);
			session.getBasicRemote().sendText(JSONParser.json_encode(json));
		}
	}
	
	/**
	 * 客户端断开连接时调用的方法
	 * @param id 用户账号
	 * @param session
	 * @throws IOException
	 */
	@OnClose
	public void onClose(@PathParam("id") String id, Session session) throws IOException {
		users.remove(id);
		System.out.println(id+" 用户退出");
	}
	
	/**
	 * 客户端发送消息时调用的方法
	 * @param id 用户账号
	 * @param msg 消息内容(包含发送方账号，接收方账号和消息内容)
	 * @param session
	 */
	@OnMessage
	public void onMessage(@PathParam("id") String id, String msg, Session session) throws IOException {
		// 先将消息存入数据库
		// 判断接收方是否在线，如果在线则立马发送消息
		// 对于当时不在线的用户，一旦用户登录立马发送消息
		// 参数msg作为一个字符串，分为五个域：发送方账号，接收方账号，消息标题，消息类型，消息内容，用逗号分隔(发送方的账号由参数id给出)
		// （例如："sender:160400424@stu.hit.edu.cn, address:160400423@stu.hit.edu.cn, title:消息标题, content:消息内容, type:消息类型"）
		// 其中必填项有两个content和type，如果某个域为空，用"null"填充
		// （例如："sender:null, address:null, title:null, content:消息内容, type:消息类型"）
		
		// 第一步，分离消息的五个域并生成websocket所能识别的消息对象
		Message message = new Message();
		int firstIndex = 0;
		int secondIndex = msg.indexOf(",");
		while(secondIndex != -1) {
			String kv = msg.substring(firstIndex, secondIndex);
			kv = kv.trim();
			
			int index = kv.indexOf(":");
			String key = kv.substring(0, index);
			String value = kv.substring(index+1);
			
			new BeanOperator(message, "Message."+key, value);
			
			firstIndex = secondIndex + 1;
			secondIndex = msg.indexOf(",", firstIndex);
		}
		
		String type = msg.substring(firstIndex).trim();
		message.setType(type.substring(type.indexOf(":")+1));
		
		// 第二步，存数据库
		// (1) 如果发送方为null，表示是系统消息，如果此时接收方也为null，则表示该系统消息是向全体用户发送
		// (2) 如果发送方和接收方均不为null，则根据type的值判断消息类型，"trade"为交易消息，"person"为私信
		if(message.getSender().equals("null")) {
			// 系统消息
			SysNotice sn = new SysNotice();
			sn.setSys_id(sn_dao.count()+1);
			sn.setTitle(message.getTitle());
			sn.setContent(message.getContent());
			sn.setTime(new Date().getTime());
			sn.setIs_delete(0);
			sn_dao.insert(sn);
			
			if(message.getAddress().equals("null")) {
				User users[] = user_dao.selectAll();
				for(int i=0;i<users.length;i++) {
					// 如果这些人在线的话，发送提醒消息
					SysNoticeAccept sna = new SysNoticeAccept();
					sna.setAccept_id(sna_dao.count()+1);
					sna.setAddress(users[i].getId());
					sna.setSys_id(sn.getSys_id());
					sna.setIs_read(0);
					sna.setIs_delete(0);
					sna_dao.insert(sna);
					
					if(WebSocketServer.users.containsKey(users[i].getId())) {
						JSONArray json = new JSONArray();
						json.set("system", 1);
						WebSocketServer.users.get(users[i].getId()).getBasicRemote().sendText(JSONParser.json_encode(json));
					}
				}
			} else {
				// 向指定用户发送，如果接收者在线则发送提醒消息
				SysNoticeAccept sna = new SysNoticeAccept();
				sna.setAccept_id(sna_dao.count()+1);
				sna.setSys_id(sn.getSys_id());
				sna.setAddress(message.getAddress());
				sna.setIs_read(0);
				sna.setIs_delete(0);
				sna_dao.insert(sna);
				
				if(WebSocketServer.users.containsKey(message.getAddress())) {
					JSONArray json = new JSONArray();
					json.set("system", 1);
					WebSocketServer.users.get(message.getAddress()).getBasicRemote().sendText(JSONParser.json_encode(json));
				}
			}
		} else {
			int msgType = 0;
			if(message.getType().equals("trade")) {
				msgType = PerGroup.TYPE_TRANSACTION;	// 交易消息
			} else if(message.getType().equals("person")) {
				msgType = PerGroup.TYPE_PERSONAL;	// 私信
			}
			
			String user1 = message.getSender();
			String user2 = message.getAddress();
			
			PerGroup pg = null;
			if(pg_dao.exist(user1, user2, msgType)) {
				// 如果这样的用户组已经存在，那么就不用创建了
				pg = pg_dao.selectByUser(user1, user2, msgType);
				// 将接收者的消息状态设置为有新消息
				pg_dao.setNew(pg.getGroup_id(), user2, 1);
			} else {
				// 创建用户组
				pg = new PerGroup();
				pg.setGroup_id(pg_dao.count()+1);
				pg.setUser1(user1);
				pg.setUser2(user2);
				pg.setNew_msg1(0);
				pg.setNew_msg2(1);
				pg.setType(msgType);
				pg.setDelete1(0);
				pg.setDelete2(0);
				
				pg_dao.insert(pg);
			}
			
			PerNotice pn = new PerNotice();
			pn.setMsg_id(pn_dao.count()+1);
			pn.setGroup_id(pg.getGroup_id());
			pn.setContent(message.getContent());
			pn.setSender(user1);
			pn.setAddress(user2);
			pn.setTime(new Date().getTime());
			pn.setIs_read(0);
			
			pn_dao.insert(pn);
			
			// 如果接收方在线，则发送提醒消息
			if(WebSocketServer.users.containsKey(user2)) {
				JSONArray json = new JSONArray();
				JSONArray msg_json = new JSONArray(false);
				msg_json.set(pn.getGroup_id());
				json.set(message.getType(), msg_json);
				WebSocketServer.users.get(user2).getBasicRemote().sendText(JSONParser.json_encode(json));
			}
		}
	}
	
	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
//		error.printStackTrace();
	}
}
