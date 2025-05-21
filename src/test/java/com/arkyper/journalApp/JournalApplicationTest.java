package com.arkyper.journalApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootTest
public class JournalApplicationTest {
    
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads(){};

    @Test
    void platformTransactionManagerBeanExists() {
        applicationContext.getBean(PlatformTransactionManager.class);
    }
}
