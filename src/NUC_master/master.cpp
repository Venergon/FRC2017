#include "bridge.h"
#include "image_processing.h"
#include "auto_master.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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
		char buffer[256];
		if (receive_message(buffer, 256) == 1) {
			if (strcmp(buffer, "gear")) {
				image_set_mode(IMAGE_MODE_SHIP);
			} else if (strcmp(buffer, "ball")) {
				image_set_mode(IMAGE_MODE_BOILER);
			}
			image_update();
			char to_send[256];
			sprintf(to_send, "%f:%f", image_get_distance(), image_get_x());
			send_message(to_send);
		}
	}
	return EXIT_SUCCESS;
}
