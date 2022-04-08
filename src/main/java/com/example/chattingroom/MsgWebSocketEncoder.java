package com.example.chattingroom;

import java.util.HashMap;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.alibaba.fastjson.JSONObject;

public class MsgWebSocketEncoder implements Encoder.Text<HashMap> {
    
    @Override
    public String encode(HashMap hashmap) throws EncodeException {
        try {
            return JSONObject.toJSONString(hashmap);
        } catch (Exception e) {
            
        }
        return null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void destroy() {}
}
