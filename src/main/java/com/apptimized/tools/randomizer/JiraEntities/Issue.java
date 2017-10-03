package com.apptimized.tools.randomizer.JiraEntities;

public class Issue {

    private String key = null;
    private String summary = null;
    private String assignee = null;
    private String packager = null;
    private String qa_engineer = null;

    public Issue() {
    }

    public Issue(String key) {
        setKey(key);
    }

    public Issue(String key, String assignee, String packager, String qa_engineer) {
        setKey(key);
        setAssignee(assignee);
        setPackager(packager);
        setQa_engineer(qa_engineer);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getPackager() {
        return packager;
    }

    public void setPackager(String packager) {
        this.packager = packager;
    }

    public String getQa_engineer() {
        return qa_engineer;
    }

    public void setQa_engineer(String qa_engineer) {
        this.qa_engineer = qa_engineer;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
