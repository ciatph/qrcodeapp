package com.leo.qrcodeapp.events;

public class ActionEvent {
    public int action = EventStatus.INSTANCE.ACTION_VIEW;
    public int type = EventStatus.INSTANCE.DATA_EVENT;   // 1=event, 2=detected guest QR

    public ActionEvent(int event, int type){
        this.action = event;
        this.type = type;
    }

    public void setEvent(int event, int type){
        this.action = event;
        this.type = type;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getEvent(){
        return action;
    }

    public int getType(){
        return type;
    }
}
