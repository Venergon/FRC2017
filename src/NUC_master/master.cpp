#include "bridge.h"
#include "image_processing.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
  printf("Setting up bridge\n");
	bridge_setup();
  printf("Bridge setup!\n");
	int setup_success = image_setup();
	if (setup_success == EXIT_FAILURE) { //The image did not setup properly (camera not connected)
		fprintf(stderr, "Image setup failed!\n");
		return EXIT_FAILURE;
	}



	image_set_mode(IMAGE_MODE_SHIP);
	//image_set_mode(IMAGE_MODE_SHIP);
	while(1) {
		char buffer[256];
    //printf("Receiving message\n");
		if (receive_message(buffer, 256) == 1) {
      printf("Message is '%s'\n", buffer);
			if (strcmp(buffer, "gear\n") == 0) {
        printf("Changing to gear mode\n");
        image_set_mode(IMAGE_MODE_SHIP);
			} else if (strcmp(buffer, "ball\n") == 0) {
        printf("Changing to ball mode\n");
				image_set_mode(IMAGE_MODE_BOILER);
			}
			image_update();
			char to_send[256];
      float distance = image_get_distance();
      float x = image_get_x();
      if (distance == std::numeric_limits<float>::max() && x == std::numeric_limits<float>::max()){
        sprintf(to_send, "NULL");
      } else {
        sprintf(to_send, "%f:%f", image_get_distance(), image_get_x());
      }
      printf("Sending '%s'\n", to_send);
			send_message(to_send);
		}
	}
	return EXIT_SUCCESS;
}
