package org.uuzics.simplecaptchasolverapi.util.processor;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.tesseract.TessBaseAPI;
import org.uuzics.simplecaptchasolverapi.Configuration;

import javax.imageio.IIOImage;
import java.awt.*;
import java.awt.image.*;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OcrProcessor {
    /**
     * Perform OCR process on captcha image.
     *
     * @param originalImage Image to be OCRed
     * @param configuration Configuration
     * @return OCRed captcha text
     * @throws UnsupportedEncodingException Throws UnsupportedEncodingException
     */
    public static String doOcrProcess(BufferedImage originalImage, Configuration configuration) throws UnsupportedEncodingException {
        String tess_data = configuration.getOcr().getTess_data();
        String tess_lang = configuration.getOcr().getTess_lang();

        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.Init(tess_data, tess_lang);

        // The following pieces of code are taken from tess4j-4.6.1 net.sourceforge.tess4j.Tesseract
        // From net.sourceforge.tess4j.Tesseract.doOCR, line 355 to 356, renamed RenderedImage.
        IIOImage oimage = new IIOImage(originalImage, null, null);
        RenderedImage renderedImage = oimage.getRenderedImage();

        // From net.sourceforge.tess4j.Tesseract.setImage, line 454 to 461.
        ByteBuffer buff = getImageByteBuffer(renderedImage);
        int bpp;
        DataBuffer dbuff = originalImage.getData(new Rectangle(1, 1)).getDataBuffer();
        if (dbuff instanceof DataBufferByte) {
            bpp = originalImage.getColorModel().getPixelSize();
        } else {
            bpp = 8; // BufferedImage.TYPE_BYTE_GRAY image
        }

        // From net.sourceforge.tess4j.Tesseract.setImage, line 477 to 480, introduced/renamed local variables,
        // changed API from net.sourceforge.tess4j.TessAPI.TessBaseAPISetImage to org.bytedeco.tesseract.TessBaseAPI.SetImage
        int xSize = originalImage.getWidth();
        int ySize = originalImage.getHeight();
        int byteSpp = bpp / 8;
        int byteSpl = (int) Math.ceil(xSize * bpp / 8.0);
        tessBaseAPI.SetImage(buff, xSize, ySize, byteSpp, byteSpl);

        BytePointer bp = tessBaseAPI.GetUTF8Text();
        String ocrText = bp.getString("utf-8");
        tessBaseAPI.End();
        bp.deallocate();

        return ocrText;
    }

    /**
     * Gets pixel data of an <code>RenderedImage</code> object.
     * This method is taken from tess4j-4.6.1 <code>net.sourceforge.tess4j.util.ImageIOHelper</code>
     * and changed to a static method.
     *
     * @param image an <code>RenderedImage</code> object
     * @return a byte buffer of pixel data
     */
    private static ByteBuffer getImageByteBuffer(RenderedImage image) {
        ColorModel cm = image.getColorModel();
        WritableRaster wr = image.getData().createCompatibleWritableRaster(image.getWidth(), image.getHeight());
        image.copyData(wr);
        BufferedImage bi = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
        return convertImageData(bi);
    }

    /**
     * Converts <code>BufferedImage</code> to <code>ByteBuffer</code>.
     * This method is taken from tess4j-4.6.1 <code>net.sourceforge.tess4j.util.ImageIOHelper</code>
     * and changed to a static method.
     *
     * @param bi Input image
     * @return pixel data
     */
    private static ByteBuffer convertImageData(BufferedImage bi) {
        DataBuffer buff = bi.getRaster().getDataBuffer();
        byte[] pixelData = ((DataBufferByte) buff).getData();
        ByteBuffer buf = ByteBuffer.allocateDirect(pixelData.length);
        buf.order(ByteOrder.nativeOrder());
        buf.put(pixelData);
        ((Buffer) buf).flip();
        return buf;
    }
}
