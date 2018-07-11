package com.example.sampletask.schedule;

import com.example.sampletask.RequestUtil;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SampletaskApplication.class})
@SpringBootTest
@WebAppConfiguration
public class ScheduledTasksTest {

    @Autowired
    private ScheduledTasks scheduledTasks;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyPriceRepository companyPriceRepository;
    @Autowired
    private RequestUtil requestUtil;

    @Before
    public void setUp() throws Exception {
        reset(companyRepository, companyPriceRepository, requestUtil);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(companyRepository, companyPriceRepository, requestUtil);
    }


    @Test
    public void invokeGrabOnce() throws Exception {
        when(companyRepository.findCompaniesByNameIn(anyList())).thenReturn(Collections.emptyList());
        when(requestUtil.get(anyString()))
                .thenReturn("[{\"symbol\":\"SNAP\",\"price\":13.305},{\"symbol\":\"FB\",\"price\":203.53}]")
                .thenReturn("{\"url\":\"https://storage.googleapis.com/iex/api/logos/SNAP.png\"}")
                .thenReturn("{\"url\":\"https://storage.googleapis.com/iex/api/logos/FB.png\"}");

        scheduledTasks.grab();

        verify(companyRepository).findCompaniesByNameIn(Arrays.asList("SNAP","FB","AIG+"));
        verify(requestUtil).get("https://api.iextrading.com/1.0/tops/last?symbols=SNAP%2Cfb%2CAIG%2B&filter=symbol,price");
        verify(requestUtil).get("https://api.iextrading.com/1.0/stock/SNAP/logo");
        verify(requestUtil).get("https://api.iextrading.com/1.0/stock/FB/logo");
        verify(companyRepository).save(new Company("SNAP","https://storage.googleapis.com/iex/api/logos/SNAP.png"));
        verify(companyRepository).save(new Company("FB","https://storage.googleapis.com/iex/api/logos/FB.png"));
        verify(companyPriceRepository).save(new CompanyPrice("SNAP",13.305));
        verify(companyPriceRepository).save(new CompanyPrice("FB",203.53));

    }
}
