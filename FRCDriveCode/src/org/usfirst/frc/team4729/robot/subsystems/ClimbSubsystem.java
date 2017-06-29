package org.usfirst.frc.team4729.robot.subsystems;

import org.usfirst.frc.team4729.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ClimbSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	CANTalon climber = new CANTalon(RobotMap.CLIMBER);
	public boolean slow = false;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void climb(double speed) {
    	climber.set(1-0.6*Math.abs(speed));
    }
    public void release(double speed) {
    	climber.set(-0.6 + 0.4*Math.abs(speed));
    }
    public void stopClimbing() {
    	climber.set(0);
    }
}