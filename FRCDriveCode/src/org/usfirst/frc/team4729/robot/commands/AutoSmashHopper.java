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

public class SmashHopper extends Command {

	Timer timer;

	boolean done;

	float TIME_SMASH;

    public SmashHopper() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	done = false;

    	TIME_SMASH = 1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveSubsystem.encoderReset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		while (timer.get() < TIME_SMASH) {
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
