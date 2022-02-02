package com.matty.birthdays.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matty.birthdays.R
import kotlinx.coroutines.delay

@Composable
fun LaunchScreen(
    delayMillis: Long,
    onDelayFinished: () -> Unit
) {
    var isOpaque by remember { mutableStateOf(value = false) }

    val alpha: Float by animateFloatAsState(
        targetValue = if (isOpaque) 1f else 0.2f,
        animationSpec = tween(durationMillis = 700)
    )

    LaunchedEffect(Unit) {
        isOpaque = true
        delay(delayMillis)
        onDelayFinished()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors
                    .primaryVariant
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.birthday_cake),
            contentDescription = stringResource(R.string.birthday_cake),
            modifier = Modifier
                .weight(1f)
                .alpha(alpha)
                .testTag("image")
        )
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .testTag("appName"),
            style = MaterialTheme.typography.subtitle1.copy(
                letterSpacing = 4.sp
            ),
            color = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchScreenPreview() {
    LaunchScreen(
        delayMillis = 0
    ) {}
}