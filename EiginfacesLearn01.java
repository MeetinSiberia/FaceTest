package ok;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.model.EigenImages;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.resize.ResizeProcessor;

public class EiginfacesLearn01 {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//read dataset
		VFSGroupDataset <FImage> dataset = 
			    new VFSGroupDataset <FImage> ("zip:/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/att_faces.zip",ImageUtilities.FIMAGE_READER);


		
		for( final Entry < String, VFSListDataset<FImage> > entry: dataset.entrySet() ) {
			DisplayUtilities.display( entry.getKey(), entry.getValue());
			System.out.println(entry);
		}
		
//		
		//divide dataset
		int nTraining = 5;
		int nTesting = 5;
		GroupedRandomSplitter <String,FImage> splits = 
		    new GroupedRandomSplitter <String,FImage> (dataset,nTraining,0,nTesting);
		GroupedDataset <String,ListDataset <FImage>,FImage> training = splits.getTrainingDataset();
		GroupedDataset <String,ListDataset <FImage>,FImage> testing = splits.getTestDataset();
		
		System.out.println("-------------------------------------");
		//PCA降维
		List <FImage> basis = DatasetAdaptors.asList(training);
		List <FImage> basisImages = new ArrayList<>();
		for( int i=0; i<basis.size();i++) {
			List<FImage> de = getface( basis.get(i) );
			basisImages.addAll(getface(basis.get(i)));
		}
//		System.out.println(basisImages.size());
//		DisplayUtilities.display(basisImages.get(5));
//		basisImages.add(imageee);
//		System.out.println(basisImages.size());
//		DisplayUtilities.display(imageee);
//		DisplayUtilities.display(basis.get(0));
//		DisplayUtilities.display(basisImages.get(0));
		
//		
		int nEigenvectors = 100;
		EigenImages eigen = new EigenImages(nEigenvectors);
		eigen.train(basisImages);
		
		
		FileOutputStream fos = new FileOutputStream("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/models/1.txt");
		DataOutputStream dos = new DataOutputStream(fos);
		eigen.writeBinary(dos);
		dos.close();
		fos.close();
//		System.out.println("eigen");
		System.out.println(eigen);    //EigenImages[width=92; height=112; pca=PrincipalComponentAnalysis[dims=100]]
//		System.out.println("eigen_visualisePC");
//		System.out.println(eigen.visualisePC(1));
//		DisplayUtilities.display("visualisePC",eigen.visualisePC(1));
		
		//可视化前12个基矢量
		List <FImage> eigenFaces = new ArrayList <FImage>();
		for(int i = 0; i <12; i ++){
		    eigenFaces.add(eigen.visualisePC(i));
		}
		System.out.println(training.getGroups());
//		DisplayUtilities.display("EigenFaces",eigenFaces);
		//模型构建完毕
		
		
		
//		根据模型，建立了一个特征数据库
		Map <String,DoubleFV []> features = new HashMap <String,DoubleFV []> ();
		for (final String person:training.getGroups()){
		    final DoubleFV [] fvs = new DoubleFV [nTraining];
		    int sum = 0;
		    for(int i = 0; i <nTraining; i ++){
		        final FImage face = training.get(person).get(i);
//		        DisplayUtilities.display(face);
//		        List <FImage> buildset = getface(face);
//		        System.out.println(buildset.size());
//		        DisplayUtilities.display(buildset.get(0));
		        fvs [i] = eigen.extractFeature(face);
//		        if(buildset.size()!=0) {
//		        	for( int j = 0; j< buildset.size(); j++) {
//				        DisplayUtilities.display(buildset.get(j));

//			        	fvs [sum] = eigen.extractFeature(buildset.get(j));
//				        System.out.println(fvs [sum]);

//			        	sum++;
//			        }
//			        fvs [i] = eigen.extractFeature(face);
//		        }
		        
//		        DisplayUtilities.display(face);
		    }
		    features.put(person,fvs);
		}
		System.out.println(features.size());
		//数据库构建完毕，开始测试应用
		//读取新图片，提取特征，利用欧氏距离找到最相似人脸
	    double correct = 0, incorrect = 0;
	    for (String truePerson : testing.getGroups()) {
	        for (FImage face : testing.get(truePerson)) {
//	        	List <FImage> buildset = getface(face);
//	        	if(buildset.size()!=0) {
//	        		for( int j = 0; j< buildset.size(); j++) {
        				DoubleFV testFeature = eigen.extractFeature(face);

//		        		DoubleFV testFeature = eigen.extractFeature(buildset.get(j));
			            String bestPerson = "Don't  find this people";
			            double minDistance = Double.MAX_VALUE;
//			            System.out.println(features.keySet());
			            double distance = 0.0;
			            for (final String person1 : features.keySet()) {
//			            	System.out.println(person1);
			                for (final DoubleFV fv : features.get(person1)) {
			                	if(fv==null) {
			                		continue;
			                	}
//			                	System.out.println("--------------");
//				            	System.out.println(fv);
//				            	System.out.println(testFeature);

			                    distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);
			                    if (distance < minDistance) {
			                        minDistance = distance;
			                        bestPerson = person1;
			                    }
			                }
			            }
			            System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson+ "\t: " + 100/(100+minDistance));
			            if (truePerson.equals(bestPerson))
			                correct++;
			            else
			                incorrect++;
//			        }
	        		
//	        	}
	        	
	        	
//	            DoubleFV testFeature = eigen.extractFeature(buildset.get(0));
//	            String bestPerson = null;
//	            double minDistance = Double.MAX_VALUE;
//	            for (final String person1 : features.keySet()) {
//	                for (final DoubleFV fv : features.get(person1)) {
//	                    double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);
//	                    if (distance < minDistance) {
//	                        minDistance = distance;
//	                        bestPerson = person1;
//	                    }
//	                }
//	            }
//	            System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson);
//	            if (truePerson.equals(bestPerson))
//	                correct++;
//	            else
//	                incorrect++;
	        }
	    }
	    System.out.println("Accuracy: " + (correct / (correct + incorrect)));
		

	}
	
	public static List <FImage> getface( FImage imageee ) {
//		FImage imageee = ImageUtilities.readF(new File("F:\\大学\\大数据\\Final_Project\\att_faces\\s1\\1.pgm"));
		FaceDetector <DetectedFace,FImage> displays = new HaarCascadeDetector(40);
    	List <DetectedFace> faces = displays.detectFaces(imageee);
    	List <FImage> result = new ArrayList<>();
    	for(DetectedFace face:faces){
    		int x = (int) face.getBounds().x;
    		int y = (int) face.getBounds().y;
    		int w = (int) face.getBounds().height;
    		int h = (int) face.getBounds().width; 		
//    		System.out.println(w);
//    		System.out.println(h);
//    		System.out.print(face.getBounds().x);
//    		System.out.print(face.getBounds().x);
//    		System.out.println(face.getBounds());
//    		imageee.drawShape(face.getBounds(),RGBColour.RED);
    		FImage clone2 = new FImage(w,h);
    		for( int i = 0; i<w; i++ ) {
    			for( int j = 0 ; j<h; j++ ) {
    				clone2.pixels[j][i] = imageee.getPixel(x+i+1, y+j+1);
    				}
    		}
//    		DisplayUtilities.display(clone2);
//    		FImage clone3 = ResizeProcessor.resample(Transforms.calculateIntensity(clone2), 100, 100);
//    		DisplayUtilities.display(clone3);
//    		System.out.println(clone3.getBounds());
    		result.add(ResizeProcessor.resample(clone2, 92, 112));
    	}
//    	DisplayUtilities.display(imageee);
		return result;
	}
	
}
