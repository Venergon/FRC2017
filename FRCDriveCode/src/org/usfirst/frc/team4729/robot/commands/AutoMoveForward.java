package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoMoveForward extends Command {


	float DISTANCE_MOVE_FORWARD;

    public AutoMoveForward() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveSubsystem.encoderReset();
    	DISTANCE_MOVE_FORWARD = 3; // Change this -Luke Fisk-Lennon
			while ((Robot.driveSubsystem.leftDistance()+Robot.driveSubsystem.rightDistance())/2 < DISTANCE_MOVE_FORWARD) {
				Robot.driveSubsystem.tank(1,1);
			}
			Robot.driveSubsystem.tank(0,0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if ((Robot.driveSubsystem.leftDistance() + Robot.driveSubsystem.rightDistance())/2 > DISTANCE_MOVE_FORWARD) {
			return true;
    	}
		return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveSubsystem.tank(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.driveSubsystem.tank(0,0);
    }
}
