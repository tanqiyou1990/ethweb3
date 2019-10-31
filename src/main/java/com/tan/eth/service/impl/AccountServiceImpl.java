package com.tan.eth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tan.eth.entity.Account;
import com.tan.eth.eth.AccountManager;
import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.service.AccountService;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.geth.Geth;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
@Service
public class AccountServiceImpl implements AccountService{
    @Override
    public Account createAccount(String pwd) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        Account wallet = AccountManager.createWallet(pwd);
        return wallet;
    }

    @Override
    public List<String> getAccountList() throws IOException {
        Admin admin = ConnectProvider.loadAdmin();
        List<String> accountList = AccountManager.getAccountList(admin);
        return accountList;
    }

    @Override
    public String loadKeyStoreContent(String keyStore) {
        return AccountManager.keyStoreContent(keyStore);
    }

    @Override
    public Boolean unlockAccount(String address, String passwd) throws IOException {
        Admin admin = ConnectProvider.loadAdmin();
        Boolean unlockAccount = AccountManager.unlockAccount(admin, address, passwd);
        return unlockAccount;
    }

    @Override
    public Boolean lockAccount(String address) throws Exception {
        Geth geth = ConnectProvider.loadGeth();
        Boolean response = AccountManager.lockAccount(geth, address);
        return response;
    }

    @Override
    public BigDecimal getEthBanlance(String address) throws IOException {
        Web3j web3j = ConnectProvider.loadWeb3j();
        BigDecimal ethBanlance = AccountManager.getEthBanlance(web3j, address);
        return ethBanlance;
    }

    @Override
    public JSONObject getErc20Balance(String address, String privateKey) throws Exception {
        Web3j web3j = ConnectProvider.loadWeb3j();
        Address adr = new Address(address);
        JSONObject erc20Balance = AccountManager.getERC20Balance(web3j, privateKey, address);
        return erc20Balance;
    }
}
