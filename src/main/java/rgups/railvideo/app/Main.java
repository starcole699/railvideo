package rgups.railvideo.app;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.*;
//import org.opencv.highgui.VideoCapture;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.Videoio;
import rgups.railvideo.system.Native;
import rgups.railvideo.utils.ImageProcessor;

/**
 * Created by Dmitry on 12.05.2017.
 */
public class Main {
    private JFrame frame;
    private JLabel imageLabel;

    byte[] bufferBytes;

    long frame_N = 0;

    int NF_DEPTH = 50;
    float[] NF_BUF;

    public static void main(String[] args) throws IOException {
        //System.loadLibrary("opencv_java320");
        System.out.println(new File("lib/native").getAbsolutePath());
        try {
            Native.addNativesFromRoot("lib/native");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Native.getNativeLibsPathSuffix());

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary("opencv_ffmpeg320_64");
        //System.loadLibrary("opencv_java320");

        Main app = new Main();
        app.initGUI();
        app.runMainLoop(args);
    }

    private void initGUI() {
        frame = new JFrame("Camera Input Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        imageLabel = new JLabel();
        //frame.add(imageLabel);

        JPanel mainPanel = new JPanel();
        frame.add(mainPanel);
        JButton b1 = new JButton("Disable middle button1");
        JButton b2 = new JButton("Disable middle button2");
        JButton b3 = new JButton("Disable middle button3");
        JButton b4 = new JButton("Disable middle button4");
        //mainPanel.add(b1);
        //mainPanel.add(b2);
        //mainPanel.add(b3);
        //mainPanel.add(b4);
        mainPanel.add(imageLabel);
        frame.setVisible(true);
    }

    private void runMainLoop(String[] args) {
        ImageProcessor imageProcessor = new ImageProcessor();
        Mat webcamMatImage = new Mat();
        BufferedImage tempImage;
//        VideoCapture capture = new VideoCapture("http://192.168.1.2:8080/shot.jpg");
//        VideoCapture capture = new VideoCapture("http://192.168.1.106:8080/video");
//        VideoCapture capture = new VideoCapture("E:\\Downloads\\films\\The Machinist '04 [Wrnr].mkv");
        VideoCapture capture = new VideoCapture("C:\\work\\raivideo_test\\192.168.1.64_01_20170629200733332.mp4");

//        VideoCapture capture = new VideoCapture("rtsp://admin:admin123@192.168.1.64:554/Streaming/Channels/101");
        http://192.168.1.64
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1920);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 1024);
        if (capture.isOpened()) {
            while (true) {
                frame_N ++;
                capture.read(webcamMatImage);
                //capture.retrieve(webcamMatImage);
                if (!webcamMatImage.empty()) {
                    //Imgproc.findContours();
                    webcamMatImage = applyFilters(webcamMatImage);
                    //tempImage = imageProcessor.toBufferedImage(webcamMatImage);
                    tempImage = toVisibleImage(webcamMatImage);
                    
                    Image visibleImg = tempImage.getScaledInstance(1920, 1080, Image.SCALE_FAST);

                    ImageIcon imageIcon = new ImageIcon(visibleImg, "Captured video");
                    imageLabel.setIcon(imageIcon);
                    frame.pack(); //this will resize the window to fit the image
                } else {
                    System.out.println(" -- Frame not captured -- Break!");
                    break;
                }
            }
        } else {
            System.out.println("Couldn't open capture.");
        }
    }

    public BufferedImage toVisibleImage(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();

        if ((bufferBytes==null) || (bufferBytes.length != bufferSize)) {
            bufferBytes = new byte[bufferSize];
        }
        matrix.get(0, 0, bufferBytes); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(bufferBytes, 0, targetPixels, 0, bufferBytes.length);
        return image;
    }


    private Mat applyFilters(Mat img) {
        Mat raw_img = img.clone();
        img = doGaussian(img);
        //img = filterNoise(img);
        //img = doSobel(img);
        //img = doLaplacian(img);
        img = doCanny(img);
        //detectChanges(img, raw_img);
        return img;
    }


    private Mat filterNoise(Mat img) {
        int bufferSize = img.channels() * img.cols() * img.rows();
        if (null==NF_BUF || NF_BUF.length != bufferSize) {
            NF_BUF = new float[bufferSize];
            frame_N = 0;
        }
            bufferBytes = new byte[bufferSize];
            img.get(0, 0, bufferBytes);
            for (int i = 0; i<bufferSize; i++){
                float b = bufferBytes[i] & 0xFF;
                NF_BUF[i] = NF_BUF[i]*(NF_DEPTH-1)/NF_DEPTH + b/NF_DEPTH;
                bufferBytes[i] = (byte)Math.round(NF_BUF[i]);
            }

        System.out.println(frame_N);
        img.put(0, 0, bufferBytes);
        return img;
    }

    private Mat doSobel(Mat img) {
        Mat dst = img.clone();
        Imgproc.Sobel(img, dst, -1, 0, 1, 3, 2.0, 1);
        //Imgproc.Sobel(img, dst, -1, 4, 3);
        return dst;
    }

    private Mat doLaplacian(Mat img) {
        Imgproc.Laplacian(img, img, -1);
        return img;
    }

    private Mat doGaussian(Mat img) {
        Mat dst = img.clone();
        Imgproc.GaussianBlur(img, dst, new Size(7, 7), -1);
        return dst;
    }


    private Mat doCanny(Mat img) {
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        //Mat img_ = img.clone();
        //double otsu_thresh_val = Imgproc.threshold(img, img_, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        //double high_thresh_val  = otsu_thresh_val*1;
        //double lower_thresh_val = otsu_thresh_val * 0.5;
        //System.out.println(" --- " + high_thresh_val + " - " + lower_thresh_val);
        //high_thresh_val = 100;
        //lower_thresh_val = 50;
        double high_thresh_val  = 150;
        double lower_thresh_val = 100;
        Imgproc.Canny(img, img, lower_thresh_val, high_thresh_val, 3, false);
        return img;
    }

    Mat oldMat;
    Mat oldRawMat;
    int last_check = 0;
    long diff_threshold = 20000;
    private void detectChanges(Mat img, Mat rawImg) {
        if ((oldMat == null)||(last_check==0)) {
            oldMat = img.clone();
            oldRawMat = rawImg.clone();
        }

        last_check++;

        if (last_check <= NF_DEPTH*2) {
            return;
        }

        last_check = 0;
        int bufferSize = img.channels() * img.cols() * img.rows();
        byte[] currentBytes = new byte[bufferSize];
        byte[] oldBytes = new byte[bufferSize];
        byte[] diffBytes = new byte[bufferSize];
        img.get(0, 0, currentBytes);
        oldMat.get(0, 0, oldBytes);

        long diff = 0;
        for (int i=0; i<bufferSize; i++){
            long one_diff = Math.abs(currentBytes[i] - oldBytes[i]);
            diff += one_diff;
            diffBytes[i] = 0;
            if (one_diff > 10) {
                diffBytes[i] = -127;
            }
        }

        System.out.println("DIFF::::::::::::::: " + diff);

        if (diff > diff_threshold) {
            JFrame frame = new JFrame("MyPanel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //frame.getContentPane().add (new MyPanel2());
            frame.pack();
            frame.setVisible(true);
            frame.toFront();
            frame.repaint();
        }

    }

}
