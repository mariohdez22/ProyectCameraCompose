package com.herz.proyectcameracompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.herz.proyectcameracompose.paginas.CameraPage
import com.herz.proyectcameracompose.paginas.HomePage
import com.herz.proyectcameracompose.routes.Routes

@Composable
fun SetupNavGraph(
    navController: NavHostController
){

    NavHost(
        navController = navController,
        startDestination = Routes.MainPage.route
    ){
        composable(
            route = Routes.MainPage.route
        ){

            HomePage(navController)
        }
        composable(
            route = Routes.CameraPage.route
        ){
            CameraPage(navController)
        }
    }
}