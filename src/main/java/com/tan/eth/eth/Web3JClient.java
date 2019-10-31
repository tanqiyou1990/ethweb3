package com.tan.eth.eth;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.parity.methods.response.ParityExportAccount;
import org.web3j.protocol.parity.methods.response.ParityTracesResponse;
import org.web3j.protocol.parity.methods.response.Trace;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.NoOpProcessor;

import java.math.BigInteger;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/10/31/031
 */
public class Web3JClient {
    private static Web3jService service = new HttpService("https://rinkeby.infura.io/v3/0cc88548dd8d42f48144dcea5739074e"); // put fullnode url here
    private static Web3j web3j = Web3j.build(service);
    private static Parity parity = Parity.build(service);

    public static List<Trace> getCallActionsInBlock(long blockNum) throws Exception {
        DefaultBlockParameterNumber number = new DefaultBlockParameterNumber(blockNum);
        Request<?, ParityTracesResponse> request = parity.traceBlock(number);
        ParityTracesResponse response = request.send();
        return response.getTraces();
    }

    public static List<Trace> getCallAction(String hash) throws Exception {
        Request<?, ParityTracesResponse> request = parity.traceTransaction(hash);
        ParityTracesResponse response = request.send();
        return response.getTraces();
    }

    public static WalletFile exportAccount(String address, String password) throws Exception {
        Request<?, ParityExportAccount> request = parity.parityExportAccount(address, password);
        ParityExportAccount response = request.send();
        return response.getWallet();
    }

    public static void main(String[] args) throws Exception {
    }
}
