package com.arkyper.journalApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.arkyper.journalApp.api.response.QuoteResponse;
import com.arkyper.journalApp.cache.AppCache;


@Service
public class QuotesService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTempelate;
    
    @Autowired
    private AppCache appCache;

    public List<QuoteResponse> getQuote() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<QuoteResponse>> response = restTempelate.exchange(appCache.getValue("quotes_api"), HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<QuoteResponse>>() {});
        return response.getBody();
    }
 }
