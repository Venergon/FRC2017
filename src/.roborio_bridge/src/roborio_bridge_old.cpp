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

#include "ugv_controller/message.h"
#include "ugv_controller/ugv_controller.h"
#include "ugv_controller/control_loop.h"

const int UDP_TIMEOUT = 100000; // in us

struct sockaddr_in controllerAddr;

bool Controller::ping() 
{
  struct pkt_cmd_ping pkt;
  pkt.header.type = PKT_TYPE_PING;
  sendPacket(&pkt, sizeof(pkt));
socklen_t addrlen = sizeof(controllerAddr);
int recv = recvfrom(udpSocket, &pkt, sizeof(pkt), 0, (struct sockaddr *) &controllerAddr, &addrlen);
  if (recv == sizeof(pkt) && pkt.header.type = PKT_TYPE_PINGRESP) {
return true;
  } else {
    return false;
  }
}

int resolvehelper(const char* hostname, int family, const char* service, sockaddr_storage* pAddr)
{

    int udpSocket = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (udpSocket < 0) {
        //throw SocketException("Opening", udpSocket);
    }

    struct timeval tv;
    tv.tv_sec = UDP_TIMEOUT / 1000000;
    tv.tv_usec = UDP_TIMEOUT % 1000000;

    setsockopt(udpSocket, SOL_SOCKET, SO_RCVTIMEO, &tv, sizeof(tv));

    // Store controller address
    memset(&controllerAddr, 0, sizeof(controllerAddr));
    controllerAddr.sin_family = AF_INET;
    controllerAddr.sin_port = htons(CONTROLLER_PORT);
    if (inet_aton(ip.c_str(), &controllerAddr.sin_addr) == 0) {
        //throw SocketException("Parsing IP", 0);
    }

    // Check if controller is online
    if (!ping()) {
        throw SocketException("Couldn't connect", 0);
    }

    lightsEnabled = true;

    leftLoop = new ControlLoop(0.01, 4.0);
    rightLoop = new ControlLoop(0.01, 4.0);
}

void motorCallback(const geometry_msgs::Twist::ConstPtr& msg) {
    ROS_INFO_STREAM("Message: " << msg->linear.x);
    const char* msg = "testing";
    size_t msg_length = strlen(msg);

    result = sendto(sock, msg, msg_length, 0, (sockaddr*)&addrDest,
            sizeof(addrDest));

    std::cout << result << " bytes sent" << std::endl;
}

int main(int argc, char **argv)
{
    int result = 0;
    int sock = socket(AF_INET, SOCK_DGRAM, 0);

    char szIP[100];

    sockaddr_in addrListen = {}; // zero-int, sin_port is 0, which picks a random port for bind.
    addrListen.sin_family = AF_INET;
    result = bind(sock, (sockaddr*)&addrListen, sizeof(addrListen));
    if (result == -1)
    {
        int lasterror = errno;
        std::cout << "error: " << lasterror;
        exit(1);
    }


    sockaddr_storage addrDest = {};
    result = resolvehelper("192.168.0.4", AF_INET, "9000", &addrDest);
    if (result != 0)
    {
        int lasterror = errno;
        std::cout << "error: " << lasterror;
        exit(1);
    } 
    ros::init(argc, argv, "roborio_bridge");
    ros::NodeHandle nh;
    ros::Subscriber sub_motor = nh.subscribe("robot/cmd_vel", 1,
            motorCallback);
    ros::spin();
}

