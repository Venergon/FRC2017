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

public class AutoCrossLine extends Command {

	public AnalogGyro gyro;
	Timer timer;

	boolean done;
	boolean straight;
	
	String team;	

	float TIME_TO_CROSS_LINE;
	int ANGLE_STRAIGHT;

    public AutoCrossLine(String teamColour) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	done = false;
    	team = teamColour;

    	TIME_TO_CROSS_LINE = 3; // Change this
    	if (teamColour == "red") {
    		ANGLE_STRAIGHT = 0;
    	} else {
    		ANGLE_STRAIGHT = 180;
    	}
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
		while (Math.abs(gyro.getAngle()-ANGLE_STRAIGHT) < 10) {
			if (team == "red") {
				Robot.driveSubsystem.tank(1, 0.5);
			} else {
				Robot.driveSubsystem.tank(0.5, 1);
			}
		}
		timer.reset();
  		while (timer.get() < TIME_TO_CROSS_LINE) {
  			Robot.driveSubsystem.tank(1, 1);
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
