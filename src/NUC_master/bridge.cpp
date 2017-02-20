//#include <ros/ros.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <memory.h>
#include <ifaddrs.h>
#include <net/if.h>
#include <errno.h>
#include <stdlib.h>
#include <iostream>
#include <string.h>
#include <assert.h>
#include <sys/ioctl.h>

#include <geometry_msgs/Twist.h>
//#include <ros/serialization.h>

typedef struct _message_parts {
	char part1[256];
	char part2[256];
} message_parts;

int socket_num;

void send_message(/*const geometry_msgs::Twist::ConstPtr& msg*/ const char* msg) {
    //ROS_INFO_STREAM("Message: " << msg->linear.x);
    char new_msg[256];
    //ros::serialization::serialize(new_msg, msg);
    size_t msg_length = strlen(msg);
    int sent = write(socket_num, msg, msg_length); //strlen = string length
    //result = sendto(sock, msg, msg_length, 0, (sockaddr*)&addrDest,
    //        sizeof(addrDest));


    std::cout << msg << sent << " bytes sent of " << msg_length << std::endl;
    char buffer[256];
}

// arrow = ->
void create_socket() {
    socket_num = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in *address = (sockaddr_in *) malloc (sizeof(struct sockaddr_in)); //malloc = create object
    address->sin_family = AF_INET;
    address->sin_port = htons(1917);
    inet_aton("127.0.0.1", &(address->sin_addr));
    int connectSuccess = -1;
    while (connectSuccess == -1) {
        connectSuccess = connect(socket_num, (sockaddr *) address, sizeof(struct sockaddr_in));
    }
}

void receive_message() {
    int readResult = 0;
    char buffer[256];
    int bytesLeft = 0;
    ioctl(socket_num, FIONREAD, &bytesLeft);
    if (bytesLeft > 0) {
        readResult = read(socket_num, buffer, 256);
        if (readResult > 0) {
            buffer[readResult] = '\0';
            std::cout << "buffer is" << buffer << "readResult is" << readResult << std::endl;
        }
    }
}

struct_essage_parts *decode_message(char *message) {
	message_parts *parts = (message_type *) malloc(sizeof(message_parts));
	int colonIndex = 0;
	while (message[colonIndex] != ':' && message[colonIndex] != '\0') {
		colonIndex++;
	}
	if (message[colonIndex] == '\0') {
		parts = NULL;	
	} else {
		message[colonIndex] = '\0';
		strncpy(parts->part1, message, 256);
		strncpy(parts->part2, &(message[colonIndex+1]), 256);
	}
	return parts;
}

int bridge_setup()
{
    int result = 0;
    create_socket();
    return 0;
    //int sock = socket(AF_INET, SOCK_DGRAM, 0);

    //char szIP[100];

    //sockaddr_in addrListen = {}; // zero-int, sin_port is 0, which picks a random port for bind.
    //addrListen.sin_family = AF_INET;
    //result = bind(sock, (sockaddr*)&addrListen, sizeof(addrListen));
    /*if (result == -1)
      {
      int lasterror = errno;
      std::cout << "error: " << lasterror;
      exit(1);
      }


      sockaddr_storage addrDest = {};
    //result = resolvehelper("192.168.0.4", AF_INET, "9000", &addrDest);
    if (result != 0)
    {
    int lasterror = errno;
    std::cout << "error: " << lasterror;
    exit(1);
    } */
    /*ros::init(argc, argv, "roborio_bridge");
    ros::NodeHandle nh;
    ros::Subscriber sub_motor = nh.subscribe("robot/cmd_vel", 1,
            motorCallback);
    ros::Timer timer = nh.createTimer(ros::Duration(0.1), receive);
    ros::spin();*/
}
