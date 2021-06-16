package com.ustb.zerotrust.BlindVerify;

import com.ustb.zerotrust.utils.HashUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;


import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by admin on 2020/7/5.
 */
public class Verify {

    /**
     *双线性对映射 证明结果
     * @param pairing   系统初始化参数
     * @param g         系统公钥参数
     * @param uLists    系统公钥参数
     * @param v         系统公钥参数
     * @param sigmasValues 总的签名值
     * @param v_iLists  随机数列表
     * @param signLists 签名列表
     * @param miuLists  miu值列表
     * @return
     */
    public boolean verifyResult(Pairing pairing, Element g, ArrayList<ElementPowPreProcessing> uLists, Element v, Element sigmasValues,
                                ArrayList<Element> v_iLists, ArrayList<Element> signLists,
                                ArrayList<Element> miuLists) {
        Element pairing1 = pairing.pairing(sigmasValues, g);   //e(u,v)^ab

        Element v_i_1 = v_iLists.get(0);
        Element hashValue = g.pow(BigInteger.valueOf(HashUtil.javaDefaultHash(String.valueOf(1)))).powZn(v_i_1);
        for (int i = 1; i < signLists.size(); i++) {
            Element v_i_i = v_iLists.get(i);
            hashValue = hashValue.mul(g.pow(BigInteger.valueOf(HashUtil.javaDefaultHash(String.valueOf(i + 1)))).powZn(v_i_i));
        }

        Element miuValue = miuLists.get(0);
        ElementPowPreProcessing uValue = uLists.get(0);
        Element miuValues = uValue.powZn(miuValue);
        for (int i = 1; i < miuLists.size(); i++) {
            Element miu = miuLists.get(i);
            miuValues = miuValues.mul(uLists.get(i).powZn(miu));
        }

        Element pairing2 = pairing.pairing(hashValue.mul(miuValues), v);

        boolean result = false;
        if (pairing1.equals(pairing2)) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

}
