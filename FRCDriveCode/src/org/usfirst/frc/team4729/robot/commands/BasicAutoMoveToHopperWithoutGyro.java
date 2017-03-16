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

public class BasicAutoMoveToHopperWithoutGyro extends Command {

	Timer timer;

	boolean done;
	boolean straight;
	
	String team;

	double TIME_MOVE_BACK;
	double TIME_TO_HOPPER;
	double TIME_TO_CURVE;
	double TIME_TO_TURN;

    public BasicAutoMoveToHopperWithoutGyro(String teamColour, boolean isStraight) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	done = false;
    	straight = isStraight;

    	TIME_MOVE_BACK = 1; // Change this
    	TIME_TO_HOPPER = 3; // Change this
    	TIME_TO_CURVE = 2; // Change this
    	TIME_TO_TURN = 2;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveSubsystem.encoderReset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (!straight) {
			while (timer.get() < TIME_MOVE_BACK) {
				Robot.driveSubsystem.tank(-1,-1);
			}
			timer.reset();
			while (timer.get() < TIME_TO_CURVE) {
	    		Robot.driveSubsystem.tank(1, 0.5);
			}
    	}
		timer.reset();
  		while (timer.get() < TIME_TO_HOPPER) {
  			Robot.driveSubsystem.tank(1, 1);
		}
  		timer.reset();
  		while (timer.get() < TIME_TO_TURN) {
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
