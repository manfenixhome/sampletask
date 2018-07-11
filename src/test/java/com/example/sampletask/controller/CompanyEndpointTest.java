package com.example.sampletask.controller;

import com.example.sampletask.SampletaskApplication;
import com.example.sampletask.models.Company;
import com.example.sampletask.models.CompanyPrice;
import com.example.sampletask.repository.CompanyPriceRepository;
import com.example.sampletask.repository.CompanyRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SampletaskApplication.class})
@SpringBootTest
@WebAppConfiguration
public class CompanyEndpointTest {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyPriceRepository companyPriceRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        reset(companyRepository, companyPriceRepository);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(companyRepository, companyPriceRepository);
    }

    @Test
    public void ping() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("PONG"));
    }

    @Test
    public void sendFewCompaniesWithTimeFrame() throws Exception {
        List<Company> companies = Arrays.asList(
                new Company("FB", "https://storage.googleapis.com/iex/api/logos/FB.png"),
                new Company("SNAP", "https://storage.googleapis.com/iex/api/logos/SNAP.png")
        );
        when(companyRepository.findCompaniesByNameIn(anyList())).thenReturn(companies);
        when(companyPriceRepository.findCompanyPriceByCompanyIdAndDateIsBetween(anyString(),any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(
                        new CompanyPrice("FB",203.53,df.parse("2018-07-11T09:29:44.782+0000")),
                        new CompanyPrice("FB",203.54,df.parse("2018-07-11T09:29:51.177+0000"))
                ))
                .thenReturn(Arrays.asList(
                        new CompanyPrice("SNAP",13.305,df.parse("2018-07-11T09:29:44.480+0000")),
                        new CompanyPrice("SNAP",13.306,df.parse("2018-07-11T09:29:50.908+0000"))
                ));
        mockMvc.perform(get("/company?symbols=FB,SNAP&from=1&to=1531310506000"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"FB\",\"logo\":\"https://storage.googleapis.com/iex/api/logos/FB.png\",\"prices\":[{\"price\":203.53,\"date\":\"2018-07-11T09:29:44.782+0000\"},{\"price\":203.54,\"date\":\"2018-07-11T09:29:51.177+0000\"}]},{\"name\":\"SNAP\",\"logo\":\"https://storage.googleapis.com/iex/api/logos/SNAP.png\",\"prices\":[{\"price\":13.305,\"date\":\"2018-07-11T09:29:44.480+0000\"},{\"price\":13.306,\"date\":\"2018-07-11T09:29:50.908+0000\"}]}]"));

        verify(companyRepository).findCompaniesByNameIn(Arrays.asList("FB","SNAP"));
        verify(companyPriceRepository).findCompanyPriceByCompanyIdAndDateIsBetween("FB",new Date(1), new Date(1531310506000L));
        verify(companyPriceRepository).findCompanyPriceByCompanyIdAndDateIsBetween("SNAP",new Date(1), new Date(1531310506000L));
    }

    @Test
    public void sendWrongCompanySymbols()throws Exception {
        when(companyRepository.findCompaniesByNameIn(anyList())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/company?symbols=FB,SNAP"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(companyRepository).findCompaniesByNameIn(anyList());
    }

    @Test
    public void sendOnlyFromDate() throws Exception{
        List<Company> companies = Arrays.asList(
                new Company("FB", "https://storage.googleapis.com/iex/api/logos/FB.png")
        );
        when(companyRepository.findCompaniesByNameIn(anyList())).thenReturn(companies);
        when(companyPriceRepository.findCompanyPriceByCompanyIdAndDateIsAfter(anyString(),any(Date.class)))
                .thenReturn(Arrays.asList(
                        new CompanyPrice("FB",203.53,df.parse("2018-07-11T09:29:44.782+0000")),
                        new CompanyPrice("FB",203.54,df.parse("2018-07-11T09:29:51.177+0000"))
                ));
        mockMvc.perform(get("/company?symbols=FB&from=10000"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"FB\",\"logo\":\"https://storage.googleapis.com/iex/api/logos/FB.png\",\"prices\":[{\"price\":203.53,\"date\":\"2018-07-11T09:29:44.782+0000\"},{\"price\":203.54,\"date\":\"2018-07-11T09:29:51.177+0000\"}]}]"));

        verify(companyRepository).findCompaniesByNameIn(Collections.singletonList("FB"));
        verify(companyPriceRepository).findCompanyPriceByCompanyIdAndDateIsAfter("FB",new Date(10000));
    }

    @Test
    public void sendOnlyToDate() throws Exception {
        List<Company> companies = Arrays.asList(
                new Company("FB", "https://storage.googleapis.com/iex/api/logos/FB.png")
        );
        when(companyRepository.findCompaniesByNameIn(anyList())).thenReturn(companies);
        when(companyPriceRepository.findCompanyPriceByCompanyIdAndDateIsBefore(anyString(),any(Date.class)))
                .thenReturn(Arrays.asList(
                        new CompanyPrice("FB",203.53,df.parse("2018-07-11T09:29:44.782+0000")),
                        new CompanyPrice("FB",203.54,df.parse("2018-07-11T09:29:51.177+0000"))
                ));
        mockMvc.perform(get("/company?symbols=FB&to=555"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"FB\",\"logo\":\"https://storage.googleapis.com/iex/api/logos/FB.png\",\"prices\":[{\"price\":203.53,\"date\":\"2018-07-11T09:29:44.782+0000\"},{\"price\":203.54,\"date\":\"2018-07-11T09:29:51.177+0000\"}]}]"));

        verify(companyRepository).findCompaniesByNameIn(Collections.singletonList("FB"));
        verify(companyPriceRepository).findCompanyPriceByCompanyIdAndDateIsBefore("FB",new Date(555));
    }

    @Test
    public void sendFromAndToWithEmptySymbols()  throws Exception  {
        mockMvc.perform(get("/company?symbols=&from=10&to=555"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void sendFromAndToWithoutSymbols()  throws Exception  {
        mockMvc.perform(get("/company?from=10&to=555"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sendFromBiggerThenTo() throws Exception {
        mockMvc.perform(get("/company?symbols=FB&from=10000&to=1"))
                .andExpect(status().isBadRequest());
    }
}
