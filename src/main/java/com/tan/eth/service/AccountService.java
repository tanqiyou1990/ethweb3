package com.tan.eth.service;


import com.alibaba.fastjson.JSONObject;
import com.tan.eth.entity.Account;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
@Service
public interface AccountService {

    /**
     * 创建账户
     * @return
     * @throws IOException
     */
    Account createAccount(String pwd) throws IOException, CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException;

    /**
     * 获取账户列表
     * @return
     */
    List<String> getAccountList() throws IOException;

    /**
     * 获取keyStore内容
     * @param keyStore
     * @return
     */
    String loadKeyStoreContent(String keyStore);

    /**
     * 解锁账户
     * @param address
     * @param passwd
     * @return
     */
    Boolean unlockAccount(String address, String passwd) throws IOException;

    /**
     * 账户锁定
     * @param address
     * @return
     */
    Boolean lockAccount(String address) throws Exception;

    /**
     * 获取账户以太余额
     * @param address
     * @return
     */
    BigDecimal getEthBanlance(String address) throws IOException;


    /**
     * 获取代币地址（USDT）
     * @param address
     * @return
     */
    JSONObject getErc20Balance(String address) throws Exception;

}
