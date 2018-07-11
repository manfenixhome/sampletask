package com.example.sampletask.schedule;

import com.example.sampletask.RequestUtil;
import com.example.sampletask.models.Company;
import com.example.sampletask.models.CompanyPrice;
import com.example.sampletask.repository.CompanyPriceRepository;
import com.example.sampletask.repository.CompanyRepository;
import com.example.sampletask.response.CompanyEntityResponse;
import com.example.sampletask.response.CompanyLogoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ScheduledTasks {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyPriceRepository companyPriceRepository;

    @Autowired
    private RequestUtil requestUtil;

    @Value("${iextrading.endpoint}")
    private String BASE_ENDPOINT;

    @Value("${iextrading.symbols}")
    private String[] symbols;

    @Scheduled(fixedDelayString = "${iextrading.schedule.interval}")
    public void grab() throws IOException {
        List<String> upperCaseSymbols = Arrays.asList(symbols)
                .stream().map(s -> s.toUpperCase())
                .collect(Collectors.toList());

        Set<String> existingCompanies = companyRepository.findCompaniesByNameIn(upperCaseSymbols).stream()
                .map(c -> c.getName().toUpperCase())
                .collect(Collectors.toSet());

        for (CompanyEntityResponse cm : getCompanies(symbols)) {
            String symbol = cm.getSymbol().toUpperCase();

            if (!existingCompanies.contains(symbol)) {
                CompanyLogoResponse logo = getLogo(symbol);
                companyRepository.save(new Company(symbol, logo.getUrl()));
            }
            companyPriceRepository.save(new CompanyPrice(symbol, cm.getPrice()));
        }
    }

    private CompanyEntityResponse[] getCompanies(String[] symbols) throws IOException {
        String encodedSymbols = URLEncoder.encode(String.join(",", symbols), StandardCharsets.UTF_8.name());

        String resp = requestUtil.get(BASE_ENDPOINT + "tops/last?symbols=" + encodedSymbols + "&filter=symbol,price");
        return mapper.readValue(resp, CompanyEntityResponse[].class);
    }

    private CompanyLogoResponse getLogo(String symbol) throws IOException {
        String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8.name());

        String logoResp = requestUtil.get(BASE_ENDPOINT + "stock/" + encodedSymbol + "/logo");
        return mapper.readValue(logoResp, CompanyLogoResponse.class);
    }
}