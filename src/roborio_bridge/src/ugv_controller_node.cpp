#include <stdint.h>
#include <ctime>
#include <sys/time.h>

#include <ros/ros.h>
#include <ros/console.h>
#include <nav_msgs/Odometry.h>
#include <std_msgs/Int32.h>
#include <std_msgs/Float64.h>
#include <std_msgs/Bool.h>
#include <std_msgs/String.h>
#include <std_msgs/Float64MultiArray.h>
#include <geometry_msgs/Twist.h>
#include <geometry_msgs/Quaternion.h>
#include <geometry_msgs/Vector3Stamped.h>
#include <tf/transform_broadcaster.h>

#include "ugv_controller/ugv_controller.h"
#include "ugv_controller/message.h" //TODO only required because of timestamp struct. this should not be necessary

namespace ugv_controller
{
	class ControllerNode
	{
		public:
			static const double CMDVEL_TIMEOUT_RES = 0.1;

			ControllerNode(ros::NodeHandle nh, ros::NodeHandle pnh) : 
				nh(nh), pnh(pnh)
			{
				odomRawPub = nh.advertise<std_msgs::Float64MultiArray>("odom_raw", 2);
				odomXYPub = nh.advertise<nav_msgs::Odometry>("odometry", 2);
				estopPub = nh.advertise<std_msgs::Bool>("estop", 2);
				vbatPub = nh.advertise<std_msgs::Float64>("battery", 2);
				panPub = nh.advertise<geometry_msgs::Vector3Stamped>("pan_angle", 2);
				
				cmdVelSub = nh.subscribe("cmd_vel", 1,
						&ControllerNode::cmdVelCallback, this, ros::TransportHints().tcpNoDelay());
				safetySub = nh.subscribe("safety", 1,
						&ControllerNode::safetyCallback, this);
				colourSub = nh.subscribe("colour", 1,
						&ControllerNode::colourCallback, this);
				panCmdSub = nh.subscribe("pan_cmd", 1,
            &ControllerNode::panCmdCallback, this, ros::TransportHints().tcpNoDelay());
				panEncSetSub = nh.subscribe("pan_encoder_set", 1,
            &ControllerNode::panEncSetCallback, this);
				blueSub = nh.subscribe("blue", 1,
            &ControllerNode::blueCallback, this);

				std::string ip;
				pnh.param("ip_addr", ip, std::string("192.168.1.185"));
				controller = new Controller(ip);

        setTime();

				double lp, li, ld, lffp, lffd, lffdd, rp, ri, rd, rffp, rffd, rffdd;
				pnh.param("lp", lp, 0.30);
				pnh.param("li", li, 0.03);
				pnh.param("ld", ld, 0.01);
				pnh.param("lffp", lffp, 0.0);
				pnh.param("lffd", lffd, 0.0);
				pnh.param("lffdd", lffdd, 0.0);

				pnh.param("rp", rp, 0.30);
				pnh.param("ri", ri, 0.03);
				pnh.param("rd", rd, 0.01);
				pnh.param("rffp", rffp, 0.0);
				pnh.param("rffd", rffd, 0.0);
				pnh.param("rffdd", rffdd, 0.0);
				controller->setPIDGains(lp, li, ld, lffp, lffd, lffdd, rp, ri, rd, rffp, rffd, rffdd);
				
				pnh.param("lights_enabled", lightsEnabled, true);
				controller->setLightsEnabled(lightsEnabled);

/*				double panSpeed;
				pnh.param("pan_speed", panSpeed, 0.0);
				if (panSpeed < 0) { panSpeed = 0; }
				if (panSpeed > 1) { panSpeed = 1; }
				controller->setPan((uint16_t)(panSpeed * 65535.0));
*/
				int decimation;
				pnh.param("vbat_estop_decimation", decimation, 10);
				if (decimation < 1) { decimation = 1; }
				if (decimation > 1000) { decimation = 1000; }
				vbatEstopDecimation = decimation;

				int hz;
				pnh.param("publish_hz", hz, 50);
				if (hz < 1) { hz = 1; }
				if (hz > 200) {hz = 200; }
        statusHz = hz;
				controller->setStatusHz((uint8_t)hz);

				double safetyPeriod;
				pnh.param("safety_period", safetyPeriod, 0.5);
				safetyFlashTimer = nh.createTimer(ros::Duration(safetyPeriod),
							&ControllerNode::safetyFlashCallback, this);

				double c;
				pnh.param("cmdvel_timeout", c, 0.5);
				cmdVelTimeout = (int) (c / CMDVEL_TIMEOUT_RES);
				cmdVelTimeoutTimer = nh.createTimer(ros::Duration(CMDVEL_TIMEOUT_RES),
							&ControllerNode::cmdVelTimeoutCallback, this);

        pnh.param("max_linear", maxLinear, 4.0);
        pnh.param("max_angular", maxAngular, 6.0);
				
				odomRawMsg.data.resize(4);

				estopState = false;
				cmdVelTimeoutCounter = 0;
        spinCounter = 0;
			}

