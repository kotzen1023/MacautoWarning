package com.macauto.macautowarning.data;


public class HistoryItem {

    private String msg_id;
    private String msg_code;
    private String msg_title;
    private String msg_content;
    private String announce_date;
    private String internal_doc_no;
    private String internal_part_no;
    private String internal_model_no;
    private String internal_machine_no;
    private String internal_plant_no;
    private String announcer;
    private String ime_code;
    private boolean read_sp;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(String msg_code) {
        this.msg_code = msg_code;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getAnnounce_date() {
        return announce_date;
    }

    public void setAnnounce_date(String announce_date) {
        this.announce_date = announce_date;
    }

    public String getInternal_doc_no() {
        return internal_doc_no;
    }

    public void setInternal_doc_no(String internal_doc_no) {
        this.internal_doc_no = internal_doc_no;
    }

    public String getInternal_part_no() {
        return internal_part_no;
    }

    public void setInternal_part_no(String internal_part_no) {
        this.internal_part_no = internal_part_no;
    }

    public String getInternal_model_no() {
        return internal_model_no;
    }

    public void setInternal_model_no(String internal_model_no) {
        this.internal_model_no = internal_model_no;
    }

    public String getInternal_machine_no() {
        return internal_machine_no;
    }

    public void setInternal_machine_no(String internal_machine_no) {
        this.internal_machine_no = internal_machine_no;
    }

    public String getInternal_plant_no() {
        return internal_plant_no;
    }

    public void setInternal_plant_no(String internal_plant_no) {
        this.internal_plant_no = internal_plant_no;
    }

    public String getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }

    public String getIme_code() {
        return ime_code;
    }

    public void setIme_code(String ime_code) {
        this.ime_code = ime_code;
    }

    public boolean isRead_sp() {
        return read_sp;
    }

    public void setRead_sp(boolean read_sp) {
        this.read_sp = read_sp;
    }
}
