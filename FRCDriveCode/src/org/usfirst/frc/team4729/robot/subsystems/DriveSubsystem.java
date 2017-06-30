package org.usfirst.frc.team4729.robot.subsystems;

import org.usfirst.frc.team4729.robot.Robot;
import org.usfirst.frc.team4729.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class DriveSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Encoder leftEncoder;
    Encoder rightEncoder;
    CANTalon driveTrainLeftFront = new CANTalon(RobotMap.LEFT_FRONT_DRIVE);
    CANTalon driveTrainLeftBack = new CANTalon(RobotMap.LEFT_BACK_DRIVE);
    CANTalon driveTrainRightFront = new CANTalon(RobotMap.RIGHT_FRONT_DRIVE);
    CANTalon driveTrainRightBack = new CANTalon(RobotMap.RIGHT_BACK_DRIVE);
    public AnalogGyro gyro;

	double leftSpeed = 0;
	double rightSpeed = 0;
	double turnSpeed = 0;
	double moveSpeed = 0;
	double leftFinal = 0;
	double rightFinal = 0;
	double moveFinal = 0;
	double turnFinal = 0;
    
	static double DEADZONE = 0.15;
	static double ACCELERATION = 0.15;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	gyro = new AnalogGyro(RobotMap.GYRO);
    	gyro.calibrate();

    	driveTrainLeftFront.changeControlMode(TalonControlMode.PercentVbus);
    	driveTrainLeftBack.changeControlMode(TalonControlMode.PercentVbus);
    	driveTrainRightFront.changeControlMode(TalonControlMode.PercentVbus);
    	driveTrainRightBack.changeControlMode(TalonControlMode.PercentVbus);
        leftEncoder = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B, false, Encoder.EncodingType.k4X);
        rightEncoder = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B, false, Encoder.EncodingType.k4X);

    }

    public double leftDistance() {
        return leftEncoder.getDistance();
    }

    public double rightDistance() {
        return rightEncoder.getDistance();
    }

    public void encoderReset() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void arcade(double desiredMove, double desiredTurn) {
    	if (Math.abs(desiredMove) < Math.abs(moveSpeed)){
    		moveSpeed = desiredMove;
    	}
    	
    	if (Math.abs(desiredTurn) < Math.abs(turnSpeed)) {
    		turnSpeed = desiredTurn;
    	}
    	
    	turnSpeed += (desiredTurn-turnSpeed)*ACCELERATION;
    	moveSpeed += (desiredMove-moveSpeed)*ACCELERATION;
    	
    	if (Math.abs(moveSpeed) > DEADZONE) {
    		moveFinal = moveSpeed*0.7+moveSpeed/Math.abs(moveSpeed)*0.3;
    		turnFinal = turnSpeed/Math.abs(turnSpeed)*Math.sqrt(Math.abs(turnSpeed));
    	}
    	else  if (Math.abs(turnSpeed) > DEADZONE) {
    		moveFinal = 0;
    		turnFinal = turnSpeed*0.7+turnSpeed/Math.abs(turnSpeed)*0.3;
    	}
    	else {
    		moveFinal = 0;
    		turnFinal = 0;
    	}
    	
    	moveFinal = moveSpeed;
    	turnFinal = turnSpeed;
    	
    	if (Robot.flipped) {
    		driveTrainLeftFront.set(moveFinal + -turnFinal);
    		driveTrainLeftBack.set(moveFinal + -turnFinal);
    		driveTrainRightFront.set(-moveFinal - (-turnFinal));
    		driveTrainRightBack.set(-moveFinal - (-turnFinal));
    	} else {
    		driveTrainLeftFront.set(-moveFinal + turnFinal);
    		driveTrainLeftBack.set(-moveFinal + turnFinal);
    		driveTrainRightFront.set(moveFinal - (-turnFinal));
    		driveTrainRightBack.set(moveFinal - (-turnFinal));
    	}
    }
      
    public void tank (double desiredLeft, double desiredRight) {
    	if (Math.abs(desiredLeft) < Math.abs(leftSpeed)){
    		leftSpeed = desiredLeft;
    	}
    	
    	if (Math.abs(desiredRight) < Math.abs(rightSpeed)) {
    		rightSpeed = desiredRight;
    	}
    	
    	rightSpeed += (desiredRight-rightSpeed)*ACCELERATION;
    	leftSpeed += (desiredLeft-leftSpeed)*ACCELERATION;
    	
    	if (Math.abs(leftSpeed) > DEADZONE) {
    		leftFinal = leftSpeed*0.7+leftSpeed/Math.abs(leftSpeed)*0.3;
    	}
    	if (Math.abs(rightSpeed) > DEADZONE) {
    		rightFinal = rightSpeed*0.7+rightSpeed/Math.abs(rightSpeed)*0.3;
    	}
    	
    	if (Robot.flipped) {
    		driveTrainLeftFront.set(leftFinal);
    		driveTrainLeftBack.set(leftFinal);
    		driveTrainRightFront.set(-rightFinal);
    		driveTrainRightBack.set(-rightFinal);
    	} else {
    		driveTrainLeftFront.set(-rightFinal);
    		driveTrainLeftBack.set(-rightFinal);
    		driveTrainRightFront.set(leftFinal);
    		driveTrainRightBack.set(leftFinal);
    	}
	}
}
