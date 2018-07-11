package com.example.sampletask.models;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Objects;

public class CompanyPrice {
    @Id
    private String id;
    private String companyId;
    private double price;
    private Date date;

    public CompanyPrice() {
    }

    public CompanyPrice(String companyId, double price, Date date) {
        this.companyId = companyId;
        this.price = price;
        this.date = date;
    }

    public CompanyPrice(String companyId, double price) {
        this.companyId = companyId;
        this.price = price;
        this.date = new Date();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyPrice that = (CompanyPrice) o;
        return Double.compare(that.price, price) == 0 &&
                Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(companyId, price);
    }

    @Override
    public String toString() {
        return "CompanyPrice{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", price=" + price +
                ", date='" + date + '\'' +
                '}';
    }
}
