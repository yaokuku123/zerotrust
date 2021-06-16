package com.ustb.zerotrust.BlindVerify;

import com.ustb.zerotrust.utils.FileUtil;
import com.ustb.zerotrust.utils.HashUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by admin on 2020/5/29.
 */
public class Sign {

    /**
     * 对源文件进行不拆分的情况下，计算并存储签名信息
     *
     * @param fileUtil
     * @param uLists
     * @param g
     * @param x
     * @return
     */
    public ArrayList<Element> sign(FileUtil fileUtil, String fileName, ArrayList<ElementPowPreProcessing> uLists,
                                   Element g, Element x, int blockFileSize, int pieceFileSize) {
        ArrayList<Element> signLists = new ArrayList<>();
        BigInteger mb;
        BigInteger mb1;
        ElementPowPreProcessing signU;
        Element signUpow;
        byte[] bytesByFile;

        for (int i = 0; i < 100; i++) {
            bytesByFile = fileUtil.getBytes(fileName, i, 0,blockFileSize,pieceFileSize);
            mb = new BigInteger(1,bytesByFile);
            signU = uLists.get(0);
            Element uContinuedProduct = signU.pow(mb);


            for (int j = 1; j < 10; j++) {
                mb1 = new BigInteger(1,fileUtil.getBytes(fileName, i, j,blockFileSize,pieceFileSize));
                signU = uLists.get(j);
                signUpow = signU.pow(mb1);
                uContinuedProduct = uContinuedProduct.mul(signUpow);


            }
            //生成签名
            Element e = Sign.sign(g, uContinuedProduct, x, i + 1);
            signLists.add(e);
        }
        return signLists;
    }


    public ArrayList<Element> sign(ArrayList<ArrayList<File>> pieceFilesAll, FileUtil fileUtil, ArrayList<ElementPowPreProcessing> uLists,
                                   Element g, Element x) {
        ArrayList<Element> signLists = new ArrayList<>();
        BigInteger mb;
        BigInteger mb1;
        ElementPowPreProcessing signU;
        Element signUpow;
        File partFile;
        File pieceFile;
        byte[] bytesByFile;

        for (int i = 0; i < pieceFilesAll.size(); i++) {
            int len = pieceFilesAll.get(i).size();
            partFile = pieceFilesAll.get(i).get(0);
            bytesByFile = fileUtil.getBytes(partFile);
            mb = new BigInteger(bytesByFile);
            signU = uLists.get(0);
            Element uContinuedProduct = signU.pow(mb);


            for (int j = 1; j < len; j++) {
                pieceFile = pieceFilesAll.get(i).get(j);
                mb1 = new BigInteger(fileUtil.getBytes(pieceFile));
                signU = uLists.get(j);
                signUpow = signU.pow(mb1);
                uContinuedProduct = uContinuedProduct.mul(signUpow);


            }
            //生成签名
            Element e = Sign.sign(g, uContinuedProduct, x, i + 1);
            signLists.add(e);
            System.out.println(signLists.size());

        }
        return signLists;
    }


    public static Element sign(Element g, Element u, Element x, byte[] m, int n) {

        BigInteger mb = new BigInteger(m);
        int hashValue = HashUtil.javaDefaultHash(String.valueOf(n));
        Element hash = g.pow(BigInteger.valueOf(hashValue));

        Element sign = (u.pow(mb).mul(hash)).powZn(x);
        return sign;

    }

    //多重
    public static Element sign(Element g, Element u, Element x, int n) {


        int hashValue = HashUtil.javaDefaultHash(String.valueOf(n));
        Element hash = g.pow(BigInteger.valueOf(hashValue));

        Element sign = (u.mul(hash)).powZn(x);
        return sign;

    }

    public static Element sigma(Element signArray, Element v_i) {
        Element result = signArray.powZn(v_i);
        return result;
    }

    public static Element sigmas(Element[] signArray, Element v) {
        Element result = null;
        if (signArray.length > 0) {
            result = sigma(signArray[0], v);
            for (int i = 1; i < signArray.length; i++) {
                result = result.mul(sigma(signArray[i], v));
            }
        }

        return result;
    }


    public static Element miu(Element v_i, byte[] m) {
        BigInteger mb = new BigInteger(m);
        Element result = v_i.mul(mb);
        return result;
    }


    public static void main(String[] args) {
        //文件读取生成TypeA类型的，基于椭圆曲线的对称质数阶双线性群（G1=G2）,G和G1是乘法循环群
        //Pairing pairing = PairingFactory.getPairing("a.properties");
        //PairingFactory.getInstance().setUsePBCWhenPossible(true);

        //动态代码生成
        //rBit是Zp中阶数p的比特长度；qBit是G中阶数的比特长度
        System.out.println(System.currentTimeMillis());
        int rbits = 117;
        int qbits = 1024;
        TypeACurveGenerator pg = new TypeACurveGenerator(rbits, qbits);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);

        byte[] m = new byte[128];
        for (int i = 0; i < 128; i++) {
            m[i] = (byte) i;
        }

        BigInteger mb = new BigInteger(m);
        //初始化相关参数
        Element g = pairing.getG1().newRandomElement().getImmutable();     //生成生成元

        Element x = pairing.getZr().newRandomElement().getImmutable();

        Element u = pairing.getG1().newRandomElement().getImmutable();

        Element v = g.powZn(x);

        Element v_i = pairing.getZr().newRandomElement().getImmutable();

//        int hashValue= HashUtil.javaDefaultHash(String.valueOf(1));
//
//        Element sign = (u.pow(mb).mul(g.pow(BigInteger.valueOf(hashValue)))).powZn(x);

        Element signResult = Sign.sign(g, u, x, m, 1);


        Element sigmaValue = Sign.sigma(signResult, v_i);
        Element miuValue = Sign.miu(v_i, m);

        System.out.println(sigmaValue);
        System.out.println(miuValue);

        Element pairing1 = pairing.pairing(sigmaValue, g);   //e(u,v)^ab


        int hashValue = HashUtil.javaDefaultHash(String.valueOf(1));
        Element hash = g.pow(BigInteger.valueOf(hashValue));
        Element pairing2 = pairing.pairing(hash.powZn(v_i).mul(u.powZn(miuValue)), v);   //e(u,v)^ab
        boolean result = false;
        if (pairing1.equals(pairing2)) {
            result = true;
        } else {
            result = false;
        }
        System.out.println(result);

        System.out.println(System.currentTimeMillis());


    }


}
