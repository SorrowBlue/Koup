package com.sorrowblue.koup.compiler

import com.google.auto.service.AutoService
import com.sorrowblue.koup.annotation.SharedPreferenceKoup
import java.io.File
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic.Kind.ERROR


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.sorrowblue.koup.annotation.SharedPreferenceKoup")
@SupportedOptions(KoupCompiler.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@AutoService(Processor::class)
class KoupCompiler : AbstractProcessor() {
	companion object {
		const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
	}

	override fun process(
		annotations: MutableSet<out TypeElement>?,
		roundEnv: RoundEnvironment
	): Boolean {
		val annotatedElements = roundEnv.getElementsAnnotatedWith(SharedPreferenceKoup::class.java)
		if (annotatedElements.isEmpty()) return false

		annotatedElements.forEach {
			val thisPackage = (it.enclosingElement as PackageElement).qualifiedName.toString()
			val thisClass = it.simpleName.toString()

			val prefix = it.findField("prefix")
			generateClass2(thisPackage, thisClass, prefix, it.notFilter("prefix"))


		}


		return true
	}

	override fun getSupportedOptions(): Set<String> {
		return Collections.singleton("org.gradle.annotation.processing.aggregating")
	}

	fun Element.findField(name: String) = (enclosedElements.firstOrNull {
		it.kind.isField && it.simpleName.toString() == name
	} as? VariableElement)?.constantValue?.toString()

	fun Element.notFilter(name: String) = enclosedElements.filter {
		it.kind.isField && it.simpleName.toString() != "INSTANCE" && it.simpleName.toString() != name
	}

	fun Element.toTypeElementOrNull(): TypeElement? {
		if (this !is TypeElement) {
			processingEnv.messager.printMessage(ERROR, "Invalid element type, class expected", this)
			return null
		}

		return this
	}

	private fun generateClass2(
		packageName: String,
		className: String,
		prefix: String?,
		fieldElements: List<Element>
	) {
		val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
			?: run {
				processingEnv.messager.printMessage(ERROR, "Can't find the target directory for generated Kotlin files.")
				return
			}
		val joinToString = fieldElements.map {
			val varElement = it as VariableElement
			"object ${varElement.simpleName} : Koup_$className<${varElement.kotlinPrimitive}>(${varElement.constantAutoValue})"
		}
		val file = File(kaptKotlinGeneratedDir, "${packageName.replace(".", "/")}/preferences/").let {
			if (it.exists().not()) {
				it.mkdirs()
			}
			File(it, "Koup_$className.kt")
		}

		val text = """
			package $packageName.preferences

			sealed class Koup_$className<T : Any>(def: T, key: String? = null) : com.sorrowblue.koup.KoupInfo<T>(def) {
				
				override val prefix = "${prefix ?: className.toUpperCase(Locale.getDefault())}"
				
				${joinToString.joinToString("\n\t\t\t\t") { it }}
			}
""".trimIndent()
		file.writeText(text)
	}

	private val VariableElement.kotlinPrimitive
		get() = when (val type = asType().toString()) {
			"int" -> "Int"
			"java.lang.String" -> "String"
			"boolean" -> "Boolean"
			"float" -> "Float"
			"double" -> "Double"
			else -> type
		}

	private val VariableElement.constantAutoValue
		get() = when (asType().toString()) {
			"float" -> "${constantValue}f"
			"java.lang.String" -> """ "$constantValue" """.trim(' ')
			else -> constantValue.toString()
		}

}

//
//val text = """
//			package ${packagename.split("/").joinToString(".")}.preferences
//
//			import com.sorrowblue.preflin.PreflinInfo
//
//			sealed class $fileName<T : Any>(def: T) : PreflinInfo<T>(def) {
//
//				override val prefix = "$prefix"
//
//				${joinToString.joinToString("\n\t\t\t\t") { it }}
//			}
//""".trimIndent()
