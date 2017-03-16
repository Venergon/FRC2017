
package org.usfirst.frc.team4729.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4729.robot.commands.AutonomousGroup;
import org.usfirst.frc.team4729.robot.commands.TestNucOutput;
import org.usfirst.frc.team4729.robot.commands.TwoStickArcade;
import org.usfirst.frc.team4729.robot.subsystems.ClimbSubsystem;
import org.usfirst.frc.team4729.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4729.robot.subsystems.FuelSubsystem;
//import org.usfirst.frc.team4729.robot.subsystems.TCPSubsystem;
import org.usfirst.frc.team4729.robot.subsystems.TCPSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static DriveSubsystem driveSubsystem;
	public static TCPSubsystem tcpSubsystem;
	public static ClimbSubsystem climbSubsystem;
	public static FuelSubsystem fuelSubsystem;
	public static OI oi;
	public static int BLUE;
	public static int RED;
	
	public static SendableChooser<String> teamChooser;
	public static String team;
	
	public static SendableChooser<String> autoChooser;
	public static String autoMode;
	
	public static SendableChooser<String> nucConnectedChooser;
	public static String nucConnected;
	
	public static boolean flipped;

    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	driveSubsystem = new DriveSubsystem();
    	climbSubsystem = new ClimbSubsystem();
    	fuelSubsystem = new FuelSubsystem();
		oi = new OI();
		flipped = false;
		
		BLUE = 0;
		RED = 1;
		
		teamChooser = new SendableChooser<String>();
		teamChooser.addDefault("Red", "red");
		teamChooser.addDefault("Blue", "blue");
		
		autoChooser = new SendableChooser<String>();
		autoChooser.addDefault("Shoot, then move to hopper", "shoot_then_hopper");
		autoChooser.addObject("Smash that hopper, then shoot", "hopper_then_shoot");
		autoChooser.addObject("Just go to the hopper", "just_hopper");
		SmartDashboard.putData("automode", autoChooser);
		
		nucConnectedChooser = new SendableChooser<String>();
		nucConnectedChooser.addDefault("Yes", "yes");
		nucConnectedChooser.addObject("No", "no");
		SmartDashboard.putData("nucConnected?", nucConnectedChooser);
        // instantiate the command used for the autonomous period
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
    	String mode = (String) autoChooser.getSelected();
    	String team = (String) teamChooser.getSelected();
    	autonomousCommand = new AutonomousGroup(mode,team);
    	nucConnected = (String) nucConnectedChooser.getSelected();
    	SmartDashboard.putString("nucConnected", nucConnected);
    	tcpSubsystem = new TCPSubsystem(1917);
   	
    	TestNucOutput testNucOutput = new TestNucOutput();
    	testNucOutput.start();
    	if (autonomousCommand != null) autonomousCommand.start();
        //if (autonomousCommand != null) autonomousCommand.start(BLUE);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
//    	nucConnected = (boolean) nucConnectedChooser.getSelected();
//    	SmartDashboard.putBoolean("nucConnected", nucConnected);
//    	if (tcpSubsystem == null) {
//    		tcpSubsystem = new TCPSubsystem(1917);
//    	}


		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        Joystick leftStick = new Joystick(0);
        Joystick rightStick = new Joystick(1);
        TwoStickArcade twoStickArcade = new TwoStickArcade(leftStick, rightStick);
        twoStickArcade.start();
        
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
