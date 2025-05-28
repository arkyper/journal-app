package com.arkyper.journalApp.repository;

import com.arkyper.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$"));
        query.addCriteria(Criteria.where("isSentimentAnalysis").is(true));
        // query.addCriteria(Criteria.where("roles").in("USER", "ADMIN"));
        // query.addCriteria(Criteria.where("roles").nin("USER", "ADMIN"));
        List<User> user = mongoTemplate.find(query, User.class);
        return user;
    }

}
