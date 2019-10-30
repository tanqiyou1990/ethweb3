package com.tan.eth.service;


import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
@Service
public interface AccountService {

    /**
     * 创建账户
     * @param pswd
     * @return
     * @throws IOException
     */
    String createAccount(String pswd) throws IOException;

    /**
     * 生成默认账户的地址
     * @return
     */
    String createDefaultAccountAddress() throws IOException, CipherException;

    /**
     * 获取账户列表
     * @return
     */
    List<String> getAccountList() throws IOException;

    /**
     * 解锁账户
     * @param address
     * @param passwd
     * @return
     */
    Boolean unlockAccount(String address, String passwd) throws IOException;

    /**
     * 获取账户以太余额
     * @param address
     * @return
     */
    String getEthBanlance(String address) throws IOException;


    /**
     * 获取代币地址（USDT）
     * @param address
     * @return
     */
    Uint256 getErc20Balance(String address) throws Exception;

}
