import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 读取视频文件，获取其中的帧画面生成jpg文件。
 */
public class Grabber {

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param videofile 源视频文件路径
     * @param framefile 截取帧的图片存放路径
     * @throws Exception
     */
    public static void fetchFrame(String videofile, String framefile)
            throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File(framefile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        ff.start();
        int length = ff.getLengthInFrames();
        System.out.println("total frames: " + length);
        int i = 0;
        Frame f = null;
        while (i < length) {
            // 过滤前50帧
            f = ff.grabFrame();
            if ((i > 50) && (f.image != null)) {
                break;
            }
            i++;
        }

        //转化为 IplImage
//        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//        IplImage img = converter.convert(f);

        //转化为 BufferedImage
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        BufferedImage bufferedImage = java2DFrameConverter.convert(f);

        //缩放图像
//        int width = img.width();
//        int height = img.height();
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//        bi.getGraphics().drawImage(bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

        ImageIO.write(bufferedImage, "jpg", targetFile);

        ff.stop();
        System.out.println("生成耗费时间：" + (System.currentTimeMillis() - start) + "ms, 生成文件：" + framefile);
    }

    public static void main(String[] args) {
        try {
            Grabber.fetchFrame("D:\\VR2B52.wmv", "D:\\test.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

