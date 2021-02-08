package net.iceyleagons.computercraft.image;

import lombok.SneakyThrows;
import org.apache.commons.lang.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author TOTHTOMI
 */
public class MonitorImage {

    private BufferedImage bufferedImage;
    private int row = 200;
    private int column = 100;
    private Graphics2D graphics2D;
    private boolean scale = false; //does not work as wanted

    private static final File test = new File("test.png");

    public MonitorImage(int sizeInMapsX, int sizeInMapsY) {
        bufferedImage = new BufferedImage(sizeInMapsX*128, sizeInMapsY*128, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, 10);
        //this.scale = scale;
        graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics2D.setFont(Font.getFont("Calibri"));
        setCursorStroke(1f);

    }

    public void setCursorPos(int row, int column) {
        this.row = scale ? (bufferedImage.getWidth()/100)*row : row; //(getWidth/100)*row
        this.column = scale ? (bufferedImage.getHeight()/100)*column : column; //(getHeight/100)*row
    }

    public void setColor(int r, int g, int b) {
        graphics2D.setColor(new Color(r, g, b));
    }

    public void setCursorStroke(float width) {
        graphics2D.setStroke(new BasicStroke(width));
    }

    public void drawRect(int width, int height, boolean fill) {
        if (fill)
            graphics2D.fillRect(column, row, width, height);
        else
            graphics2D.drawRect(column, row, width, height);
    }

    public void drawCircle(int width, int height, boolean fill) {
        if (fill)
            graphics2D.fillOval(column, row, width, height);
        else
            graphics2D.drawOval(column, row, width, height);
    }

    public void drawLine(int x2, int y2) {
        graphics2D.drawLine(column, row, scale ? (bufferedImage.getHeight()/100)*x2 : x2, scale ? (bufferedImage.getWidth()/100)*y2 : y2);
    }

    public void printText(String string) {
        graphics2D.drawString(string, column, row);
    }

    public void clearScreen() {
        graphics2D.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @SneakyThrows
    public void test() {
        setCursorStroke(0.1f);
        printDebugLines();

        ImageIO.write(bufferedImage, "png", test);
    }

    private void printDebugLines() {
        int mapsY = getMapsRequired(bufferedImage.getHeight());
        int mapsX = getMapsRequired(bufferedImage.getWidth());

        graphics2D.setColor(Color.darkGray);
        for (int i = 0; i < mapsY; i++) {
            graphics2D.drawLine(0, i*128, bufferedImage.getWidth(), i*128);
        }
        for (int i = 0; i < mapsX; i++) {
            graphics2D.drawLine(i*128, 0, i*128, bufferedImage.getHeight());
        }
    }

    private int getMapsRequired(int imageValue) {
        int remainder = imageValue%128;
        int max = imageValue/128;

        return remainder != 0 ? max + 1 : max;
    }

    public BufferedImage[] getMapImages() {
        int mapsY = getMapsRequired(bufferedImage.getHeight());
        int mapsX = getMapsRequired(bufferedImage.getWidth());

        BufferedImage[] images = new BufferedImage[mapsY*mapsX];
        int count = 0;
        for (int i = 0; i < mapsY; i++) {
            for (int j = 0; j < mapsX; j++) {
                images[count++] =  bufferedImage.getSubimage(j*128, i*128, 128, 128);

            }
        }

        return images;
    }

    public void testPattern() {
        graphics2D.setColor(Color.WHITE);
        int column = 2;
        Random random = new Random();
        for (int j = 0; j < 6; j++) {
            for (int i = 1; i < 10; i++) {
                setCursorPos(10 * i, column += 10);
                setColor(random.nextInt(244), random.nextInt(244), random.nextInt(244));
                printText("Test");
            }
        }

        column = 2;
        for (int i = 1; i < 14; i++) {
            setCursorPos(100, column);
            setColor(random.nextInt(244), random.nextInt(244), random.nextInt(244));
            column += 40;
            drawRect(10, 10, random.nextBoolean());
        }


        setCursorPos(150, 0);
        drawLine(bufferedImage.getWidth(), 0);

        setCursorPos(150, 0);
        drawLine(bufferedImage.getWidth(), 150);

        setColor(255, 255, 255);
        column = 2;
        for (int i = 1; i < 14; i++) {
            setCursorPos(120, column);
            column += 40;
            drawCircle(10, 10, random.nextBoolean());
        }



        test();
    }

    public static void main(String[] args) throws IOException {
        MonitorImage monitor = new MonitorImage(2,2);
        monitor.testPattern();

        /*
        BufferedImage[] images = monitor.getMapImages();
        System.out.println(images.length);
        for (int i = 0; i < images.length; i++) {
            File file =  new File(i +".png");
            ImageIO.write(images[i], "png", file);
        }

         */
    }



}
