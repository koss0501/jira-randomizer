package com.apptimized.tools.randomizer.JiraEntities;

import com.apptimized.tools.randomizer.Utils.JsonActions;
import com.apptimized.tools.randomizer.RestClient.RestActions;

import java.util.Map;

public class User {
    private String name;
    private String emailAddress;
    private String active;
    private boolean isQA = false;
    private boolean isPackager = false;
    private boolean isAssignee = false;
    private User qaEngineer;

    public User() {}

    public User(String name) {
        Map<String, String> userData = JsonActions.parseUserDataToMap(RestActions.getUserInfo(name));
        setName(userData.get("name"));
        setEmailAddress(userData.get("emailAddress"));
        setActive(userData.get("active"));
    }

    public User(String name, String emailAddress, String active) {
        setName(name);
        setEmailAddress(emailAddress);
        setActive(active);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public boolean isQA() {
        return isQA;
    }

    public void setQA(boolean QA) {
        isQA = QA;
    }

    public String getQaEngineerName() {
        if(qaEngineer == null)
            return null;
        return qaEngineer.getName();
    }

    public User getQaEngineer() {
        return qaEngineer;
    }

    public void setQaEngineer(User qaEngineer) {
        this.qaEngineer = qaEngineer;
    }

    public boolean isPackager() {
        return isPackager;
    }

    public void setPackager(boolean packager) {
        isPackager = packager;
    }

    public boolean isAssignee() {
        return isAssignee;
    }

    public void setAssignee(boolean assignee) {
        isAssignee = assignee;
    }

}
