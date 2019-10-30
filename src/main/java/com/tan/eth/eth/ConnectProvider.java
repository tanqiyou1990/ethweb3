package com.tan.eth.eth;

import com.tan.eth.utils.Environment;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
public class ConnectProvider {

    public static Admin loadAdmin() {
        return Admin.build(new HttpService(Environment.RPC_URL));
    }

    public static Web3j loadWeb3j() {
        return Web3j.build(new HttpService(Environment.RPC_URL));
    }
}
