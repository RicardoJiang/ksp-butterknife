package com.zj.butterknife_ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * Created by benny.
 */
class ButterKnifeGenerator {

    @OptIn(KotlinPoetKspPreview::class)
    fun generate(
        codeGenerator: CodeGenerator,
        logger: KSPLogger,
        list: List<KSPropertyDeclaration>
    ) {
        val map = list.groupBy {
            val parent = it.parent as KSClassDeclaration
            val key = "${parent.toClassName().simpleName},${parent.packageName.asString()}"
            key
        }

        map.forEach {
            val classItem = it.value[0].parent as KSClassDeclaration
            val fileSpecBuilder = FileSpec.builder(
                classItem.packageName.asString(),
                "${classItem.toClassName().simpleName}ViewBind"
            )

            val functionBuilder = FunSpec.builder("bindView")
                .receiver(classItem.toClassName())

            it.value.forEach { item ->
                val symbolName = item.simpleName.asString()
                val annotationValue =
                    (item.annotations.firstOrNull()?.arguments?.firstOrNull()?.value as? Int) ?: 0
                functionBuilder.addStatement("$symbolName = findViewById(${annotationValue})")
            }

            fileSpecBuilder.addFunction(functionBuilder.build())
                .build()
                .writeTo(codeGenerator, false)
        }
    }

}