package com.tan.eth.config;

import com.tan.eth.entity.Account;
import com.tan.eth.entity.TxRecord;
import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.eth.UsdtContract;
import com.tan.eth.service.dao.AccountMao;
import com.tan.eth.service.dao.TxRecordMao;
import com.tan.eth.utils.RunModel;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author by Tan
 * geth --datadir /data --cache 4096 --rpc --rpcport 6666 --rpcapi db,eth,net,web3,personal --ws
 * @create 2019/10/31/031
 */
@Component
@Slf4j
public class TaskRunner implements CommandLineRunner {

    @Autowired
    private TxRecordMao txRecordMao;

    @Autowired
    private AccountMao accountMao;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(String... strings) throws Exception {

        //将Account重新载入缓存
        Set<Object> keys = redisTemplate.keys(RunModel.REDIS_ACCOUNT_KEY_PREX + "*");
        redisTemplate.delete(keys);
        List<Account> accounts = accountMao.findAll();
        if(accounts != null && accounts.size() > 0){
            for(Account item: accounts){
                redisTemplate.opsForValue().set(RunModel.REDIS_ACCOUNT_KEY_PREX + item.getAddress(), item.getPrivateKey());
            }
        }
        log.info("========合计账户:{}",accounts == null ? "0" : accounts.size());


        //检查上次最后监听的区块高度
        BigInteger latestBlockNumber = txRecordMao.getLatestBlockNumber();
        DefaultBlockParameter blockParameterName = DefaultBlockParameter.valueOf(RunModel.BLOCK_FROM);
        if(latestBlockNumber != null){
            blockParameterName = DefaultBlockParameter.valueOf(latestBlockNumber);
        }
        log.info("========监听高度开始于:{}",latestBlockNumber == null ? RunModel.BLOCK_FROM : latestBlockNumber.toString());


        Web3j web3j = ConnectProvider.loadWeb3jSocket();

        Credentials credentials = Credentials.create(RunModel.PUBLIC_PRIVATE_KEY);
        UsdtContract contract = UsdtContract.load(RunModel.CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());

        /**
         * 监听USDT交易
         */
        contract.transferEventFlowable(blockParameterName, DefaultBlockParameterName.LATEST)
            .subscribe(tx -> {
                BigInteger amount = tx.value.getValue();
                String srcAccount = tx.from.getValue();
                String dstAccount = tx.to.getValue();

                /**
                 * 仅仅监听转入目标账户是平台账户的数据
                 */
                Boolean dstTx = redisTemplate.hasKey(RunModel.REDIS_ACCOUNT_KEY_PREX + dstAccount);
                if(dstTx){
                    log.info("新USDT交易信息:" + "from: " + srcAccount + ", to: " + dstAccount + ", value: " + amount);
                    TxRecord r = new TxRecord();
                    r.setFrom(srcAccount);
                    r.setTo(dstAccount);
                    r.setBlockNumber(tx.log.getBlockNumber());
                    r.setBlockHash(tx.log.getBlockHash());
                    r.setTxHash(tx.log.getTransactionHash());
                    r.setAmount(amount);
                    r.setCoin(1);
                    r.setSendFlag(false);
                    txRecordMao.saveTxRecord(r);
                }
            }, err -> {
                log.error(err.getMessage());
            });

        /**
         * 监听以太坊交易
         */
        Web3j eb3j = ConnectProvider.loadWeb3jSocket();
        Disposable ethSubscribe = eb3j.transactionFlowable()
                .subscribe(etx -> {
                    String to = etx.getTo();
                    /**
                     * 仅仅监听转入目标账户是平台账户的数据
                     */
                    Boolean dstTx = redisTemplate.hasKey(RunModel.REDIS_ACCOUNT_KEY_PREX + to);
                    if (dstTx) {
                        String from = etx.getFrom();
                        BigInteger value = etx.getValue();
                        log.info("新以太坊交易信息:" + "from: " + from + ", to: " + to + ", value: " + value.toString());
                        TxRecord r = new TxRecord();
                        r.setFrom(from);
                        r.setTo(to);
                        r.setBlockNumber(etx.getBlockNumber());
                        r.setBlockHash(etx.getBlockHash());
                        r.setTxHash(etx.getHash());
                        r.setAmount(etx.getValue());
                        r.setCoin(2);
                        r.setSendFlag(false);
                        txRecordMao.saveTxRecord(r);
                    }

                }, err -> {
//                    log.error(err.getMessage());
                });
    }

}
