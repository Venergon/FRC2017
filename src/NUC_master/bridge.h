#ifndef BRIDGE
#define BRIDGE
void create_socket();
void send_message(const char *msg);
int receive_message(char * msg, int msg_size);
int bridge_setup();

#define PART_SIZE 256

typedef struct _message_parts {
	char part1[PART_SIZE];
	char part2[PART_SIZE];
} message_parts;

#endif
