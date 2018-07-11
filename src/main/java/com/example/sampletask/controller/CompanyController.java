package com.example.sampletask.controller;

import com.example.sampletask.repository.CompanyPriceRepository;
import com.example.sampletask.repository.CompanyRepository;
import com.example.sampletask.models.Company;
import com.example.sampletask.models.ResultCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyPriceRepository companyPriceRepository;

    @GetMapping(value = "/ping")
    public ResponseEntity ping() {
        return new ResponseEntity("PONG", HttpStatus.OK);
    }

    @GetMapping(value = "/company")
    public ResponseEntity companies(@RequestParam("symbols") String symbols,
                              @RequestParam(value = "from", defaultValue = "0") Long from,
                              @RequestParam(value = "to", defaultValue = "0") Long to) {
        //from cannot be bigger than to
        if (to >0 && from > 0 && from > to) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        List<String> rsymbols = Arrays.stream(symbols.split(",")).filter(s-> !s.trim().isEmpty()).collect(Collectors.toList());
        if (rsymbols.isEmpty()) {
            return new ResponseEntity(Collections.emptyList(), HttpStatus.OK);
        } else {
            List<Company> companies = companyRepository.findCompaniesByNameIn(rsymbols);
            Stream<ResultCompany> res = getResultCompanies(companies, from, to);
            return new ResponseEntity(res, HttpStatus.OK);
        }
    }

    private Stream<ResultCompany> getResultCompanies(List<Company> companies, Long from, Long to) {
        if (from > 0 && to > 0) {
            return companies.stream().map(c -> new ResultCompany(c.getName(), c.getLogo(), companyPriceRepository.findCompanyPriceByCompanyIdAndDateIsBetween(c.getName(), new Date(from), new Date(to))));
        } else if (from > 0) {
            return companies.stream().map(c -> new ResultCompany(c.getName(), c.getLogo(), companyPriceRepository.findCompanyPriceByCompanyIdAndDateIsAfter(c.getName(), new Date(from))));
        } else if (to > 0) {
            return companies.stream().map(c -> new ResultCompany(c.getName(), c.getLogo(), companyPriceRepository.findCompanyPriceByCompanyIdAndDateIsBefore(c.getName(), new Date(to))));
        } else {
            return companies.stream().map(c -> new ResultCompany(c.getName(), c.getLogo(), companyPriceRepository.findCompanyPriceByCompanyId(c.getName())));
        }

    }
}
