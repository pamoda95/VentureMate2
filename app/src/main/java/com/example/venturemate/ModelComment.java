package com.example.venturemate;

public class ModelComment {
    private String cId;
    private String comment;
    private String uDp;
    private String uName;
    private String uid;
    private String timestamp;
    public ModelComment (String cId,String comment,String uDp,String uName,String uid,String timestamp) {
        this.setcId(cId);
        this.setComment(comment);
        this.setTimestamp(timestamp);
        this.setuDp(uDp);
        this.setuName(uName);
        this.setUid(uid);
    }


    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
