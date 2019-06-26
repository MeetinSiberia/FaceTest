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

/**
 * OpenIMAJ Chapter 3
 *
 */
import no.uib.cipr.matrix.sparse.ILUT;

public class Chap03 {
    public static void main( String[] args ) throws IOException {
    	//Create an image
    	
    	
    	MBFImage image = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));
    	image = ColourSpace.convert(image, ColourSpace.CIE_Lab);
    	FloatKMeans cluster = FloatKMeans.createExact(2);
    	
    	
    	float[][] imageData = image.getPixelVectorNative(new float[image.getWidth() * image.getHeight()][3]);
    	FloatCentroidsResult result = cluster.cluster(imageData);
        DisplayUtilities.display(image, "Original Image");
    }
}
