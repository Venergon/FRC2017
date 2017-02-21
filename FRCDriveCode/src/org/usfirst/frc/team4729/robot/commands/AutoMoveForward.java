package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoMoveForward extends Command {
	
	RobotDrive driveTrain;
	Encoder leftEncoder;
	Encoder rightEncoder;
	
	float DISTANCE_MOVE_FORWARD;
	
    public AutoMoveForward() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	driveTrain = new RobotDrive(RobotMap.LEFT_DRIVE, RobotMap.RIGHT_DRIVE);
    	leftEncoder = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B, false, Encoder.EncodingType.k4X);
    	rightEncoder = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B, false, Encoder.EncodingType.k4X);
    	DISTANCE_MOVE_FORWARD = 3; // Change this
    	driveTrain.tankDrive(1,1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if ((leftEncoder.getDistance()+rightEncoder.getDistance())/2 > DISTANCE_MOVE_FORWARD) {
			driveTrain.tankDrive(0,0);
			return true;
		} else {
			return false;
		}
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
