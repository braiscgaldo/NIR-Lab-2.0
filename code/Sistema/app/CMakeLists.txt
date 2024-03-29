# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
            src/main/cpp/pre-compile/dump_mat.c
            src/main/cpp/dlpspec_calib.c
            src/main/cpp/dlpspec_had_defs.h
            src/main/cpp/dlpspec_calib.h
            src/main/cpp/dlpspec_helper.h
            src/main/cpp/dlpspec_scan.h
            src/main/cpp/dlpspec_scan_col.c
            src/main/cpp/dlpspec_scan_col.h
            src/main/cpp/dlpspec_scan_had.c
            src/main/cpp/dlpspec_scan_had.h
            src/main/cpp/dlpspec_setup.h
            src/main/cpp/dlpspec_types.h
            src/main/cpp/dlpspec_util.c
            src/main/cpp/dlpspec_util.h
            src/main/cpp/dlpspec_scan.c
            src/main/cpp/dlpspec_helper.c
            src/main/cpp/tpl.c
            src/main/cpp/tpl.h
            src/main/cpp/interface.c )

add_library( # Sets the name of the library.
        interface

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/pre-compile/dump_mat.c
        src/main/cpp/dlpspec_calib.c
        src/main/cpp/dlpspec_had_defs.h
        src/main/cpp/dlpspec_calib.h
        src/main/cpp/dlpspec_helper.h
        src/main/cpp/dlpspec_scan.h
        src/main/cpp/dlpspec_scan_col.c
        src/main/cpp/dlpspec_scan_col.h
        src/main/cpp/dlpspec_scan_had.c
        src/main/cpp/dlpspec_scan_had.h
        src/main/cpp/dlpspec_setup.h
        src/main/cpp/dlpspec_types.h
        src/main/cpp/dlpspec_util.c
        src/main/cpp/dlpspec_util.h
        src/main/cpp/dlpspec_scan.c
        src/main/cpp/dlpspec_helper.c
        src/main/cpp/tpl.c
        src/main/cpp/tpl.h
        src/main/cpp/interface.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

target_link_libraries( # Specifies the target library.
        interface

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )