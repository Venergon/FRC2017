#ifndef IGVC_CONTROLLER_H
#define IGVC_CONTROLLER_H

#include <string>
#include <stdint.h>
#include <stdexcept>
#include <iostream>
#include <exception>
#include <sstream>

#include <sys/socket.h>
#include <netinet/in.h>

#include "message.h"
#include "control_loop.h"

namespace ugv_controller
{

const int UDP_TIMEOUT = 100000; // in us

class SocketException : public std::exception
{
	public:
	SocketException(const std::string stage, const int code) : stage(stage), code(code)  {}
	
	virtual ~SocketException() throw () {};
		
	virtual const char *what() const throw ()
	{
		static std::ostringstream oss;
		oss.str("");
		oss << "Socket operation failed. " << stage << ", " << code;
		return oss.str().c_str();
	}

	private:
	int code;
	std::string stage;
};

struct controllerStatus
{
	struct timestamp timestamp;

	double lDisp;
	double rDisp;
	double lVel;
	double rVel;

	double vBat;

	double panEnc;

	bool estop;
};

class Controller
{
	public:
	static const double ENCODER_TICKS_PER_REV = 8192;
	static const double WHEEL_DIAMETER = 171.0 * 3.14 / 1000;
	static const double WHEEL_DISTANCE = 0.45;

	static const double PAN_ENCODER_TICKS_PER_RAD = 1000.0; // TODO set this correctly

	Controller(std::string ip) throw (SocketException);
	~Controller();

	struct controllerStatus getStatusBlocking() throw (SocketException);

	void setOutputs(bool out0, bool out1, bool out2, bool out3) throw (SocketException);
	void setVels(double lVel, double rVel) throw (SocketException);
	void setPan(uint16_t speed, int32_t leftLimit, int32_t rightLimit) throw (SocketException);
	 
	void setPIDGains(double lp, double li, double ld, double lffp, double lffd, double lffdd,
			double rp, double ri, double rd, double rffp, double rffd, double rffdd) throw (SocketException);
	void setLightsEnabled(bool enabled) throw (SocketException);
	void setStatusHz(uint8_t hz) throw (SocketException); // Set to 0 to stop
  void setPanEnc(int32_t pos) throw (SocketException);

	void setTime(struct timestamp time) throw (SocketException);

	bool ping();

	private:
	bool lightsEnabled;
	int udpSocket;
	struct sockaddr_in controllerAddr;

	ControlLoop *leftLoop, *rightLoop;

	void sendPacket(void *pkt, size_t len) throw (SocketException);
};

}

#endif

