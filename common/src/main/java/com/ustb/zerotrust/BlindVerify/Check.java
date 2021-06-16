package com.ustb.zerotrust.BlindVerify;

import com.ustb.zerotrust.utils.FileUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;


import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

import static com.ustb.zerotrust.BlindVerify.Sign.sigma;


/**
 * Created by admin on 2020/7/5.
 */
public class Check {


    /**
     * 获取随机数Vi值
     *
     * @param pairing   系统初始化参数
     * @param signLists 签名列表
     * @return
     */
    public ArrayList<Element> getViList(Pairing pairing, ArrayList<Element> signLists) {

        ArrayList<Element> v_iLists = new ArrayList<>();

        //产生双线性群中的随机数
        Element v_i = pairing.getZr().newRandomElement().getImmutable();
        v_iLists.add(v_i);

        for (int i = 1; i < signLists.size(); i++) {
            byte[] v_i_bytes = v_i.getImmutable().toCanonicalRepresentation();
            byte[] zp_bytes = v_i_bytes;
            try {
                MessageDigest hash = MessageDigest.getInstance("SHA-512");
                zp_bytes = hash.digest(v_i_bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将元素哈希到双线性群中
            Element v_i_1 = pairing.getZr().newElementFromHash(zp_bytes, 0, zp_bytes.length).getImmutable();
            v_iLists.add(v_i_1);
            v_i = v_i_1;
        }
        return v_iLists;
    }


    /**
     * 获取总的签名值
     *
     * @param pairing   系统初始化参数
     * @param signLists 签名列表
     * @param v_iLists  随机数Vi值列表
     * @return
     */
    public Element getSigh(Pairing pairing, ArrayList<Element> signLists, ArrayList<Element> v_iLists) {
        Element v_i = v_iLists.get(0);
        Element sigmasValues = sigma(signLists.get(0), v_i);

        for (int i = 1; i < signLists.size(); i++) {
            Element v_i_1 = v_iLists.get(i);
            sigmasValues = sigmasValues.mul(sigma(signLists.get(i), v_i_1));

        }
        return sigmasValues;
    }

    /**
     * 获取miu值
     *
     * @param pieceFilesAll 所有文件片
     * @param fileUtil      文件操作类
     * @param v_iLists      随机数Vi值列表
     * @return
     */
    public ArrayList<Element> getMiuList(ArrayList<ArrayList<File>> pieceFilesAll, FileUtil fileUtil, ArrayList<Element> v_iLists) {
        ArrayList<Element> miuLists = new ArrayList<>();
        BigInteger mb;
        BigInteger mb1;
        Element v_i;
        for (int j = 0; j < pieceFilesAll.get(0).size(); j++) {
            ArrayList<File> pieceFileList = pieceFilesAll.get(0);
            File file = pieceFileList.get(j);
            mb = new BigInteger(fileUtil.getBytes(file));
            v_i = v_iLists.get(0);
            Element sum = v_i.mul(mb);
            for (int i = 1; i < pieceFilesAll.size(); i++) {
                File file1 = pieceFilesAll.get(i).get(j);
                mb1 = new BigInteger(fileUtil.getBytes(file1));
                sum = sum.add(v_iLists.get(i).mul(mb1));

            }
            miuLists.add(sum);

        }
        return miuLists;
    }

    /**
     * 对源文件进行不拆分的情况下，计算并存储查询信息
     *
     * @param fileUtil
     * @param fileName
     * @param v_iLists
     * @param blockFileSize
     * @param pieceFileSize
     * @return
     */
    public ArrayList<Element> getMiuList(FileUtil fileUtil, String fileName, ArrayList<Element> v_iLists,
                                          int blockFileSize, int pieceFileSize) {
        ArrayList<Element> miuLists = new ArrayList<>();
        BigInteger mb;
        BigInteger mb1;
        Element v_i;
        for (int j = 0; j < 10; j++) {
            mb = new BigInteger(1,fileUtil.getBytes(fileName, 0, j, blockFileSize, pieceFileSize));
            v_i = v_iLists.get(0);
            Element sum = v_i.mul(mb);
            for (int i = 1; i < 100; i++) {
                mb1 = new BigInteger(1,fileUtil.getBytes(fileName, i, j, blockFileSize, pieceFileSize));
                sum = sum.add(v_iLists.get(i).mul(mb1));

            }
            miuLists.add(sum);

        }
        return miuLists;
    }

}
