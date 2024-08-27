option(BUILD_CROSSVM
       "Checkout and configure linux to build crossvm module instead of using pre-configured rootfs"
       OFF)

if("$ENV{BUILD_CROSSVM}" STREQUAL "ON")
   set(BUILD_CROSSVM ON)
endif()