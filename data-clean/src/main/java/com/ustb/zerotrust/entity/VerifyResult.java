package com.ustb.zerotrust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String softName;
    private String tableName;
    private Boolean result;
    private List<FieldInfoWithCleanMethod> fieldInfoWithCleanMethodList;
}
