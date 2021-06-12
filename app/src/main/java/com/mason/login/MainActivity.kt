package com.mason.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.mason.login.ui.theme.LoginTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Layout()
        }
    }
}

@Composable
fun Layout() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LoginTheme {
        Column {
            TopAppBar(
                title = {
                    Text(text = "Compose Login")
                },
                actions = {
                    Text(text = "帮助", modifier = Modifier.padding(horizontal = 20.dp))
                }
            )
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                Content(onLoginAction = {
                    scope.launch {
                        snackbarHostState.showSnackbar("登录成功", actionLabel = "确定")
                    }
                })
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Snackbar(
                snackbarHostState = snackbarHostState,
                onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun Content(onLoginAction: () -> Unit) {
    var accountValue by rememberSaveable { mutableStateOf("") }
    var passwordValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxSize()
    ) {
        val passwordFocusRequest = remember { FocusRequester() }
        val localFocusManager = LocalFocusManager.current
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "密码登录",
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            value = accountValue,
            onValueChange = { accountValue = it },
            placeholder = {
                Text(text = "手机号或邮箱")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                passwordFocusRequest.requestFocus()
            })
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = passwordValue,
            onValueChange = { passwordValue = it },
            placeholder = { Text(text = "密码") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequest),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                localFocusManager.clearFocus()
                onLoginAction()
            })
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                localFocusManager.clearFocus()
                onLoginAction()
            },
            Modifier
                .fillMaxWidth(),
            enabled = passwordValue.isNotEmpty() && accountValue.isNotEmpty()
        ) {
            Text(text = "登录", fontSize = 20.sp)
        }
    }
}

@Composable
fun Snackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = "消失",
                                color = Color.White
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

@Preview
@Composable
fun PreviewLayout() {
    Layout()
}