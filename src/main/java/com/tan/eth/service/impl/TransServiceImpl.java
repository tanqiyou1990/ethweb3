package com.tan.eth.service.impl;

import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.eth.TransManager;
import com.tan.eth.service.TransService;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

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
    public EthBlock getBlockEthBlock(Integer blockNumber) throws IOException {
        Web3j web3j = ConnectProvider.loadWeb3j();
        EthBlock block = TransManager.getBlockEthBlock(web3j, blockNumber);
        return block;
    }
}
