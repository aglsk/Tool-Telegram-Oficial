package org.gampiot.tooltelegram

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut

import org.koin.androidx.compose.koinViewModel

import org.gampiot.tooltelegram.R
import org.gampiot.tooltelegram.ui.theme.AntSummerTheme
import org.gampiot.tooltelegram.ui.components.ApplicationScreen
import org.gampiot.tooltelegram.ui.components.appbars.TopBar
import org.gampiot.tooltelegram.ui.components.cards.SimpleCard
import org.gampiot.tooltelegram.ui.screens.sendgroup.SendGroupMessageScreen
import org.gampiot.tooltelegram.ui.screens.sendcommunity.SendCommunityMessageScreen
import org.gampiot.tooltelegram.ui.screens.preferences.SettingsScreen
import org.gampiot.tooltelegram.ui.screens.preferences.LibrariesScreen
import org.gampiot.tooltelegram.ui.viewmodels.preferences.d.AppPreferencesViewModel

class MainActivity : ComponentActivity() {

    companion object {
         const val MSAX_SLIDE_DISTANCE: Int = 100
         const val MSAX_DURATION: Int = 700
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appPrefsViewModel = koinViewModel<AppPreferencesViewModel>()
            val isUseMonet by appPrefsViewModel.isUseMonet.collectAsState(initial = true)
            val isUseHighContrast by appPrefsViewModel.isUseHighContrast.collectAsState(initial = false)
            AntSummerTheme(
               highContrastDarkTheme = isUseHighContrast,
               dynamicColor = isUseMonet
            ) {
                 val navController = rememberNavController()
                 NavHost(
                     navController = navController,
                     startDestination = "main",
                     enterTransition = { 
                          materialSharedAxisXIn(
                              forward = true, 
                              slideDistance = MSAX_SLIDE_DISTANCE,
                              durationMillis = MSAX_DURATION
                          ) 
                     },
                     exitTransition = { 
                          materialSharedAxisXOut(
                              forward = true, 
                              slideDistance = MSAX_SLIDE_DISTANCE,
                              durationMillis = MSAX_DURATION
                          ) 
                     },
                     popEnterTransition = { 
                          materialSharedAxisXIn(
                              forward = false, 
                              slideDistance = MSAX_SLIDE_DISTANCE,
                              durationMillis = MSAX_DURATION
                          ) 
                     },
                     popExitTransition = { 
                          materialSharedAxisXOut(
                              forward = false,
                              slideDistance = MSAX_SLIDE_DISTANCE,
                              durationMillis = MSAX_DURATION
                          ) 
                     }
                 ) {
                      composable("main") {
                           MainScreen(
                                onSendCommunityMessageClicked = {
                                       navController.navigate("sendChannel")
                                },
                                onSendGroupMessageClicked = {
                                      navController.navigate("sendGroup")
                                },
                                onSettingsClicked = {
                                      navController.navigate("settings")
                                }
                           )
                      }
                      
                      composable("sendGroup") {
                           SendGroupMessageScreen(navController)
                      }
                      
                      composable("sendChannel") {
                           SendCommunityMessageScreen(navController)
                      }
                      
                      composable("settings") {
                           SettingsScreen(navController)
                      }
                      
                      composable("settings/libraries") {
                           LibrariesScreen(navController)
                      }
                 }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSendCommunityMessageClicked: () -> Unit,
    onSendGroupMessageClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    val btnModifier = Modifier.fillMaxWidth()
    ApplicationScreen(
          modifier = Modifier
              .padding(start = 10.dp, end = 10.dp)
              .fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          topBar = {
               TopBar(
                    barTitle = stringResource(id = R.string.app_name),
                    scrollBehavior = it,
                    icon = Icons.Outlined.Settings,
                    onClickIcon = onSettingsClicked
               )
          },
          content = { 
               Column(
                    modifier = Modifier
                         .fillMaxSize()
               ) {
                   SimpleCard(
                       modifier = Modifier,
                       stringResource(id = R.string.app_name),
                       content = {
                             Column {
                                  Spacer(modifier = Modifier.weight(1F))
                                  Button(
                                       modifier = btnModifier,
                                       onClick = onSendGroupMessageClicked
                                  ) {
                                       Text(
                                           text = stringResource(id = R.string.send_group_message)
                                       )
                                  }
                                  Spacer(modifier = Modifier.weight(1F))
                                  Button(
                                       modifier = btnModifier,
                                       onClick = onSendCommunityMessageClicked
                                  ) {
                                       Text(
                                           text = stringResource(id = R.string.send_community_message)
                                       )
                                  }
                             }
                       }
                   )
               }
          }
    )
}
