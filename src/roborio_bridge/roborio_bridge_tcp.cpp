#include <ros/ros.h>
#include <geometry_msgs/Twist.h>
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

int socket_num;

void motorCallback(const geometry_msgs::Twist::ConstPtr& msg) {
    ROS_INFO_STREAM("Message: " << msg->linear.x);
    const char* new_msg = "testing";
    size_t msg_length = strlen(new_msg);
    
    int sent = write(socket_num,new_msg, msg_length); //strlen = string length
    //result = sendto(sock, msg, msg_length, 0, (sockaddr*)&addrDest,
    //        sizeof(addrDest));
    

    std::cout << new_msg << sent << " bytes sent of " << msg_length << std::endl;
    char buffer[256];
    int readResult = -1;
    while (readResult < 0) {
        
    }
}

// arrow = ->
void create_socket() {
    socket_num = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in *address = (sockaddr_in *) malloc (sizeof(struct sockaddr_in)); //malloc = create object
    address->sin_family = AF_INET;
    address->sin_port = htons(1917);
    inet_aton("127.0.0.1", &(address->sin_addr));
    connect(socket_num, (sockaddr *) address, sizeof(struct sockaddr_in));
}

int main(int argc, char **argv)
{
    int result = 0;
    create_socket();
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
    ros::init(argc, argv, "roborio_bridge");
    ros::NodeHandle nh;
    ros::Subscriber sub_motor = nh.subscribe("robot/cmd_vel", 1,
            motorCallback);
    ros::spin();
}

