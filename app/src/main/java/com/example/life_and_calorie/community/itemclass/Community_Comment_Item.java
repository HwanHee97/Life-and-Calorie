package com.example.life_and_calorie.community.itemclass;

import java.io.Serializable;

public class Community_Comment_Item implements Serializable {

    private String comment_text;
    private String comment_date;//DB에서 생성후 받아옴
    private String comment_writer_ID;

    public Community_Comment_Item(String comment_text, String comment_date, String comment_writer_ID) {
        this.comment_text = comment_text;
        this.comment_date = comment_date;
        this.comment_writer_ID = comment_writer_ID;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_writer_ID() {
        return comment_writer_ID;
    }

    public void setComment_writer_ID(String comment_writer_ID) {
        this.comment_writer_ID = comment_writer_ID;
    }
}
