// add initial image changer
// create an imagePanel class:
// -has a change image method
// --calls .repaint
// -has a image variable
// override paint method [draw(imagetodraw)]

package ImageIsolator;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.AlphaComposite;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;


public class IsolatorGUI extends JFrame {

    int width = 1000;
    int height = 800;
    int controlsHeight = 150;
    int imgWidth = 300;
    int imgHeight = 500;

    JLabel instructions, initialImageL, processedImageL, absToleranceL, relToleranceL;
    JPanel initial, processed, grid, controlsP, absSliderP, relSliderP;
    JButton loadB, saveB, resetB, clearMarkersB, isolateB, rescaleB;
    JSlider absToleranceS, relToleranceS;
    int absTolerance, relTolerance;
    BufferedImage initialImage, initialImageNull, processedImage, processedImageNull,
	pointImage;
    

    public IsolatorGUI(){

	absTolerance = 50;
	relTolerance = 50;

	pointImage = drawCircle(20, 5, 1, 0xff0000);
	
	JFrame frame = new JFrame("Image Isolator");

	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	
	frame.setLayout(new BorderLayout());

        
	
	/**frame.addComponentListener(new ComponentListener() {
		@Override
		public void componentHidden(ComponentEvent e) {}
		@Override
		public void componentShown(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		
		
		@Override
		public void componentResized(ComponentEvent e) {
		    redrawPanel(initial, initialImage, initialImageL);
		    redrawPanel(processed, processedImage, processedImageL);
		}
		});*/
	
		


	// settup instruction label
	instructions = new JLabel("Load an image, click on pixels to be isolated selecting the absolute and relative tolerance for each pixel, once ready, press isolate.");
	frame.add(instructions, BorderLayout.NORTH);


	initialImageNull = buildTextImage("initial image",
					  BufferedImage.TYPE_INT_ARGB);
	processedImageNull = buildTextImage("processed image",
					    BufferedImage.TYPE_INT_ARGB);
	
	initialImage = initialImageNull;
	processedImage = processedImageNull;
	
	grid = new JPanel();
	initial = new JPanel();
	processed = new JPanel();
	grid.setLayout(new GridLayout(1,2));
	controlsP = new JPanel();
	controlsP.setLayout(new GridLayout(2, 3));
	absSliderP = new JPanel();
	absSliderP.setLayout(new GridLayout(2, 1));
	relSliderP = new JPanel();
	relSliderP.setLayout(new GridLayout(2, 1));
	
	// settup initial side

	initial.setLayout(new BorderLayout());

	//code from https://stackoverflow.com/questions/17267109/how-to-add-a-mouselistener-to-a-frame
	initial.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
		    int x = e.getX();
		    int y = e.getY();
		    
		    int[] point = scalePoint(x, y,
					     imgWidth,
					     imgHeight,
					     initialImageL.getWidth(),
					     initialImageL.getHeight());

		    x = point[0];
		    y = point[1];
		    if(ImageIsolation.initialImage != null) {
			if(x < ImageIsolation.initialImage.getWidth() && // check click actually on image
			   y < ImageIsolation.initialImage.getHeight()){
			    ImageIsolation.addStartPixel(x, y, absTolerance, relTolerance);

			    setInitialImage(initialImage);
			}
		    }
		}
	    });

        

	absToleranceL = new JLabel("Absolute Tolerance");
	relToleranceL = new JLabel("Relative Tolerance");


	// Code from http://www.java2s.com/Tutorial/Java/0240__Swing/Sliderchangeevent.htm
	absToleranceS = new JSlider(0, 255, 0);
	absToleranceS.setMajorTickSpacing(85);
	absToleranceS.setMinorTickSpacing(17);
	absToleranceS.setPaintTicks(true);
	absToleranceS.setPaintLabels(true);
	absToleranceS.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    absTolerance = absToleranceS.getValue();
		}
	    });
	
	relToleranceS = new JSlider(0, 255, 0);
	relToleranceS.setMajorTickSpacing(85);
	relToleranceS.setMinorTickSpacing(17);
	relToleranceS.setPaintTicks(true);
	relToleranceS.setPaintLabels(true);
	relToleranceS.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    relTolerance = relToleranceS.getValue();
		}
	    });

	

	
	loadB = new JButton("Load Image");
	loadB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    ImageIsolation.loadImage();
		    ImageIsolation.clearStartPixels();
		}
		
	    });

	clearMarkersB = new JButton("Clear Markers");
	clearMarkersB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    ImageIsolation.clearStartPixels();
		    setInitialImage(initialImage);
		}
	    });

	isolateB = new JButton("Isolate");
	isolateB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    ImageIsolation.isolate();
		}
	    });

	rescaleB = new JButton("Rescale Image");
	rescaleB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    setInitialImage(initialImage);
		    setProcessedImage(processedImage);
		}
	    });
	
	//initial.add(initialControls, BorderLayout.SOUTH);
	initialImageL = new JLabel(new ImageIcon(initialImage));
	initial.add(initialImageL, BorderLayout.CENTER);
	
	
	// settup processed side
	processed.setLayout(new BorderLayout());

	
	saveB = new JButton("Save Image");
	saveB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if(processedImage != null) {
			ImageIsolation.saveImage(ImageIsolation.processedImage);
		    }
		}
	    });
	

			
	resetB = new JButton("Reset");
	resetB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    ImageIsolation.resetInitialImage();
		    ImageIsolation.resetProcessedImage();
		    
		    //setInitialImage(null);
		}
	    });
		    
	absSliderP.add(absToleranceL);
	absSliderP.add(absToleranceS);

	relSliderP.add(relToleranceL);
	relSliderP.add(relToleranceS);
	
        controlsP.add(absSliderP);
	controlsP.add(relSliderP);

	controlsP.add(rescaleB);
        controlsP.add(resetB);
	
	controlsP.add(clearMarkersB);
	controlsP.add(isolateB);
	
	controlsP.add(loadB);
	controlsP.add(saveB);	

	processedImageL = new JLabel(new ImageIcon(processedImage));
	processed.add(processedImageL, BorderLayout.CENTER);
	
	grid.add(initial);
	grid.add(processed);

	frame.add(grid, BorderLayout.CENTER);

	controlsP.setPreferredSize(new Dimension(width, controlsHeight));
	
	frame.add(controlsP, BorderLayout.SOUTH);

	frame.pack();
	
	frame.setVisible(true);

    }

    public void redrawPanel(JPanel panel, BufferedImage panelImage, JLabel imgL) {
	Dimension panelD = panel.getSize();
	int panelW = panelD.width;
	int panelH = panelD.height;
	
	BufferedImage tmpBImg = scaleBufferedImage(panelImage, panelW, panelH);

        panel.remove(imgL);

	JLabel imgLNew = new JLabel(new ImageIcon(tmpBImg));
	initialImageL = imgLNew;
	
	panel.add(imgLNew, BorderLayout.CENTER);

	panel.revalidate();
	System.out.println("redrawn");
    }
        
    public void setInitialImage(BufferedImage img) {
	if(img == null || ImageIsolation.initialImage == null) {
	    initialImage = initialImageNull;
	    System.out.println("Setting initialImage to null image");

	    initial.remove(initialImageL);
	    
	    initialImageL = new JLabel(new ImageIcon(initialImage));
	}
	else {
	    Dimension panelD = initial.getSize();
	    int panelW = panelD.width;
	    int panelH = panelD.height;
	    

	    
	    System.out.println("Setting intialImage to img");
	

	    initial.remove(initialImageL);

	    
	    initialImage = img;

	    BufferedImage initialImage = scaleBufferedImage(img, panelW, panelH);
	
        

	    
	    initialImageL = new JLabel(new ImageIcon(drawCircleOverlay(initialImage, ImageIsolation.startPixels)));
	}

	
	initial.add(initialImageL, BorderLayout.CENTER);

	initial.revalidate();
    }

    

    public void setProcessedImage(BufferedImage img) {
	if(img == null || ImageIsolation.initialImage == null){
	    processedImage = processedImageNull;
	    System.out.println("Setting processedImage to null image");
	}
	
	else {
	    
	    Dimension panelD = processed.getSize();
	    int panelW = panelD.width;
	    int panelH = panelD.height;
	    
	    processedImage = scaleBufferedImage(img, panelW, panelH);
	    System.out.println("Setting processedImage to img");
	}

	
	processed.remove(processedImageL);
	
	processedImageL = new JLabel(new ImageIcon(processedImage));
        processed.add(processedImageL, BorderLayout.CENTER);

	processed.revalidate();
    }

    BufferedImage buildTextImage(String text, int type){
	BufferedImage img = new BufferedImage(imgWidth, imgHeight, type);
	Graphics g = img.getGraphics();
	g.setColor(Color.GRAY);
	Font font = new Font("Serif", Font.PLAIN, 24);
	g.setFont(font);
	g.drawString(text, 20, imgHeight/2);
	return img;
    }

    // code from https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    BufferedImage scaleBufferedImage(BufferedImage img, int newx, int newy) {
	Image tempImg = img.getScaledInstance(newx, newy, Image.SCALE_SMOOTH);

        img = new BufferedImage(newx, newy, BufferedImage.TYPE_INT_ARGB);
	Graphics2D bGr = img.createGraphics();
	bGr.drawImage(tempImg, 0, 0, null);
	bGr.dispose();

	return img;
	    
    }

    int[] scalePoint(double x, double y, double realW, double realH, double width, double height) {

	double scaleX = realW/width;
	double scaleY = realH/height;

	x = x * scaleX;
	y = y * scaleY;

	int[] point = new int[2];
	point[0] = (int)x;
	point[1] = (int)y;
	return point;
    }

   
    
    BufferedImage drawCircle(int radius, int thickness, int crossWidth, int color) {
	BufferedImage circle = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_ARGB);


	for(int x = 0; x < radius*2; x++) {
	    for(int y = 0; y < radius*2; y++) {
		int val = (x-radius)*(x-radius) + (y-radius)*(y-radius);
		if(val < radius && (val > thickness || (Math.abs(x-radius) < crossWidth ^
							Math.abs(y-radius) < crossWidth))) {
		    circle.setRGB(x, y, 0xffff0000);
		    
		    }
		
	    }
	}


	
	return circle;
    }

    BufferedImage drawCircleOverlay(BufferedImage img, ArrayList<int[]> startPixels) {
        
	for(int[] i : startPixels) {
	    int[] j = new int[2];
	    j = scalePoint(i[0], i[1],
			   img.getWidth(),
			   img.getHeight(),
			   initialImage.getWidth(),
			   initialImage.getHeight()
			   );
	    for(int x = 0; x < img.getWidth(); x++) {
		for(int y = 0; y < img.getHeight(); y++) {
		
		    
		    if(x == j[0] && y == j[1]) {

			img = scaleBufferedImage(img, initial.getWidth(), initial.getHeight());
	
	
		        Graphics2D imgG = img.createGraphics();
			imgG.drawImage(pointImage,
				       x - pointImage.getWidth()/2,
				       y - pointImage.getHeight()/2,
				       null);
		        imgG.dispose();
		    }
		}
		
	    }
	}

	//	ImageIsolation.saveImage(img);
	
	return img;
    }
}
