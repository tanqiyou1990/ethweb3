package com.tan.eth.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tan.eth.entity.TxRecord;
import com.tan.eth.service.dao.TxRecordMao;
import com.tan.eth.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author by Tan
 * @create 2019/11/10/010
 */
@Component
@Slf4j
public class SchedulerTask {

    @Autowired
    private TxRecordMao txRecordMao;


    /**
     * 发送交易信息到客户端
     */
    @Scheduled(fixedDelay = 10000)
    public void sendTxRecords() {
        //载入尚未完成发送的交易信息
        List<TxRecord> unSendRecords = txRecordMao.findUnSendRecords(20);
        if(unSendRecords != null && unSendRecords.size() > 0){
            List<BathUpdateOptions> bathUpdateOptions = new ArrayList<>();
            ExecutorService exec= Executors.newCachedThreadPool();
            ArrayList<Future<TxRecord>> results=new ArrayList<>();
            //多线程发送
            for(TxRecord item : unSendRecords) {
                results.add(exec.submit(new TxRecordSender(item)));
            }

            for(Future<TxRecord> fs: results) {
                try {
                    TxRecord txRecord = fs.get();
                    if(txRecord.getSendFlag()) {
                        BathUpdateOptions updateOptions = txRecordMao.getBathUpdateOptions(txRecord.getTxHash());
                        bathUpdateOptions.add(updateOptions);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }finally {
                    exec.shutdown();
                }
            }
            log.warn("发送成功数量:{}",bathUpdateOptions.size());
            if(bathUpdateOptions.size() > 0) {
                txRecordMao.bathUpdateSendFlag(bathUpdateOptions);
            }
        }
    }
}
