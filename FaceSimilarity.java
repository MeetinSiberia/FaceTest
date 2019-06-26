package ok;
/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
//package org.openimaj.examples.image.faces;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.openimaj.feature.FloatFV;
import org.openimaj.feature.FloatFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.image.processing.face.feature.FacePatchFeature;
import org.openimaj.image.processing.face.feature.FacePatchFeature.Extractor;
import org.openimaj.image.processing.face.feature.comparison.FaceFVComparator;
import org.openimaj.image.processing.face.similarity.FaceSimilarityEngine;
import org.openimaj.math.geometry.shape.Rectangle;

/**
 * Example showing how to use the {@link FaceSimilarityEngine} class to compare
 * faces detected in two images.
 * 
 * @author Jonathon Hare (jsh2@ecs.soton.ac.uk)
 * 
 */
public class FaceSimilarity {
	/**
	 * Main method for the example.
	 * 
	 * @param args
	 *            Ignored.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// first, we load two images
		final URL image1url = new URL(
				"https://hips.hearstapps.com/hbz.h-cdn.co/assets/16/31/1600x1066/hbz-barack-obama-gettyimages-76137105.jpg?resize=480:*");
				//"https://i.ytimg.com/vi/G6NfRMv-4OY/maxresdefault.jpg"
				//"https://www.rollingstone.com/wp-content/uploads/2018/07/shutterstock_9159619h-5ee1301c-f58f-4183-a50d-8fb706d78fe1.jpg?crop=900:600&width=440"
				//"https://www.irishtimes.com/polopoly_fs/1.3060705.1493125022!/image/image.jpg_gen/derivatives/box_620_330/image.jpg");
				//"https://o.aolcdn.com/images/dims3/GLOB/legacy_thumbnail/630x315/format/jpg/quality/85/http%3A%2F%2Fi.huffpost.com%2Fgen%2F3490880%2Fimages%2Fn-OBAMA-628x314.jpg"
				//"https://thenypost.files.wordpress.com/2015/04/540187445.jpg?quality=90&strip=all&w=618&h=410&crop=1"
				//"https://s-i.huffpost.com/gen/4472690/thumbs/o-SOPHIE-GREGOIRE-900.jpg?16"
				//"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSW691vBGTO5B1WE0HzKT4UPBfonUKGpJrQX8FwhXw5EjJzfMnQ"
				//"https://cdn.cnn.com/cnnnext/dam/assets/150511085228-obama-in-south-dakota-2015-exlarge-169.jpeg"
				//"https://image.cleveland.com/home/cleve-media/width600/img/open_impact/photo/barack-obama-richard-cordray-da409b17419d1690.jpg"
				//"http://i2.cdn.turner.com/cnn/dam/assets/131210072046-33-mandela-1210-story-top.jpg"
				//"https://media.gettyimages.com/photos/president-barack-obama-delivers-a-statement-on-economy-at-the-james-picture-id519241750?s=612x612"
				//"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBYVMIDO55KHKTc7o3dNlTeO63BtcPdPAQbNJEzcXhzFvNtUbGXA"
				//"https://img.washingtonpost.com/rf/image_480w/2010-2019/WashingtonPost/2015/11/05/National-Politics/Images/495396274.jpg?uuid=CciaqoN5EeWnymq27CD4OQ"
				//"http://s3.amazonaws.com/rapgenius/fema_-_39841_-_official_portrait_of_president-elect_barack_obama_on_jan-_13.jpg"
		final URL image2url = new URL(
				"https://s-i.huffpost.com/gen/4472690/thumbs/o-SOPHIE-GREGOIRE-900.jpg?16");
				//"https://static.seattletimes.com/wp-content/uploads/2017/12/204680_Holiday_Bowl_31_210104-780x520.jpg"
				//"https://images.thestar.com/NLEVJYhEzNQEM6eZajfFSupgQ9M=/1086x715/smart/filters:cb(1532692340180)/https://www.thestar.com/content/dam/thestar/sports/raptors/2018/07/17/raptors-masai-ujiri-finds-another-giant-in-barack-obama/obamaujiri.jpg"
				//"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlRcRBT6b_guQLXQVmSeFnHZQcvFR23HyFcaCK67QKDDlXsOJysw"
				//"https://upload.wikimedia.org/wikipedia/commons/d/d7/US_President_Barack_Obama_taking_his_Oath_of_Office_-_2009Jan20.jpg"
				//"https://i.kinja-img.com/gawker-media/image/upload/s--j4DuF8RA--/c_scale,f_auto,fl_progressive,q_80,w_800/osnzyinsyen8vmzhago8.jpg"
				//"https://imagesvc.timeincapp.com/v3/mm/image?url=https%3A%2F%2Ftimedotcom.files.wordpress.com%2F2017%2F12%2Fbarack-obama.jpeg&w=800&q=85"

		final FImage image1 = ImageUtilities.readF(image1url);
		final FImage image2 = ImageUtilities.readF(image2url);

		// then we set up a face detector; will use a haar cascade detector to
		// find faces, followed by a keypoint-enhanced detector to find facial
		// keypoints for our feature. There are many different combinations of
		// features and detectors to choose from.
		final HaarCascadeDetector detector = HaarCascadeDetector.BuiltInCascade.frontalface_alt2.load();
		final FKEFaceDetector kedetector = new FKEFaceDetector(detector);

		// now we construct a feature extractor - this one will extract pixel
		// patches around prominant facial keypoints (like the corners of the
		// mouth, etc) and build them into a vector.
		final Extractor extractor = new FacePatchFeature.Extractor();

		// in order to compare the features we need a comparator. In this case,
		// we'll use the Euclidean distance between the vectors:
		final FaceFVComparator<FacePatchFeature, FloatFV> comparator =
				new FaceFVComparator<FacePatchFeature, FloatFV>(FloatFVComparison.EUCLIDEAN);

		// Now we can construct the FaceSimilarityEngine. It is capable of
		// running the face detector on a pair of images, extracting the
		// features and then comparing every pair of detected faces in the two
		// images:
		final FaceSimilarityEngine<KEDetectedFace, FacePatchFeature, FImage> engine =
				new FaceSimilarityEngine<KEDetectedFace, FacePatchFeature, FImage>(kedetector, extractor, comparator);

		// we need to tell the engine to use our images:
		engine.setQuery(image1, "image1");
		engine.setTest(image2, "image2");

		// and then to do its work of detecting, extracting and comparing
		engine.performTest();

		// finally, for this example, we're going to display the "best" matching
		// faces in the two images. The following loop goes through the map of
		// each face in the first image to all the faces in the second:
		for (final Entry<String, Map<String, Double>> e : engine.getSimilarityDictionary().entrySet()) {
			// this computes the matching face in the second image with the
			// smallest distance:
			double bestScore = Double.MAX_VALUE;
			String best = null;
			for (final Entry<String, Double> matches : e.getValue().entrySet()) {
				if (matches.getValue() < bestScore) {
					bestScore = matches.getValue();
					best = matches.getKey();
				}
			}

			// and this composites the original two images together, and draws
			// the matching pair of faces:
			final FImage img = new FImage(image1.width + image2.width, Math.max(image1.height, image2.height));
			img.drawImage(image1, 0, 0);
			img.drawImage(image2, image1.width, 0);

			img.drawShape(engine.getBoundingBoxes().get(e.getKey()), 1F);

			final Rectangle r = engine.getBoundingBoxes().get(best);
			r.translate(image1.width, 0);
			img.drawShape(r, 1F);

			// and finally displays the result
			DisplayUtilities.display(img);
		}
	}
}
