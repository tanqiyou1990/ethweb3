package com.tan.eth.eth;

import com.tan.eth.utils.RunModel;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
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

    public static TransactionReceipt  transferUsdt(Web3j web3j, String privateKey, String to, BigInteger value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        UsdtGasProvider gasProvider = new UsdtGasProvider(gasPrice, gasLimit);
        UsdtContract contract = UsdtContract.load(RunModel.CONTRACT_ADDRESS, web3j, credentials, gasProvider);
        TransactionReceipt receipt = contract.transfer(new Address(to), new Uint256(value)).send();
        web3j.shutdown();
        return receipt;
    }


    /**
     * ETH交易转账
     * @param web3j
     * @param privKey
     * @param to
     * @param amount
     * @return
     * @throws Exception
     */
    public static  TransactionReceipt transferEth(Web3j web3j, String privKey, String to, BigDecimal amount) throws Exception {
        Credentials credentials = Credentials.create(privKey);
        TransactionReceipt transferReceipt = Transfer.sendFunds(web3j, credentials, to,amount,  Convert.Unit.ETHER).send();
        web3j.shutdown();
        return transferReceipt;
    }


    /**
     * 发起一笔交易（自定义参数）
     *
     * @param from       发起人钱包地址
     * @param to         转入的钱包地址
     * @param value      转账金额，单位是wei
     * @param privateKey 钱包私钥
     * @param gasPrice   转账费用
     * @param gasLimit
     * @throws IOException
     * @throws CipherException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static EthSendTransaction transfer(
            Web3j web3j,
            String from,
            String to,
            BigInteger value,
            String privateKey,
            BigInteger gasPrice,
            BigInteger gasLimit) throws IOException, CipherException, ExecutionException, InterruptedException {


        //加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        //获取nonce，交易笔数
        BigInteger nonce = getNonce(web3j,from);
        //创建RawTransaction交易对象
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
        //签名Transaction，这里要对交易做签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        web3j.shutdown();
        return ethSendTransaction;
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


    /**
     * 获取nonce，交易笔数
     *
     * @param from
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static BigInteger getNonce(Web3j web3j, String from) throws ExecutionException, InterruptedException {
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = transactionCount.getTransactionCount();
        return nonce;
    }




}
