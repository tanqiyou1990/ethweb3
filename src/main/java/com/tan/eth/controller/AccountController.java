package com.tan.eth.controller;

import com.alibaba.fastjson.JSONObject;
import com.tan.eth.entity.Account;
import com.tan.eth.service.AccountService;
import com.tan.eth.service.dao.AccountMao;
import com.tan.eth.utils.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.math.BigDecimal;
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

    @Autowired
    private AccountMao accountMao;

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

    /**
     * 初始化账户信息
     * @param address
     * @param password
     * @param keyStore
     * @param privateKey
     * @param publicKey
     * @return
     */
    @PostMapping("/init")
    public ResultEntity init(@RequestParam String address,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) String keyStore,
                             @RequestParam String privateKey,
                             @RequestParam String publicKey){
        Account account = new Account();
        account.setAddress(address);
        account.setPassword(password);
        account.setKeyStore(keyStore);
        account.setPrivateKey(privateKey);
        account.setPublicKey(publicKey);
        accountMao.saveAccount(account);
        return ResultEntity.success("保存成功");
    }

    /**
     * 获取以太币余额
     * @param address
     * @return
     * @throws IOException
     */
    @GetMapping("/ethbalance/{address}")
    public ResultEntity ethbalance(@PathVariable("address") String address) throws IOException {
        BigDecimal banlance = accountService.getEthBanlance(address);
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
            @RequestParam String address) throws Exception {
        JSONObject banlance = accountService.getErc20Balance(address);
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
