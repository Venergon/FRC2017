package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class BasicAutoMoveToHopper extends Command {

	public AnalogGyro gyro;
	Timer timer;

	boolean done;
	boolean straight;
	
	String team;	

	float TIME_MOVE_BACK;
	float TIME_TO_HOPPER;
	int ANGLE_TO_HOPPER;

    public BasicAutoMoveToHopper(String teamColour, boolean isStraight) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	done = false;
    	straight = isStraight;
    	team = teamColour;

    	TIME_MOVE_BACK = 1; // Change this
    	TIME_TO_HOPPER = 3; // Change this
    	ANGLE_TO_HOPPER = 90;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveSubsystem.encoderReset();
    	gyro = new AnalogGyro(RobotMap.GYRO);
    	gyro.calibrate();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (!straight) {
			while (timer.get() < TIME_MOVE_BACK) {
				Robot.driveSubsystem.tank(-1,-1);
			}
			while (Math.abs(gyro.getAngle()-0) < 10) {
				if (team == "red") {
					Robot.driveSubsystem.tank(1, 0.5);
				} else {
					Robot.driveSubsystem.tank(0.5, 1);
				}
			}
			timer.reset();
    	}
  		while (timer.get() < TIME_TO_HOPPER) {
  			Robot.driveSubsystem.tank(1, 1);
		}
  		while (Math.abs(gyro.getAngle()-ANGLE_TO_HOPPER) > 10) {
  			if (team == "red") {
  				Robot.driveSubsystem.tank(-1, 1);
  			} else {
  				Robot.driveSubsystem.tank(1, -1);
  			}
    	}
		Robot.driveSubsystem.tank(0, 0);
		done = true;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (done) {
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
