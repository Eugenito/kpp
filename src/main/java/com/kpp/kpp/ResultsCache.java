package com.kpp.kpp;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ResultsCache {
    private final Map<String, Fields> hashMap = new LinkedHashMap<>();

    public boolean findByKeyInHashMap(String key){
        return hashMap.containsKey(key);
    }

    public void addToMap(String key, Fields result){
        hashMap.put(key, result);
    }

    public Fields getParameters(String key){
        return hashMap.get(key);
    }

    public Map<String, Fields> getHashMap() {
        return hashMap;
    }
}