			~ControllerNode()
			{
        controller->setLightsEnabled(false);
				delete controller;
			}

			void spin()
			{
				struct controllerStatus status;
				try {
					status = controller->getStatusBlocking();
					updateStatus(status);

          static int flash_on = 0;
          controller->setLightsEnabled(lightsEnabled && (++flash_on % 2 < 1));
          updateLights();

				} catch (std::exception& ex) {
          controller->setStatusHz(statusHz);
					ROS_ERROR_STREAM(ex.what());
				}
				
				ros::spinOnce();
			}

		private:
			Controller *controller;

			ros::NodeHandle nh;
			ros::NodeHandle pnh;
			
			ros::Publisher odomRawPub;
			ros::Publisher odomXYPub;
			ros::Publisher estopPub;
			ros::Publisher vbatPub;
			ros::Publisher panPub;

			ros::Subscriber safetySub;
			ros::Subscriber cmdVelSub;
			ros::Subscriber colourSub;
			ros::Subscriber panCmdSub;
			ros::Subscriber panEncSetSub;
      ros::Subscriber blueSub;

			ros::Timer safetyFlashTimer;
			ros::Timer cmdVelTimeoutTimer;

      tf::TransformBroadcaster br;

			unsigned int vbatEstopDecimation;


			bool estopState;

      bool lightsEnabled;
			bool out1, out2, out3, out4; // Lights

			std_msgs::Float64MultiArray odomRawMsg;
			
      double maxLinear;
      double maxAngular;

      int spinCounter;

			enum safety {
				SAFETY_OFF = 0,
				SAFETY_ON = 1,
				SAFETY_FLASHING = 2
			} safetyState;

			int cmdVelTimeoutCounter;
			int cmdVelTimeout;
      
      int statusHz;

      void setTime()
      {
				ros::Time rostime = ros::Time::now();
				struct timestamp time;
				time.tv_sec = rostime.sec;
				time.tv_nsec = rostime.nsec;
				controller->setTime(time);
      }

			void safetyFlashCallback(const ros::TimerEvent&)
			{
				if (safetyState == SAFETY_FLASHING) {
					out2 ^= 1;

					updateLights();
				}
			}

			void cmdVelTimeoutCallback(const ros::TimerEvent&)
			{
				if (cmdVelTimeoutCounter > cmdVelTimeout) {
					// No new cmd_vel message for a while
					controller->setVels(0, 0);

          if (cmdVelTimeoutCounter == (cmdVelTimeout+1))
            ROS_WARN("cmd_vel timeout occurred\n");

				}

			  cmdVelTimeoutCounter++;
			}

			void updateLights()
			{
				controller->setOutputs(out1, out2, out3, out4);
			}

