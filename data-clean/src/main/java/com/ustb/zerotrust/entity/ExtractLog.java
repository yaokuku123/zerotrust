package com.ustb.zerotrust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WYP
 * @date 2021-09-02 14:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExtractLog implements Serializable {

    private Integer id;
    private String fileName;
    private String extractTxid;
    private Date cleanTime;
}
