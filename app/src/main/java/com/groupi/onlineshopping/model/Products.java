package com.groupi.onlineshopping.model;

public class Products
{
    public String category,date,pid,price,time;
    public String description,name,image;

    public Products()
    {

    }

    public Products(String category, String date, String pid, String price, String time, String description, String name, String image)
    {
        this.category = category;
        this.date = date;
        this.pid = pid;
        this.price = price;
        this.time = time;
        this.description = description;
        this.name = name;
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
