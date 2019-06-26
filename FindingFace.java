package ok;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.image.pixel.PixelSet;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

public class FindingFace {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

//		Video<MBFImage> video = new VideoCapture(500, 500);
//        VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
////        for (MBFImage image : video) {
////            DisplayUtilities.displayName(image.process(new CannyEdgeDetector(1)), "videoFrames");
////        }
//        
//        display.addVideoListener( new VideoDisplayListener <MBFImage>(){
//        	public void beforeUpdate(MBFImage frame){
//		    	FaceDetector <DetectedFace,FImage> displays = new HaarCascadeDetector(40);
//		    	List <DetectedFace> faces = displays.detectFaces(Transforms.calculateIntensity(frame));
////		    	frame.processInplace(new CannyEdgeDetector());
//		    	for(DetectedFace face:faces){
//		    	    frame.drawShape(face.getBounds(),RGBColour.RED);
//		    	}
//		    }
//
//		    public void afterUpdate(VideoDisplay <MBFImage> display){
//		    	
//		    }
//		  });
//		FImage imageee = ImageUtilities.readF(new File("F:\\大学\\大数据\\Final_Project\\att_faces\\s14\\1.pgm"));
//		System.out.print(imageee.getBounds());

//		FImage imageee = ImageUtilities.readF(new File("F:\\大学\\大数据\\Final_Project\\奥巴马\\2.jpg"));

		MBFImage imageee = ImageUtilities.readMBF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Obama/0.jpg"));
// 		MBFImage imageee = ImageUtilities.readMBF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Mike/01.jpg"));
		FaceDetector <DetectedFace,FImage> displays = new HaarCascadeDetector(40);
    	List <DetectedFace> faces = displays.detectFaces(Transforms.calculateIntensity(imageee));
//    	List <DetectedFace> faces = displays.detectFaces(imageee);

    	for(DetectedFace face:faces){
    		int x = (int) face.getBounds().x;
    		int y = (int) face.getBounds().y;
    		int w = (int) face.getBounds().height;
    		int h = (int) face.getBounds().width;
    		
//    		if(w%92!=0) {
//    			if( x >= (w+w%92+1)/2 ) {
//    				x -= (w+w%92+1)/2;
//    			}else {
//    				x = 0;
//    			}
//    		}
//    		
//    		if(h%92!=0) {
//    			if( y >= (h+h%92+1)/2 ) {
//    				y -= (h+h%92+1)/2;
//    			}else {
//    				y = 0;
//    			}
//    		}
    		
//    		System.out.println(w);
//    		System.out.println(h);
//    		System.out.print(face.getBounds().x);
//    		System.out.print(face.getBounds().x);
//    		System.out.println(face.getBounds());
    		imageee.drawShape(face.getBounds(),RGBColour.RED);
    		MBFImage clone2 = new MBFImage(w,h);
//    		FImage clone2 = new FImage(w,h);
    		for( int i = 0; i<w; i++ ) {
    			for( int j = 0 ; j<h; j++ ) {
    				clone2.setPixel(i, j, imageee.getPixel(x+i+1, y+j+1)); 
//    				clone2.pixels[j][i] = imageee.getPixel(x+i+1, y+j+1);

    			}
    		}
    		DisplayUtilities.display(clone2);
    		FImage clone3 = ResizeProcessor.resample(Transforms.calculateIntensity(clone2), 100, 100);
//    		FImage clone3 = ResizeProcessor.resample(clone2, 92, 112);

    		DisplayUtilities.display(clone3);
    		System.out.println(clone3.getBounds());
    	}
    	DisplayUtilities.display(imageee);
//    	FImage clone4 = Transforms.calculateIntensity(imageee);
//    	VFSListDataset <FImage> dataset = null;

//    	GroupedDataset <String,ListDataset <FImage>,FImage> training;
//    	ListDataset <FImage> l = new ListDataset<>();
//    	List <FImage> a = new ArrayList<>() ;
//    	a.add(clone4);
//    	training.put("001", (ListDataset<FImage>) clone4);
    			
    			
//    	FImage clone = Transforms.calculateIntensity(imageee);
//    	System.out.print(clone.getPixelNative(1, 1));
//    	DisplayUtilities.display(clone);
    	
//    	System.out.print(imageee.getPixel(1, 1)[1]);

//    	for(int y = 0; y <imageee.getHeight(); y ++){
//    	    for(int x = 0; x <imageee.getWidth(); x ++){
//    	        clone.getBand(1).pixels [y] [x] = 0;
//    	        clone.getBand(2).pixels [y] [x] = 0;
//    	    }
//    	}
    	
		  
	}
	

}
