package org.usfirst.frc.team4729.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	Talon intakeMotor = new Talon(2);
	
	public void intake() {
		intakeMotor.set(1);
	}
	
	public void outake() {
		intakeMotor.set(-1);
	}
	
	public void stop() {
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}