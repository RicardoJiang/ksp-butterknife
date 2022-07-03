package com.zj.butterknife_ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate

class ButterKnifeProcessor(
    val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(BindView::class.qualifiedName!!)
        val ret = symbols.filter { !it.validate() }.toList()
        val butterKnifeList = symbols
            .filter { it is KSPropertyDeclaration && it.validate() }
            .map {
                it as KSPropertyDeclaration
            }.toList()
        ButterKnifeGenerator().generate(codeGenerator, logger, butterKnifeList)
        return ret
    }
}