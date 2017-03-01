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
	int STAGE_TURN_TO_HOPPER;
	int STAGE_DONE;

	float DISTANCE_MOVE_BACK;
	float DISTANCE_TO_HOPPER;
	int ANGLE_TO_HOPPER;

    public AutoCurveToHopper() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	leftEncoder = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B, false, Encoder.EncodingType.k4X);
    	rightEncoder = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B, false, Encoder.EncodingType.k4X);

    	DISTANCE_MOVE_BACK = 3; // Change this
    	DISTANCE_TO_HOPPER = 3; // Change this
    	ANGLE_TO_HOPPER = 90;

    	STAGE_MOVE_BACK = 0;
    	STAGE_CURVE = 1;
    	STAGE_GO_UNTIL_HOPPER = 2;
    	STAGE_TURN_TO_HOPPER = 3;
    	STAGE_DONE = 4;

    	stage = STAGE_MOVE_BACK;

    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	gyro = new AnalogGyro(RobotMap.GYRO);
    	gyro.calibrate();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
			while (!(leftEncoder.getDistance()+rightEncoder.getDistance())/2 < DISTANCE_MOVE_BACK)) {
				Robot.driveSubsystem.tank(-1,-1);
			}
			while (Math.abs(gyro.getAngle()-0) < 10) {
    		Robot.driveSubsystem.tank(1, 0.5);
			}
  		while ((leftEncoder.getDistance()+rightEncoder.getDistance())/2 < DISTANCE_TO_HOPPER)) {
  			Robot.driveSubsystem.tank(1, 1);
  			leftEncoder.reset();
  			rightEncoder.reset();
			}
  		while (Math.abs(gyro.getAngle()-ANGLE_TO_HOPPER) > 10) {
    		Robot.driveSubsystem.tank(-1, 1);
    	}
			Robot.driveSubsystem.tank(0, 0);
			stage = STAGE_DONE;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (stage == STAGE_DONE) {
    		return true;
    	}
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
