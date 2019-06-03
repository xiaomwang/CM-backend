package org.unamedgroup.conference.feature.entity;


import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Vector;

import java.io.IOException;
import java.util.List;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;

/**
 * 人脸检测类
 * @date 2019-5-23
 * @author  zhoutao
 */
@Component
public class MtCnn {


    private static final float FACTOR = 0.709f;
    private static final float P_NET_THRESHOLD = 0.5f;
    private static final float R_NET_THRESHOLD = 0.5f;
    private static final float O_NET_THRESHOLD = 0.7f;

    private static final float IMAGE_MEAN = 127.5f;
    private static final float IMAGE_STD = 128;

    private static final String PD_PATH = "src/main/resources/models/FaceDetect.pb";

    private static final String P_NET_IN_NAME = "pnet/input:0";
    private static final String[] P_NET_OUT_NAME = new String[]{"pnet/prob1:0", "pnet/conv4-2/BiasAdd:0"};
    private static final String R_NET_IN_NAME = "rnet/input:0";
    private static final String[] R_NET_OUT_NAME = new String[]{"rnet/prob1:0", "rnet/conv5-2/conv5-2:0",};
    private static final String O_NET_IN_NAME = "onet/input:0";
    private static final String[] O_NET_OUT_NAME = new String[]{"onet/prob1:0", "onet/conv6-2/conv6-2:0", "onet/conv6-3/conv6-3:0"};

    private Session session;

