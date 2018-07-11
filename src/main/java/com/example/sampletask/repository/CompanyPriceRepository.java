package com.example.sampletask.repository;

import com.example.sampletask.models.CompanyPrice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface CompanyPriceRepository extends MongoRepository<CompanyPrice, String> {

    CompanyPrice save(CompanyPrice companyEntity);

    List<CompanyPrice> findAll();

    List<CompanyPrice> findCompanyPriceByCompanyId(String company);

    List<CompanyPrice> findCompanyPriceByCompanyIdAndDateIsAfter(String company, Date from);

    List<CompanyPrice> findCompanyPriceByCompanyIdAndDateIsBefore(String company, Date to);

    List<CompanyPrice> findCompanyPriceByCompanyIdAndDateIsBetween(String company, Date from, Date to);
}
