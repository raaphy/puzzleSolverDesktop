package de.raphaelschilling;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class GuiMain extends JFrame {
    private static String PICTURE_PATH = "./examplePicture/braun2.jpg";
    private double RESICE_FACTOR = 0.2;
    private JLabel originalPictureLabel;
    private JLabel analyseLabel;

    public static void main(String[] args) {
        GuiMain frame = new GuiMain();
    }

    public GuiMain() {
        setProporties();
        drawOriginalPicture(PICTURE_PATH);
        addColorMouseListener();
        add(analyseLabel);
    }

    private void addColorMouseListener() {
        originalPictureLabel.addMouseListener(new MouseListener() {
            public boolean isRunning = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(isRunning) {
                    return;
                }
                isRunning = true;
                e.translatePoint(originalPictureLabel.getX(), originalPictureLabel.getY());
                System.out.println("x: " + e.getX() + " y: " + e.getY());
                drawAnalysePicture((int) (e.getX() / RESICE_FACTOR), (int) (e.getY() / RESICE_FACTOR));
                isRunning = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void drawIntArray(int[][] pictureMatrix) {
        BufferedImage bufferedImage = new BufferedImage(pictureMatrix.length, pictureMatrix[0].length, TYPE_INT_RGB);
        for (int y = 0; y < pictureMatrix[0].length; y++) {
            for (int x = 0; x < pictureMatrix.length; x++) {
                bufferedImage.setRGB(x, y, pictureMatrix[x][y]);
            }
        }
        Image image = bufferedImage.getScaledInstance((int) (pictureMatrix.length*RESICE_FACTOR), -1, Image.SCALE_FAST);
        analyseLabel.setIcon(new ImageIcon(image));

        pack();
    }

    private static int[][] arrayFromImage(String picturePath) {
        try {
            BufferedImage bufferedImage= ImageIO.read(new File(picturePath));
            final int width = bufferedImage.getWidth();
            final int height = bufferedImage.getHeight();
            int[][] result = new int[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    result[x][y] = bufferedImage.getRGB(x,y);
                }
            }
            return result;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private void drawOriginalPicture(String picturePath) {
        Image image = null;
        try {
            BufferedImage bufferedImage= ImageIO.read(new File(picturePath));
            image = bufferedImage.getScaledInstance((int)(bufferedImage.getWidth()*RESICE_FACTOR), -1, Image.SCALE_FAST);
        } catch (IOException ignored) {
        }
        if(image != null) {
            originalPictureLabel = new JLabel(new ImageIcon(image));
            add(originalPictureLabel);
        pack();}
    }

    private void drawAnalysePicture(int xPuzzleColor, int yPuzzleColor) {
        PuzzleSolver puzzleSolver = new PuzzleSolver();
        puzzleSolver.setImageArray(arrayFromImage(PICTURE_PATH));
        puzzleSolver.setCoordiatesReferenceColour(xPuzzleColor, yPuzzleColor);
        drawIntArray(puzzleSolver.getMaskPicture());

    }

    private void setProporties() {
        getContentPane().setLayout(new FlowLayout());
        setVisible(true);
        setTitle("PuzzleSover");
        setSize(1000, 620);
        setLocation(50, 50);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        analyseLabel = new JLabel();

    }

}
