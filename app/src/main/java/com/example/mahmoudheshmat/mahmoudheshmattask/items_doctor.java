package com.example.mahmoudheshmat.mahmoudheshmattask;


public class items_doctor {

    private String id;
    private String name;
    private String spec;
    private String address;
    private String price;

    public items_doctor(String id, String name, String spec, String address, String price){
        this.id = id;
        this.name = name;
        this.spec = spec;
        this.address = address;
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
