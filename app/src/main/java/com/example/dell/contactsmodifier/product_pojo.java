package com.example.dell.contactsmodifier;


public class product_pojo {

    private String Name;
    private String Number;
    private String Status;

    public product_pojo(){}

    public product_pojo(String Name, String Number, String Status){
        this.Name = Name;
        this.Number = Number;
        this.Status=Status;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
