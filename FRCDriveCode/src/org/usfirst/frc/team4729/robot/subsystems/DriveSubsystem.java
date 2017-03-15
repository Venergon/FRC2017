package org.usfirst.frc.team4729.robot.subsystems;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class DriveSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Encoder leftEncoder;
    Encoder rightEncoder
    RobotDrive driveTrain = new RobotDrive(RobotMap.LEFT_DRIVE, RobotMap.RIGHT_DRIVE);
    public AnalogGyro gyro;

	double leftSpeed = 0;
	double rightSpeed = 0;
	double turnSpeed = 0;
	double moveSpeed = 0;
	double leftFinal = 0;
	double rightFinal = 0;
	double moveFinal = 0;
	double turnFinal = 0;
    
	static double DEADZONE = 0.15;
	static double ACCELERATION = 0.15;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	gyro = new AnalogGyro(RobotMap.GYRO);
    	gyro.calibrate();

        leftEncoder = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B, false, Encoder.EncodingType.k4X);
        rightEncoder = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B, false, Encoder.EncodingType.k4X);

    }

    public double leftDistance() {
        return leftEncoder.getDistance();
    }

    public double rightDistance() {
        return rightEncoder.getDistance();
    }

    public void encoderReset() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void arcade(double desiredMove, double desiredTurn) {
    	if (Math.abs(desiredMove) < Math.abs(moveSpeed)){
    		moveSpeed = desiredMove;
    	}
    	
    	if (Math.abs(desiredTurn) < Math.abs(turnSpeed)) {
    		turnSpeed = desiredTurn;
    	}
    	
    	turnSpeed += (desiredTurn-turnSpeed)*ACCELERATION;
    	moveSpeed += (desiredMove-moveSpeed)*ACCELERATION;
    	
    	if (Math.abs(moveSpeed) > DEADZONE) {
    		moveFinal = moveSpeed*0.7+moveSpeed/Math.abs(moveSpeed)*0.3;
    		turnFinal = turnSpeed/Math.abs(turnSpeed)*Math.sqrt(Math.abs(turnSpeed));
    	}
    	else  if (Math.abs(turnSpeed) > DEADZONE) {
    		moveFinal = 0;
    		turnFinal = turnSpeed*0.7+turnSpeed/Math.abs(turnSpeed)*0.3;
    	}
    	else {
    		moveFinal = 0;
    		turnFinal = 0;
    	}
    	
    	if (Robot.flipped) {
    		driveTrain.arcadeDrive(-moveFinal, -turnFinal);
    	} else {
    		driveTrain.arcadeDrive(moveFinal, -turnFinal);
    	}
    }
      
    public void tank (double desiredLeft, double desiredRight) {
    	if (Math.abs(desiredLeft) < Math.abs(leftSpeed)){
    		leftSpeed = desiredLeft;
    	}
    	
    	if (Math.abs(desiredRight) < Math.abs(rightSpeed)) {
    		rightSpeed = desiredRight;
    	}
    	
    	rightSpeed += (desiredRight-rightSpeed)*ACCELERATION;
    	leftSpeed += (desiredLeft-leftSpeed)*ACCELERATION;
    	
    	if (Math.abs(leftSpeed) > DEADZONE) {
    		leftFinal = leftSpeed*0.7+leftSpeed/Math.abs(leftSpeed)*0.3;
    	}
    	if (Math.abs(rightSpeed) > DEADZONE) {
    		rightFinal = rightSpeed*0.7+rightSpeed/Math.abs(rightSpeed)*0.3;
    	}
    	
    	if (Robot.flipped) {
    		driveTrain.tankDrive(-leftFinal, -rightFinal);
    	} else {
    		driveTrain.tankDrive(rightFinal, leftFinal);
    	}
	}
}
