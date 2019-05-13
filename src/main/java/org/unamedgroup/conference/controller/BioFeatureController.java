package org.unamedgroup.conference.controller;

import com.arcsoft.face.FaceFeature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.unamedgroup.conference.entity.temp.RoomTime;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.feature.entity.util.ImageUtil;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.feature.service.IDetectFaceService;
import org.unamedgroup.conference.security.JWTUtil;
import org.unamedgroup.conference.service.GeneralService;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * BioFeatureController
 * 错误代码使用1xxx
 *
 * @author zhoutao
 * @date 2019/03/29
 */
@Api(value = "人脸识别 API", description = "人脸识别接口", protocols = "http")
@CrossOrigin
@RestController
@RequestMapping("/bioFeature")
public class BioFeatureController {

    @Autowired
    IDetectFaceService detectFaceService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GeneralService generalService;

    /**
     * 人脸获取
     *
     * @param imgStr
     * @return
     */
    @ApiOperation(value = "设置人脸特征信息api", protocols = "http"
            , produces = "application/json", consumes = "application/json"
            , response = Integer.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgStr", value = "图片流信息", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userID", value = "用户id", required = true, dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    @ResponseBody
    public Object faceDetect(String imgStr) {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }
        String phone = JWTUtil.getPhoneNumber(subject.getPrincipal().toString());
        Integer userID = userRepository.getUserByPhoneNumber(phone).getUserID();

        try {
            InputStream inputStream = ImageUtil.base64InputStream(imgStr);
            FaceFeature faceFeature = detectFaceService.addFaceFeature(inputStream, userID);
            if (faceFeature == null) {
                System.err.println("找不到人脸信息");
                return new FailureInfo(4000,"找不到人脸信息" );
            }
            return new SuccessInfo("设置人脸成功" );
        } catch (Exception e) {

            System.err.println("发生错误，请检查：" + e.toString());
            return new FailureInfo(4001, "发生错误请检查" );
        }
    }

    /**
     * 人脸匹配
     *
     * @param imgStr
     * @return result
     */
    @ApiOperation(value = "对比人脸特征信息api", protocols = "http"
            , produces = "application/json", consumes = "application/json"
            , response = Integer.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgStr", value = "图片流信息", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userID", value = "用户id", required = true, dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public Object faceCompare(String imgStr, Integer roomID) {
        // 获取当前时间
        // 格式化时间
        try {
            // 处理图片信息
            InputStream inputStream = ImageUtil.base64InputStream(imgStr);
            //现在时间
            Date now = new Date();
            //半个小时后时间
            Date date = new Date(now.getTime() + (long)30*60*1000);
            List<Conference> conferenceList = generalService.getConferencesByLocationAndDate(roomID, now, date);
            Double result = result = detectFaceService.compareFace(inputStream, conferenceList.get(0).getUser());
            if (result == -1) {
                System.err.println("找不到人脸信息");
                return new FailureInfo(4000,"找不到人脸信息" );
            }
            if (result >= 0.8){
                return new SuccessInfo("人脸匹配成功" );
            }else{
                return new FailureInfo(4002,"人脸匹配失败" );
            }
        } catch (Exception e) {
            System.err.println("发生错误，请检查：" + e.toString());
            return new FailureInfo(4001,"发生错误，请检查" );
        }
    }
}
