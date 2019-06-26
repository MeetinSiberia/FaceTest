package ok;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;

import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.math.geometry.shape.Ellipse;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;


import no.uib.cipr.matrix.sparse.ILUT;

/**
 * OpenIMAJ Chapter 1 - 2
 *
 */
@SuppressWarnings("unused")
public class Chap02 {
    public static void main( String[] args ) throws IOException {
    	//Create an image
        //#1 MBFImage image = new MBFImage(320,70, ColourSpace.RGB);
    	
    	//#2 MBFImage image = ImageUtilities.readMBF(new File("/Users/Phoenix/Desktop/gdufs-logo.jpg"));
    	
    	MBFImage image = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));

        //Fill the image with white
        ///image.fill(RGBColour.WHITE);
        		        
        //Render some test into the image
        ///image.drawText("Hello World", 10, 60, HersheyFont.CURSIVE, 50, RGBColour.BLACK);

        //Apply a Gaussian blur
        //#1 #2 image.processInplace(new FGaussianConvolve(2f));
        //#3 System.out.println(image.colourSpace);
        //#3 DisplayUtilities.display(image);
        
    	//#4 MBFImage clone = image.clone();
    	//#4 for (int y=0; y<image.getHeight(); y++) {
    	//#4 	for(int x=0; x<image.getWidth(); x++) {
    	//#4 		clone.getBand(0).pixels[y][x] = 0; //Red
    	//#4 		//clone.getBand(1).pixels[y][x] = 0; //Green 
    	//#4 		clone.getBand(2).pixels[y][x] = 0; //Blue
    	//#4 	}
    	//#4 }
    	//#4 DisplayUtilities.display(clone, "Red Cloned");
    	//#4 DisplayUtilities.display(clone, "Red Cloned");
    	 
        //Display the image
        DisplayUtilities.display(image, "Original");
        DisplayUtilities.display(image.getBand(0), "Red Channel");
        
        image.processInplace(new CannyEdgeDetector());
        image.drawShapeFilled(new Ellipse(700f, 450f, 20f, 10f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(650f, 425f, 25f, 12f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(600f, 380f, 30f, 15f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(500f, 300f, 100f, 70f, 0f), RGBColour.WHITE);
        image.drawText("OpenIMAJ is", 425, 300, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
        image.drawText("Awesome", 425, 330, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
        DisplayUtilities.display(image);
    }
}
