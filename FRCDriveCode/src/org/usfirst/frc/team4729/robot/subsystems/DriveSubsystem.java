package org.usfirst.frc.team4729.robot.subsystems;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    RobotDrive driveTrain = new RobotDrive(RobotMap.LEFT_DRIVE, RobotMap.RIGHT_DRIVE);
    
    double maxSpeed = 1;
	double leftSpeed = 0;
	double rightSpeed = 0;
	double turnSpeed = 0;
	double forwardSpeed = 0;
    
	static double ACCELERATION = 0.5;
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
	
    public void arcade(double desiredMove, double desiredTurn, double maxSpeed) {
    	if (Math.abs(desiredMove) < Math.abs(turnSpeed)){
    		forwardSpeed = desiredMove;
    	}
    	
    	if (Math.abs(desiredTurn) < Math.abs(turnSpeed)) {
    		turnSpeed = desiredTurn;
    	}
    	turnSpeed += (desiredTurn-turnSpeed)*ACCELERATION;
    	forwardSpeed += (desiredMove-forwardSpeed)*ACCELERATION;
    	if (Robot.flipped) {
    		driveTrain.arcadeDrive(-forwardSpeed*maxSpeed, -turnSpeed*maxSpeed);
    	} else {
    		driveTrain.arcadeDrive(forwardSpeed*maxSpeed, turnSpeed*maxSpeed);
    	}
    }
      
    public void tank (double desiredLeft, double desiredRight, double maxSpeed) {
    	if (Math.abs(desiredLeft) < Math.abs(leftSpeed)){
    		leftSpeed = desiredLeft;
    	}
    	
    	if (Math.abs(desiredRight) < Math.abs(rightSpeed)) {
    		rightSpeed = desiredRight;
    	}
    	
    	rightSpeed += (desiredRight-rightSpeed)*ACCELERATION;
    	leftSpeed += (desiredLeft-leftSpeed)*ACCELERATION;
    	if (Robot.flipped) {
    		driveTrain.tankDrive(-leftSpeed*maxSpeed, -rightSpeed*maxSpeed);
    	} else {
    		driveTrain.tankDrive(rightSpeed*maxSpeed, leftSpeed*maxSpeed);
    	}
	}
}

/*package org.usfirst.frc.team4729.robot.subsystems;

import org.usfirst.frc.team4729.robot.Robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
/*
public class DriveSubsystem extends Subsystem {
	RobotDrive driveTrain = new RobotDrive(0, 1);
	
	double leftSpeed = 0;
	double rightSpeed = 0;
	double turnSpeed = 0;
	double forwardSpeed = 0;
	
	double acceleration = 0.05;
	double speed = 1;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void arcade(double desiredMove, double desiredTurn) {
    	if ((desiredMove < 0.1) && (desiredMove > -0.1)){
    		desiredMove = 0;
    		forwardSpeed = 0;
    	}
    	if ((desiredTurn < 0.1) && (desiredTurn > -0.1)){
    		desiredTurn = 0;
    		turnSpeed = 0;
    	}
    	
    	if  (((desiredMove > 0) && (forwardSpeed < 0)) || ((desiredMove < 0) && (forwardSpeed > 0))){
    		forwardSpeed = 0;
    	}
    	if (((desiredTurn > 0) && (turnSpeed < 0)) || ((desiredTurn < 0) && (turnSpeed > 0))){
    		turnSpeed = 0;
    	}
    	
    	if (Math.abs(desiredMove) < Math.abs(turnSpeed)){
    		forwardSpeed = desiredMove;
    	}
    	
    	if (Math.abs(desiredTurn) < Math.abs(turnSpeed)) {
    		turnSpeed = desiredTurn;
    	}
    	
    	turnSpeed += (desiredTurn-turnSpeed)*acceleration;
    	forwardSpeed += (desiredMove-forwardSpeed)*acceleration;
    	driveTrain.arcadeDrive(-forwardSpeed*speed, -turnSpeed*speed);
    }
    
    
    
    public void tank (double desiredLeft, double desiredRight) {
    	if ((desiredLeft < 0.1) && (desiredLeft > -0.1)){
    		desiredLeft = 0;
    		leftSpeed = 0;
    	}
    	if ((desiredRight < 0.1) && (desiredRight > -0.1)){
    		desiredRight = 0;
    		rightSpeed = 0;
    	}
    	
    	if  (((desiredLeft > 0) && (leftSpeed < 0)) || ((desiredLeft < 0) && (leftSpeed > 0))){
    		leftSpeed = 0;
    	}
    	if (((desiredRight > 0) && (rightSpeed < 0)) || ((desiredRight < 0) && (rightSpeed > 0))){
    		rightSpeed = 0;
    	}
    	
    	if (Math.abs(desiredLeft) < Math.abs(leftSpeed)){
    		leftSpeed = desiredLeft;
    	}
    	
    	if (Math.abs(desiredRight) < Math.abs(rightSpeed)) {
    		rightSpeed = desiredRight;
    	}
    	rightSpeed += (desiredRight-rightSpeed)*acceleration;
    	leftSpeed += (desiredLeft-leftSpeed)*acceleration;
	}
    
}*/
