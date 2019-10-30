package com.tan.eth.entity;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author by Tan
 * @create 2019/10/30/030
 */
@Data
public class Account {

    private String address;
    private String password;
    private String keyStore;
    private String privateKey;
    private String publicKey;

}
