package org.uuzics.simplecaptchasolverapi.util.processor;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.uuzics.simplecaptchasolverapi.Configuration;

import java.awt.image.BufferedImage;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;

public class PreOcrProcessor {
    public static BufferedImage doPreOcrProcess(BufferedImage originalImage, Configuration configuration) {
        double threshold_thresh = configuration.getOcr().getThreshold_thresh();
        double threshold_maxval = configuration.getOcr().getThreshold_maxval();
        int erode_element_size = configuration.getOcr().getErode_element_size();
        int resize_size = configuration.getOcr().getResize_size();

        OpenCVFrameConverter.ToMat toMatConverter = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();

        Mat originMat = toMatConverter.convert((java2DFrameConverter.convert(originalImage)));

        Mat grayMat = new Mat(originMat.rows(), originMat.cols(), CV_8UC1);
        opencv_imgproc.cvtColor(originMat, grayMat, opencv_imgproc.COLOR_RGB2GRAY);

        Mat binMat = new Mat(originMat.rows(), originMat.cols(), CV_8UC1);
        opencv_imgproc.threshold(grayMat, binMat, threshold_thresh, threshold_maxval, opencv_imgproc.THRESH_BINARY);

        Mat erodeMat = new Mat(originMat.rows(), originMat.cols(), CV_8UC1);
        Mat erodeElement = opencv_imgproc.getStructuringElement(MORPH_RECT, new Size(erode_element_size, erode_element_size));
        opencv_imgproc.erode(binMat, erodeMat, erodeElement);

        Mat resizeMat = new Mat(originMat.rows() * resize_size, originMat.cols() * resize_size, CV_8UC1);
        opencv_imgproc.resize(erodeMat, resizeMat, resizeMat.size(), resize_size, resize_size, opencv_imgproc.INTER_LINEAR);

        return java2DFrameConverter.convert(toMatConverter.convert(resizeMat));
    }
}
