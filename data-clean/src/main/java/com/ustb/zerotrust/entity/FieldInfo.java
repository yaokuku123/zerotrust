package com.ustb.zerotrust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-08-30 16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldInfo implements Serializable {

    private String fieldName;
    private Integer safeLevel;

}
