package com.ustb.zerotrust.entity;

import com.sun.net.httpserver.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bytebuddy.asm.MemberSubstitution;
import org.springframework.web.bind.annotation.CrossOrigin;

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
