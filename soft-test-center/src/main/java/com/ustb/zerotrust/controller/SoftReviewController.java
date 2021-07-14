package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import com.ustb.zerotrust.service.SoftReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "获取待处理软件个数",notes = "接口:/soft/PendingNums")
    @GetMapping("/PendingNums")
    public ResponseResult PendingNums(){

        ResponseResult responseResult = null;
        try {
            Integer nums = softReviewService.PendingNums();
            responseResult = ResponseResult.success().data("nums",nums);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult = ResponseResult.error();
        }
        return responseResult;
    }

    @ApiOperation(value = "获取被驳回软件个数",notes = "接口:/soft/RejectNums")
    @GetMapping("/RejectNums")
    public ResponseResult RejectNums(){
        ResponseResult responseResult = null;
        try {
            Integer nums = softReviewService.RejectNums();
            responseResult = ResponseResult.success().data("nums",nums);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult = ResponseResult.error();
        }
        return responseResult;
    }

    @ApiOperation(value = "获取已处理软件个数",notes = "接口:/soft/HandledNums")
    @GetMapping("/HandledNums")
    public ResponseResult HandledNums(){
        ResponseResult responseResult = null;
        try {
            Integer nums = softReviewService.HandledNums();
            responseResult = ResponseResult.success().data("nums",nums);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult = ResponseResult.error();
        }
        return responseResult;
    }


    @ApiOperation(value = "获取所有已处理软件信息",notes = "接口:/soft/getAllPending")
    @GetMapping("/getAllPending")
    public ResponseResult getAllPending(){
        List<SoftInfo> allPending = null;
        ResponseResult pendingSoft = null;
        try {
            allPending = softReviewService.getAllPending();
            pendingSoft = ResponseResult.success().data("PendingSoft", allPending);
        } catch (Exception e) {
            e.printStackTrace();
            pendingSoft = ResponseResult.error();
        }
        return pendingSoft;
    }


    @ApiOperation(value = "获取所有已驳回软件信息",notes = "接口:/soft/getAllReject")
    @GetMapping("/getAllReject")
    public ResponseResult getAllReject(){
        List<SoftInfo> allReject = null;
        ResponseResult rejectSoft = null;
        try {
            allReject = softReviewService.getAllReject();
            rejectSoft = ResponseResult.success().data("RejectSoft", allReject);
        } catch (Exception e) {
            e.printStackTrace();
            rejectSoft = ResponseResult.error();
        }
        return rejectSoft;
    }


    @ApiOperation(value = "获取所有已处理软件信息",notes = "接口:/soft/getAllHandled")
    @GetMapping("/getAllHandled")
    public ResponseResult getAllHandled(){
        List<SoftInfo> allHandled = null;
        ResponseResult handledSoft = null;
        try {
            allHandled = softReviewService.getAllHandled();
            handledSoft = ResponseResult.success().data("HandledSoft", allHandled);
        } catch (Exception e) {
            e.printStackTrace();
            handledSoft = ResponseResult.error();
        }
        return handledSoft;
    }

    @ApiOperation(value = "审核通过待处理软件",notes = "接口:/soft/examineSoftSuccess")
    @GetMapping("/examineSoftSuccess")
    public ResponseResult examineSoftSuccess(@RequestParam("id") int id){
        try {
            softReviewService.examineSoftSuccess(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error();
        }
        return ResponseResult.success();
    }

    @ApiOperation(value = "审核驳回处理软件",notes = "接口:/soft/examineSoftFail")
    @GetMapping("/examineSoftFail")
    public ResponseResult examineSoftFail(@RequestParam("id") int id){
        try {
            softReviewService.examineSoftFail(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error();
        }
        return ResponseResult.success();
    }

    @GetMapping("/findNameById")
    public ResponseResult findNameById(@RequestParam("id") int id){
        String txid = softReviewService.findTxidById(id);
        System.out.println(txid);
        return ResponseResult.success().data("txid",txid);
    }

}
