package com.groupi.onlineshopping.model;

public class Cart
{
    private String pid,name,price,discount;
    private int quantity;


    public Cart()
    {

    }

    public Cart(String pid, String name, String price, String discount, int quantity) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
