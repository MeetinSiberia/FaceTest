package ok;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.model.EigenImages;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.resize.ResizeProcessor;



public class TestRun3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Results");
		JPanel panel = new JPanel();
		JLabel label1 = new JLabel("1");
		JLabel label2 = new JLabel("2");
		JLabel label3 = new JLabel("3");
		JLabel label4 = new JLabel("4");
		
		JFileChooser fc1 = new JFileChooser();
		JFileChooser fc2 = new JFileChooser();
		JLabel lbltest1 = new JLabel("1");
		JLabel lbltest2 = new JLabel("2");
		
		JLabel lblfc1 = new JLabel("1");
		JLabel lblfc2 = new JLabel("2");
		JLabel lblfc3 = new JLabel("3");
		JLabel lblfc4 = new JLabel("4");
		JLabel lblfc5 = new JLabel("5");
		JLabel lblfc6 = new JLabel("6");
		JLabel lblfc7 = new JLabel("7");
		
		int returnVal;
		FImage image3;
		FImage image4; 
		JFrame f = null; 
		
		DoubleFV testFeature2;
        String bestPerson = null;
        double minDistance = Double.MAX_VALUE;
		
		VFSGroupDataset <FImage> dataset = 
			    new VFSGroupDataset <FImage> ("zip:/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/att_faces.zip",ImageUtilities.FIMAGE_READER);
		//FImage imageee = ImageUtilities.readF(new File("F:\\大学\\大数据\\Final_Project\\att_faces\\s14\\9.pgm"));

		//divide dataset
		int nTraining = 5;
		int nTesting = 5;
		GroupedRandomSplitter <String,FImage> splits = 
		    new GroupedRandomSplitter <String,FImage> (dataset,nTraining,0,nTesting);
		GroupedDataset <String,ListDataset <FImage>,FImage> training = splits.getTrainingDataset();
		GroupedDataset <String,ListDataset <FImage>,FImage> testing = splits.getTestDataset();
		
		System.out.println("-------------------------------------");
		
		//load the EigenImages model
		int nEigenvectors = 100;
		EigenImages eigen = new EigenImages(nEigenvectors);
		FileInputStream fos = new FileInputStream("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/3.txt");
		DataInputStream dos = new DataInputStream(fos);
		eigen.readBinary(dos);
		dos.close();
		fos.close();


		//model building done
		//build a base basis set
		Map <String,DoubleFV []> features = new HashMap <String,DoubleFV []> ();
		for (final String person:training.getGroups()){
		    final DoubleFV [] fvs = new DoubleFV [nTraining];
		    int sum = 0;
		    for(int i = 0; i <nTraining; i ++){
		        final FImage face = training.get(person).get(i);
			    fvs [i] = eigen.extractFeature(face);
		    }
		    features.put(person,fvs);
		}
		
        System.out.println(features.keySet().size());

		//add a Mike's face's basis
        String text = "/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Mike/01.jpg";
		FImage image2 = ImageUtilities.readF(new File(text));
		System.out.println(text);
        lblfc1.setText("New Face 1: " + text);
        List <FImage> buildset2 = getface(image2);
	    DoubleFV [] fvs = new DoubleFV [nTraining];
    	fvs [0] = eigen.extractFeature(buildset2.get(0));
    	
    	text = "/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Mike/02.jpg";
    	image2 = ImageUtilities.readF(new File(text));
    	System.out.println(text);
        lblfc2.setText("New Face 2: " + text);
        buildset2 = getface(image2);
    	fvs [1] = eigen.extractFeature(buildset2.get(0));
    	
    	text = "/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Mike/03.jpg";    	
    	image2 = ImageUtilities.readF(new File(text));
    	System.out.println(text);
        lblfc3.setText("New Face 3: " + text);    	
        buildset2 = getface(image2);
    	fvs [2] = eigen.extractFeature(buildset2.get(0));
    	
    	text = "/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Mike/04.jpg";
    	image2 = ImageUtilities.readF(new File(text));
    	System.out.println(text);
        lblfc4.setText("New Face 4: " + text);     	
        buildset2 = getface(image2);
    	fvs [3] = eigen.extractFeature(buildset2.get(0));
    	
    	text = "/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Mike/06.jpg";
    	image2 = ImageUtilities.readF(new File(text));
    	System.out.println(text);
        lblfc5.setText("New Face 5: " + text);     	    	
        buildset2 = getface(image2);
    	fvs [4] = eigen.extractFeature(buildset2.get(0));
	    features.put("Mike",fvs);
	    
	    //add a Obama's face's basis
	    //Obama's face has been cut in to 92:112,dont need to find face
		//image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Obama/0.png"));
	    image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/3.pgm"));
	    fvs = new DoubleFV [nTraining];
    	fvs [0] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/5.pgm"));
    	fvs [1] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/7.pgm"));
    	fvs [2] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/9.pgm"));
    	fvs [3] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/10.pgm"));
    	fvs [4] = eigen.extractFeature(image2);
	    features.put("Test",fvs);

	    
    	//test Obama's face
	    //text = "/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Obama/6.png";
		//FImage image4 = ImageUtilities.readF(new File(text));				
		//label1 = new JLabel(new ImageIcon(text));
    	//System.out.println(text);
        //lblfc6.setText("Test 1: " + text);     	    	
		
        returnVal = fc1.showOpenDialog(panel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc1.getSelectedFile();
            image4 = ImageUtilities.readF(file);
            String text1 = file.toString();
    		label1 = new JLabel(new ImageIcon(text1));
        	System.out.println(text1);
            lblfc6.setText("Test 1: " + text1); 
            lbltest1.setText("Test Face 1: " + text1);
        	DisplayUtilities.display(image4);
    		//testFeature2 = eigen.extractFeature(image4);
    		testFeature2 = eigen.extractFeature(getface(image4).get(0));
            bestPerson = null;
            minDistance = Double.MAX_VALUE;
            System.out.println(features.keySet().size());
            for (final String person1 : features.keySet()) {
                for (final DoubleFV fv : features.get(person1)) {
                	if(fv==null) {
                		continue;
                	}
                    
                    double distance = fv.compare(testFeature2, DoubleFVComparison.EUCLIDEAN);
                    
                    if (distance < minDistance) {
                        minDistance = distance;
                        bestPerson = person1;
                    }
                }
            }
            System.out.println("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");
            label3.setText("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");


        }
		
        
        
        //test Mike's face
        //text = "/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/MikeTest/3.jpg";
		//FImage image3 = ImageUtilities.readF(new File(text));	
		//label2 = new JLabel(new ImageIcon(text));
    	//System.out.println(text);
        //lblfc7.setText("Test 2: " + text); 
        
        returnVal = fc2.showOpenDialog(panel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc2.getSelectedFile();
            image3 = ImageUtilities.readF(file);
            String text1 = file.toString();
    		label2 = new JLabel(new ImageIcon(text1));
        	System.out.println(text1);
            lblfc7.setText("Test 2: " + text1); 
            lbltest2.setText("Test Face 2: " + text1);
        	DisplayUtilities.display(image3);
        	//DisplayUtilities.display(image3);
    		testFeature2 = eigen.extractFeature(getface(image3).get(0));
    		bestPerson = null;
    		minDistance = Double.MAX_VALUE;
    		System.out.println(features.keySet().size());
    		for (final String person1 : features.keySet()) {
    		    for (final DoubleFV fv : features.get(person1)) {
    				if(fv==null) {
    					continue;
    				}
    				double distance = fv.compare(testFeature2, DoubleFVComparison.EUCLIDEAN);
    				if (distance < minDistance) {
    				    minDistance = distance;
    				    bestPerson = person1;
    				}
    		    }
    		}
    		System.out.println("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");				
    	    label4.setText("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");
        }
		
		

	
	    
        panel.add(lblfc1);
        panel.add(lblfc2);
        panel.add(lblfc3);
        panel.add(lblfc4);
        panel.add(lblfc5);
        
	    panel.add(label1);
	    panel.add(label3);
        panel.add(lblfc6);

	    panel.add(label2);
	    panel.add(label4);
        panel.add(lblfc7);

	    frame.add(panel);
	    frame.setSize(800, 600);
	    frame.setLocationRelativeTo(null);
		// this.setLocation(600, 600);
		// this.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);		
		
	}
	
	public static List <FImage> getface( FImage imageee ) {
		FaceDetector <DetectedFace,FImage> displays = new HaarCascadeDetector(40);
    	List <DetectedFace> faces = displays.detectFaces(imageee);
    	List <FImage> result = new ArrayList<>();
    	for(DetectedFace face:faces){
    		int x = (int) face.getBounds().x;
    		int y = (int) face.getBounds().y;
    		int w = (int) face.getBounds().height;
    		int h = (int) face.getBounds().width; 		
    		FImage clone2 = new FImage(w,h);
    		for( int i = 0; i<w; i++ ) {
    			for( int j = 0 ; j<h; j++ ) {
    				clone2.pixels[j][i] = imageee.getPixel(x+i+1, y+j+1);
    				}
    		}
    		result.add(ResizeProcessor.resample(clone2, 92, 112));
    	}
		return result;
	}
}


