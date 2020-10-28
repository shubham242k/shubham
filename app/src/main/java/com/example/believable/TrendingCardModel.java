package com.example.believable;

public class TrendingCardModel {

    String name,price;
    String backgroundImageUrl;


    public TrendingCardModel(String name, String price, String backgroundImageUrl) {
        this.name = name;
        this.price = price;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setCity(String city) {
        this.name = city;
    }

    public String getPrice() {
        return price;
    }

    public void setCountry(String country) {
        this.price = country;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImage) {
        this.backgroundImageUrl = backgroundImage;
    }
}
