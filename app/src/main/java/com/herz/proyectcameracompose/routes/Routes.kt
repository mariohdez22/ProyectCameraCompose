package com.herz.proyectcameracompose.routes

const val ROOT_HOME_PAGE = "home"
const val ROOT_CAMERA_PAGE = "camera"

sealed class Routes(
    val route: String
){
    object MainPage : Routes(route = ROOT_HOME_PAGE)
    object CameraPage : Routes(route = ROOT_CAMERA_PAGE)
}