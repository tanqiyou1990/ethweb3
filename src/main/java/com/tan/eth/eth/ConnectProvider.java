package com.tan.eth.eth;

import com.tan.eth.utils.RunModel;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author by Tan
 * @create 2019/10/29/029
 */
public class ConnectProvider {

    public static Admin loadAdmin() {
        return Admin.build(new HttpService(RunModel.RPC_URL));
    }

    public static Web3j loadWeb3j() {
        return Web3j.build(new HttpService(RunModel.RPC_URL));
    }

    public static Web3j loadWeb3jSocket() throws URISyntaxException, ConnectException {
        WebSocketClient webSocketClient = new WebSocketClient(new URI(RunModel.SOCKET_URL));
        WebSocketService socketService = new WebSocketService(webSocketClient,true);
        socketService.connect();
        return Web3j.build(socketService);
    }

    public static Geth loadGeth() {
        return Geth.build(new HttpService(RunModel.RPC_URL));
    }
}
