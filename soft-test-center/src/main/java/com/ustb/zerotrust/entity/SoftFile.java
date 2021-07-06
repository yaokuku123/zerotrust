package com.ustb.zerotrust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-07-06 14:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftFile implements Serializable {
    private int id;
    private String filename;

}
