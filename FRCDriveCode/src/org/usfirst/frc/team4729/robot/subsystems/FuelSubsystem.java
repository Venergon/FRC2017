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
	Talon preloaderLeft = new Talon(RobotMap.PRELOADER_LEFT);
	Talon preloaderRight = new Talon(RobotMap.PRELOADER_RIGHT);
	Talon shooterLeft = new Talon(RobotMap.SHOOTER_LEFT);
	Talon shooterRight = new Talon(RobotMap.SHOOTER_RIGHT);
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

	public void startFire() {
		preloaderLeft.set(1);
		preloaderRight.set(1);
	}

	public void stopFire() {
		preloaderLeft.set(0);
		preloaderRight.set(0);
	}

    public void startPreFire() {
    	shooterLeft.set(0.5);
    	shooterRight.set(0.5);
    }

    public void stopPreFire() {
    	shooterLeft.set(0);
    	shooterRight.set(0);
    }
}
