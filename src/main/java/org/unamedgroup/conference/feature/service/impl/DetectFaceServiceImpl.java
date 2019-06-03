package org.unamedgroup.conference.feature.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unamedgroup.conference.feature.entity.Box;
import org.unamedgroup.conference.feature.entity.FaceNet;
import org.unamedgroup.conference.feature.entity.MtCnn;
import org.unamedgroup.conference.feature.entity.util.Utils;
import org.unamedgroup.conference.feature.service.IDetectFaceService;
import org.unamedgroup.conference.service.UserManageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Vector;

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
    @Autowired
    MtCnn mtCnn;
    @Autowired
    FaceNet faceNet;

    /**
     * 根据图片信息提取到人脸的特征信息
     *
     * @param file
     * @return faceFeature
     */
    @Override
    public int detectFeature(File file, Integer userID) {

        if (userManageService == null) {
            userManageService = new org.unamedgroup.conference.service.impl.UserManageServiceImpl();
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            detectFeature(inputStream, userID);
            return 0;
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    /**
     * 根据图片信息提取到人脸的特征信息
     *
     * @param inputStream
     * @return faceFeature
     */
    @Override
    public int detectFeature(InputStream inputStream, Integer userID) {
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Vector<Box> boxes = mtCnn.detectFaces(bufferedImage,40);
            if(boxes.size() == 0){
                return -1;
            }
            BufferedImage face = Utils.getFace(bufferedImage, boxes.get(0).box);
            float[][] result = faceNet.getFeature(face);
            byte[] bytes = Utils.floatArrayToByteArray(result);

            System.out.println("转换前特征值：" + bytes);
            String feature = Base64.getEncoder().encodeToString(bytes);
            System.out.println("转换后特征字符串：" + feature);
            userManageService.setUserFeature(userID, feature);
            return 0;
        } catch (IOException e) {
            System.err.println("流转图片失败" + e.toString());
            return -1;
        }
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
        try {
            //从数据库中读取人脸特征信息
            String sourceData = userManageService.getUserFeature(userID);
            if (sourceData == null) {
                System.out.println("查询人脸信息失败！");
                return 0;
            }
            System.out.println("转换前特征字符串：" + sourceData);
            //奖字符串转换成byte数组
            byte[] data = Base64.getDecoder().decode(sourceData);
            float[][] faceFeature = Utils.byteArrayToFloatArray(data);

            System.out.println("转换后特征值：" + data);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Vector<Box> boxes = mtCnn.detectFaces(bufferedImage,40);
            if(boxes.size() == 0){
                return -1;
            }
            BufferedImage face = Utils.getFace(bufferedImage, boxes.get(0).box);

            float[][] testFaceFeature = faceNet.getFeature(face);
            float result = faceNet.compareFeature(faceFeature, testFaceFeature);
            return result;
        } catch (IOException e) {
            System.err.println("匹配人脸出错，请检查" + e.toString());
            return -1;
        }
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
