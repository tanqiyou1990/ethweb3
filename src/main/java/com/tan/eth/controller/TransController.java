package com.tan.eth.controller;

import com.tan.eth.entity.Account;
import com.tan.eth.service.AccountService;
import com.tan.eth.service.TransService;
import com.tan.eth.utils.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;

/**
 * @create 2019/10/29/029
 */
@Slf4j
@RestController
@RequestMapping("/trans")
public class TransController {


    @Autowired
    private TransService transService;


    /**
     * 根据交易哈希查找交易信息
     * @param hash
     * @return
     * @throws IOException
     */
    @GetMapping("/transInfoByHash/{hash}")
    public ResultEntity transInfoByHash(@PathVariable("hash") String hash) throws IOException {
        Optional<Transaction> transaction = transService.transactionInfoByHash(hash);
        return ResultEntity.success(transaction);
    }


    /**
     * USDT转账交易
     * @param privateKey
     * @param dstAddress
     * @param amount
     * @return
     * @throws Exception
     */
    @PostMapping("/usdtTrans")
    public ResultEntity usdtTrans(
            @RequestParam String privateKey,
            @RequestParam String dstAddress,
            @RequestParam BigInteger amount) throws Exception {
        TransactionReceipt transactionReceipt = transService.transferUsdt(privateKey, dstAddress, amount);
        return ResultEntity.success(transactionReceipt);
    }


    /**
     * 查询区块信息
     * @param blockNumber
     * @return
     * @throws IOException
     */
    @GetMapping("/blockDetail/{block}")
    public ResultEntity blockDetail(@PathVariable("block") Integer blockNumber) throws IOException {
        EthBlock block = transService.getBlockEthBlock(blockNumber);
        return ResultEntity.success(block);
    }

}
