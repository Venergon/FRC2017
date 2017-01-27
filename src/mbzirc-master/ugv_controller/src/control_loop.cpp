#include <stdio.h>
#include <stdint.h>
#include <math.h>

#include "ugv_controller/control_loop.h"

namespace ugv_controller
{
	int16_t ControlLoop::doLoop(double measured)
	{
    double diffSetPoint = setPoint - effSetPoint;

    // apply acceleration limit
    if (maxAcceleration <= 0)
      effSetPoint = setPoint;
    else {
      if(diffSetPoint > maxAcceleration * dt)
        diffSetPoint = maxAcceleration * dt;
      else if(diffSetPoint < -maxAcceleration * dt)
        diffSetPoint = -maxAcceleration * dt;

      effSetPoint += diffSetPoint;
    }

		double err = effSetPoint - measured;

		integral += err * dt;
    if (setPoint == 0 && measured == 0)
      integral = 0;
		else if (integral > INTEGRAL_CAP)
      integral = INTEGRAL_CAP;
    else if (integral < -INTEGRAL_CAP)
      integral = -INTEGRAL_CAP;
		

		// Derivate not used at the moment
		double derivative = (err - prevErr) / dt;
    prevErr = err;

		double output = (kp * err + ki * integral + kffp * effSetPoint + kffd * diffSetPoint);

		int16_t rawOutput = (int16_t)(output * (2500 / 3.5 / 3));

		//printf("this=%p, set=%lf\t eset=%2lf\tmeasured=%2lf\toutput=%2lf\trawout=%d\n", this, setPoint, effSetPoint, measured, output, rawOutput);
		//printf("this=%p, set=%lf\t eset=%2lf, meas=%2f, err=%2f, pout=%2f, iout=%2f, ffout=%2f\n", this, setPoint, effSetPoint, measured, err, kp*err, ki*integral, kd*effSetPoint);

    // Hysteruses removal
    if (fabs(rawOutput) > 20){
      if (fabs(rawOutput) < 120){
        rawOutput += 120 * (rawOutput >0 ? 1:-1);
      }
    }
		return rawOutput;
	}

	void ControlLoop::zeroIntegrator()
	{
		integral = 0;
	}
}

