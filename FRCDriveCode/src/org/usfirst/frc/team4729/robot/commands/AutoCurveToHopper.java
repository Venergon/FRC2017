package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class AutoCurveToHopper extends Command {
	
	Encoder leftEncoder;
	Encoder rightEncoder;
	public AnalogGyro gyro;
	
	int stage;
	
	int STAGE_MOVE_BACK;
	int STAGE_CURVE;
	int STAGE_GO_UNTIL_HOPPER;
	
	float DISTANCE_MOVE_BACK;
	
    public AutoCurveToHopper() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	leftEncoder = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B, false, Encoder.EncodingType.k4X);
    	rightEncoder = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B, false, Encoder.EncodingType.k4X);
    	DISTANCE_MOVE_BACK = 3; // Change this
    	
    	STAGE_MOVE_BACK = 0;
    	STAGE_CURVE = 1;
    	STAGE_GO_UNTIL_HOPPER = 2;
    	
    	stage = STAGE_MOVE_BACK;
    	
    	Robot.driveSubsystem.tank(-1,-1);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	gyro = new AnalogGyro(RobotMap.GYRO);
    	gyro.calibrate();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (stage == STAGE_MOVE_BACK) {
	    	if ((leftEncoder.getDistance()+rightEncoder.getDistance())/2 < DISTANCE_MOVE_BACK) {
	    		Robot.driveSubsystem.tank(1, 0.5);
	    		stage = STAGE_CURVE;
	    	}
    	} else if (stage == STAGE_CURVE) {
    		if (Math.abs(gyro.getAngle()-0) < 10) {
    			Robot.driveSubsystem.tank(1, 1);
    			leftEncoder.reset();
    			rightEncoder.reset();
    			stage = STAGE_GO_UNTIL_HOPPER;
    		}
    	} else if (stage == STAGE_GO_UNTIL_HOPPER) {
    		
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
