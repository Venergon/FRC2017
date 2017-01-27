#include <stdint.h>
#include <cstdlib>
#include <stdbool.h>
#include <cstring>
#include <cerrno>
#include <string>

#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <sys/time.h>
#include <stdio.h>

#include "ugv_controller/ugv_controller.h"
#include "ugv_controller/message.h"
#include "ugv_controller/control_loop.h"

namespace ugv_controller
{
	Controller::Controller(std::string ip) throw (SocketException)
	{
		// Setup socket
		udpSocket = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP);
		if (udpSocket < 0) {
			throw SocketException("Opening", udpSocket);
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
			throw SocketException("Parsing IP", 0);
		}

		// Check if controller is online
		if (!ping()) {
			throw SocketException("Couldn't connect", 0);
		}

		lightsEnabled = true;

		leftLoop = new ControlLoop(0.01, 4.0);
		rightLoop = new ControlLoop(0.01, 4.0);
	}

	Controller::~Controller()
	{
		close(udpSocket);
	}

	bool Controller::ping()
	{
		struct pkt_cmd_ping pkt;
		pkt.header.type = PKT_TYPE_PING;
		sendPacket(&pkt, sizeof(pkt));

		socklen_t addrlen = sizeof(controllerAddr);
		int recv = recvfrom(udpSocket, &pkt, sizeof(pkt), 0, (struct sockaddr *)&controllerAddr, &addrlen);

		if (recv == sizeof(pkt) && pkt.header.type == PKT_TYPE_PINGRESP) {
			return true;
		} else {
			return false;
		}
	}

	class controllerStatus Controller::getStatusBlocking() throw (SocketException)
	{
		struct pkt_stat_stream pkt;
		socklen_t addrlen = sizeof(controllerAddr);

		int recv = recvfrom(udpSocket, &pkt, sizeof(pkt), 0, (struct sockaddr *)&controllerAddr, &addrlen);

		if (recv != (int)sizeof(pkt)) {
			if (recv == -1) {
				throw SocketException("Receiving/errno", errno);
			} else {
				throw SocketException("Receiving", recv);
			}
		}

		struct controllerStatus stat;

		stat.timestamp = pkt.timestamp;

		stat.lDisp = (double)pkt.l_disp / ENCODER_TICKS_PER_REV * WHEEL_DIAMETER;
		stat.rDisp = (double)pkt.r_disp / ENCODER_TICKS_PER_REV * WHEEL_DIAMETER;
		stat.lVel = (double)pkt.l_vel / ENCODER_TICKS_PER_REV * WHEEL_DIAMETER / 0.02;//leftLoop->dt;
		stat.rVel = (double)pkt.r_vel / ENCODER_TICKS_PER_REV * WHEEL_DIAMETER / 0.02;//rightLoop->dt;

		stat.vBat = (double)pkt.vbat / 695;

		stat.panEnc = (double)pkt.pan_enc;

		static bool oldEstop = false;
		stat.estop = (pkt.din & 1) == 1;

		if (!oldEstop && stat.estop) {
			leftLoop->zeroIntegrator();
			rightLoop->zeroIntegrator();
		}
		oldEstop = stat.estop;

		// Run drive loop
		struct pkt_cmd_wheel spkt;

		spkt.header.type = PKT_TYPE_WHEEL;
		spkt.left = leftLoop->doLoop(stat.lVel);
		spkt.right = -rightLoop->doLoop(stat.rVel);

		sendPacket(&spkt, sizeof(spkt));

		return stat;
	}

	void Controller::sendPacket(void *pkt, size_t len) throw (SocketException)
	{
		if (sendto(udpSocket, pkt, len, 0,
					(struct sockaddr *)&controllerAddr, sizeof(controllerAddr)) < 0) {
			throw SocketException("Sending", (int)(((pkt_header *)pkt)->type));
		}
	}

	void Controller::setOutputs(bool out1, bool out2, bool out3, bool out4) throw (SocketException)
	{
		if (lightsEnabled) {
			struct pkt_cmd_dout pkt;

			pkt.header.type = PKT_TYPE_DOUT;
			pkt.d1 = out1;
			pkt.d2 = out2;
			pkt.d3 = out3;
			pkt.d4 = out4;
			
			sendPacket(&pkt, sizeof(pkt));
		}
	}
	
	void Controller::setVels(double lVel, double rVel) throw (SocketException)
	{
		/*struct pkt_cmd_wheel pkt;

		pkt.header.type = PKT_TYPE_WHEEL;
		pkt.left = (int16_t)(lVel / WHEEL_DIAMETER * ENCODER_TICKS_PER_REV);
		pkt.right = (int16_t)(rVel / WHEEL_DIAMETER * ENCODER_TICKS_PER_REV);

		sendPacket(&pkt, sizeof(pkt));*/

		leftLoop->setPoint = lVel;
		rightLoop->setPoint = rVel;
	}

	void Controller::setPan(uint16_t speed, int32_t leftLimit, int32_t rightLimit) throw (SocketException)
	{
		struct pkt_cmd_panm pkt;

		pkt.header.type = PKT_TYPE_PANM;
		pkt.speed = speed;
    pkt.left_limit = leftLimit;
    pkt.right_limit = rightLimit;

		sendPacket(&pkt, sizeof(pkt));
	}

  void Controller::setPanEnc(int32_t pos) throw (SocketException)
  {
    struct pkt_cmd_panenc pkt;

    pkt.header.type = PKT_TYPE_PANENC;
    pkt.position = pos;

    sendPacket(&pkt, sizeof(pkt));
  }

	int16_t truncGain(double gain)
	{
		if (gain > 2) {
			gain = 2;
		} else if (gain < 0) {
			gain = 0;
		}

		return (int16_t)(gain * 16383);
	}

	void Controller::setPIDGains(double lp, double li, double ld, double lffp, double lffd, double lffdd,
			double rp, double ri, double rd, double rffp, double rffd, double rffdd) throw (SocketException)
	{
		/*struct pkt_cmd_pid pkt;

		pkt.header.type = PKT_TYPE_PID;
		pkt.l_p = truncGain(lp);
		pkt.l_i = truncGain(li);
		pkt.l_d = truncGain(ld);
		pkt.r_p = truncGain(rp);
		pkt.r_i = truncGain(ri);
		pkt.r_d = truncGain(rd);

		sendPacket(&pkt, sizeof(pkt));*/

		leftLoop->kp = lp;
		leftLoop->ki = li;
		leftLoop->kd = ld;
    leftLoop->kffp = lffp;
    leftLoop->kffd = lffd;
    leftLoop->kffdd = lffdd;

		rightLoop->kp = rp;
		rightLoop->ki = ri;
		rightLoop->kd = rd;
    rightLoop->kffp = rffp;
    rightLoop->kffd = rffd;
    rightLoop->kffdd = rffdd;
	}

	void Controller::setLightsEnabled(bool enabled) throw (SocketException)
	{
		if (!enabled) {
			setOutputs(false, false, false, false);
		}
		lightsEnabled = enabled;
	}

	void Controller::setStatusHz(uint8_t hz) throw (SocketException)
	{
		struct pkt_cmd_stream pkt;

		pkt.header.type = PKT_TYPE_STREAM;
		pkt.frequency = hz;

		sendPacket(&pkt, sizeof(pkt));

		leftLoop->dt = 1.0 / hz;
		rightLoop->dt = 1.0 / hz;
	}

	void Controller::setTime(struct timestamp time) throw (SocketException)
	{
		struct pkt_cmd_time pkt;

		pkt.header.type = PKT_TYPE_TIME;
		pkt.time = time;

		sendPacket(&pkt, sizeof(pkt));
	}
}

