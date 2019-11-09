package com.tan.eth.eth;

import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

/**
 * @author by Tan
 * @create 2019/11/9/009
 */
public class UsdtGasProvider implements ContractGasProvider {

    private BigInteger gasPrice;
    private BigInteger gasLimit;

    public UsdtGasProvider(BigInteger s, BigInteger l) {
        this.gasPrice = s;
        this.gasLimit = l;
    }

    /**
     * 根据不同交易方法设置不同的gasPrice
     * @param s
     * @return
     */
    @Override
    public BigInteger getGasPrice(String s) {
        switch (s) {
            case "transfer":
                return this.gasPrice;
            default:
                return this.gasPrice;
        }
    }

    @Override
    public BigInteger getGasPrice() {
        return this.gasPrice;
    }

    @Override
    public BigInteger getGasLimit(String s) {
        switch (s) {
            case "transfer":
                return this.gasLimit;
            default:
                return this.gasLimit;
        }
    }

    @Override
    public BigInteger getGasLimit() {
        return this.gasLimit;
    }
}
