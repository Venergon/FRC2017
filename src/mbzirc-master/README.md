# mbzirc
Setting up (including change an existing development environment to the new build system):

- Pull this repo, cd to mbz\_utils
- Run git remote prune origin
- Update .bashrc with export MBZ\_WS=/home/mbzirc/catkin\_ws 
- Run cp .bash\_aliases ~/
- Run unalias -a
- Run source ~/.bashrc
- Run ./install\_deb\_repo.sh
- Run ./install\_deps.sh
- Run ./pull\_required\_repos.sh
- Run ./setup\_build
- Run catkin clean
- Run mbz\_make

In general use mbz\_make to build all relevant packages. Use mbz\_make <package\_name> to build one package.

*Do not use catkin\_make*

Use catkin profile set <profile\_name> to change build profiles:

- all: build all packages (not recommended)
- default: builds all packages except debs, and defunct packages (recommended for development)
- platform: as with default but without simulation packages (recommended for vehicles)

# deb packages
Some packages (velodyne, mavros, libuvc, usb\_cam, rtklib, compressed\_image\_transport) are not built by default. But are distributed internally from mtrnlab2 as binary deb packages.
To edit one of these packages:

- Develop code
- To test, compile with mbz\_make <package\_name>
- If you recompile a deb intended package in your workspace like this, procmon will need to be restarted to run the newly built binaries, rather than the ones in /opt/ros/

When ready to deploy:

- Run mbz\_utils/make\_debs <path/to/package/dir>
- Run install\_deps on clients
- catkin clean (need to clear the workspace of the devel and build files for this package. catkin clean <packagename> does not seem to be enough)
- mbz\_make

# FAQ
- I'd like to add a new package - Create a new directory in this repo.
- I've modified an open source driver/package - Fork the open source repo using agvc-unsw-2 organisation and add the new repo to the pull\_required\_repos.sh script.
- I'd like to add a new feature - Create a new branch (git checkout -b new\_branch\_name), push to that branch until it is tested/ready (git push -u origin new\_branch\_name) then merge into master (whilst on master, git merge new\_branch\_name).

# V-REP simulation
How to integrate ros with V-REP:

- Assumes you've got V-REP runnning (Downloaded/installed V-REP from http://www.coppeliarobotics.com/downloads.html) and ROS indigo installed.
- Build
- Copy libv\_repExtRos.so and libv\_repExtRosSkeleton.so from <catkin\_workspace>/devel/lib to the root of your V-REP install directory
- Soft link the couch plugin to the root of your V-REP install (ln -s <catkin\_workspace>/build/mbzirc/mbz\_simulation/vrep\_pepper\_bridge/vrep\_pepper\_bridge <V\_REP\_install\_root>/vrep\_pepper\_bridge)
- Start roscore
- Start vrep (in the start up text it should say "Plugin 'Ros': load succeeded.", if not then something is broken)
- Open the mbz\_simulation/scenes/test\_pepper.ttt scene in V-REP
- Start the simulation, pepper moves with Twist commands (a rqt virtual joystick: rosrun rqt\_robot\_steering rqt\_robot\_steering)
