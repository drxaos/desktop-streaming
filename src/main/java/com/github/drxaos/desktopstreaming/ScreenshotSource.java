package com.github.drxaos.desktopstreaming;

import io.vertx.core.Vertx;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenshotSource implements ImageSource {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Server(new ScreenshotSource(new Rectangle(150, 150, 850, 650))));
    }

    Rectangle rectangle;
    Robot robot;

    public ScreenshotSource() {
        rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public ScreenshotSource(Rectangle rectangle) {
        this.rectangle = rectangle;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage getImage() {
        return getDesktopScreenshot();
    }

    private BufferedImage getDesktopScreenshot() {
        return robot.createScreenCapture(rectangle);
    }
}
