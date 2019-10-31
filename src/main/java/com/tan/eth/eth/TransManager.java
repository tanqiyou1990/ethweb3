package com.tan.eth.eth;

import com.tan.eth.utils.RunModel;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
public class TransManager {

    /**
     * ERC-20Token交易（调用solidity合约方式） 推荐使用
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static TransactionReceipt  transferUsdt(Web3j web3j, String privateKey, String to, BigInteger value) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        UsdtContract contract = UsdtContract.load(RunModel.CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
        TransactionReceipt receipt = contract.transfer(new Address(to), new Uint256(value)).send();
        web3j.shutdown();
        return receipt;
    }

    /**
     * 根据交易哈希查找交易信息
     * @param web3j
     * @param hash
     * @return
     * @throws IOException
     */
    public static EthTransaction getTransactionByHash(Web3j web3j, String hash) throws IOException {
        EthTransaction send = web3j.ethGetTransactionByHash(hash).send();
        web3j.shutdown();
        return send;
    }


    /**
     * 根据区块链高度查询区块信息
     * @param web3j
     * @param blockNumber
     * @return
     * @throws IOException
     */
    public static EthBlock getBlockEthBlock(Web3j web3j, Integer blockNumber) throws IOException {
        DefaultBlockParameterNumber defaultBlockParameter = new DefaultBlockParameterNumber(blockNumber);
        EthBlock block = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
        web3j.shutdown();
        return block;
    }




}
