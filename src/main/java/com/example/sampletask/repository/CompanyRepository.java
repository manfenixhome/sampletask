package com.example.sampletask.repository;

import com.example.sampletask.models.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompanyRepository extends MongoRepository<Company, String> {

    Company save(Company companyEntity);

    List<Company> findAll();

    List<Company> findCompaniesByNameIn(List<String> names);
}
