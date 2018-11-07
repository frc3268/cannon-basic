package org.usfirst.frc.team3268.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

@SuppressWarnings("deprecation")
public class Robot extends SampleRobot {

	// sync ports to wiring
	// drive
	private static final int FRONT_LEFT_MOTOR_PORT = 0;
	private static final int BACK_LEFT_MOTOR_PORT = 1;
	private static final int FRONT_RIGHT_MOTOR_PORT = 8;
	private static final int BACK_RIGHT_MOTOR_PORT = 9;
	// solenoid
	private static final int OPEN_CANNON_SOLENOID_PORT = 0;
	private static final int CLOSE_CANNON_SOLENOID_PORT = 1;

	// controls
	private static final int FIRING_BUTTON = 1;

	// objects used for operation
	private DifferentialDrive drive;
	private Solenoid cannon;
	private Compressor compressor;
	private Joystick stick = new Joystick(0);

	public Robot() {

		// create motors
		SpeedController frontLeftMotor = new Talon(FRONT_LEFT_MOTOR_PORT);
		SpeedController backLeftMotor = new Talon(BACK_LEFT_MOTOR_PORT);
		SpeedController frontRightMotor = new Talon(FRONT_RIGHT_MOTOR_PORT);
		SpeedController backRightMotor = new Talon(BACK_RIGHT_MOTOR_PORT);
		// create sides
		SpeedControllerGroup leftSide = new SpeedControllerGroup(frontLeftMotor, backLeftMotor);
		SpeedControllerGroup rightSide = new SpeedControllerGroup(frontRightMotor, backRightMotor);
		rightSide.setInverted(true); // adjust to taste
		// create total drive
		drive = new DifferentialDrive(leftSide, rightSide);
		drive.setExpiration(0.1);

		// declare cannon
		cannon = new Solenoid(OPEN_CANNON_SOLENOID_PORT, CLOSE_CANNON_SOLENOID_PORT);
	}

	public void operatorControl() {
		drive.setSafetyEnabled(true);
		compressor.start();
		while (isOperatorControl() && isEnabled()) {

			// Drive arcade style
			drive.arcadeDrive(-stick.getY(), stick.getX());

			// open cannon if the firing button is pressed
			cannon.set(!stick.getRawButton(FIRING_BUTTON));

			// The motors will be updated every 5ms
			Timer.delay(0.005);
		}
		compressor.stop();
	}

}
