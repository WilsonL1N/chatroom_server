package com.example.chattingroom;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Component;

@ServerEndpoint(value="/msgWebsocket/{roomId}", encoders=MsgWebSocketEncoder.class)
@Component
public class MsgWebSocketService {
    private static final Map<Integer, Set<Session>> rooms = new ConcurrentHashMap<Integer, Set<Session>>();
    private static final Map<String, Integer> members = new HashMap<String, Integer>();
    private static final Map<Integer, String> users = new HashMap<Integer, String>();

    public MsgWebSocketService() {
    }

    @OnOpen
    public void onOpen(@PathParam("roomId") int roomId, Session session) throws Exception{
        // WebSocketSupport.storageSession(session);
        if (!rooms.containsKey(roomId)) {
            Set<Session> members = new HashSet<>();
            members.add(session);
            System.out.println(members);
            rooms.put(roomId, members);
        } else {
            rooms.get(roomId).add(session);
        }
        System.out.println("session open. ID:" + session.getId());
    }

    @OnClose
    public void onClose(@PathParam("roomId") int roomId, Session session) throws Exception {
        int uid = members.get(session.getId());
        String name = users.get(uid);
        rooms.get(roomId).remove(session);
        members.remove(session.getId());
        if (rooms.get(roomId).size() == 0) {
            rooms.remove(roomId);
            return;
        }
        JSONObject res = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name","System");
        user.put("uid","0");
        res.put("type",1);
        res.put("content", name+" leaves!");
        res.put("user",user);
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
        res.put("time", ft.format(dNow));
        broadcast(roomId, res.toJSONString());
        broadcastMembers(roomId);
        System.out.println("session close. ID:" + session.getId());
    }

    // message:{
    //     type: int,  // 0: text, 1: sysMsg, 2:pic?
    //     user: { uid: int, name: String,}
    //     content: String,
    //     time: String,
    // }

    @OnMessage
    public void onMessage(@PathParam("roomId") int roomId, String message, Session session) throws Exception {
        JSONObject recv = JSONObject.parseObject(message);
        if (recv.get("type").equals("userInfo")) {
            int uid = (int) recv.get("uid");
            members.put(session.getId(), uid);
            String name = (String) recv.get("name");
            users.put(uid, name);
            JSONObject res = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("name","System");
            user.put("uid","0");
            res.put("type",1);
            res.put("content","Welcome "+name+"!");
            res.put("user",user);
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
            res.put("time", ft.format(dNow));
            broadcast(roomId, res.toJSONString());
            broadcastMembers(roomId);
        } else {
            broadcast(roomId, message);
            // System.out.println("get client msg. SID:" + session.getId() + ". msg:" + message);
        }
    }

    @OnError
    public void onError(@PathParam("roomId") int roomId, Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void broadcast(int roomId, String msg) throws Exception {
        // JSONObject o = JSONObject.parseObject(msg);
        for (Session s : rooms.get(roomId)) {
            s.getBasicRemote().sendText(msg);
        }
    }

    public static void broadcastMembers(int roomId) throws Exception {
        List<Integer> uids = new ArrayList<Integer>();
        List<String> names = new ArrayList<String>();
        for (Session s : rooms.get(roomId)) {
            String sid = s.getId();
            int uid = members.get(sid);
            String name = users.get(uid);
            uids.add(uid);
            names.add(name);
        }

        JSONObject res = new JSONObject();
        res.put("type", "memberList");
        res.put("uids", uids);
        res.put("names", names);
        String msg = res.toJSONString();
        
        broadcast(roomId, msg);
    }
}
