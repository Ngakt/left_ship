package com.example.huoda.myapplication.commons.models;


import java.util.Date;
import java.util.List;

public interface IConversation {

    String getId();

    String getConvTitle();

    String getConvPhoto();

    List<IUser> getUsers();

    IMessage getLastMsg();

    void setLastMsg(IMessage msg);

    int getUnreadCount();

    Date getCreatedAt();
}
