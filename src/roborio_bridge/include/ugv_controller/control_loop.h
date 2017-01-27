#ifndef CONTROL_LOOP_H
#define CONTROL_LOOP_H

#include <stdint.h>

namespace ugv_controller
{
	class ControlLoop
	{
		public:
			static const double INTEGRAL_CAP = 2; // TODO make this a parameter in the node

			ControlLoop(double p, double i, double d, double dt, double ffp, double ffd, double ffdd, double maxAccel = 0)
				: kp(p), ki(i), kd(d), dt(dt), kffp(ffp), kffd(ffd), kffdd(ffdd), setPoint(0), prevErr(0), integral(0), effSetPoint(0), maxAcceleration(maxAccel) {}
			
      ControlLoop(double dt, double maxAccel = 0)
				: kp(0), ki(0), kd(0), dt(dt), kffp(0), kffd(0), kffdd(0), setPoint(0), prevErr(0), integral(0), effSetPoint(0), maxAcceleration(maxAccel) {}

			int16_t doLoop(double measured);

			void zeroIntegrator();
			
			double kp;
			double ki;
			double kd;

			double kffp;
      double kffd;
      double kffdd;

			double dt;

			double setPoint;
      double effSetPoint;

      double maxAcceleration;

		private:
			double prevErr;
			double integral;
	};
}

#endif

