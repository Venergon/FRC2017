package org.usfirst.frc.team4729.robot.subsystems;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    RobotDrive driveTrain = new RobotDrive(RobotMap.LEFT_DRIVE, RobotMap.RIGHT_DRIVE);
    

	double leftSpeed = 0;
	double rightSpeed = 0;
	double turnSpeed = 0;
	double moveSpeed = 0;
	double leftFinal = 0;
	double rightFinal = 0;
	double moveFinal = 0;
	double turnFinal = 0;
    
	static double ACCELERATION = 0.1;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
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
    	
    	if (Math.abs(moveSpeed) > 0.05) {
    		moveFinal = moveSpeed*0.6+moveSpeed/Math.abs(moveSpeed)*0.4;
    		turnFinal = turnSpeed;
    	}
    	else  if (Math.abs(turnSpeed) > 0.05) {
    		moveFinal = 0;
    		turnFinal = turnSpeed*0.6+turnSpeed/Math.abs(turnSpeed)*0.4;
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
    	
    	if (Math.abs(leftSpeed) > 0.05) {
    		leftSpeed = leftSpeed*0.6+leftSpeed/Math.abs(leftSpeed)*0.4;
    	}
    	if (Math.abs(rightSpeed) > 0.05) {
    		rightSpeed = rightSpeed*0.6+rightSpeed/Math.abs(rightSpeed)*0.4;
    	}
    	
    	if (Robot.flipped) {
    		driveTrain.tankDrive(-leftSpeed, -rightSpeed);
    	} else {
    		driveTrain.tankDrive(rightSpeed, leftSpeed);
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
	double moveSpeed = 0;
	
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
    		moveSpeed = 0;
    	}
    	if ((desiredTurn < 0.1) && (desiredTurn > -0.1)){
    		desiredTurn = 0;
    		turnSpeed = 0;
    	}
    	
    	if  (((desiredMove > 0) && (moveSpeed < 0)) || ((desiredMove < 0) && (moveSpeed > 0))){
    		moveSpeed = 0;
    	}
    	if (((desiredTurn > 0) && (turnSpeed < 0)) || ((desiredTurn < 0) && (turnSpeed > 0))){
    		turnSpeed = 0;
    	}
    	
    	if (Math.abs(desiredMove) < Math.abs(turnSpeed)){
    		moveSpeed = desiredMove;
    	}
    	
    	if (Math.abs(desiredTurn) < Math.abs(turnSpeed)) {
    		turnSpeed = desiredTurn;
    	}
    	
    	turnSpeed += (desiredTurn-turnSpeed)*acceleration;
    	moveSpeed += (desiredMove-moveSpeed)*acceleration;
    	driveTrain.arcadeDrive(-moveSpeed*speed, -turnSpeed*speed);
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
