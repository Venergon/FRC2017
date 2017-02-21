package org.usfirst.frc.team4729.robot.subsystems;

import org.usfirst.frc.team4729.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class FuelSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	Talon preloader = new Talon(RobotMap.PRELOADER);
	Talon shooter = new Talon(RobotMap.SHOOTER);
	Talon intake = new Talon(RobotMap.INTAKE);
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void intake() {
    	intake.set(-1);
    }
    
    public void stopIntake() {
    	intake.set(0);
    }
    
    public void startShooting() {
    	shooter.set(0.75);
    	preloader.set(1);
    }
    
    public void stopShooting() {
    	shooter.set(0);
    	preloader.set(0);
    }
}