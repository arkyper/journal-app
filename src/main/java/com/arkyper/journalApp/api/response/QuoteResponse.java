package com.arkyper.journalApp.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteResponse {
    private String quote;
    private String author;
    private String category;
} 
