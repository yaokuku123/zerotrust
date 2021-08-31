package com.ustb.zerotrust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldInfoWithCleanMethod implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fieldName;
    private Integer safeLevel;
    private String cleanMethod;
}
