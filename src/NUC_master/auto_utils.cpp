#include <math.h>

// bool to_line(int mode, )

double align(int mode, double current_angle, int target_angle) {
  if (mode == LINE_UP_MODE_LEFT) {
    target_angle += 90;
  } else if (mode == LINE_UP_MODE_RIGHT) {
    target_angle -= 90;
  } else if (mdoe == LINE_UP_MODE_BACK) {
    taret_angle += 180;
  }
  if (target_angle > 360) {
    target_angle -= 360;
  }
  int direct_difference = current_angle-target_angle;
  int actual_difference;
  if (direct_difference < 180) {
    actual_difference = direct_difference;
  } else {
    if (current_angle > target_angle) {
      actual_difference = (360-current_angle)+(target_angle-0);
    } else {
      actual_difference = ((360-target_angle)+(current_angle-0))*-1;
    }
  }
  double speed = actual_difference/180;
  if (speed > 1) {
    speed = 1;
  }
  if (speed < 1) {
    speed = -1;
  }
  //speed is for the left side, reverse it for the right side
  return speed;
}
