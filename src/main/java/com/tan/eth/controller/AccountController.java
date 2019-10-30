package com.tan.eth.controller;

import com.alibaba.fastjson.JSONObject;
import com.tan.eth.entity.Account;
import com.tan.eth.service.AccountService;
import com.tan.eth.utils.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @create 2019/10/29/029
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 创建新账户
     * @return
     * @throws IOException
     */
    @PostMapping("/create")
    public ResultEntity create(@RequestParam(required = false) String pswd) {
        Account account = null;
        String msg = null;
        try {
            account = accountService.createAccount(pswd);
        } catch (IOException e) {
            msg = e.getMessage();
        } catch (CipherException e) {
            msg = e.getMessage();
        } catch (InvalidAlgorithmParameterException e) {
            msg = e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            msg = e.getMessage();
        } catch (NoSuchProviderException e) {
            msg = e.getMessage();
        }
        if(StringUtils.isEmpty(account)){
            return ResultEntity.failed(msg);
        }else {
            return ResultEntity.success(account);
        }
    }

//    /**
//     * 获取账户列表
//     * @return
//     * @throws IOException
//     */
//    @GetMapping("/list")
//    public ResultEntity list() throws IOException {
//        List<String> accountList = accountService.getAccountList();
//        return ResultEntity.success(accountList);
//    }


    /**
     * 解锁账户
     * @param address
     * @param pswd
     * @return
     * @throws IOException
     */
    @PostMapping("/unlock")
    public ResultEntity unlock(@RequestParam String address, @RequestParam String pswd) throws IOException {
        Boolean aBoolean = accountService.unlockAccount(address, pswd);
        return ResultEntity.success(aBoolean);
    }

    /**
     * 锁定账户
     * @param address
     * @return
     * @throws IOException
     */
    @PostMapping("/lock")
    public ResultEntity lock(@RequestParam String address) throws Exception {
        Boolean aBoolean = accountService.lockAccount(address);
        return ResultEntity.success(aBoolean);
    }


    /**
     * 获取以太币余额
     * @param address
     * @return
     * @throws IOException
     */
    @GetMapping("/ethbalance/{address}")
    public ResultEntity ethbalance(@PathVariable("address") String address) throws IOException {
        String banlance = accountService.getEthBanlance(address);
        return ResultEntity.success(banlance);
    }

    /**
     * 获取USDT余额
     * @param address
     * @return
     * @throws IOException
     */
    @GetMapping("/usdtbalance")
    public ResultEntity usdtbalance(
            @RequestParam String address,
            @RequestParam String privateKey) throws Exception {
        JSONObject banlance = accountService.getErc20Balance(address, privateKey);
        return ResultEntity.success(banlance);
    }


    /**
     * 获取keyStore内容
     * @param keyStore
     * @return
     */
    @GetMapping("/keyStore")
    public ResultEntity loadKeySotre(@RequestParam String keyStore) {
        String s = accountService.loadKeyStoreContent(keyStore);
        if(StringUtils.isEmpty(s)){
            return ResultEntity.failed("未找到keyStore");
        }
        return ResultEntity.success(s);
    }

}