			void publishOdomXY(ros::Time time,
					double lDisp, double rDisp, double lVel, double rVel)
			{
				// Covariances from Husky
				const double ODOM_POSE_COVAR_MOTION[] = {1e-3, 0, 0, 0, 0, 0,
					0, 1e-3, 0, 0, 0, 0,
					0, 0, 1e6, 0, 0, 0,
					0, 0, 0, 1e6, 0, 0,
					0, 0, 0, 0, 1e6, 0,
					0, 0, 0, 0, 0, 1e6};
				const double ODOM_POSE_COVAR_NOMOVE[] = {1e-9, 0, 0, 0, 0, 0,
					0, 1e-3, 1e-9, 0, 0, 0,
					0, 0, 1e6, 0, 0, 0,
					0, 0, 0, 1e6, 0, 0,
					0, 0, 0, 0, 1e6, 0,
					0, 0, 0, 0, 0, 1e-9};
				const double ODOM_TWIST_COVAR_MOTION[] = {1e-3, 0, 0, 0, 0, 0,
					0, 1e-3, 0, 0, 0, 0,
					0, 0, 1e6, 0, 0, 0,
					0, 0, 0, 1e6, 0, 0,
					0, 0, 0, 0, 1e6, 0,
					0, 0, 0, 0, 0, 1e6};
				const double ODOM_TWIST_COVAR_NOMOVE[] = {1e-9, 0, 0, 0, 0, 0,
					0, 1e-3, 1e-9, 0, 0, 0,
					0, 0, 1e6, 0, 0, 0,
					0, 0, 0, 1e6, 0, 0,
					0, 0, 0, 0, 1e6, 0,
					0, 0, 0, 0, 0, 1e-9};
			
				static nav_msgs::Odometry odom;
				
				static double oldDL = 0;
				static double oldDR = 0;
				
				double l = lDisp - oldDL;
				double r = rDisp - oldDR;
				
				oldDL += l;
				oldDR += r;
				
				double ds = l + r / 2;
				double da = (r - l) / controller->WHEEL_DISTANCE;

				geometry_msgs::Quaternion q = odom.pose.pose.orientation;
				double heading = atan2(2 * q.y * q.w, 1 - 2 * q.y * q.y);
				
				heading += da;
				
				q.w = cos(heading / 2);
				q.y = sin(heading / 2);
				
				odom.pose.pose.orientation = q;
				odom.pose.pose.position.x += ds * cos(heading);
				odom.pose.pose.position.y += ds * sin(heading);
				
				odom.twist.twist.linear.x = (lVel + rVel) / 2;
				odom.twist.twist.linear.y = (lVel + rVel) / 2;
				odom.twist.twist.angular.z = (rVel - lVel) / controller->WHEEL_DISTANCE;
				
				// Covariances
				if (lVel == 0 && rVel == 0) {
					for (int i = 0; i < 36; i++) {
						odom.pose.covariance[i] = ODOM_POSE_COVAR_NOMOVE[i];
						odom.twist.covariance[i] = ODOM_TWIST_COVAR_NOMOVE[i];
					}
				} else {
					for (int i = 0; i < 36; i++) {
						odom.pose.covariance[i] = ODOM_POSE_COVAR_MOTION[i];
						odom.twist.covariance[i] = ODOM_TWIST_COVAR_MOTION[i];
					}
				}

				odom.header.stamp = time;
				odomXYPub.publish(odom);
			}

			void updateStatus(const struct controllerStatus& status)
			{
				static int decimateCounter = 0;
				if (++decimateCounter >= vbatEstopDecimation) {
					decimateCounter = 0;
					
					std_msgs::Float64 vbatMsg;
					vbatMsg.data = status.vBat;
					vbatPub.publish(vbatMsg);

					std_msgs::Bool estopMsg;
					estopMsg.data = status.estop;
					estopState = status.estop;
					estopPub.publish(estopMsg);
				}

                // flashing red light on battery low
                out1 = status.vBat < 11.2;

				ros::Time rosTimestamp(status.timestamp.tv_sec, status.timestamp.tv_nsec);

        ros::Time now = ros::Time::now();
        // fallback time calculation if time is bad
        if(fabs((now - rosTimestamp).toSec()) > .005) {
          setTime();
          rosTimestamp = now;
        }

        // broadcast pan angle
				geometry_msgs::Vector3Stamped panMsg;
				panMsg.vector.z = status.panEnc;
				panMsg.header.stamp = rosTimestamp;
				panPub.publish(panMsg);

        // broadcast panning laser transform
        tf::Transform transform;
        transform.setOrigin( tf::Vector3(0.05, 0.0, 0.73) );
        tf::Quaternion q = tf::createQuaternionFromYaw(-status.panEnc * (M_PI/23000.0) + 1.57079635);
        //q.setRPY(0, 0, status.panEnc/48000.0);
        transform.setRotation(q);
        br.sendTransform(tf::StampedTransform(transform, rosTimestamp, "/base_link", "/panning_mount"));

        // broadcast odom
				odomRawMsg.data[0] = status.lDisp;
				odomRawMsg.data[1] = status.rDisp;
				odomRawMsg.data[2] = status.lVel;
				odomRawMsg.data[3] = status.rVel;
				odomRawPub.publish(odomRawMsg);

				publishOdomXY(rosTimestamp,
						status.lDisp, status.rDisp, status.lVel, status.rVel);
			}

