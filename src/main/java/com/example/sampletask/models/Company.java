package com.example.sampletask.models;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Company {

    @Id
    private String name;

    private String logo;

    public Company() {
    }

    public Company(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Company{" +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) &&
                Objects.equals(logo, company.logo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, logo);
    }
}
