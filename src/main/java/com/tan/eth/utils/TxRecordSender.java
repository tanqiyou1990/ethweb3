package com.tan.eth.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tan.eth.entity.TxRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author by Tan
 * @create 2019/11/11/011
 */
@Slf4j
public class TxRecordSender implements Callable<TxRecord> {


    private TxRecord record;
    private MultiValueMap<String, Object> param;

    public TxRecordSender(TxRecord record) {
        Float money = record.getAmount().floatValue();
        String userAddress = record.getTo();
        String accounthash = record.getTxHash();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("money", money.toString());
        param.add("userAddress",userAddress);
        param.add("accounthash",accounthash);
        String sign = HashKit.sha1(money.toString() + userAddress + accounthash + RunModel.TX_SEND_PASS);
        param.add("sign",sign);
        this.param = param;
        this.record = record;
    }

//    public static void main(String[] args) {
//        String sign = HashKit.sha1("30x7583742d2856422e96c3ee0596806f278df6beae0x29f88009e9218afc4d3f938a46f4f9ace0096ff2c2d2da75eab8509c98d7c6cfHZDCsU5V");
//        System.out.println(sign);
//    }

    @Override
    public TxRecord call() throws Exception {
        String response = HttpUtil.txSendPost(param);
        log.warn("请求返回结果：{}",response.toString());
        JSONObject retObj = JSONObject.parseObject(response);
        if(retObj.getInteger("status") == 1){
            log.warn("发送成功");
            record.setSendFlag(true);
            return record;
        }else {
            log.warn("发送失败:{}",retObj.getString("msg"));
            record.setSendFlag(false);
            return record;
        }
    }
}
