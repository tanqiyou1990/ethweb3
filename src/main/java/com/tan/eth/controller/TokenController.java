package com.tan.eth.controller;

import com.alibaba.fastjson.JSONObject;
import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.eth.TokenManager;
import com.tan.eth.eth.UsdtContract;
import com.tan.eth.service.TransService;
import com.tan.eth.utils.ResultEntity;
import com.tan.eth.utils.RunModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

/**
 * @create 2019/10/29/029
 */
@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {




    @GetMapping("/info")
    public ResultEntity symbol(@RequestParam String privateKey) throws Exception {
        Web3j web3j = ConnectProvider.loadWeb3j();
        Credentials credentials = Credentials.create(privateKey);
        UsdtContract contract = UsdtContract.load(RunModel.CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());

        JSONObject object = new JSONObject();
        Utf8String symbol = contract.symbol().send();
        Utf8String name = contract.name().send();
        Uint256 decimal = contract.decimals().send();
        object.put("symbol",symbol);
        object.put("name",name);
        object.put("decimal",decimal);
        return ResultEntity.success(object);
    }

    @PutMapping("/deploy")
    public ResultEntity deploy(@RequestParam String privateKey) throws Exception {
        Web3j web3j = ConnectProvider.loadWeb3j();
        String s = TokenManager.contractDeploy(web3j, privateKey);
        return ResultEntity.success(s);
    }

}
