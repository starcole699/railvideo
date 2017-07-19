package rgups.railvideo.ui;

import org.opencv.core.Mat;
import rgups.railvideo.core.flow.RailvideoEvent;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by Dmitry on 30.06.2017.
 */
public class ImageProcessorFrame {

    private JFrame frame;
    private JLabel imageLabel;

    private ProcessorFrameController master;

    byte[] bufferBytes;


    public ImageProcessorFrame(ProcessorFrameController master) {
        this.master = master;
    }


    public void dispose(){
        if (null != frame) {
            frame.dispose();
        }
    }

    private void initGUI() {
        frame = new JFrame("Camera Input Example");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(1024, 768);
        imageLabel = new JLabel();
        //frame.add(imageLabel);

        JPanel mainPanel = new JPanel();
        frame.add(mainPanel);
        JButton b1 = new JButton("Disable middle button1");
        //mainPanel.add(b1);
        mainPanel.add(imageLabel);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (null != master) {
                    master.onFrameClosed();
                }
            }
        });
        frame.setVisible(true);
    }


    public void showFrame() {
        if (null == frame) {
            initGUI();
        } else if (!frame.isVisible()) {
            frame.setVisible(true);
        }

        frame.toFront();
    }


    public void updateImage(Mat image, RailvideoEvent event) {
        showFrame();
        BufferedImage tempImage;

        tempImage = toVisibleImage(image);

        ImageIcon imageIcon = new ImageIcon(tempImage, "Captured video");
        imageLabel.setIcon(imageIcon);
        frame.pack();
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
}
