package com.example.flousino.Model;

public class Data {
    private int amount;
    private String type;
    private String date;
    private String note;
    private String id;
    public Data() {

    }

    public Data(int amount, String type, String date, String note, String id) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.note = note;
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public String getId() {
        return id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setId(String id) {
        this.id = id;
    }
}
