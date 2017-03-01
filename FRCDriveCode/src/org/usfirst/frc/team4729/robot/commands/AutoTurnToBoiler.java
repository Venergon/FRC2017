package org.usfirst.frc.team4729.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurnToBoiler extends Command {

    public AutoTurnToBoiler() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    //go forward, turn left, go forward, turn intill see the boiler
    //data[0] is the distance, data[1] is the x position
    //get distance and x position
    float[] distrancex = Robot.tcpSubsystem.requestImageData();
    float distrance = distrancex[0];
    float iamx = distrancex[1];
    //if not in centre, turn
    float center = CAMERA_WIDTH/2;
    if(distrance > center-1 && distrance < center+1) {
    }
    //if not close enough, go forward
    //if close enough, stop
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
