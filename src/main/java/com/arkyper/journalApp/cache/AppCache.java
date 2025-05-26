package com.arkyper.journalApp.cache;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.arkyper.journalApp.entity.ConfigJournalAppEntity;
import com.arkyper.journalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;

@Component
public class AppCache {
    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    private Map<String, String> APP_CACHE;

    @PostConstruct
    public void init() {
        APP_CACHE = new HashMap<>();
        List<ConfigJournalAppEntity> allKeyValues = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity entity : allKeyValues) {
            APP_CACHE.put(entity.getKey(), entity.getValue());
        }
    }

    public String getValue(String key) {
        return APP_CACHE.get(key);
    }
}