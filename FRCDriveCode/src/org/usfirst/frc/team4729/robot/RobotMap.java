package org.usfirst.frc.team4729.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//Motor Maps
	public static int LEFT_FRONT_DRIVE = 3;
	public static int LEFT_BACK_DRIVE = 4;
	public static int RIGHT_FRONT_DRIVE = 1;
	public static int RIGHT_BACK_DRIVE = 2;
	
	
	public static int INTAKE = 5;
	public static int CLIMBER = 0;
	public static int PRELOADER_LEFT = 7;
	public static int PRELOADER_RIGHT = 8;
	public static int SHOOTER_LEFT = 9;
	public static int SHOOTER_RIGHT = 10;
	
	//Sensors
	public static int ENCODER_LEFT_A = 0;
	public static int ENCODER_LEFT_B = 1;
	public static int ENCODER_RIGHT_A = 2;
	public static int ENCODER_RIGHT_B = 3;
	public static int GYRO = 0;
	
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}