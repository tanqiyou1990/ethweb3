package com.tan.eth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * 运行配置项
 */
@Component
public class RunModel {

	public static String KEY_STORE_PATH;

	public static String PUBLIC_PRIVATE_KEY;

	public static String REDIS_ACCOUNT_KEY_PREX;

	public static String RPC_URL;

	public static String SOCKET_URL;

	public static String CONTRACT_ADDRESS;

	public static BigInteger BLOCK_FROM;

	public static String TX_SEND_URL;

	public static String TX_SEND_PASS;

	public static BigInteger DEFAULT_GAS_LIMIT;

	public static BigInteger DEFAULT_GAS_PRICE;

	@Value("${env.key_store_path}")
	public void setKeyStorePath(String keyStorePath) {
		RunModel.KEY_STORE_PATH = keyStorePath;
	}

	@Value("${env.public_private_key}")
	public void setPublicPrivateKey(String publicPrivateKey) {
		RunModel.PUBLIC_PRIVATE_KEY = publicPrivateKey;
	}

	@Value("${env.redis_account_key_prex}")
	public void setRedisAccountKeyPrex(String redisAccountKeyPrex) {
		RunModel.REDIS_ACCOUNT_KEY_PREX = redisAccountKeyPrex;
	}

	@Value("${env.socket_url}")
	public void setSocketUrl(String socketUrl) {
		RunModel.SOCKET_URL = socketUrl;
	}

	@Value("${env.rpc_url}")
	public void setRpcUrl(String rpcUrl) {
		RunModel.RPC_URL = rpcUrl;
	}

	@Value("${env.contract_address}")
	public void setContractAddress(String contractAddress) {
		RunModel.CONTRACT_ADDRESS = contractAddress;
	}

	@Value("${env.block_from}")
	public void setBlockFrom(BigInteger blockFrom) {
		RunModel.BLOCK_FROM = blockFrom;
	}

	@Value("${env.tx_send_url}")
	public void setTxSendUrl(String txSendUrl) { RunModel.TX_SEND_URL = txSendUrl; }

	@Value("${env.tx_send_pass}")
	public void setTxSendPass(String txSendPass) { RunModel.TX_SEND_PASS = txSendPass; }

	@Value("${env.default_gas_limit}")
	public void setDefaultGasLimit(BigInteger defaultGasLimit) { RunModel.DEFAULT_GAS_LIMIT = defaultGasLimit; }

	@Value("${env.default_gas_price}")
	public void setDefaultGasPrice(BigInteger defaultGasPrice) { RunModel.DEFAULT_GAS_PRICE = defaultGasPrice; }
}
