#!/bin/sh

if [ -n "$DESTDIR" ] ; then
    case $DESTDIR in
        /*) # ok
            ;;
        *)
            /bin/echo "DESTDIR argument must be absolute... "
            /bin/echo "otherwise python's distutils will bork things."
            exit 1
    esac
    DESTDIR_ARG="--root=$DESTDIR"
fi

echo_and_run() { echo "+ $@" ; "$@" ; }

echo_and_run cd "/home/sophie/Documents/FRC2017/src/mbzirc-master/nmea_navsat_driver"

# snsure that Python install destination exists
echo_and_run mkdir -p "$DESTDIR/home/sophie/Documents/FRC2017/install/lib/python2.7/dist-packages"

# Note that PYTHONPATH is pulled from the environment to support installing
# into one location when some dependencies were installed in another
# location, #123.
echo_and_run /usr/bin/env \
    PYTHONPATH="/home/sophie/Documents/FRC2017/install/lib/python2.7/dist-packages:/home/sophie/Documents/FRC2017/build/lib/python2.7/dist-packages:$PYTHONPATH" \
    CATKIN_BINARY_DIR="/home/sophie/Documents/FRC2017/build" \
    "/usr/bin/python" \
    "/home/sophie/Documents/FRC2017/src/mbzirc-master/nmea_navsat_driver/setup.py" \
    build --build-base "/home/sophie/Documents/FRC2017/build/mbzirc-master/nmea_navsat_driver" \
    install \
    $DESTDIR_ARG \
    --install-layout=deb --prefix="/home/sophie/Documents/FRC2017/install" --install-scripts="/home/sophie/Documents/FRC2017/install/bin"
