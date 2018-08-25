// depth first search of a nested hashmap of pixels to check tolerance

package ImageIsolator;

import java.lang.Math;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Image;

class DFSearch {

    static int[][] adjacent = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    
    static int[][] ArrayIsolator(int[][] imageArray,  int[][] startPixels) {
	
	int[][] mask = null;
	for(int[] i : startPixels) {
	    if(mask == null) 
		mask = maskSearch(imageArray, i[0], i[1], i[2], i[3]);
	    else 
	        mask = orMasks(mask, maskSearch(imageArray, i[0], i[1], i[2], i[3]));
	}

	int[][] masked = andMasks(imageArray, mask);

	return masked;

	
    }

    // ands each int in base with the corisponding mask int. base and mast must be equal size.
    static int[][] andMasks(int[][] base, int[][] mask) {
	int baseLen = base.length;
	int baseHeight = base[0].length;
	
	for(int y = 0; y < baseHeight; y++) {
	    for(int x = 0; x < baseLen; x++){
		
		base[x][y] = base[x][y] & mask[x][y];
		
	    }
	}

	return base;
	
    }

    static int[][] orMasks(int[][] base, int[][] mask) {
	int baseLen = base.length;
	int baseHeight = base[0].length;

        for(int y = 0; y < baseHeight; y++) {
	    for(int x = 0; x < baseLen; x++){
		
		base[x][y] = base[x][y] | mask[x][y];
		
	    }
	}

	return base;
	
    }

    /** given an image array of pixels, using a depth first search creates an
        array of values: 0(not valid or unvisited) or 1(visited and valid)
	(pixel is valid if its RGB is within a tolerance amout of the previous pixel)
	returns the image mask

	x is the outside array and y is the inside so that array[2][5] is point (2, 5)
    */
    static int[][] maskSearch(int[][] image, int x, int y, int tolerance, int relativeTolerance) {

        

	int[][] arrayMask = new int[image.length][image[0].length];

	Boolean[][] visited = new Boolean[image.length][image[0].length];

	
	final int refrencePixel = image[x][y];
	int lastPixelRGB = refrencePixel;
	//System.out.println("Refrence pixel: " + Integer.toString(lastPixelRGB));

       
	int[] checkPixel = new int[2];
	checkPixel[0] = x;
	checkPixel[1] = y;
	int[] lastPixel = checkPixel;
	
	ArrayList<int[]> que = new ArrayList<int[]>();
	que.add(checkPixel);

	int width = image.length;
	int height = image[0].length;
	

	
	long count = 0;
	while(que.size() != 0) {

	    lastPixel = que.get(0);
	    que.remove(0);
	    
	    x = lastPixel[0];
	    y = lastPixel[1];

		

	    for(int[] i : adjacent) {
		checkPixel = lastPixel.clone();
		checkPixel[0] += i[0];
		checkPixel[1] += i[1];


		
		if(checkPixel[0] < width && checkPixel[0] >= 0 &&
		   checkPixel[1] < height && checkPixel[1] >= 0) {
		    Boolean visitedCheck = visited[checkPixel[0]][checkPixel[1]];
		    if(visitedCheck == null) {
			visited[checkPixel[0]][checkPixel[1]] = true;
			if(testPixel(refrencePixel, lastPixel, checkPixel, image, tolerance, relativeTolerance)) {
			    que.add(checkPixel);
			    arrayMask[checkPixel[0]][checkPixel[1]] = 0xffffffff;
			}
		    }
		}
		
	    }
	}
	
	return arrayMask;
    }
    
    static Boolean testPixel(int refrencePixel, int[] lastPixel, int[] checkPixel,
			     int[][] image, int tolerance, int relativeTolerance) {

	    int pixel = image[checkPixel[0]][checkPixel[1]];
	    int lastPixelRGB = image[lastPixel[0]][lastPixel[1]];
	    if(checkTolerance(refrencePixel, lastPixelRGB, pixel, tolerance, relativeTolerance)) {
		return true;
		
	    }
	
	    return false;
	}
    
   
	
	
    
    // checks if the total RGB difference of two pixels is within a tolerance amount
    static Boolean checkTolerance(int refPixel, int lastPixel, int checkPixel, int tolerance, int relativeTolerance) {

	int refRed = (refPixel>>16) & 0xff;
	int refGreen = (refPixel>>8) & 0xff;
	int refBlue = refPixel & 0xff;

	int lastRed = (lastPixel>>16) & 0xff;
	int lastGreen = (lastPixel>>8) & 0xff;
	int lastBlue = lastPixel & 0xff;

	int checkRed = (checkPixel>>16) & 0xff;
	int checkGreen = (checkPixel>>8) & 0xff;
	int checkBlue = checkPixel & 0xff;

        int refRedDif = Math.abs(refRed - checkRed);
	int refGreenDif = Math.abs(refGreen - checkGreen);
	int refBlueDif = Math.abs(refBlue - checkBlue);

	int lastRedDif = Math.abs(lastRed - checkRed);
	int lastGreenDif = Math.abs(lastGreen - checkGreen);
	int lastBlueDif = Math.abs(lastBlue - checkBlue);

	if(refRedDif < tolerance
	   && refGreenDif < tolerance
	   && refBlueDif < tolerance){ // check if the colors are within tolerance of refrence pixel
	    if(lastRedDif < relativeTolerance
	       && lastGreenDif < relativeTolerance
	       && lastBlueDif < relativeTolerance) { // check if the colors are withing tolerance of the last pixel
		return true;
	    }
	}
	return false;
	
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

    static String arrayToString(int[][] intArray) {
	String output = "{";
	    for(int[] i : intArray) {
		output += arrayToString(i);
		output += ", ";
	    }
	output = output.substring(0, output.length() - 2);
	output += "}";

	return output; 
    }
    
    public static String arrayToStringGrid(int[][] intArray) {
	String output = "{";
	    for(int[] i : intArray) {
		output += arrayToString16(i);
		output += ", ";
		output += "\n";
	    }
	output = output.substring(0, output.length() - 2);
	output += "}";

	return output; 
    }
    static String arrayToString16(int[] intArray) {
	String output = "[";
	for(int i : intArray) {
	    output += Integer.toString(i, 10)+", ";
	}
	output = output.substring(0, output.length() - 2);
	output += "]";

	return output;
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

    static String strArrayToString(ArrayList<String> stringArray) {
	if(stringArray.size() == 0) {
	    return "{empty}";
	}
	
        String output = "{";
	for(String i : stringArray) {
	    output += i;
	    output += ", ";
	}
	output = output.substring(0, output.length() - 2);
	output += "}";

	return output;
    }
    

    
}
