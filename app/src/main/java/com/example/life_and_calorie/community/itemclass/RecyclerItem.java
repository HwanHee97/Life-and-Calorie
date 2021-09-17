package com.example.life_and_calorie.community.itemclass;

import java.io.Serializable;

public class RecyclerItem implements Serializable {//게시글 객체 클래스
    //게시글 id는 db에서 알아서 생성 => _id
    private String _ID;
    private String title;
    private String text;
    private String date;//DB에서 생성후 받아옴
    private String writer_ID;
    RecyclerItem(){}

    public RecyclerItem(String title, String text, String writer){
        this.title=title;
        this.text=text;
        this.writer_ID=writer;
    }
    public RecyclerItem(String _ID, String title, String text, String date, String writer){
        this._ID=_ID;
        this.title=title;
        this.text=text;
        this.date=date;
        this.writer_ID=writer;
    }

    public String get_ID() { return _ID; }
    public void set_ID(String _ID) { this._ID = _ID; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getWriter_ID() { return writer_ID; }
    public void setWriter_ID(String writer_ID) { this.writer_ID = writer_ID; }

}
