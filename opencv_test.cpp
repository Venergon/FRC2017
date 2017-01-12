#include "opencv2/core/core.hpp"
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/opencv.hpp>
#include <stdio.h>
#include <math.h>

#define RED 2
#define GREEN 1
#define BLUE 0
#define FOCAL 910
#define ACTUAL 210

using namespace cv;

Mat onlyGreen(Mat original);
Mat erosion(Mat original, int erosion_size);
Mat dilation(Mat original, int erosion_size);
Mat contours(Mat current, Mat original);

bool cmp(vector<Point> a, vector<Point> b);

int main(int argc, char** argv) {
	VideoCapture cap(0); // open the default camera
	if(!cap.isOpened())  // check if we succeeded
		return -1;

	Mat edges;
	namedWindow("edges",1);
	for(;;)
	{
		Mat frame;
		cap >> frame; // get a new frame from camera
		Mat modified = onlyGreen(frame);
		cvtColor( modified, modified, CV_BGR2GRAY );
		modified = contours(modified, frame);

		imshow("Tape Tracker", modified);
		if(waitKey(30) >= 0) break;
	}
	// the camera will be deinitialized automatically in VideoCapture destructor
	return 0;
}

Mat onlyGreen(Mat original){
	Mat modified = original.clone();

	MatIterator_<Vec3b> it, end;
	for( it = modified.begin<Vec3b>(), end = modified.end<Vec3b>(); it != end; ++it)
	{
		if ((*it)[GREEN] > 200 && (*it)[BLUE] > 200 && (*it)[RED] < 200){
			(*it)[BLUE] = 0;
			(*it)[RED] = 0;
		}else{
			(*it)[RED] = 0;
			(*it)[GREEN] = 0;
			(*it)[BLUE] = 0;
		}
	}
	modified = dilation(modified, 10);
	modified = erosion(modified, 10);
	return modified;

}

/**  @function Erosion  */
Mat erosion(Mat original, int erosion_size)
{
	Mat modified = original.clone();

	int erosion_type;
	erosion_type = MORPH_RECT;
	Mat element = getStructuringElement( erosion_type,
			Size( 2*erosion_size + 1, 2*erosion_size+1 ),
			Point( erosion_size, erosion_size ) );

	/// Apply the erosion operation
	erode( modified, modified, element );

	return modified;
}

/** @function Dilation */
Mat dilation(Mat original, int erosion_size)
{
	Mat modified = original.clone();

	int erosion_type;
	erosion_type = MORPH_RECT;
	Mat element = getStructuringElement( erosion_type,
			Size( 2*erosion_size + 1, 2*erosion_size+1 ),
			Point( erosion_size, erosion_size ) );

	/// Apply the erosion operation
	dilate( modified, modified, element );

	return modified;
}

//contours
Mat contours(Mat current, Mat original){
	vector<vector<Point> > contours;
	Mat modified = current.clone();
	findContours(modified, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE);
	RotatedRect biggestRect;
	sort(contours.begin(),contours.end(),cmp);
	Mat newcurr = original.clone();
	if (contours.size() > 1) {
		RotatedRect rect1 = minAreaRect(contours[0]);
		RotatedRect rect2 = minAreaRect(contours[1]);
		float distanceX = rect1.center.x-rect2.center.x;
		float distanceY = rect1.center.y-rect2.center.y;
		float distance = sqrt(pow(distanceX,2)+pow(distanceY,2));
		float actualDistance = (ACTUAL*FOCAL)/distance;
		printf("%f mm\n",actualDistance);
		line(newcurr,rect1.center,rect2.center,Scalar(255,255,255),6);
	}
	return newcurr;
}

bool cmp(vector<Point> a, vector<Point> b) {
	RotatedRect rect1 = minAreaRect(a);
	RotatedRect rect2  = minAreaRect(b);
	return rect1.size.width*rect1.size.height > rect2.size.width*rect2.size.height;
}
