package org.unamedgroup.conference.feature.entity.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Image 通用处理类
 * @author zhoutao
 * @date 2019.03.12
 */
public class Utils {

    /**
     * base64图片编码前缀
     */
    private static final String[] imageHead = new String[]{"data:image/jpeg;base64,", "data:image/png;base64,", "data:image/jpg;base64,"};

    /**
     * 获取到人脸的BufferedImage
     * @param bufferedImage
     * @param rt
     * @return imageInfo
     */
    public static BufferedImage getFace(BufferedImage bufferedImage, int[] rt) {
        BufferedImage face = bufferedImage.getSubimage(rt[0], rt[1], rt[2] - rt[0] + 1, rt[3] - rt[1] + 1);
        //裁剪出人脸的图片
        return face;
    }

    /**
     * 将图片的base64编码转换成流
     * @param imgStr
     * @return inputStream
     */
    public static InputStream base64InputStream(String imgStr) {
        if (imgStr == null) {
            System.err.println("base64编码图片信息为空");
            return null;
        }
        Decoder decoder = Base64.getDecoder();
        byte[] bytes = null;
        try {
            for (String header : imageHead) {
                if (imgStr.contains(header)) {
                    bytes = decoder.decode(imgStr.replaceAll(header, ""));
                }
            }
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
        } catch (Exception e) {
            System.err.println("base64图片转换失败，错误信息： " + e.toString());
            return null;
        }
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 将字节流人脸特征信息转为浮点数组
     * @param data
     * @return
     */
    public static float[][] byteArrayToFloatArray(byte[] data) {
        ByteArrayInputStream bas = new ByteArrayInputStream(data);
        DataInputStream ds = new DataInputStream(bas);
        float[] fArr = new float[data.length / 4];
        try {
            for (int i = 0; i < fArr.length; i++) {
                fArr[i] = ds.readFloat();
            }
        } catch (IOException e) {
            System.out.println("ByteArray to FloatArray failed");
            e.toString();
        }
        float[][] feature = new float[1][];
        feature[0] = fArr;
        return feature;
    }

    /**
     * 将浮点数组人脸特征信息转为字节流
     * @param data
     * @return
     */
    public static byte[] floatArrayToByteArray(float[][] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        for (int i = 0; i < data[0].length; i++) {
            try {
                dataOutputStream.writeFloat(data[0][i]);
            } catch (IOException e) {
                System.out.println("FloatArray to ByteArray failed");
                e.toString();
            }
        }
        return out.toByteArray();
    }
}



