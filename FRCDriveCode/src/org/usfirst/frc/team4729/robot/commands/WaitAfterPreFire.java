package org.usfirst.frc.team4729.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WaitAfterPreFire extends Command {
	Timer timer;
	
	double TIME_TO_WAIT;

    public WaitAfterPreFire() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	timer = new Timer();
    	
    	TIME_TO_WAIT = 5;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (timer.get() < TIME_TO_WAIT) {
    		return false;
    	}
    	return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
