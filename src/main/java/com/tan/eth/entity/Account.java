package com.tan.eth.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author by Tan
 * @create 2019/10/30/030
 */
@Data
@Document(collection="account_record")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private String address;
    private String password;
    private String keyStore;
    private String privateKey;
    private String publicKey;
    private Long createTime;

}
