package com.tan.eth.eth;

import com.tan.eth.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 账号管理相关
 */
@Slf4j
public class AccountManager {


	/**
	 * 创建账号
	 */
	public static  String createNewAccount(Admin admin, String password) throws IOException {
		NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
		String address = newAccountIdentifier.getAccountId();
		admin.shutdown();
		return address;
	}

	public static String newAddress() throws IOException, CipherException {
		Credentials credentials = WalletUtils.loadCredentials(Environment.OFFICIAL_ACCOUNT_PSWD,Environment.OFFICIAL_ACCOUNT_KEYSTORE);
		String address = credentials.getAddress();
		return address;
	}


	/**
	 * 获取账号列表
	 */
	public static List<String> getAccountList(Admin admin) throws IOException {
		PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
		List<String> addressList;
		addressList = personalListAccounts.getAccountIds();
		admin.shutdown();
		return addressList;
	}

	/**
	 * 账号解锁
	 */
	public static Boolean unlockAccount(Admin admin, String address, String password) throws IOException {
		//账号解锁持续时间 单位秒 缺省值300秒
		BigInteger unlockDuration = BigInteger.valueOf(60L);
		PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password, unlockDuration).send();
		Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
		admin.shutdown();
		return isUnlocked;
	}


	/**
	 * 查询以太币账户余额
	 * @throws IOException
	 */
	public static  String getEthBanlance(Web3j web3j,String userAddress) throws IOException {
		//获取指定钱包的以太币余额
		BigInteger integer=web3j.ethGetBalance(userAddress, DefaultBlockParameterName.LATEST).send().getBalance();
		//eth默认会部18个0这里处理比较随意
		String decimal = toDecimal(18,integer);
		return decimal;
	}

	/**
	 * 获取ERC-20 token指定地址余额
	 *
	 * @param address         查询地址
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static Uint256 getERC20Balance(Web3j web3j, Address address) throws Exception {
		//加载合约
		Credentials credentials = WalletUtils.loadCredentials(Environment.OFFICIAL_ACCOUNT_PSWD, Environment.OFFICIAL_ACCOUNT_KEYSTORE);
		UsdtContract usdt = UsdtContract.load(Environment.CONTRACT_ADDRESS,web3j,credentials, new DefaultGasProvider());
		boolean valid = usdt.isValid();
		if(!valid) {
			throw new Exception("合约加载失败");
		}
		RemoteCall<Uint256> uint256TotalSupply = usdt._totalSupply();
		log.warn("总发行量:{}",uint256TotalSupply.send().toString());
		RemoteCall<Uint256> uint256RemoteCall = usdt.balanceOf(address);
		return uint256RemoteCall.send();
	}

	/**
	 * 转换成符合 decimal 的数值
	 * @param decimal
	 * @return
	 */
	public static String toDecimal(int decimal,BigInteger integer){
		StringBuffer sbf = new StringBuffer("1");
		for (int i = 0; i < decimal; i++) {
			sbf.append("0");
		}
		String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
		return balance;
	}


}
