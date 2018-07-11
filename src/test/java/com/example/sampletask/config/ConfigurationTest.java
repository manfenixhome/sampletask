package com.example.sampletask.config;

import com.example.sampletask.RequestUtil;
import com.example.sampletask.repository.CompanyPriceRepository;
import com.example.sampletask.repository.CompanyRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ConfigurationTest {

    @Bean
    @Primary
    public CompanyRepository mockCompanyRepository() {
        return Mockito.mock(CompanyRepository.class);
    }

    @Bean
    @Primary
    public CompanyPriceRepository mockCompanyPriceRepository() {
        return Mockito.mock(CompanyPriceRepository.class);
    }

    @Bean
    @Primary
    public RequestUtil mockRequestUtil() {
        return Mockito.mock(RequestUtil.class);
    }
}
