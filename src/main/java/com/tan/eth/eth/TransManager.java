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
     * erc20代币转账
     *
     * @param from            转账地址
     * @param to              收款地址
     * @param value           转账金额
     * @param privateKey      转账这私钥
     * @return 交易哈希
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public static String transferERC20Token(Web3j web3j, String from, String to, BigInteger value, String privateKey) throws ExecutionException, InterruptedException, IOException {
        //加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        //获取nonce，交易笔数
        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send();
        if (ethGetTransactionCount == null) {
            return null;
        }
        nonce = ethGetTransactionCount.getTransactionCount();
        //gasPrice和gasLimit 都可以手动设置
        BigInteger gasPrice;
        EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
        if (ethGasPrice == null) {
            return null;
        }
        gasPrice = ethGasPrice.getGasPrice();
        //BigInteger.valueOf(4300000L) 如果交易失败 很可能是手续费的设置问题
        BigInteger gasLimit = BigInteger.valueOf(60000L);
        //ERC20代币合约方法
        value = value.multiply(value);
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(value)),
                Collections.singletonList(new TypeReference<Type>() {
                }));
        //创建RawTransaction交易对象
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit,
                RunModel.CONTRACT_ADDRESS, encodedFunction);

        //签名Transaction
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String hash = ethSendTransaction.getTransactionHash();
        if (hash != null) {
            return hash;
        }
        return null;
    }

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
