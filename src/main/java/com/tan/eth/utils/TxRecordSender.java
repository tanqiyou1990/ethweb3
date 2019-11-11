package com.tan.eth.utils;

import com.alibaba.fastjson.JSONObject;
import com.tan.eth.entity.TxRecord;
import lombok.extern.slf4j.Slf4j;

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
    private Map<String, String> param;

    public TxRecordSender(TxRecord record) {
        String money = record.getAmount().toString();
        String userAddress = record.getTo();
        String accounthash = record.getTxHash();
        Map<String, String> param = new HashMap<>();
        param.put("money", money);
        param.put("userAddress",userAddress);
        param.put("accounthash",accounthash);
        String sign = HashKit.sha1(money + userAddress + accounthash + RunModel.TX_SEND_PASS);
        param.put("sign",sign);
        this.record = record;
        this.param = param;
    }

//    public static void main(String[] args) {
//        String sign = HashKit.sha1("30x15b342f70f6bdef73f93a55c0a78ba116cbd69840x29f88009e9218afc4d3f938a46f4f9ace0096ff2c2d2da75eab8509c98d7c6cfHZDCsU5V");
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
