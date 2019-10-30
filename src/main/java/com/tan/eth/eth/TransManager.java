package com.tan.eth.eth;

import com.tan.eth.utils.Environment;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
public class TransManager {

    /**
     * ERC-20Token交易（调用solidity合约方式） 推荐使用
     *
     * @param contractAddress 合约地址
     * @param to              收款地址
     * @param value           转账金额
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String transferERC20Token(Web3j web3j,
                                   String contractAddress,
                                   Address to,
                                   Uint256 value) throws Exception {

        //加载转账所需的凭证，用私钥
        Credentials credentials = WalletUtils.loadCredentials(Environment.OFFICIAL_ACCOUNT_PSWD, Environment.OFFICIAL_ACCOUNT_KEYSTORE);
        UsdtContract contract = UsdtContract.load(
                contractAddress, web3j, credentials,new DefaultGasProvider());
        boolean valid = contract.isValid();
        if(!valid) {
            throw new Exception("合约加载失败");
        }
        TransactionReceipt transactionReceipt = contract.transfer(to, value).sendAsync().get();
        String transactionHash = transactionReceipt.getTransactionHash();
        return transactionHash;
    }




}
