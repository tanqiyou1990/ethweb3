package com.tan.eth.config;

import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.eth.UsdtContract;
import com.tan.eth.utils.RunModel;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

/**
 * @author by Tan
 * @create 2019/10/31/031
 */
@Component
public class TaskRunner implements CommandLineRunner {

    private static String privateKey = "0xaeade09175ddaa09767881c19b1bdde86fd7fcdf914afa35e6d5efceaee665de";



    @Override
    public void run(String... strings) throws Exception {
//        Web3j web3j = ConnectProvider.loadWeb3j();
//        Credentials credentials = Credentials.create(privateKey);
//        UsdtContract contract = UsdtContract.load(RunModel.CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
//        contract.transferEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
//                .subscribe(event -> {
//                    Uint256 value = event.value;
//                    BigInteger amount = value.getValue();
//                    System.out.println("from: " + event.from + ", to: " + event.to + ", value: " + amount);
//                });
    }

}