    public MtCnn() {
        Graph graph = new Graph();
        byte[] graphDef;
        try {
            graphDef = IOUtils.toByteArray(new FileInputStream(PD_PATH));
            graph.importGraphDef(graphDef);
            session = new Session(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检测人脸
     *
     * @param img
     * @param minFaceSize
     * @return Vector<Box>
     * @throws Exception
     */
    public Vector<Box> detectFaces(BufferedImage img, int minFaceSize){
        int w = img.getWidth();
        int h = img.getHeight();
        Vector<Box> boxes = null;
        try {
            boxes = pNet(img, minFaceSize, w, h);
            squareLimit(boxes, w, h);
            if (boxes.size() == 0) {
                return boxes;
            }
            boxes = rNet(img, boxes);
            squareLimit(boxes, w, h);
            if (boxes.size() == 0) {
                return boxes;
            }
            boxes = oNet(img, boxes);
            return boxes;
        } catch (IOException e) {
            System.err.println("人脸检测失败" + e.toString());
            return null;
        }
    }

    /**
     * MTCNN中PNet
     *
     * @param img
     * @param minFaceSize
     * @param w
     * @param h
     * @return Vector<Box>
     * @throws IOException
     */
    private Vector<Box> pNet(BufferedImage img, int minFaceSize, int w, int h) throws IOException {
        int whMin = min(w, h);
        Vector<Box> totalBoxes = new Vector<>();
        float currentFaceSize = minFaceSize;
        while (currentFaceSize <= whMin) {
            float scale = 12.0f / currentFaceSize;
            List<Box> list = pNetForword(img, scale);
            totalBoxes.addAll(list);
            //Face Size等比递增
            currentFaceSize /= FACTOR;
        }
        //NMS 0.7
        nms(totalBoxes, 0.7f, "Union");
        return updateBoxes(totalBoxes);
    }

    /**
     * MTCNN中PNet检测
     *
     * @param img
     * @param scale
     * @return List<Box>
     * @throws IOException
     */
    private List<Box> pNetForword(BufferedImage img, float scale) throws IOException {
        //(1)Image Resize
        BufferedImage resizeImg = resize(img, scale);
        //(2)RUN CNN
        Tensor<Float> x = image2FloatTensor(resizeImg);
        List<Tensor<?>> outputs = predict(x, P_NET_IN_NAME, P_NET_OUT_NAME);
        Tensor<Float> outP = outputs.get(0).expect(Float.class);
        Tensor<Float> outB = outputs.get(1).expect(Float.class);
        long[] shape = outP.shape();
        int pNetOutSizeH = (int) shape[1];
        int pNetOutSizeW = (int) shape[2];
        float[][][][] p = outP.copyTo(new float[1][pNetOutSizeH][pNetOutSizeW][2]);
        float[][][][] b = outB.copyTo(new float[1][pNetOutSizeH][pNetOutSizeW][4]);
        float[][][] pNetOutBias = b[0];
        float[][] pNetOutProb = new float[pNetOutSizeH][pNetOutSizeW];
        expandProb(p[0], pNetOutProb);
        //(3) data parse
        Vector<Box> curBoxes = new Vector<>();
        generateBoxes(pNetOutProb, pNetOutBias, scale, curBoxes);
        //(4)nms 0.5
        nms(curBoxes, 0.5f, "Union");
        curBoxes.forEach(Box::calibrate);
        //(5)add to totalBoxes
        return curBoxes.stream().filter(box -> !box.deleted).collect(toList());
    }

    /**
     * 不同scale裁剪图片
     *
     * @param img
     * @param scale
     * @return BufferedImage
     * @throws IOException
     */
    private static BufferedImage resize(BufferedImage img, float scale) throws IOException {
        return Thumbnails.of(img).scale(scale).asBufferedImage();
    }

    /**
     * 取出预测的Tensor
     *
     * @param x
     * @param inName
     * @param outputNames
     * @return List<Tensor < ?>>
     */
    private List<Tensor<?>> predict(Tensor<Float> x, String inName, String[] outputNames) {
        Session.Runner runner = session.runner().feed(inName, x);
        for (String outName : outputNames) {
            runner.fetch(outName);
        }
        return runner.run();
    }

    /**
     * 将BufferedImage转Tensor
     *
     * @param img
     * @return Tensor<Float>
     */
    private Tensor<Float> image2FloatTensor(BufferedImage img) {
        float[][][][] floatValues = image2FloatArr(img);
        return Tensors.create(floatValues);
    }

    private float[][][][] image2FloatArr(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        float[][][][] floatValues = new float[1][h][w][3];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int val = img.getRGB(i, j);
                floatValues[0][j][i][0] = (((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
                floatValues[0][j][i][1] = (((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
                floatValues[0][j][i][2] = ((val & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            }
        }
        return floatValues;
    }

    private void expandProb(float[][][] src, float[][] dst) {
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[0].length; j++) {
                dst[i][j] = src[i][j][0];
            }
        }
    }

    private void generateBoxes(float[][] prob, float[][][] bias, float scale, Vector<Box> boxes) {
        int h = prob.length;
        int w = prob[0].length;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float score = prob[y][x];
                //only accept prob >threadshold(0.6 here)
                if (score > P_NET_THRESHOLD) {
                    Box box = new Box();
                    //score
                    box.score = score;
                    //box
                    box.box[0] = Math.round(x * 2 / scale);
                    box.box[1] = Math.round(y * 2 / scale);
                    box.box[2] = Math.round((x * 2 + 11) / scale);
                    box.box[3] = Math.round((y * 2 + 11) / scale);
                    //bbr
                    System.arraycopy(bias[y][x], 0, box.bbr, 0, 4);
                    boxes.addElement(box);
                }
            }
        }
    }

    /**
     * Non-Maximum Suppression
     * nms，不符合条件的deleted设置为true
     */
    private void nms(Vector<Box> boxes, float threshold, String method) {
        //NMS.两两比对
        //int delete_cnt=0;
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (!box.deleted) {
                //score<0表示当前矩形框被删除
                for (int j = i + 1; j < boxes.size(); j++) {
                    Box box2 = boxes.get(j);
                    if (!box2.deleted) {
                        int x1 = max(box.box[0], box2.box[0]);
                        int y1 = max(box.box[1], box2.box[1]);
                        int x2 = min(box.box[2], box2.box[2]);
                        int y2 = min(box.box[3], box2.box[3]);
                        if (x2 < x1 || y2 < y1) {
                            continue;
                        }
                        int areaIoU = (x2 - x1 + 1) * (y2 - y1 + 1);
                        float iou = 0f;
                        if ("Union".equals(method)) {
                            iou = 1.0f * areaIoU / (box.area() + box2.area() - areaIoU);
                        } else if ("Min".equals(method)) {
                            iou = 1.0f * areaIoU / (min(box.area(), box2.area()));
                        }
                        //删除prob小的那个框
                        if (iou >= threshold) {
                            if (box.score > box2.score) {
                                box2.deleted = true;
                            } else {
                                box.deleted = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 限制人脸矩形大小
     * @param boxes
     * @param w
     * @param h
     */
    private void squareLimit(Vector<Box> boxes, int w, int h) {
        boxes.forEach(Box::toSquareShape);
        boxes.forEach(box -> box.limit_square(w, h));
    }

    /**
     *  rNet
     * @param img
     * @param boxes
     * @return
     * @throws IOException
     */
    private Vector<Box> rNet(BufferedImage img, Vector<Box> boxes) throws IOException {
        //RNet Input Init
        float[][][][] rNetIn = getCropFloatArray(img, boxes, 24);
        //Run RNet
        rNetForward(rNetIn, boxes);
        //R_NET_THRESHOLD
        checkScore(boxes, R_NET_THRESHOLD);
        //Nms
        nms(boxes, 0.7f, "Union");
        boxes.forEach(Box::calibrate);
        return updateBoxes(boxes);
    }

    /**
     *  去除小于阈值的box
     * @param boxes
     * @param threshold
     */
    private void checkScore(Vector<Box> boxes, float threshold) {
        for (Box box : boxes) {
            if (box.score < threshold) {
                box.deleted = true;
            }
        }
    }

    /**
     *  rNet取出Boxes
     * @param rNetIn
     * @param boxes
     */
    private void rNetForward(float[][][][] rNetIn, Vector<Box> boxes) {
        netForward(rNetIn, boxes, R_NET_IN_NAME, R_NET_OUT_NAME);
    }

    /**
     * 截取box中指定的矩形框(越界要处理)，并resize到size*size大小，返回数据存放到data中。
     *
     * @param img
     * @param box
     * @param size
     * @return float[][][]
     */
    private float[][][] cropAndResize(BufferedImage img, Box box, int size) throws IOException {
        //(2)crop and resize
        float scale = 1.0f * size / box.width();
        BufferedImage bufferedImage = Thumbnails.of(img)
                .sourceRegion(box.left(), box.top(), box.width(), box.height())
                .scale(scale).asBufferedImage();
        //(3)save
        float[][][][] floatValues = image2FloatArr(bufferedImage);
        return floatValues[0];
    }

    /**
     * oNet
     * @param img
     * @param boxes
     * @return
     * @throws IOException
     */
    private Vector<Box> oNet(BufferedImage img, Vector<Box> boxes) throws IOException {
        //ONet Input Init
        float[][][][] oNetIn = getCropFloatArray(img, boxes, 48);
        //Run ONet
        oNetForward(oNetIn, boxes);
        //O_NET_THRESHOLD
        checkScore(boxes, O_NET_THRESHOLD);
        boxes.forEach(Box::calibrate);
        //Nms
        nms(boxes, 0.7f, "Min");
        return updateBoxes(boxes);
    }

    /**
     *  从BufferedImage获取输入
     * @param img
     * @param boxes
     * @param size
     * @return
     * @throws IOException
     */
    private float[][][][] getCropFloatArray(BufferedImage img, Vector<Box> boxes, int size) throws IOException {
        int num = boxes.size();
        float[][][][] in = new float[num][size][size][3];
        int idx = 0;
        for (Box box : boxes) {
            float[][][] curCrop = cropAndResize(img, box, size);
            in[idx++] = curCrop;
        }
        return in;
    }

    /**
     * 取出oNet输出boxes
     * @param oNetIn
     * @param boxes
     */
    private void oNetForward(float[][][][] oNetIn, Vector<Box> boxes) {
        netForward(oNetIn, boxes, O_NET_IN_NAME, O_NET_OUT_NAME);
    }

    /**
     * 从rNet，oNet获取输出
     * @param netIn
     * @param boxes
     * @param inName
     * @param outNames
     */
    private void netForward(float[][][][] netIn, Vector<Box> boxes, String inName, String[] outNames) {
        Tensor<Float> x = Tensors.create(netIn);
        List<Tensor<?>> outputs = predict(x, inName, outNames);
        Tensor<Float> rNetP = outputs.get(0).expect(Float.class);
        Tensor<Float> rNetB = outputs.get(1).expect(Float.class);
        int c1 = (int) rNetP.shape()[0];
        float[][] p = rNetP.copyTo(new float[c1][2]);
        float[][] b = rNetB.copyTo(new float[c1][4]);
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            box.score = p[i][1];
            System.arraycopy(b[i], 0, box.bbr, 0, 4);
        }
    }

    /**
     *  更新boxes，删除不满足的box
     * @param boxes
     * @return Vector<Box>
     */
    private static Vector<Box> updateBoxes(Vector<Box> boxes) {
        Vector<Box> b = new Vector<Box>();
        for (int i = 0; i < boxes.size(); i++) {
            if (!boxes.get(i).deleted) {
                b.addElement(boxes.get(i));
            }
        }
        return b;
    }
}