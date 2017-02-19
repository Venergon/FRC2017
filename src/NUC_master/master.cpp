#include "bridge.h"
#include "image_processing.h"
#include <stdio.h>
#include <stdlib.h>

int main() {
  bridge_setup();
  int setup_success = image_setup();
  if (setup_success == EXIT_FAILURE) { //The image did not setup properly (camera not connected)
      fprintf(stderr, "Image setup failed!\n");
      return EXIT_FAILURE;
  }
  image_set_mode(IMAGE_MODE_SHIP);
  //image_set_mode(IMAGE_MODE_SHIP);
  while(1) {
    image_update();
    printf("%f\n",image_get_distance());
    printf("%f\n",image_get_x());
  }
  return EXIT_SUCCESS;
}
