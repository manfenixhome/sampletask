package com.example.sampletask.response;

public class CompanyEntityResponse {
    private String symbol;
    private double price;

    public CompanyEntityResponse() { }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "CompanyEntityResponse{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                '}';
    }
}
