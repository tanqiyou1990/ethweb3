package com.tan.eth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 运行配置项
 */
@Component
public class RunModel {

	public static String KEY_STORE_PATH;

	public static String RPC_URL;

	public static String CONTRACT_ADDRESS;

	@Value("${env.key_store_path}")
	public void setKeyStorePath(String keyStorePath) {
		RunModel.KEY_STORE_PATH = keyStorePath;
	}

	@Value("${env.rpc_url}")
	public void setRpcUrl(String rpcUrl) {
		RunModel.RPC_URL = rpcUrl;
	}

	@Value("${env.contract_address}")
	public void setContractAddress(String contractAddress) {
		RunModel.CONTRACT_ADDRESS = contractAddress;
	}
}
