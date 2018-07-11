package com.example.sampletask.models;

import java.util.List;

public class ResultCompany {
    private String name;
    private String logo;
    private List<CompanyPrice> prices;

    public ResultCompany(String name, String logo, List<CompanyPrice> prices) {
        this.name = name;
        this.logo = logo;
        this.prices = prices;
    }

    public ResultCompany() {
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public List<CompanyPrice> getPrices() {
        return prices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPrices(List<CompanyPrice> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "ResultCompany{" +
                "name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", prices=" + prices +
                '}';
    }
}
