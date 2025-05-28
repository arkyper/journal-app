package com.arkyper.journalApp.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arkyper.journalApp.cache.AppCache;
import com.arkyper.journalApp.entity.JournalEntry;
import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.enums.Sentiment;
import com.arkyper.journalApp.repository.UserRepositoryImpl;
import com.arkyper.journalApp.service.EmailService;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private AppCache appCache;
    
    // @Scheduled(cron = "0 0 9 * * SUN")
    public void fethcUsersAndSendSAMail() {
        List<User> users = userRepositoryImpl.getUsersForSA();
        for(User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).toList();
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for(Sentiment sentiment : sentiments) {
                if(sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) +1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if(entry.getValue() > maxCount) {
                    mostFrequentSentiment = entry.getKey();
                    maxCount = entry.getValue();
                }
            }
            if(mostFrequentSentiment != null) {
                emailService.sendEmail(user.getEmail(), "Sentiment Analysis for last 7 days ", mostFrequentSentiment.toString());
            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }
}
