package com.fcroce.easystores.components.camera

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
@Composable
fun BarcodeScannerAnalysis(
    modifier: Modifier = Modifier,
    callback: (barcodes: List<Barcode>) -> Unit
) {
    val cameraController = getCameraController()
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
        )
        .setZoomSuggestionOptions(
            ZoomSuggestionOptions.Builder { zoomRatio: Float ->
                cameraController.apply { setZoomRatio(zoomRatio) }
                true
            }
                .setMaxSupportedZoomRatio(
                    cameraController.cameraInfo?.zoomState?.value?.maxZoomRatio ?: 5.0f
                )
                .build()
        )
        .build()

    val scanner = BarcodeScanning.getClient(options)

    CameraPreviewScreen(
        modifier = modifier,
        cameraController = cameraController,
        onFrameAnalyzed = { imageProxy ->
            imageProxy.image?.let { mediaImage ->
                val image = InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
                )

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty() && barcodes.first().rawValue != "") {
                            cameraController.clearImageAnalysisAnalyzer()
                            imageProxy.close()
                            callback(barcodes)
                        }
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }

            imageProxy.close()
        }
    )
}