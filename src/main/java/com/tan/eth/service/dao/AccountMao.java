package com.tan.eth.service.dao;

import com.tan.eth.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/11/10/010
 */
@Repository
public class AccountMao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    public void saveAccount(Account account) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("address").is(account.getAddress()))
        );
        AggregationResults<Account> accountRecord = mongoTemplate.aggregate(agg, "account_record", Account.class);
        List<Account> mappedResults = accountRecord.getMappedResults();
        if(mappedResults ==null || mappedResults.size() == 0){
            mongoTemplate.save(account);
        }
        redisTemplate.opsForValue().set(account.getAddress(), account.getPrivateKey());
    }

    public List<Account> findAll(){
        List<Account> all = mongoTemplate.findAll(Account.class);
        return all;
    }

}
