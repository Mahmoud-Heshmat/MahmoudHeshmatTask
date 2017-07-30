package com.example.mahmoudheshmat.mahmoudheshmattask;


public class items_booking {


    private String doctor_id;
    private String patient_id;
    private String name;
    private String address;
    private String price;
    private String type;
    private String date_visit;
    private String medicine;
    private String confirm;

    public items_booking(String doctor_id, String name, String address, String price, String type, String date_visit, String medicine, String confirm){
        this.doctor_id = doctor_id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.price = price;
        this.date_visit = date_visit;
        this.medicine = medicine;
        this.confirm = confirm;
    }

    public items_booking(String patient_id, String name, String date_visit, String medicine, String confirm){
        this.patient_id = patient_id;
        this.name = name;
        this.date_visit = date_visit;
        this.medicine = medicine;
        this.confirm = confirm;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_visit() {
        return date_visit;
    }

    public void setDate_visit(String date_visit) {
        this.date_visit = date_visit;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
}