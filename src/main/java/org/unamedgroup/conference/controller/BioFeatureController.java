package org.unamedgroup.conference.controller;

import com.arcsoft.face.FaceFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.unamedgroup.conference.feature.entity.util.ImageUtil;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.feature.service.IDetectFaceService;
import java.util.Base64.Decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * BioFeatureController
 * 错误代码使用1xxx
 *
 * @author zhoutao
 * @date 2019/03/29
 */
@CrossOrigin
@RestController
@RequestMapping("/bioFeature")
public class BioFeatureController {

    @Autowired
    IDetectFaceService detectFaceService;

    /**
     * 人脸获取
     *
     * @param imgStr
     * @param userID
     * @return
     */
//    @RequestMapping(value = "/set", method = RequestMethod.POST)
//    @ResponseBody
//    public Integer faceDetect(String imgStr, Integer userID) {
//        try {
//            InputStream inputStream = ImageUtil.base64InputStream(imgStr);
//            FaceFeature faceFeature = detectFaceService.addFaceFeature(inputStream, userID);
//            if (faceFeature == null) {
//                System.err.println("找不到人脸信息");
//                return -1;
//            }
//            return 0;
//        } catch (Exception e) {
//            System.err.println("发生错误，请检查：");
//            e.toString();
//        }
//        return -1;
//    }

    /**
     * 人脸匹配
     *
     * @param imgStr
     * @param userID
     * @return result
     */
/*
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public Double faceCompare(String imgStr, Integer userID) {
        try {
            InputStream inputStream = ImageUtil.base64InputStream(imgStr);
            Double result = detectFaceService.compareFace(inputStream, userID);
            if (result == -1) {
                System.err.println("找不到人脸信息");
                return -1.0;
            }
            return result;
        } catch (Exception e) {
            System.err.println("发生错误，请检查：");
            e.toString();
        }
        return -1.0;
    }
*/

}
