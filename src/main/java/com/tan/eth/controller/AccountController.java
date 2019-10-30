package com.tan.eth.controller;

import com.tan.eth.service.AccountService;
import com.tan.eth.utils.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
     * @param pswd
     * @return
     * @throws IOException
     */
    @PostMapping("/create")
    public ResultEntity create(@RequestParam String pswd) throws IOException {
        String account = accountService.createAccount(pswd);
        if(StringUtils.isEmpty(account)){
            return ResultEntity.failed("生成地址失败");
        }else {
            return ResultEntity.success(account);
        }
    }

    /**
     * 获取账户列表
     * @return
     * @throws IOException
     */
    @GetMapping("/list")
    public ResultEntity list() throws IOException {
        List<String> accountList = accountService.getAccountList();
        return ResultEntity.success(accountList);
    }


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
     * 在默认账户下生成新地址
     * @return
     * @throws IOException
     * @throws CipherException
     */
    @GetMapping("/defaultAddress")
    public ResultEntity defaultAddress() throws IOException, CipherException {
        String defaultAccountAddress = accountService.createDefaultAccountAddress();
        return ResultEntity.success(defaultAccountAddress);
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
    @GetMapping("/usdtbalance/{address}")
    public ResultEntity usdtbalance(@PathVariable("address") String address) throws Exception {
        Uint256 banlance = accountService.getErc20Balance(address);
        return ResultEntity.success(banlance);
    }

}
