package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.service.SoftReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WYP
 * @date 2021-07-07 9:26
 */
@RestController
@CrossOrigin
@RequestMapping("/soft")
public class SoftReviewController {

    @Autowired
    private SoftReviewService softReviewService;

    @GetMapping("/PendingNums")
    public ResponseResult<Integer> PendingNums(){
        Integer nums = null;
        try {
            nums = softReviewService.PendingNums();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(nums);
    }

    @GetMapping("/RejectNums")
    public ResponseResult<Integer> RejectNums(){
        Integer nums = null;
        try {
            nums = softReviewService.RejectNums();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(nums);
    }


}
