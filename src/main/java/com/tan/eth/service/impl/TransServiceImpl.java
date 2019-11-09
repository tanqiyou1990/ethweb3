package com.tan.eth.service.impl;

import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.eth.TransManager;
import com.tan.eth.service.TransService;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author by Tan
 * @create 2019/10/30/030
 */
@Service
public class TransServiceImpl implements TransService {
    @Override
    public Optional<Transaction> transactionInfoByHash(String hash) throws IOException {
        Web3j web3j = ConnectProvider.loadWeb3j();
        EthTransaction transactionByHash = TransManager.getTransactionByHash(web3j, hash);
        return transactionByHash.getTransaction();
    }

    @Override
    public TransactionReceipt transferUsdt(String privateKey, String to, BigInteger value) throws Exception {
        Web3j web3j = ConnectProvider.loadWeb3j();
        TransactionReceipt transactionReceipt = TransManager.transferUsdt(web3j, privateKey, to, value);
        return transactionReceipt;
    }

    @Override
    public TransactionReceipt transferUsdt(String privateKey, String to, BigInteger value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Web3j web3j = ConnectProvider.loadWeb3j();
        TransactionReceipt transactionReceipt = TransManager.transferUsdt(web3j, privateKey, to, value, gasPrice, gasLimit);
        return transactionReceipt;
    }

    @Override
    public EthBlock getBlockEthBlock(Integer blockNumber) throws IOException {
        Web3j web3j = ConnectProvider.loadWeb3j();
        EthBlock block = TransManager.getBlockEthBlock(web3j, blockNumber);
        return block;
    }

    @Override
    public TransactionReceipt transferEth(String privKey, String to, BigDecimal amount) throws Exception {
        TransactionReceipt receipt = TransManager.transferEth(ConnectProvider.loadWeb3j(), privKey, to, amount);
        return receipt;
    }

    @Override
    public EthSendTransaction transferEth(String from, String to, BigInteger value, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws InterruptedException, ExecutionException, CipherException, IOException {
        Web3j web3j = ConnectProvider.loadWeb3j();
        EthSendTransaction transaction = TransManager.transfer(web3j, from, to, value, privateKey, gasPrice, gasLimit);
        return transaction;
    }
}
