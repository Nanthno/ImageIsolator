// Median between GUI and other programs

package ImageIsolator;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import ImageIsolator.DFSearch;


class ImageProcessor {

    static File inputFile = new File("ImageIsolator/Images/smile.png");
    static File outputFile = new File("isolated2.png");

    static int testTolerance = 80;
    static int testRelativeTolerance = 30;

    // format (x, y, tolerance, relativetolerance)
    static int[][] startPixels = {{130, 60, testTolerance, testRelativeTolerance},
				  {130, 130, testTolerance, testRelativeTolerance},
				  {113, 103, testTolerance, testRelativeTolerance},
				  {154, 107, testTolerance, testRelativeTolerance}};

    public static void main(String[] args) {
	
	System.out.println("uploading image...");
	BufferedImage img = loadImage(inputFile);
	System.out.println("success");

	BufferedImage isolatedImage = isolate(img, startPixels);


	saveImage(outputFile, isolatedImage);

	System.out.println("image complete");
    }


    static BufferedImage loadImage(File inputFile) {
	BufferedImage img = null;
	System.out.println("uploading image...");
	try {
	    img = ImageIO.read(inputFile);
	    
	} catch (IOException e) {
	    System.out.println("cannot load file " + inputFile);
	    e.printStackTrace();
	}

	return img;
    }

    static void saveImage(File outputFile, BufferedImage img) {
	try {
	    ImageIO.write(img, "png", outputFile);
	}catch(IOException e) {System.out.println("errorSaving");}

	return;
    }

    static BufferedImage isolate(BufferedImage img, int[][] startPixels) {
	System.out.println("Begining isolation with startPixels: " + DFSearch.arrayToString(startPixels));

	int[][] imageArray = imageToArrays(img);

	
	System.out.println("Image Size: (" + imageArray.length + ", " + imageArray[0].length + ")");

	imageArray = DFSearch.ArrayIsolator(imageArray, startPixels);

	BufferedImage finalImage = arraysToImage(imageArray);

	System.out.println("isolation compleate");

	return finalImage;
    }
    
    static BufferedImage arraysToImage(int[][] imageArray) {

	int width = imageArray.length;
	int height = imageArray[0].length;

	BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	for(int x = 0; x < width; x++) {
	    for(int y = 0; y < height; y++) {
		int pixel = imageArray[x][y];
		newImage.setRGB(x, y, pixel);
	    }
	}

	return newImage;
    }
		
    
    static int[][] imageToArrays(BufferedImage img) {

	int imgHeight = img.getHeight();
	int imgWidth = img.getWidth();

	int[][] pixelArray = new int[imgWidth][imgHeight];
	for(int y = 0; y < imgHeight; y++) {
	    for(int x = 0; x < imgWidth; x++) {
		//System.out.println("(" + x + ", " + y + "); imageSize = (" + imgWidth + ", " + imgHeight + "); arraySize = (" + pixelArray.length + ", " + pixelArray[x].length + ")");
		pixelArray[x][y] = img.getRGB(x, y);

	    }
	}

	return pixelArray;

    
    }

   
}
