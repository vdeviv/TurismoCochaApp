package com.turismoapp.mayuandino.feature.login.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.turismoapp.mayuandino.ui.theme.PurpleMayu
import com.turismoapp.mayuandino.ui.theme.GrayText
import com.turismoapp.mayuandino.ui.theme.TextBlack
import com.turismoapp.mayuandino.ui.theme.WhiteBackground
import com.turismoapp.mayuandino.ui.theme.GreenMayu
import com.turismoapp.mayuandino.ui.theme.RedMayu
import com.turismoapp.mayuandino.ui.theme.YellowMayu

import com.turismoapp.mayuandino.feature.login.data.repository.AuthRepository
import com.turismoapp.mayuandino.feature.login.domain.usecase.SignUpUseCase
import com.turismoapp.mayuandino.feature.login.domain.usecase.ValidateEmailUseCase
import com.turismoapp.mayuandino.feature.login.domain.usecase.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth

val PrimaryPurple = PurpleMayu
val BackgroundGray = WhiteBackground
val TextGray = GrayText
val ErrorRed = RedMayu
val SuccessGreen = GreenMayu
val WarningYellow = YellowMayu


class RegisterViewModelFactory(
    private val signUpUseCase: SignUpUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                signUpUseCase = signUpUseCase,
                validateEmailUseCase = validateEmailUseCase,
                validatePasswordUseCase = ValidatePasswordUseCase()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun getRegisterViewModel(): RegisterViewModel {
    val firebaseAuth = FirebaseAuth.getInstance()
    val authRepository = AuthRepository(firebaseAuth)
    val validateEmailUseCase = ValidateEmailUseCase()
    val validatePasswordUseCase = ValidatePasswordUseCase()
    val signUpUseCase = SignUpUseCase(authRepository, validateEmailUseCase, validatePasswordUseCase)

    val factory = RegisterViewModelFactory(signUpUseCase, validateEmailUseCase)
    return viewModel(factory = factory)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = getRegisterViewModel(),
    onRegistrationSuccess: () -> Unit,
    onBackToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(uiState.formState.email) {
        emailError = if (uiState.formState.email.isNotEmpty()) {
            ValidateEmailUseCase().invoke(uiState.formState.email)
        } else null
    }

    var passwordError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(uiState.formState.password) {
        passwordError = if (uiState.formState.password.isNotEmpty()) {
            ValidatePasswordUseCase().invoke(uiState.formState.password)
        } else null
    }

    var passwordConfirmError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(uiState.formState.password, uiState.formState.passwordConfirm) {
        passwordConfirmError =
            if (uiState.formState.passwordConfirm.isNotEmpty() &&
                uiState.formState.password != uiState.formState.passwordConfirm
            )
                "Las contraseñas no coinciden"
            else null
    }

    val passwordRequirements = remember(uiState.formState.password) {
        listOf(
            PasswordRequirementItem("Mínimo 6 caracteres", uiState.formState.password.length >= 6),
            PasswordRequirementItem("Al menos una letra mayúscula", uiState.formState.password.any { it.isUpperCase() }),
            PasswordRequirementItem("Al menos una letra minúscula", uiState.formState.password.any { it.isLowerCase() }),
            PasswordRequirementItem("Al menos un número", uiState.formState.password.any { it.isDigit() })
        )
    }

    val allRequirementsMet = passwordRequirements.all { it.met }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            Toast.makeText(context, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show()
            onRegistrationSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackToLogin) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = PrimaryPurple
                    )
                }
            }

            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryPurple)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Crear Cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )

            Text(
                text = "Completa tus datos para registrarte",
                fontSize = 15.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )
            OutlinedTextField(
                value = uiState.formState.email,
                onValueChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                label = { Text("Correo electrónico") },
                placeholder = { Text("ejemplo@correo.com") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = if (emailError == null && uiState.formState.email.isNotEmpty()) SuccessGreen else TextGray
                    )
                },
                trailingIcon = {
                    if (uiState.formState.email.isNotEmpty()) {
                        Icon(
                            imageVector = if (emailError == null) Icons.Filled.CheckCircle else Icons.Filled.Error,
                            contentDescription = null,
                            tint = if (emailError == null) SuccessGreen else ErrorRed
                        )
                    }
                },
                isError = emailError != null,
                supportingText = {
                    AnimatedVisibility(
                        visible = emailError != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = emailError ?: "",
                            color = ErrorRed,
                            fontSize = 12.sp
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = GrayText.copy(alpha = 0.3f),
                    errorBorderColor = ErrorRed
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.formState.password,
                onValueChange = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
                label = { Text("Contraseña") },
                placeholder = { Text("Mínimo 6 caracteres") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = if (passwordError == null && uiState.formState.password.isNotEmpty()) SuccessGreen else TextGray
                    )
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (uiState.formState.password.isNotEmpty() && passwordError == null) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null,
                                tint = TextGray
                            )
                        }
                    }
                },
                isError = passwordError != null,
                supportingText = {
                    AnimatedVisibility(
                        visible = passwordError != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = passwordError ?: "",
                            color = ErrorRed,
                            fontSize = 12.sp
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = GrayText.copy(alpha = 0.3f),
                    errorBorderColor = ErrorRed
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.formState.passwordConfirm,
                onValueChange = { viewModel.onEvent(RegisterEvent.PasswordConfirmChanged(it)) },
                label = { Text("Confirmar contraseña") },
                placeholder = { Text("Escribe la misma contraseña") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LockOpen,
                        contentDescription = null,
                        tint = if (passwordConfirmError == null && uiState.formState.passwordConfirm.isNotEmpty()) SuccessGreen else TextGray
                    )
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (uiState.formState.passwordConfirm.isNotEmpty()) {
                            Icon(
                                imageVector =
                                    if (passwordConfirmError == null && uiState.formState.password == uiState.formState.passwordConfirm)
                                        Icons.Filled.CheckCircle
                                    else Icons.Filled.Error,
                                contentDescription = null,
                                tint =
                                    if (passwordConfirmError == null &&
                                        uiState.formState.password == uiState.formState.passwordConfirm
                                    ) SuccessGreen else ErrorRed,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                imageVector = if (showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null,
                                tint = TextGray
                            )
                        }
                    }
                },
                isError = passwordConfirmError != null,
                supportingText = {
                    AnimatedVisibility(
                        visible = passwordConfirmError != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = passwordConfirmError ?: "",
                            color = ErrorRed,
                            fontSize = 12.sp
                        )
                    }
                },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = GrayText.copy(alpha = 0.3f),
                    errorBorderColor = ErrorRed
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (
                            emailError == null &&
                            passwordError == null &&
                            passwordConfirmError == null
                        ) {
                            viewModel.onEvent(RegisterEvent.Submit)
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(
                visible = uiState.formState.password.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Requisitos de seguridad:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        passwordRequirements.forEach {
                            PasswordRequirementRow(text = it.text, met = it.met)
                        }

                        Spacer(Modifier.height(8.dp))

                        val strength = passwordRequirements.count { it.met }
                        val strengthText = when (strength) {
                            0, 1 -> "Débil"
                            2 -> "Regular"
                            3 -> "Buena"
                            4 -> "Excelente"
                            else -> ""
                        }
                        val strengthColor = when (strength) {
                            0, 1 -> ErrorRed
                            2 -> WarningYellow
                            3 -> Color(0xFF17A2B8)
                            4 -> SuccessGreen
                            else -> GrayText
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Fortaleza: ", fontSize = 12.sp, color = GrayText)
                            Text(text = strengthText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = strengthColor)

                            Spacer(Modifier.width(8.dp))

                            LinearProgressIndicator(
                                progress = strength / 4f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp),
                                color = strengthColor,
                                trackColor = Color(0xFFE0E0E0)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(RegisterEvent.Submit)
                },
                enabled = !uiState.isLoading &&
                        uiState.formState.email.isNotEmpty() &&
                        uiState.formState.password.isNotEmpty() &&
                        uiState.formState.passwordConfirm.isNotEmpty() &&
                        emailError == null &&
                        passwordError == null &&
                        passwordConfirmError == null &&
                        allRequirementsMet,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Filled.PersonAdd, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Crear Cuenta", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes una cuenta? ", color = TextGray)
                Text(
                    "Inicia sesión",
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

data class PasswordRequirementItem(
    val text: String,
    val met: Boolean
)

@Composable
fun PasswordRequirementRow(text: String, met: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(
            imageVector = if (met) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (met) SuccessGreen else GrayText.copy(alpha = 0.3f),
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text, fontSize = 12.sp, color = if (met) SuccessGreen else GrayText)
    }
}
