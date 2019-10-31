package com.tan.eth.eth;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;


/**
 * @create 2019/10/31/031
 */
@Slf4j
public class TokenManager {

    /**
     * 部署USDT合约
     * @param web3j
     * @param privkey
     * @return
     * @throws Exception
     */
    public static String contractDeploy(Web3j web3j, String privkey) throws Exception {
        Credentials credentials = Credentials.create(privkey);
        UsdtContract contract = UsdtContract.deploy(
                web3j,
                credentials,
                new DefaultGasProvider(),
                new Uint256(1000000),
                new Utf8String("my token outmama"),
                new Utf8String("OTMM"),
                new Uint256(8)).send();
        String contractAddress = contract.getContractAddress();
        return contractAddress;
    }

//    public static void main(String[] args) throws Exception {
//        Web3j web3j = Web3j.build(new HttpService("http://47.244.186.179:6667"));
//        String s = contractDeploy(web3j, "0x2fcc96f5c0ba57cdf54b17c3b59fedd7f56cb51af4d1091719c6ffa41848315a");
//        System.out.print(s);
//    }

}
