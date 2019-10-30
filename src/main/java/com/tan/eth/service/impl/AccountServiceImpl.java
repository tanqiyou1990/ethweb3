package com.tan.eth.service.impl;

import com.tan.eth.eth.AccountManager;
import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.service.AccountService;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;

import java.io.IOException;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
@Service
public class AccountServiceImpl implements AccountService{
    @Override
    public String createAccount(String pswd) throws IOException {
        Admin admin = ConnectProvider.loadAdmin();
        String newAccount = AccountManager.createNewAccount(admin,pswd);
        admin.shutdown();
        return newAccount;
    }

    @Override
    public String createDefaultAccountAddress() throws IOException, CipherException {
        String s = AccountManager.newAddress();
        return s;
    }

    @Override
    public List<String> getAccountList() throws IOException {
        Admin admin = ConnectProvider.loadAdmin();
        List<String> accountList = AccountManager.getAccountList(admin);
        admin.shutdown();
        return accountList;
    }

    @Override
    public Boolean unlockAccount(String address, String passwd) throws IOException {
        Admin admin = ConnectProvider.loadAdmin();
        Boolean unlockAccount = AccountManager.unlockAccount(admin, address, passwd);
        admin.shutdown();
        return unlockAccount;
    }

    @Override
    public String getEthBanlance(String address) throws IOException {
        Web3j web3j = ConnectProvider.loadWeb3j();
        String ethBanlance = AccountManager.getEthBanlance(web3j, address);
        web3j.shutdown();
        return ethBanlance;
    }

    @Override
    public Uint256 getErc20Balance(String address) throws Exception {
        Web3j web3j = ConnectProvider.loadWeb3j();
        Address adr = new Address(address);
        Uint256 erc20Balance = AccountManager.getERC20Balance(web3j, adr);
        web3j.shutdown();
        return erc20Balance;
    }
}
