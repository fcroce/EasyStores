package com.fcroce.easystores.components.camera

import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageProxy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@Composable
fun CameraPreviewWithImageAnalysis(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController,
    onFrameAnalyzed: (ImageProxy) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView: PreviewView = remember { PreviewView(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val resolutionSize = Size(1024, 768)
    val resolutionSelector = ResolutionSelector.Builder()
        .setResolutionStrategy(
            ResolutionStrategy(
                resolutionSize,
                ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
            )
        )
        .build()
    cameraController.bindToLifecycle(lifecycleOwner)
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    cameraController.setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
    cameraController.imageAnalysisBackpressureStrategy = STRATEGY_KEEP_ONLY_LATEST
    cameraController.imageAnalysisResolutionSelector = resolutionSelector
    previewView.controller = cameraController
    previewView.scaleType = PreviewView.ScaleType.FILL_CENTER

    Box(modifier.clipToBounds()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        cameraController.setImageAnalysisAnalyzer(executor) { imageProxy ->
            onFrameAnalyzed(imageProxy)
        }
    }
}