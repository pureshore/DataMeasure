package com.api.agent.dto.entity;

import java.util.Map;

public class MessageRequest {
    private Map<String, String> replaces;
    private PushExt pushExt;
    
    // Constructors
    public MessageRequest() {}
    
    public MessageRequest(Map<String, String> replaces, PushExt pushExt) {
        this.replaces = replaces;
        this.pushExt = pushExt;
    }
    
    // Getters and Setters
    public Map<String, String> getReplaces() {
        return replaces;
    }
    
    public void setReplaces(Map<String, String> replaces) {
        this.replaces = replaces;
    }
    
    public PushExt getPushExt() {
        return pushExt;
    }
    
    public void setPushExt(PushExt pushExt) {
        this.pushExt = pushExt;
    }
    
    @Override
    public String toString() {
        return "MessageRequest{" +
                "replaces=" + replaces +
                ", pushExt=" + pushExt +
                '}';
    }
}
