package com.tan.eth.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author by Tan
 * @create 2019/11/9/009
 */
@Data
@Document(collection="tx_record")
public class TxRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String from;
    private String to;
    private BigInteger amount;
    private String txHash;
    private String blockHash;
    private BigInteger blockNumber;
    /**
     * 转账的币种：2-以太坊;1-USDT
     */
    private Integer coin;
    /**
     * 0:待发送
     * 1:发送成功
     * -1:发送失败20次后取消发送
     */
    private String sendFlag;
    private Long createTime;
    private Long sendTime;
}
