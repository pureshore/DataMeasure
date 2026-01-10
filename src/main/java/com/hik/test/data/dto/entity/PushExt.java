package com.hik.test.data.dto.entity;

public class PushExt {
    private String msgId;
    private String msgSubType;
    private String msgContent;
    
    // Constructors
    public PushExt() {}
    
    public PushExt(String msgId, String msgSubType, String msgContent) {
        this.msgId = msgId;
        this.msgSubType = msgSubType;
        this.msgContent = msgContent;
    }
    
    // Getters and Setters
    public String getMsgId() {
        return msgId;
    }
    
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    
    public String getMsgSubType() {
        return msgSubType;
    }
    
    public void setMsgSubType(String msgSubType) {
        this.msgSubType = msgSubType;
    }
    
    public String getMsgContent() {
        return msgContent;
    }
    
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
    
    @Override
    public String toString() {
        return "PushExt{" +
                "msgId='" + msgId + '\'' +
                ", msgSubType='" + msgSubType + '\'' +
                ", msgContent='" + msgContent + '\'' +
                '}';
    }
}
