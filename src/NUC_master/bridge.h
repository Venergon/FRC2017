#ifndef BRIDGE
#define BRIDGE
void create_socket();
void send_message();
void receive_message();
int bridge_setup();

#define PART_SIZE 256

typedef struct _message_parts {
	char part1[PART_SIZE];
	char part2[PART_SIZE];
} message_parts;

#endif