			// XXX This could be extended to limit velocity and acceleration
			void processCmdVel(double& lVel, double& rVel, const geometry_msgs::Twist& cmdVel)
			{
        geometry_msgs::Twist cmd = cmdVel;
        if(cmd.linear.x > maxLinear)
          cmd.linear.x = maxLinear;
        else if(cmd.linear.x < -maxLinear)
          cmd.linear.x = -maxLinear;

        if(cmd.angular.z > maxAngular)
          cmd.angular.z = maxAngular;
        else if(cmd.angular.z < -maxAngular)
          cmd.angular.z = -maxAngular;

				if (estopState) {
					lVel = (2 * cmd.linear.x - controller->WHEEL_DISTANCE * cmd.angular.z) / 2;
					rVel = (2 * cmd.linear.x + controller->WHEEL_DISTANCE * cmd.angular.z) / 2;
				} else {
					lVel = 0;
					rVel = 0;
				}
			}

			void cmdVelCallback(const geometry_msgs::Twist::ConstPtr& msg)
			{
				cmdVelTimeoutCounter = 0;

				double lVel, rVel;
				processCmdVel(lVel, rVel, *msg);

				controller->setVels(lVel, rVel);
			}

			void safetyCallback(const std_msgs::Int32::ConstPtr& msg)
			{
				safetyState = static_cast<enum safety>(msg->data);

				switch (safetyState) {
					case SAFETY_OFF:
						out2 = false;
						updateLights();
						break;
					case SAFETY_ON:
						out2 = true;
						updateLights();
						break;
				}
			}

			void colourCallback(const std_msgs::Int32::ConstPtr& msg)
			{
				out1 = msg->data & 1;
				out2 = msg->data & 2;
				out3 = msg->data & 4;
				out4 = msg->data & 8;
				updateLights();
			}

      void panCmdCallback(const geometry_msgs::Vector3::ConstPtr& msg)
      {
        double panSpeed = msg->x;
				if (panSpeed < -1) { panSpeed = -1; }
				if (panSpeed > 1) { panSpeed = 1; }
       
        double left_max = msg->y;
        double right_max = msg->z;
        controller->setPan(
                (int32_t)(panSpeed * 32767.0),
                (int32_t)(left_max), 
                (int32_t)(right_max)
        ); 
      }

      void panEncSetCallback(const std_msgs::Int32::ConstPtr& msg)
      {
        controller->setPanEnc(msg->data);
      }

      void  blueCallback(const std_msgs::Bool::ConstPtr& msg)
      {
        out3 = msg->data;
				updateLights();
      }
	};
}

void motorCallback(const geometry_msgs::Twist::ConstPtr& msg) {
      ROS_INFO_STREAM("Message: " << msg->linear.x);
      const char* test_msg = "testing";
      size_t msg_length = strlen(test_msg);

      result = sendto(sock, test_msg, msg_length, 0, (sockaddr*)&addrDest,
              sizeof(addrDest));

      std::cout << result << " bytes sent" << std::endl;
}

int main(int argc, char **argv)
{
    result = 0;
    sock = socket(AF_INET, SOCK_DGRAM, 0);

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


    addrDest = {};
    ugv_controller::Controller *c = new ugv_controller::Controller("192.168.0.4");
   
    //result = resolvehelper("192.168.0.4", AF_INET, "9000", &addrDest);
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


int main(int argc, char **argv)
{
	ros::init(argc, argv, "ugv_controller_node");
	ros::NodeHandle nh("controller");
	ros::NodeHandle pnh("~");

	ugv_controller::ControllerNode *c;

	ros::Rate startupRetry(1);
	while (ros::ok()) {
		try {
			c = new ugv_controller::ControllerNode(nh, pnh);
			break;
		} catch (std::exception& ex) {
			ROS_ERROR_STREAM(ex.what() << " retrying in 1 second...");
		}
	
		ros::spinOnce();
		startupRetry.sleep();
	}

	ROS_INFO_STREAM("Connected to controller!");

	while (ros::ok()) {
		try {
			c->spin();
		} catch (std::exception& ex) {
			ROS_WARN_STREAM(ex.what());
		}
	}

	delete c;

	return 0;
}

