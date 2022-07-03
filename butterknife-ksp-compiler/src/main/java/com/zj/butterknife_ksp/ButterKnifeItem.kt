package com.zj.butterknife_ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class ButterKnifeItem(
    val parent: KSClassDeclaration,
    val symbol: String,
    val annotationValue: Int
)