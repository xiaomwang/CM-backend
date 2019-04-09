package org.unamedgroup.conference.feature.service.impl;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.enums.ImageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unamedgroup.conference.feature.entity.ImageInfo;
import org.unamedgroup.conference.feature.entity.util.ImageUtil;
import org.unamedgroup.conference.feature.factory.FaceConfiguration;
import org.unamedgroup.conference.feature.service.IDetectFaceService;
import org.unamedgroup.conference.service.UserManageService;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Interface
 *
 * @author zhoutao
 * @date 2019-03-12
 */
@Component
public class DetectFaceServiceImpl implements IDetectFaceService {

    @Autowired
    UserManageService userManageService;


    /**
     * 根据图片信息提取到人脸的特征信息
     *
     * @param file
     * @return faceFeature
     */
    @Override
    public FaceFeature addFaceFeature(File file, Integer userID) {

        if (userManageService == null) {
            userManageService = new org.unamedgroup.conference.service.impl.UserManageServiceImpl();
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }
        return addFaceFeature(inputStream, userID);
    }

    /**
     * 根据图片信息提取到人脸的特征信息
     *
     * @param inputStream
     * @return faceFeature
     */
    @Override
    public FaceFeature addFaceFeature(InputStream inputStream, Integer userID) {
        //从工厂中获取FaceEngine引擎
        FaceEngine faceEngine = FaceConfiguration.create();
        //进行人脸识别
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        //获取图片导入内存
        ImageInfo imageInfo = ImageUtil.getRGBData(inputStream);
        //获取识别的人脸数组对象
        faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        //如果读不到人脸信息返回Exception
        if (faceInfoList.isEmpty()) {
            return null;
        }
        //提取人脸特征
        FaceFeature faceFeature = new FaceFeature();
        faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);

        //byte数组转字符串类型
        System.out.println("转换前特征值：" + faceFeature.getFeatureData());
        String feature = Base64.getEncoder().encodeToString(faceFeature.getFeatureData());
        System.out.println("转换后特征字符串：" + feature);
        userManageService.setUserFeature(userID, feature);
        return faceFeature;
    }

    /**
     * 根据人脸的特征信息进行人脸匹配
     *
     * @param inputStream
     * @param userID
     * @return
     */
    @Override
    public double compareFace(InputStream inputStream, Integer userID) {
        //获取人脸识别引擎
        FaceEngine faceEngine = FaceConfiguration.create();
        //进行人脸识别
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        //获取图片导入内存
        ImageInfo imageInfo = ImageUtil.getRGBData(inputStream);
        //获取识别的人脸数组对象
        faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        //读不到人脸信息返回错误
        if (faceInfoList.isEmpty()) {
            return -1;
        }
        //提取人脸特征
        FaceFeature testFaceFeature = new FaceFeature();
        faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), testFaceFeature);
        FaceSimilar faceSimilar = new FaceSimilar();
        //从数据库中读取人脸特征信息
        String sourceData = userManageService.getUserFeature(userID);

        if (sourceData == null) {
            System.out.println("查询人脸信息失败！");
            return 0;
        }
        System.out.println("转换前特征字符串：" + sourceData);
        //奖字符串转换成byte数组
        byte[] data = Base64.getDecoder().decode(sourceData);
        System.out.println("转换后特征值：" + data);
        //生成匹配的人脸特征信息
        FaceFeature faceFeature = new FaceFeature();
        faceFeature.setFeatureData(data);
        //人脸比对相似度
        faceEngine.compareFaceFeature(faceFeature, testFaceFeature, faceSimilar);
        return faceSimilar.getScore();
    }

    /**
     * 根据人脸的特征信息进行人脸匹配
     *
     * @param file
     * @param userID
     * @return
     */
    @Override
    public double compareFace(File file, Integer userID) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }
        return compareFace(inputStream, userID);
    }

}
