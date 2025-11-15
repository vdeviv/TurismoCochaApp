package com.example.turismoapp.feature.login.presentation

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
import com.example.turismoapp.feature.login.data.repository.AuthRepository
import com.example.turismoapp.feature.login.domain.usecase.SignUpUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth

// Colores (los mismos del LoginScreen)
val PrimaryPurple = Color(0xFF6B4EE6)
val BackgroundGray = Color(0xFFF8F9FA)
val TextGray = Color(0xFF6C757D)
val ErrorRed = Color(0xFFDC3545)
val SuccessGreen = Color(0xFF28A745)
val WarningYellow = Color(0xFFFFC107)

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

    // Estados locales para mostrar/ocultar contraseñas
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    // Validación en tiempo real del email
    var emailError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(uiState.formState.email) {
        emailError = if (uiState.formState.email.isNotEmpty()) {
            ValidateEmailUseCase().invoke(uiState.formState.email)
        } else null
    }

    // Validación en tiempo real de la contraseña
    var passwordError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(uiState.formState.password) {
        passwordError = if (uiState.formState.password.isNotEmpty()) {
            ValidatePasswordUseCase().invoke(uiState.formState.password)
        } else null
    }

    // Validación de coincidencia de contraseñas
    var passwordConfirmError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(uiState.formState.password, uiState.formState.passwordConfirm) {
        passwordConfirmError = if (uiState.formState.passwordConfirm.isNotEmpty()) {
            if (uiState.formState.password != uiState.formState.passwordConfirm) {
                "Las contraseñas no coinciden"
            } else null
        } else null
    }

    // Requisitos de contraseña
    val passwordRequirements = remember(uiState.formState.password) {
        listOf(
            PasswordRequirementItem("Mínimo 6 caracteres", uiState.formState.password.length >= 6),
            PasswordRequirementItem("Al menos una letra mayúscula", uiState.formState.password.any { it.isUpperCase() }),
            PasswordRequirementItem("Al menos una letra minúscula", uiState.formState.password.any { it.isLowerCase() }),
            PasswordRequirementItem("Al menos un número", uiState.formState.password.any { it.isDigit() })
        )
    }

    val allRequirementsMet = passwordRequirements.all { it.met }

    // Manejo de éxito
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            Toast.makeText(context, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show()
            onRegistrationSuccess()
        }
    }

    // Mostrar errores
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
            // Botón de retroceso
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

            // Logo o icono
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

            // Título
            Text(
                text = "Crear Cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212529)
            )

            Text(
                text = "Completa tus datos para registrarte",
                fontSize = 15.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Campo de email
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
                        if (emailError == null) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Email válido",
                                tint = SuccessGreen
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Email inválido",
                                tint = ErrorRed
                            )
                        }
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
                    unfocusedBorderColor = Color(0xFFDEE2E6),
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

            // Campo de contraseña
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
                                contentDescription = "Contraseña válida",
                                tint = SuccessGreen,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña",
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
                    unfocusedBorderColor = Color(0xFFDEE2E6),
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

            // Campo de confirmar contraseña
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
                            if (passwordConfirmError == null && uiState.formState.password == uiState.formState.passwordConfirm) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Las contraseñas coinciden",
                                    tint = SuccessGreen,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            } else if (passwordConfirmError != null) {
                                Icon(
                                    imageVector = Icons.Filled.Error,
                                    contentDescription = "Las contraseñas no coinciden",
                                    tint = ErrorRed,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                imageVector = if (showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (showConfirmPassword) "Ocultar contraseña" else "Mostrar contraseña",
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
                    unfocusedBorderColor = Color(0xFFDEE2E6),
                    errorBorderColor = ErrorRed
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (emailError == null && passwordError == null && passwordConfirmError == null) {
                            viewModel.onEvent(RegisterEvent.Submit)
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Tarjeta de requisitos de contraseña
            AnimatedVisibility(
                visible = uiState.formState.password.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
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
                                color = Color(0xFF212529)
                            )
                        }

                        passwordRequirements.forEach { requirement ->
                            PasswordRequirementRow(
                                text = requirement.text,
                                met = requirement.met
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        // Indicador de fortaleza
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
                            else -> TextGray
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fortaleza: ",
                                fontSize = 12.sp,
                                color = TextGray
                            )
                            Text(
                                text = strengthText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = strengthColor
                            )
                            Spacer(Modifier.width(8.dp))
                            LinearProgressIndicator(
                                progress = strength / 4f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp),
                                color = strengthColor,
                                trackColor = Color(0xFFE9ECEF)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Botón de registro
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple,
                    disabledContainerColor = Color(0xFFE9ECEF)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Crear Cuenta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Divider con texto
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFDEE2E6))
                Text(
                    text = "O regístrate con",
                    fontSize = 13.sp,
                    color = TextGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFDEE2E6))
            }

            Spacer(Modifier.height(24.dp))

            // Botón de Google
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Próximamente: Registro con Google", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFDEE2E6))
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null,
                    tint = Color(0xFFDB4437),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Continuar con Google",
                    color = Color(0xFF212529),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(32.dp))

            // Ya tienes cuenta
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "¿Ya tienes una cuenta? ",
                    color = TextGray,
                    fontSize = 14.sp
                )
                Text(
                    "Inicia sesión",
                    color = PrimaryPurple,
                    fontSize = 14.sp,
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (met) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (met) SuccessGreen else Color(0xFFDEE2E6),
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (met) SuccessGreen else TextGray
        )
    }
}