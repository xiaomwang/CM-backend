package org.unamedgroup.conference.feature.entity.util;

import com.arcsoft.face.Rect;
import org.unamedgroup.conference.feature.entity.ImageInfo;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.*;

/**
 * ImageInfo Class
 * @author zhoutao
 * @date 2019.03.12
 */
public class ImageUtil {

    /**
     * 从文件中获取图片的RGB信息
     * @param file
     * @return imageInfo
     */
    public static ImageInfo getRGBData(File file) {
        if (file == null){
            return null;
        }
        ImageInfo imageInfo;
        try {
            //将图片文件加载到内存缓冲区
            BufferedImage image = ImageIO.read(file);
            imageInfo = bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            System.out.println("图片读取失败，错误信息：" + e.toString());
            return null;
        }
        return imageInfo;
    }

    /**
     * 从流中获取图片的RGB信息
     * @param input
     * @return imageInfo
     */
    public static ImageInfo getRGBData(InputStream input) {
        if (input == null){
            return null;
        }
        ImageInfo imageInfo;
        try {
            //将图片流加载到内存缓冲区
            BufferedImage image = ImageIO.read(input);
            imageInfo = bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            System.out.println("流读取失败，错误信息：" + e.toString());
            return null;
        }
        return imageInfo;
    }

    /**
     * 获取到人脸的R、G、B信息
     * @param file
     * @param rt
     * @return imageInfo
     */
    public static ImageInfo getFaceRGBData(File file, Rect rt) {

        BufferedImage bufferImage;
        BufferedImage face;
        try {
            bufferImage = ImageIO.read(file);
            //输出人脸的位置信息
            System.out.println(rt.getLeft());
            System.out.println(rt.getRight());
            System.out.println(rt.getTop());
            System.out.println(rt.getBottom());
            //裁剪出人脸的图片
            face = bufferImage.getSubimage(rt.getLeft(), rt.getTop() , rt.getRight() - rt.getLeft(), rt.getBottom() - rt.getTop());
            ImageIO.write(face, "jpg", new File("face.jpg"));
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
        ImageInfo faceInfo = bufferedImage2ImageInfo(face);
        return faceInfo;
    }

    /**
     *
     * @param image
     * @return imageInfo
     */
    public static ImageInfo bufferedImage2ImageInfo(BufferedImage image) {
        ImageInfo imageInfo = new ImageInfo();
        int width = image.getWidth();
        int height = image.getHeight();
        // 使图片居中
        width = width & (~3);
        height = height & (~3);
        imageInfo.width = width;
        imageInfo.height = height;
        //根据原图片信息新建一个图片缓冲区
        BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        //得到原图的rgb像素矩阵
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        //将像素矩阵 绘制到新的图片缓冲区中
        resultImage.setRGB(0, 0, width, height, rgb, 0, width);
        //进行数据格式化为可用数据
        BufferedImage dstImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        if (resultImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
            ColorConvertOp colorConvertOp = new ColorConvertOp(cs, dstImage.createGraphics().getRenderingHints());
            colorConvertOp.filter(resultImage, dstImage);
        } else {
            dstImage = resultImage;
        }

        //获取rgb数据
        imageInfo.rgbData = ((DataBufferByte) (dstImage.getRaster().getDataBuffer())).getData();
        return imageInfo;
    }

    /**
     * 将图片的base64编码转换成流
     * @param imgStr
     * @return inputStream
     */
//    public static InputStream base64InputStream(String imgStr){
//        if(imgStr==null){
//            System.err.println("base64编码图片信息为空");
//            return null;
//        }
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] bytes;
//        try{
//            //jpeg格式的图片
//            if (imgStr.indexOf("data:image/jpeg;base64,") != -1) {
//                bytes = decoder.decodeBuffer(imgStr.replaceAll("data:image/jpeg;base64,", ""));
//            } else {
//                //png格式的图片
//                if (imgStr.indexOf("data:image/png;base64,") != -1) {
//                    bytes = decoder.decodeBuffer(imgStr.replaceAll("data:image/png;base64,", ""));
//                } else {
//                    //jpg格式的图片
//                    bytes = decoder.decodeBuffer(imgStr.replaceAll("data:image/jpg;base64,", ""));
//                }
//            }
//             for (int i = 0; i < bytes.length; ++i) {
//                 if (bytes[i] < 0){
//                     bytes[i] += 256;
//                 }
//             }
//        }catch(Exception e){
//            System.err.println("base64图片转换失败，错误信息： " + e.toString());
//            return null;
//        }
//        return new ByteArrayInputStream(bytes);
//    }
}
