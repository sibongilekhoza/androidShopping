package com.groupi.onlineshopping.model;

public class AdminOrders
{
  private String name,cell,address,town;
  private String date,time,state,totalAmount;

    public AdminOrders() {
    }

    public AdminOrders(String name, String cell, String address, String town, String date, String time, String state, String totalAmount) {
        this.name = name;
        this.cell = cell;
        this.address = address;
        this.town = town;
        this.date = date;
        this.time = time;
        this.state = state;
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
