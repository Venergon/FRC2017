#include <ros/ros.h>
#include <image_transport/image_transport.h>
#include <opencv2/highgui/highgui.hpp>
#include <cv_bridge/cv_bridge.h>

#define RED 2
#define GREEN 1
#define BLUE 0
#define FOCAL 910
#define ACTUAL 210

using namespace cv;

Mat onlyGreen(Mat original);
Mat contours(Mat current, Mat original);
Mat erosion(Mat original, int erosion_size);
Mat dilation(Mat original, int erosion_size);
Mat contours(Mat current, Mat original);

bool cmp(vector<Point> a, vector<Point> b);

void imageCallback(const sensor_msgs::ImageConstPtr& msg)
{
  try
  {
    Mat frame = cv_bridge::toCvShare(msg, "bgr8")->image;
    Mat modified = onlyGreen(frame);
    cvtColor( modified, modified, CV_BGR2GRAY );
	modified = contours(modified, frame);


    cv::imshow("view", modified);
    cv::waitKey(30);
  }
  catch (cv_bridge::Exception& e)
  {
    ROS_ERROR("Could not convert from '%s' to 'bgr8'.", msg->encoding.c_str());
  }
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

bool cmp(vector<Point> a, vector<Point> b) {
	RotatedRect rect1 = minAreaRect(a);
	RotatedRect rect2  = minAreaRect(b);
	return rect1.size.width*rect1.size.height > rect2.size.width*rect2.size.height;
}

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

int main(int argc, char **argv)
{
  ros::init(argc, argv, "image_listener");
  ros::NodeHandle nh;
  cv::namedWindow("view");
  cv::startWindowThread();
  image_transport::ImageTransport it(nh);
  image_transport::Subscriber sub = it.subscribe("/image_raw", 1, imageCallback);
  ros::spin();
  cv::destroyWindow("view");
}
