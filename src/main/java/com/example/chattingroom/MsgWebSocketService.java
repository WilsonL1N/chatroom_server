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
        JSONObject res = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name","System");
        user.put("uid","0");
        res.put("type",1);
        res.put("content","Welcome!");
        res.put("user",user);
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
        res.put("time", ft.format(dNow));
        // JSONObject msg = new JSONObject();
        // msg.put("roomId", roomId);
        // msg.put("sessionId",session.getId());

        // session.getBasicRemote().sendText(res.toJSONString());
        broadcast(roomId, res.toJSONString(), session);
        System.out.println("session open. ID:" + session.getId());
    }

    @OnClose
    public void onClose(@PathParam("roomId") int roomId, Session session) throws Exception {
        rooms.get(roomId).remove(session);
        JSONObject res = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name","System");
        user.put("uid","0");
        res.put("type",1);
        res.put("content","leave!");
        res.put("user",user);
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
        res.put("time", ft.format(dNow));
        broadcast(roomId, res.toJSONString(), session);
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
        broadcast(roomId, message, session);
        // System.out.println("get client msg. SID:" + session.getId() + ". msg:" + message);
    }

    @OnError
    public void onError(@PathParam("roomId") int roomId, Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void broadcast(int roomId, String msg, Session session) throws Exception {
        // JSONObject o = JSONObject.parseObject(msg);
        for (Session s : rooms.get(roomId)) {
            s.getBasicRemote().sendText(msg);
        }
    }
}
