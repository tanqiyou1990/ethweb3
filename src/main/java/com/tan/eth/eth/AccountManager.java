package com.tan.eth.eth;

import com.alibaba.fastjson.JSONObject;
import com.tan.eth.entity.Account;
import com.tan.eth.utils.RunModel;
import com.tan.eth.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.geth.Geth;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 账号管理相关
 */
@Slf4j
public class AccountManager {


	/**
	 * 生成eth钱包 保存对应的keyStore(无助记词方式)
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidAlgorithmParameterException
	 * @throws CipherException
	 * @throws IOException
	 */
	public static Account createWallet(String pwd) throws NoSuchAlgorithmException, NoSuchProviderException,
			InvalidAlgorithmParameterException, CipherException, IOException {
		File file = new File(RunModel.KEY_STORE_PATH);
		if(!file.exists()){
			file.mkdirs();
		}
		ECKeyPair ecKeyPair = Keys.createEcKeyPair();
		if(StringUtils.isEmpty(pwd)){
			pwd = "";
		}
		String fileName = WalletUtils.generateWalletFile(pwd, ecKeyPair, file, false);
		BigInteger privateKey = ecKeyPair.getPrivateKey();
		BigInteger publicKey = ecKeyPair.getPublicKey();

		Credentials credentials = Credentials.create(ecKeyPair);
		String address = credentials.getAddress();
		Account wallet = new Account();
		wallet.setAddress(address);
		wallet.setPassword(pwd);
		wallet.setKeyStore(fileName);
		wallet.setPrivateKey(Numeric.encodeQuantity(privateKey));
		wallet.setPublicKey(Numeric.encodeQuantity(publicKey));
		return  wallet;
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
	 * 获取keyStore内容
	 * @param keyStore
	 * @return
     */
	public static String keyStoreContent(String keyStore) {
		String filePath = RunModel.KEY_STORE_PATH + File.separator + keyStore;
		String s = FileUtils.readFileContent(filePath);
		return s;
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
	 * 锁定账户
	 * @param geth
	 * @param address
	 * @return
	 * @throws IOException
     */
	public static Boolean lockAccount(Geth geth, String address) throws Exception {
		Request<?, BooleanResponse> booleanResponseRequest = geth.personalLockAccount(address);
		BooleanResponse response = booleanResponseRequest.send();
		geth.shutdown();
		return response.success();
	}


	/**
	 * 查询以太币账户余额
	 * @throws IOException
	 */
	public static  BigDecimal getEthBanlance(Web3j web3j,String userAddress) throws IOException {
		//获取指定钱包的以太币余额
		EthGetBalance balanceWei = web3j.ethGetBalance(userAddress, DefaultBlockParameterName.LATEST).send();
		BigDecimal balanceInEther = Convert.fromWei(balanceWei.getBalance().toString(), Convert.Unit.ETHER);
		web3j.shutdown();
		//eth默认会部18个0这里处理比较随意
		return balanceInEther;
	}

	/**
	 * 获取ERC-20 token指定地址余额
	 *
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static JSONObject getERC20Balance(Web3j web3j, String address) throws Exception {
		//加载合约
		Credentials credentials = Credentials.create(RunModel.PUBLIC_PRIVATE_KEY);
		UsdtContract usdt = UsdtContract.load(RunModel.CONTRACT_ADDRESS,web3j,credentials, new DefaultGasProvider());
		boolean valid = usdt.isValid();
		if(!valid) {
			throw new Exception("合约加载失败");
		}
		Uint256 balanceUint = usdt.balanceOf(new Address(address)).send();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("value",balanceUint.getValue());
		jsonObject.put("typeAsString", balanceUint.getTypeAsString());
		web3j.shutdown();
		return jsonObject;
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
