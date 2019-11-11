package com.tan.eth.service.dao;

import com.tan.eth.entity.TxRecord;
import com.tan.eth.utils.BathUpdateOptions;
import com.tan.eth.utils.BathUpdateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/11/10/010
 */
@Repository
public class TxRecordMao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public BathUpdateOptions getBathUpdateOptions(String txHash) {
        BathUpdateOptions options = new BathUpdateOptions();
        Query query = new Query();
        //查询条件
        query.addCriteria(Criteria.where("sendFlag").is(false));
        query.addCriteria(Criteria.where("txHash").is(txHash));
        options.setQuery(query);
        //mongodb 默认是false,只更新找到的第一条记录，如果这个参数为true,就把按条件查出来多条记录全部更新。
        options.setMulti(true);
        Update update = new Update();
        //更新内容
        update.set("sendFlag", true);
        options.setUpdate(update);
        return options;
    }

    public void saveTxRecord(TxRecord record) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("txHash").is(record.getTxHash()))
        );
        AggregationResults<TxRecord> tx_record = mongoTemplate.aggregate(agg, "tx_record", TxRecord.class);
        List<TxRecord> mappedResults = tx_record.getMappedResults();
        if(mappedResults == null || mappedResults.size() == 0){
            mongoTemplate.save(record);
        }
    }

    /**
     * 批量更新sendFlag
     * @param bups
     */
    public void bathUpdateSendFlag(List<BathUpdateOptions> bups) {
        BathUpdateUtil.bathUpdate(mongoTemplate, TxRecord.class, bups);
    }


    /**
     * 查找尚未发送的交易消息
     * @return
     */
    public List<TxRecord> findUnSendRecords(Integer limit) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("sendFlag").is(false)),
                Aggregation.sort(Direction.ASC, "blockNumber"),
                Aggregation.limit(limit)
        );
        AggregationResults<TxRecord> tx_record = mongoTemplate.aggregate(agg, "tx_record", TxRecord.class);
        List<TxRecord> mappedResults = tx_record.getMappedResults();
        return mappedResults;
    }

    /**
     * 获取已经监听的最高区块
     * @return
     */
    public BigInteger getLatestBlockNumber() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Direction.DESC, "blockNumber"),
                Aggregation.limit(1)
        );
        AggregationResults<TxRecord> tx_records = mongoTemplate.aggregate(agg, "tx_record", TxRecord.class);
        List<TxRecord> tx_list = tx_records.getMappedResults();
        if(tx_list != null && tx_list.size() > 0) {
            TxRecord txRecord = tx_list.get(0);
            return txRecord.getBlockNumber();
        }else {
            return null;
        }
    }

}
