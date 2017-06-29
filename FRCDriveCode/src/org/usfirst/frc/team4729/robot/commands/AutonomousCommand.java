package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousCommand extends Command {

	int stage;
	int alliance;
	RobotDrive driveTrain;
	Encoder leftEncoder;
	Encoder rightEncoder;
	
	int STAGE_MOVE_FORWARD;
	int STAGE_TURN_TO_BOILER;
	
	float DISTANCE_MOVE_FORWARD;
	
    public AutonomousCommand(int colour) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	alliance = colour;
    	driveTrain = new RobotDrive(RobotMap.LEFT_FRONT_DRIVE, RobotMap.RIGHT_FRONT_DRIVE);
    	leftEncoder = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B, false, Encoder.EncodingType.k4X);
    	rightEncoder = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B, false, Encoder.EncodingType.k4X);
    	
    	STAGE_MOVE_FORWARD = 0;
    	STAGE_TURN_TO_BOILER = 2;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	leftEncoder.reset();
    	rightEncoder.reset();
    	stage = STAGE_MOVE_FORWARD;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (stage == STAGE_MOVE_FORWARD) {
    		
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
