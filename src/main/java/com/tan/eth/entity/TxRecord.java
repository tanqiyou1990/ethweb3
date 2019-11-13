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
    private Boolean sendFlag;
    private Long createTime;
    private Long sendTime;
}
