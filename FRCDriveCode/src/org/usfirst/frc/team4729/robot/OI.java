package org.usfirst.frc.team4729.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team4729.robot.commands.OneStickArcade;
import org.usfirst.frc.team4729.robot.commands.OneStickTank;
import org.usfirst.frc.team4729.robot.commands.ReceiveTCPPacket;
import org.usfirst.frc.team4729.robot.commands.SendUDPPacket;
import org.usfirst.frc.team4729.robot.commands.TwoStickArcade;
import org.usfirst.frc.team4729.robot.commands.TwoStickTank;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

/* Includes all the buttons for 2 joysticks and one xbox controller as well as one/two stick arcade/tank
 * Put all commands on whichever button you want to activate it, using whenPressed or whileHeld depending on which behaviour you want to cause it
 */

 /**
 * 
 * @author FRC
 *
 */
public class OI {
	Joystick leftStick   = new Joystick(0);
    Joystick rightStick  = new Joystick(1);
    Joystick xbox        = new Joystick(2);
    Button button1       = new JoystickButton(leftStick,  1);
    Button button2       = new JoystickButton(leftStick,  2);
    Button button3       = new JoystickButton(leftStick,  3);
    Button button4       = new JoystickButton(leftStick,  4);
    Button button5       = new JoystickButton(leftStick,  5);
    Button button6       = new JoystickButton(leftStick,  6);
    Button button7       = new JoystickButton(leftStick,  7);
    Button button8       = new JoystickButton(leftStick,  8);
    Button button9       = new JoystickButton(leftStick,  9);
    Button button10      = new JoystickButton(leftStick,  10);
    Button button11      = new JoystickButton(leftStick,  11);
    Button rightButton1  = new JoystickButton(rightStick, 1);
    Button rightButton2  = new JoystickButton(rightStick, 2);
    Button rightButton3  = new JoystickButton(rightStick, 3);
    Button rightButton4  = new JoystickButton(rightStick, 4);
    Button rightButton5  = new JoystickButton(rightStick, 5);
    Button rightButton6  = new JoystickButton(rightStick, 6);
    Button rightButton7  = new JoystickButton(rightStick, 7);
    Button rightButton8  = new JoystickButton(rightStick, 8);
    Button rightButton9  = new JoystickButton(rightStick, 9);
    Button rightButton10 = new JoystickButton(rightStick, 10);
    Button rightButton11 = new JoystickButton(rightStick, 11);
    Button a             = new JoystickButton(xbox,       1);
    Button b             = new JoystickButton(xbox,       2);
    Button x             = new JoystickButton(xbox,       3);
    Button y             = new JoystickButton(xbox,       4);
    Button lb            = new JoystickButton(xbox,       5);
    Button rb            = new JoystickButton(xbox,       6);
    Button start		 = new JoystickButton(xbox,       7);
    Button select        = new JoystickButton(xbox,       8);
    Button l3            = new JoystickButton(xbox,       9);
    Button r3            = new JoystickButton(xbox,       10);
    
    public OI() {
    	//button1.whileHeld  ();
    	button2.whenPressed (new OneStickTank(leftStick));
    	button3.whenPressed (new TwoStickTank(leftStick, rightStick));
    	button4.whenPressed (new OneStickArcade(leftStick));
    	button5.whenPressed (new TwoStickArcade(leftStick, rightStick));
    	button6.whileHeld (new ReceiveTCPPacket());
    	button7.whileHeld (new SendUDPPacket());
    	//button8.whileHeld ();
    	//button9.whileHeld ();
    	//button10.whileHeld ();
    	//button11.whileHeld ();
    	//rightButton1.whenPressed ();
    	//rightButton2.whileHeld ();
    	//rightButton3.whenPressed ();
    	//rightButton4.whileHeld ();
    	//rightButton5.whileHeld ();
    	//rightButton6.whileHeld ();
    	//rightButton7.whileHeld ();
    	//rightButton8.whileHeld ();
    	//rightButton9.whileHeld ();
    	//rightButton10.whileHeld ();
    	//rightButton11.whileHeld ();
    	//a.whileHeld ();
    	//b.whileHeld ();
    	//x.whileHeld ();
    	//y.whileHeld ();
    	//lb.whileHeld ();
    	//rb.whileHeld ();
    	//start.whileHeld();
    	//select.whileHeld();
    	//l3.whileHeld ();
    	//r3.whileHeld ();
    	
    	
    }
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

