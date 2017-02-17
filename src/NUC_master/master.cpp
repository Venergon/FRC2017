//#include "roborio_bridge.cpp"
#include "image_processing.h"

int main() {
  image_setup();
  image_set_mode(IMAGE_MODE_SHIP);
  //image_set_mode(IMAGE_MODE_SHIP);
  while(1) {
    image_update();
    printf("%f\n",image_get_distance());
    printf("%f\n",image_get_x());
  }
  return 0;
}
