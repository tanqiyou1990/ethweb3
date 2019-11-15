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
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;

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
    public ResultEntity transInfoByHash(@PathVariable("hash") String hash){
        Transaction transaction = null;
        try {
            transaction = transService.transactionInfoByHash(hash);
        } catch (IOException e) {
            return ResultEntity.failed(e.getMessage());
        }
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
            @RequestParam BigInteger amount) {
        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = transService.transferUsdt(privateKey, dstAddress, amount);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
        return ResultEntity.success(transactionReceipt);
    }

    /**
     *
     * @param privateKey
     * @param dstAddress
     * @param amount    单位ETH
     * @param gasPrice  单位Gwei
     * @param gasLimit
     * @return
     * @throws Exception
     */
   @PostMapping("/usdtIndTrans")
    public ResultEntity usdtIndTrans(
            @RequestParam String privateKey,
            @RequestParam String dstAddress,
            @RequestParam BigInteger amount,
            @RequestParam BigDecimal gasPrice,
            @RequestParam BigInteger gasLimit){
        BigDecimal prices = Convert.toWei(gasPrice, Convert.Unit.GWEI);
       TransactionReceipt transactionReceipt = null;
       try {
           transactionReceipt = transService.transferUsdt(privateKey, dstAddress, amount, prices.toBigInteger(), gasLimit);
       } catch (Exception e) {
           return ResultEntity.failed(e.getMessage());
       }
       return ResultEntity.success(transactionReceipt);
    }

    /**
     * ETH转账
     * @param privateKey
     * @param dstAddress
     * @param amount
     * @return
     * @throws Exception
     */
    @PostMapping("/ethTrans")
    public ResultEntity ethTransDefault(
            @RequestParam String privateKey,
            @RequestParam String dstAddress,
            @RequestParam BigDecimal amount){
        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = transService.transferEth(privateKey,dstAddress,amount);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
        return ResultEntity.success(transactionReceipt);
    }

    /**
     * 自定义交易费用转账ETH
     * @param srcAddress
     * @param privateKey
     * @param dstAddress
     * @param amount    单位 ETH
     * @param gasPrice  单位 Gwei
     * @param gasLimit
     * @return
     * @throws Exception
     */
    @PostMapping("/ethIndTrans")
    public ResultEntity ethTrans(
            @RequestParam String srcAddress,
            @RequestParam String privateKey,
            @RequestParam String dstAddress,
            @RequestParam BigDecimal amount,
            @RequestParam BigDecimal gasPrice,
            @RequestParam(required = false, defaultValue = "21000") BigInteger gasLimit){
        BigDecimal value = Convert.toWei(amount, Convert.Unit.ETHER);
        BigDecimal prices = Convert.toWei(gasPrice, Convert.Unit.GWEI);
        EthSendTransaction transaction = null;
        try {
            transaction = transService.transferEth(srcAddress, dstAddress, value.toBigInteger(), privateKey, prices.toBigInteger(), gasLimit);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
        if(transaction.getError()!=null){
            return ResultEntity.failed(transaction.getError().getMessage());
        }
        return ResultEntity.success(transaction);
    }

    /**
     * 查询区块信息
     * @param blockNumber
     * @return
     * @throws IOException
     */
    @GetMapping("/blockDetail/{block}")
    public ResultEntity blockDetail(@PathVariable("block") Integer blockNumber) {
        EthBlock block = null;
        try {
            block = transService.getBlockEthBlock(blockNumber);
        } catch (IOException e) {
            return ResultEntity.failed(e.getMessage());
        }
        return ResultEntity.success(block);
    }

}
