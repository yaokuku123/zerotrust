package com.ustb.zerotrust.entity;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: QueryParam
 * Author: yaoqijun
 * Date: 2021/6/25 8:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryParam implements Serializable {
    private Pairing pairing;
    private Element sigmasValues;
    private ArrayList<Element> viLists;
    private ArrayList<Element> signLists;
    private ArrayList<Element> miuLists;
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    /**
     * 使用Base64编码的方式将Element类型的sigmasValues转换为String类型
     *
     * @return
     */
    public String encodeSigma() throws UnsupportedEncodingException {
        byte[] sigmaByte = encoder.encode(sigmasValues.toBytes());
        String sigmaString = new String(sigmaByte, "UTF-8");
        return sigmaString;
    }

    /**
     * 使用Base64编码的方式将viLists转换为String类型
     *
     * @return
     */
    public ArrayList<String> encodeViList() throws UnsupportedEncodingException {
        ArrayList<String> viStringList = new ArrayList<>();
        for (Element elm : viLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            viStringList.add(elmString);
        }
        return viStringList;
    }

    /**
     * 使用Base64编码的方式将viLists转换为String类型
     *
     * @return
     */
    public ArrayList<String> encodeSignLists() throws UnsupportedEncodingException {
        ArrayList<String> signStringList = new ArrayList<>();
        for (Element elm : signLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            signStringList.add(elmString);
        }
        return signStringList;
    }

    /**
     * 使用Base64编码的方式将miuLists转换为String类型
     *
     * @return
     */
    public ArrayList<String> encodeMiuLists() throws UnsupportedEncodingException {
        ArrayList<String> miuStringList = new ArrayList<>();
        for (Element elm : miuLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            miuStringList.add(elmString);
        }
        return miuStringList;
    }

    /**
     * 使用Base64编码的方式将String类型的sigmasValues转换为Element类型
     *
     * @return
     */
    public Element decodeSigma(String sigmaString) throws UnsupportedEncodingException {
        byte[] sigmaByte = decoder.decode(sigmaString.getBytes("UTF-8"));
        sigmasValues = pairing.getG1().newElementFromBytes(sigmaByte).getImmutable();
        return sigmasValues;
    }

    /**
     * 使用Base64编码的方式将String类型的viLists转换
     *
     * @return
     */
    public ArrayList<Element> decodeViLists(List<String> viStringList) throws UnsupportedEncodingException {
        viLists = new ArrayList<>();
        for (String elm : viStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            Element elmE = pairing.getZr().newElementFromBytes(elmByte).getImmutable();
            viLists.add(elmE);
        }
        return viLists;
    }

    /**
     * 使用Base64编码的方式将String类型的signLists转换
     *
     * @return
     */
    public ArrayList<Element> decodeSignLists(List<String> signStringList) throws UnsupportedEncodingException {
        signLists = new ArrayList<>();
        for (String elm : signStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            Element elmE = pairing.getZr().newElementFromBytes(elmByte).getImmutable();
            signLists.add(elmE);
        }
        return signLists;
    }

    /**
     * 使用Base64编码的方式将String类型的miuLists转换
     *
     * @return
     */
    public ArrayList<Element> decodeMiuLists(List<String> miuStringList) throws UnsupportedEncodingException {
        miuLists = new ArrayList<>();
        for (String elm : miuStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            Element elmE = pairing.getZr().newElementFromBytes(elmByte).getImmutable();
            miuLists.add(elmE);
        }
        return miuLists;
    }


}
