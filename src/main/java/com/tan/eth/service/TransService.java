package com.tan.eth.service;


import com.tan.eth.entity.Account;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @create 2019/10/29/029
 */
@Service
public interface TransService {

    /**
     * 根据交易哈希查找交易信息
     * @param hash
     * @return
     */
    Optional<Transaction> transactionInfoByHash(String hash) throws IOException;

    /**
     * USDT转账
     * @param privateKey  转出账户密码
     * @param to    转入账户地址
     * @param value 交易数量
     * @return
     */
    TransactionReceipt transferUsdt(String privateKey, String to, BigInteger value) throws Exception;


    /**
     * 自定义费用转账USDT
     * @param privateKey
     * @param to
     * @param value
     * @param gasPrice
     * @param gasLimit
     * @return
     * @throws Exception
     */
    TransactionReceipt transferUsdt(String privateKey, String to, BigInteger value, BigInteger gasPrice, BigInteger gasLimit) throws Exception;


    /**
     * 根据区块高度查询
     * @param blockNumber
     * @return
     */
    EthBlock getBlockEthBlock(Integer blockNumber) throws IOException;


    /**
     * ETH交易转账
     * @param privKey
     * @param to
     * @param amount
     * @return
     */
    TransactionReceipt transferEth(String privKey, String to, BigDecimal amount) throws Exception;

    /**
     * 自定义发送ETH交易
     * @param from
     * @param to
     * @param value
     * @param privateKey
     * @param gasPrice
     * @param gasLimit
     * @return
     */
    EthSendTransaction transferEth(String from,String to,BigInteger value,String privateKey,BigInteger gasPrice,BigInteger gasLimit) throws InterruptedException, ExecutionException, CipherException, IOException;
}
