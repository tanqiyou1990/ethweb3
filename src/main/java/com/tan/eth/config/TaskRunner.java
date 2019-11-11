package com.tan.eth.config;

import com.tan.eth.entity.Account;
import com.tan.eth.entity.TxRecord;
import com.tan.eth.eth.ConnectProvider;
import com.tan.eth.eth.UsdtContract;
import com.tan.eth.service.dao.AccountMao;
import com.tan.eth.service.dao.TxRecordMao;
import com.tan.eth.utils.RunModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.List;

/**
 * @author by Tan
 * @create 2019/10/31/031
 */
@Component
@Slf4j
public class TaskRunner implements CommandLineRunner {

    private static String privateKey = "0xaeade09175ddaa09767881c19b1bdde86fd7fcdf914afa35e6d5efceaee665de";

    @Autowired
    private TxRecordMao txRecordMao;

    @Autowired
    private AccountMao accountMao;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(String... strings) throws Exception {

        //将Account载入缓存
        List<Account> accounts = accountMao.findAll();
        if(accounts != null && accounts.size() > 0){
            for(Account item: accounts){
                redisTemplate.opsForValue().set(item.getAddress(), item.getPrivateKey());
            }
        }
        log.info("========合计账户:{}",accounts == null ? "0" : accounts.size());


        //检查上次最后监听的区块高度
        BigInteger latestBlockNumber = txRecordMao.getLatestBlockNumber();
        DefaultBlockParameter blockParameterName = DefaultBlockParameter.valueOf(RunModel.BLOCK_FROM);
        if(latestBlockNumber != null){
            blockParameterName = DefaultBlockParameter.valueOf(latestBlockNumber);
        }
        log.info("========监听高度开始于:{}",latestBlockNumber == null ? "0" : latestBlockNumber.toString());

        Web3j web3j = ConnectProvider.loadWeb3j();
        Credentials credentials = Credentials.create(privateKey);
        UsdtContract contract = UsdtContract.load(RunModel.CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
        contract.transferEventFlowable(blockParameterName, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    BigInteger amount = event.value.getValue();
                    String srcAccount = event.from.getValue();
                    String dstAccount = event.to.getValue();
                    log.debug("from: " + srcAccount + ", to: " + dstAccount + ", value: " + amount);

                    /**
                     * 仅仅监听转入目标账户是平台账户的数据
                     */
                    Boolean dstTx = redisTemplate.hasKey(dstAccount);
                    if(dstTx){
                        log.info("新交易信息:" + "from: " + srcAccount + ", to: " + dstAccount + ", value: " + amount);
                        TxRecord r = new TxRecord();
                        r.setFrom(srcAccount);
                        r.setTo(dstAccount);
                        r.setBlockNumber(event.log.getBlockNumber());
                        r.setBlockHash(event.log.getBlockHash());
                        r.setTxHash(event.log.getTransactionHash());
                        r.setAmount(amount);
                        r.setSendFlag(false);
                        txRecordMao.saveTxRecord(r);
                    }
                });
    }

}
