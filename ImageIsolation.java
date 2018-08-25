package ImageIsolator;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;


class ImageIsolation {

    static BufferedImage initialImage;
    static BufferedImage processedImage;
    static ArrayList<int[]> startPixels = new ArrayList<int[]>();
    static IsolatorGUI gui;
    
    public static void main(String[] args) {
        gui = new IsolatorGUI();
    }
    
    static void loadImage() {

	JFileChooser fc = new JFileChooser();
	int returnVal = fc.showOpenDialog(gui);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    fc.setCurrentDirectory(file);
	
	    System.out.println("attempting to upload image at " + file);
	    try {
		initialImage = ImageIO.read(file);
		gui.setInitialImage(initialImage);
		gui.imgWidth = initialImage.getWidth();
		gui.imgHeight = initialImage.getHeight();

		System.out.println("success");
	    
	    } catch (IOException e) {
		System.out.println("cannot load file " + file);
		e.printStackTrace();
	    }
	    
	}
	
	return;
    }

    static void saveImage(BufferedImage img) {

	JFileChooser fc = new JFileChooser();
	int returnVal = fc.showOpenDialog(gui);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	
	    try {
	   
	    
		ImageIO.write(img, "png", file);
	    }catch(IOException e) {System.out.println("errorSaving");}
	}

	return;
    }

    static void isolate() {
	if(initialImage != null && startPixels.size() > 0) {
	    processedImage = ImageProcessor.isolate(initialImage,
	    startPixels.toArray(new int[startPixels.size()][4]));

	    gui.setProcessedImage(processedImage);
	}
    }

    public static void setInitialImage(BufferedImage img) {
	initialImage = img;
    }
    public static void resetInitialImage() {
	initialImage = null;
	gui.setInitialImage(null);
    }

    public static void setProcessedImage(BufferedImage img) {
	processedImage = img;
    }
    public static void resetProcessedImage() {
	processedImage = null;
	gui.setProcessedImage(null);
    }
    
    public static void addStartPixel(int x, int y, int absTolerance, int relTolerance){
	int[] pxl = new int[4];
	pxl[0] = x;
	pxl[1] = y;
	pxl[2] = absTolerance;
	pxl[3] = relTolerance;
	
	startPixels.add(pxl);

	System.out.println("StartPixels: " + arrayToString(startPixels));
    }
    
    public static void resetStartPixels() {
	startPixels = new ArrayList<int[]>();
    }

    static String arrayToString(ArrayList<int[]> intArray) {
	if(intArray.size() == 0) {
	    return "{empty}";
	}
	
        String output = "{";
	for(int[] i : intArray) {
	    output += arrayToString(i);
	    output += ", ";
	}
	output = output.substring(0, output.length() - 2);
	output += "}";

	return output; 
    }
     static String arrayToString(int[] intArray) {
	String output = "[";
	for(int i : intArray) {
	    output += i+", ";
	}
	output = output.substring(0, output.length() - 2);
	output += "]";

	return output;
    }

    static void clearStartPixels() {
	startPixels = new ArrayList<int[]>();
    }
}
