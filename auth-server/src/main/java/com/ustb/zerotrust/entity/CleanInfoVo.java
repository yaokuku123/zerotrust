package com.ustb.zerotrust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleanInfoVo {
    private String txid;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getExtracid() {
        return extracid;
    }

    public void setExtracid(String extracid) {
        this.extracid = extracid;
    }

    private String extracid;

}
