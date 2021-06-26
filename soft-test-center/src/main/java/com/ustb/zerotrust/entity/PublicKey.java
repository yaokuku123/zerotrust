package com.ustb.zerotrust.entity;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: PublicKey
 * Author: yaoqijun
 * Date: 2021/6/24 15:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicKey {
    private Pairing pairing;
    private Element g;
    private Element v;
    private ArrayList<ElementPowPreProcessing> uLists;
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    public PublicKey(Pairing pairing){
        this.pairing = pairing;
    }


    /**
     * 使用Base64编码的方式将Element类型的G转换为String类型
     *
     * @return
     */
    public String encodeG() throws UnsupportedEncodingException {
        byte[] gByte = encoder.encode(g.toBytes());
        String gString = new String(gByte, "UTF-8");
        return gString;
    }

    /**
     * 使用Base64编码的方式将Element类型的V转换为String类型
     *
     * @return
     */
    public String encodeV() throws UnsupportedEncodingException {
        byte[] vByte = encoder.encode(v.toBytes());
        String vString = new String(vByte, "UTF-8");
        return vString;
    }

    /**
     * 使用Base64编码的方式将ElementPowPreProcessing转换为String类型
     *
     * @return
     */
    public ArrayList<String> encodeULists() throws UnsupportedEncodingException {
        ArrayList<String> uStringList = new ArrayList<>();
        for (ElementPowPreProcessing elm : uLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            uStringList.add(elmString);
        }
        return uStringList;
    }

    /**
     * 使用Base64编码的方式将String类型的G转换为Element类型
     *
     * @return
     */
    public Element decodeG(String gString) throws UnsupportedEncodingException {
        byte[] gByte = decoder.decode(gString.getBytes("UTF-8"));
        g = pairing.getG1().newElementFromBytes(gByte).getImmutable();
        return g;
    }

    /**
     * 使用Base64编码的方式将String类型的V转换为Element类型
     *
     * @return
     */
    public Element decodeV(String vString) throws UnsupportedEncodingException {
        byte[] vByte = decoder.decode(vString.getBytes("UTF-8"));
        v = pairing.getG1().newElementFromBytes(vByte).getImmutable();
        return v;
    }

    /**
     * 使用Base64编码的方式将String类型的uList转换为ElementPowPreProcessing类型
     *
     * @return
     */
    public ArrayList<ElementPowPreProcessing> decodeULists(List<String> uStringList) throws UnsupportedEncodingException {
        uLists = new ArrayList<>();
        for (String elm : uStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            ElementPowPreProcessing elmE = pairing.getG1().newRandomElement().getImmutable().getField().getElementPowPreProcessingFromBytes(elmByte);
            uLists.add(elmE);
        }
        return uLists;
    }

}
