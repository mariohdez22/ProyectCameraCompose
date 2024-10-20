package com.herz.proyectcameracompose.paginas

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.herz.proyectcameracompose.R
import com.herz.proyectcameracompose.routes.Routes
import com.herz.proyectcameracompose.ui.theme.Pink40
import com.herz.proyectcameracompose.ui.theme.Pink80
import com.herz.proyectcameracompose.ui.theme.ProyectCameraComposeTheme
import com.herz.proyectcameracompose.ui.theme.Purple40
import com.herz.proyectcameracompose.ui.theme.Purple80

@Composable
fun HomePage(
    navController: NavHostController
){
    val context = LocalContext.current
    var permission by remember { mutableStateOf(false) }

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
            permissions ->
                permission = permissions[Manifest.permission.READ_MEDIA_IMAGES] == true &&
                permissions[Manifest.permission.READ_MEDIA_VIDEO] == true &&
                permissions[Manifest.permission.CAMERA] == true

        if(!permission){
            Toast.makeText(
                context,
                "Se denego los permisos o limito el acceso",
                Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        VerifyPermissions(
            context,
            onSuccess = {
                permission = !permission
            },
            onFailed = {
                Toast.makeText(
                    context,
                    "Se necesita que el usuario acepte los permisos",
                    Toast.LENGTH_LONG)
                    .show()
            }
        )
    }

    Scaffold(
        topBar = { MainTopBar(context) },
        modifier = Modifier
    ) {
        paddingValues ->

        Card (
            colors = CardDefaults.cardColors(
                containerColor = Pink80,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 80.dp,
                    start = 20.dp,
                    end = 20.dp
                )
        ){

            Column(
                modifier = Modifier
                    .padding(
                        start = 15.dp,
                        end = 15.dp
                    )
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if(permission){
                    MainBodyPermissionsGranted {
                        navController.navigate(Routes.CameraPage.route)
                    }
                }else{
                    MainBodyPermissionsFailed {
                        requestPermissionsLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.CAMERA
                            )
                        )
                    }
                }
            }
        }



    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview(){
    ProyectCameraComposeTheme(dynamicColor = false) {
        HomePage(rememberNavController())
    }
}

//-----------------------------------------------------------------------------------------[TOP BAR]
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    context: Context
){

    TopAppBar(
        title = {
            Text(
                text = "ProyectCam",
                fontSize = 25.sp,
                fontWeight = FontWeight.W300
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    (context as? Activity)?.finishAffinity()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "close app"
                )
            }
        },
        modifier = Modifier
            .padding(bottom = 30.dp)
    )

}

//--------------------------------------------------------------------------------------------[BODY]

@Composable
fun MainBodyPermissionsFailed(
    onClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(0.75f),
            onClick = onClick,
            colors = ButtonDefaults.elevatedButtonColors(
                contentColor = Color.White,
                containerColor = colorResource(R.color.btnAccesoCamara)
            )
        ) {
            Text(
                text = "Acceder a la camara",
                fontSize = 22.sp,
                fontWeight = FontWeight.W300,
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = stringResource(R.string.notaAccesoRestringido),
            fontSize = 13.sp,
            fontWeight = FontWeight.W300,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
fun MainBodyPermissionsGranted(
    onClick: () -> Unit
){
    ElevatedButton(
        modifier = Modifier.fillMaxWidth(0.75f),
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            contentColor = Color.White,
            containerColor = colorResource(R.color.btnAccesoCamara)
        )
    ) {
        Text(
            text = "Entrar a la camara",
            fontSize = 22.sp,
            fontWeight = FontWeight.W300,
            textAlign = TextAlign.Center
        )

    }
}


//-------------------------------------------------------------------------------------[PERMISSIONS]

fun VerifyPermissions(context: Context, onSuccess: () -> Unit, onFailed: () -> Unit){

    if(ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
        onFailed()
    }else{
        onSuccess()
    }

}