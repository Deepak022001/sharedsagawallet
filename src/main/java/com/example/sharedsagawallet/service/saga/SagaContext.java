package com.example.sharedsagawallet.service.saga;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class SagaContext {
    private Map<String,Object>data;

    public SagaContext(Map<String,Object>data){
        if(data!=null){
            this.data=data;
        }else{
            this.data=new HashMap<>();
        }
    }
    public void put(String key,Object value){
        data.put(key, value);
    }

    public Object get(String key){
        return data.get(key);
    }
}
