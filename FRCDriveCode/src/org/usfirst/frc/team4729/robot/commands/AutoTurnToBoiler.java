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
      double TOO_FAR = 10;
      double TOO_CLOSE = 10;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      boolean ifFinished = false;
      //data[0] is the distance, data[1] is the x position

      //get distance and x position
      while (ifFinished == false) {
        float[] distancex = Robot.tcpSubsystem.requestImageData();
        if (distancex == null) { // '==' = is
          Robot.driveSubsystem.arcade(0,1);
          continue;
        }
        float distance = distancex[0];
        float iamx = distancex[1];
        //if not in centre, turn
        float center = CAMERA_WIDTH/2;
        if (iamx > center-1 && iamx < center+1) {
          if (distance > TOO_FAR) { //if to far, go forwards
            Robot.driveSubsystem.arcade(1, 0);
          } else if (distance < TOO_CLOSE) { //if not close enough, go backwards
            Robot.driveSubsystem.arcade(-1,0);
          } else { //if close enough, stop
            Robot.driveSubsystem.arcade(0,0);
            ifFinished = true;
          }
        } else { //if not in center
          if (iamx < center+1) { //if facing left
            Robot.driveSubsystem.arcade(0,1);
          } else { //if facing right
            Robot.driveSubsystem.arcade(0,-1);
          }
        }
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
