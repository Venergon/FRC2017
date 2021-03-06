package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;
import org.usfirst.frc.team4729.robot.subsystems.FuelSubsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoShoot extends Command {

	Timer timer;
	double SHOOT_TIME;

    public AutoShoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	timer = new Timer();
    	SHOOT_TIME = 5;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.fuelSubsystem.startFire();
    	timer.start();
		while(timer.get() < SHOOT_TIME) {
			//wait
		}
		Robot.fuelSubsystem.stopFire();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (timer.get() > SHOOT_TIME) {
    		return true;
    	}
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.fuelSubsystem.stopPreFire();
    	Robot.fuelSubsystem.stopFire();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.fuelSubsystem.stopPreFire();
    	Robot.fuelSubsystem.stopFire();
    }
}
