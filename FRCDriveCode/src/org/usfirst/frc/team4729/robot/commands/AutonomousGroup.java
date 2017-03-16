package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousGroup extends CommandGroup {
	Timer timer;

    public AutonomousGroup(String mode, String team) {
    	if (mode.equals("shoot_then_hopper")) {
    		addSequential(new PreFire());
    		addSequential(new WaitAfterPreFire());
    		addSequential(new AutoShoot());
	    	addSequential(new BasicAutoMoveToHopperWithoutGyro(team, false));
    	} else if (mode.equals("hopper_then_shoot")) {
    		addSequential(new PreFire());
    		addSequential(new WaitAfterPreFire());
			addSequential(new BasicAutoMoveToHopperWithoutGyro(team, true));
			addSequential(new SmashHopper());
			addSequential(new AutoTurnToBoiler());
			addSequential(new AutoShoot());
    	} else if (mode.equals("just_hopper")) {
    		addSequential(new BasicAutoMoveToHopperWithoutGyro(team, true));
    	}
      // Add Commands here:
      // e.g. addSequential(new Command1());
      //      addSequential(new Command2());
      // these will run in order.

      // To run multiple commands at the same time,
      // use addParallel()
      // e.g. addParallel(new Command1());
      //      addSequential(new Command2());
      // Command1 and Command2 will run in parallel.

      // A command group will require all of the subsystems that each member
      // would require.
      // e.g. if Command1 requires chassis, and Command2 requires arm,
      // a CommandGroup containing them would require both the chassis and the
      // arm.
    }
}
