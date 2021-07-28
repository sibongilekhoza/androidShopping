package com.groupi.onlineshopping.model;

public class Users
{
  public String name;
  public String password;
  public String cell;
  public String address;

    public Users() {

    }

    public Users(String name, String password, String cell, String address) {
        this.name = name;
        this.password = password;
        this.cell = cell;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
