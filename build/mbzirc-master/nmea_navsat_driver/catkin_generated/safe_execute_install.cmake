execute_process(COMMAND "/home/sophie/Documents/FRC2017/build/mbzirc-master/nmea_navsat_driver/catkin_generated/python_distutils_install.sh" RESULT_VARIABLE res)

if(NOT res EQUAL 0)
  message(FATAL_ERROR "execute_process(/home/sophie/Documents/FRC2017/build/mbzirc-master/nmea_navsat_driver/catkin_generated/python_distutils_install.sh) returned error code ")
endif()
