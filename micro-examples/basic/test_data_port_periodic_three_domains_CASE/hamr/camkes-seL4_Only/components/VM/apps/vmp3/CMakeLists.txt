cmake_minimum_required(VERSION 3.8.2)

# This file will not be overwritten so is safe to edit

project(vmp3 C)

set(CMAKE_C_STANDARD 99)

add_subdirectory(${SB_Type_Library_DIR} SB_Type_Library)

add_executable(vmp3 vmp3.c)

target_link_libraries(vmp3
                      SB_Type_Library
                      -static-libgcc -static)