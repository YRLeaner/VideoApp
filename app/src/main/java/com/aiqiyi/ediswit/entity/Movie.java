package com.aiqiyi.ediswit.entity;

/**
 * Created by tyr on 2017/6/7.
 */
public class Movie {
    private String id;
    private String title;
    private String short_title;
    private String img;
    private String play_count;
    private String play_count_text;
    private String a_id;
    private String is_vip;
    private String type;
    private String p_type;
    private String date_timestamp;
    private String total_num;
    private String update_num;

    public Movie() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public String getPlay_count_text() {
        return play_count_text;
    }

    public void setPlay_count_text(String play_count_text) {
        this.play_count_text = play_count_text;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getDate_timestamp() {
        return date_timestamp;
    }

    public void setDate_timestamp(String date_timestamp) {
        this.date_timestamp = date_timestamp;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getUpdate_num() {
        return update_num;
    }

    public void setUpdate_num(String update_num) {
        this.update_num = update_num;
    }

    public Movie(String id, String title, String short_title, String img, String play_count, String play_count_text, String a_id, String is_vip, String type, String p_type, String date_timestamp, String total_num, String update_num) {

        this.id = id;
        this.title = title;
        this.short_title = short_title;
        this.img = img;
        this.play_count = play_count;
        this.play_count_text = play_count_text;
        this.a_id = a_id;
        this.is_vip = is_vip;
        this.type = type;
        this.p_type = p_type;
        this.date_timestamp = date_timestamp;
        this.total_num = total_num;
        this.update_num = update_num;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", short_title='" + short_title + '\'' +
                ", img='" + img + '\'' +
                ", play_count='" + play_count + '\'' +
                ", play_count_text='" + play_count_text + '\'' +
                ", a_id='" + a_id + '\'' +
                ", is_vip='" + is_vip + '\'' +
                ", type='" + type + '\'' +
                ", p_type='" + p_type + '\'' +
                ", date_timestamp='" + date_timestamp + '\'' +
                ", total_num='" + total_num + '\'' +
                ", update_num='" + update_num + '\'' +
                '}';
    }
}
