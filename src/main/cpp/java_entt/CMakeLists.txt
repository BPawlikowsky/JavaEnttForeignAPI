cmake_minimum_required(VERSION 3.0.0)
##! C++ Standard needed by the SDK is 17
set(CMAKE_CXX_STANDARD 17)
project(java_entt VERSION 0.1.0)

set(CMAKE_PREFIX_PATH /home/bartsky/dev/entt)
set(CMAKE_INCLUDE_PATH /usr/lib/jvm/java-17-jdk/include/linux)
set(CMAKE_INCLUDE_PATH /usr/lib/jvm/java-17-jdk/include)


include(CTest)
enable_testing()


find_package(EnTT REQUIRED)

include_directories(/usr/lib/jvm/java-17-jdk/include/)
include_directories(/usr/lib/jvm/java-17-jdk/include/linux)
add_library(java_entt_registry SHARED src/java_entt_registry.cpp)

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
