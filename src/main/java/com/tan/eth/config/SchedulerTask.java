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
            for(TxRecord item : unSendRecords) {
                log.warn("==================>發送交易：{}", item.getTxHash());
                Map<String, String> params = new HashMap<>();
                params.put("money", item.getAmount().toString());
                params.put("userAddress", item.getTo());
                params.put("accounthash", item.getTxHash());
                String sign = HashKit.sha1(item.getAmount().toString() + "." + item.getTo() + "." + item.getTxHash() + "." + RunModel.TX_SEND_PASS);
                params.put("sign",sign);
                try {
                    Response response = HttpUtil.txSendPost(params);
                    log.warn("请求返回结果：{}",response.toString());
                    if(response.isSuccess()){
                        JSONObject retObj = JSONObject.parseObject(response.toString());
                        if(retObj.getInteger("status") == 1){
                            log.warn("发送成功");
                            //判斷如果發送成功
                            BathUpdateOptions opts = txRecordMao.getBathUpdateOptions(item.getTxHash());
                            bathUpdateOptions.add(opts);
                        }else {
                            log.warn("发送失败:{}",retObj.getString("msg"));
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("发送交易失败:{}",e.getMessage());
                }
            }
            if(bathUpdateOptions.size() > 0) {
                log.warn("發送成功:{}",bathUpdateOptions.size());
                txRecordMao.bathUpdateSendFlag(bathUpdateOptions);
            }
        }
    }
}
