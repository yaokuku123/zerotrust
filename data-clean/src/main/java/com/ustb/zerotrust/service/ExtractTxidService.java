package com.ustb.zerotrust.service;

/**
 * @author WYP
 * @date 2021-08-15 23:55
 */
public interface ExtractTxidService {

    void insert(String txid,String fileName);

    void insertRes(String txid,String fileName);

    /**
     * 根据软件名称获取源数据字段的交易id
     * @param softName 软件名称
     * @return
     */
    String getResourceTxid(String softName);

    /**
     * 向软件信息表中记录viewTxid
     * @param softName 软件名称
     * @param viewTxid viewTxid交易
     */
    void insertViewTxid(String softName,String viewTxid);

    /**
     * 根据软件名称获取到清洗规则的客体链交易txid
     * @param softName 软件名称
     * @return 交易id
     */
    String getViewTxid(String softName);

    /**
     * 向软件信息表中记录reviewTxid
     * @param softName 软件名称
     * @param reviewTxid reviewTxid交易
     */
    void insertReviewTxid(String softName,String reviewTxid);
}
