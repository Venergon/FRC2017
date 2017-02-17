#ifndef IMAGE_PROCESSING
#define IMAGE_PROCESSING
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/opencv.hpp>
#include <stdio.h>
#include <math.h>

cv::Mat onlyGreen(cv::Mat original);
cv::Mat erosion(cv::Mat original, int erosion_size);
cv::Mat dilation(cv::Mat original, int erosion_size);
cv::Mat contours(cv::Mat current, cv::Mat original);
void image_update();
float image_get_distance();
float image_get_x();
int image_setup();
void image_set_mode(int mode);
#define IMAGE_MODE_SHIP 0
#define IMAGE_MODE_BOILER 1
#endif
