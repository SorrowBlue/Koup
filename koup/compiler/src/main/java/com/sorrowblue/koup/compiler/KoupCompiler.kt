package com.sorrowblue.koup.compiler

import com.google.auto.service.AutoService
import com.sorrowblue.android.kotpref.annotation.KotPref
import com.sorrowblue.android.kotpref.annotation.KotPrefKey
import com.sun.corba.se.impl.io.TypeMismatchException
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
@SupportedAnnotationTypes(
	"com.sorrowblue.android.kotpref.annotation.KotPref",
	"com.sorrowblue.android.kotpref.annotation.KotPrefKey"
)
@SupportedOptions(KoupCompiler.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@AutoService(Processor::class)
class KoupCompiler : AbstractProcessor() {
	companion object {
		const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
	}

	override fun process(
		annotations: MutableSet<out TypeElement>,
		roundEnv: RoundEnvironment
	): Boolean {
		val koupSharedPreferences =
			roundEnv.getElementsAnnotatedWith(KotPref::class.java).map {
				val thisPackage = (it.enclosingElement as PackageElement).qualifiedName.toString()
				val thisClass = it.simpleName.toString()

				val prefix = it.getAnnotation(KotPref::class.java)?.prefix
				val suffix = it.getAnnotation(KotPref::class.java).nameSuffix.let {
					if (it != "") it else null
				}
				KoupClass(
					thisPackage,
					thisClass,
					prefix,
					suffix,
					it.fields.map {
						Params(
							it.simpleName.toString(),
							(it as VariableElement).kotlinPrimitive,
							it.constantAutoValue,
							null,
							null
						)
					}.toMutableList()
				)
			}
		roundEnv.getElementsAnnotatedWith(KotPrefKey::class.java).map { element ->
			val thisClass = element.enclosingElement.simpleName.toString()
			koupSharedPreferences.find { it.className == thisClass }?.let {
				val fieldName = element.simpleName.toString().replace("\$annotations", "")
				it.params.forEach {
					if (it.name == fieldName) {
						it.key = element.getAnnotation(KotPrefKey::class.java).key.let {
							if (it != "") it else null
						}
						it.resId = element.getAnnotation(KotPrefKey::class.java).resId.let {
							if (it != 0) it else null
						}
					}
				}
			}
		}
		koupSharedPreferences.forEach { ko: KoupClass ->
			val className = ko.className
			val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
				?: run {
					processingEnv.messager.printMessage(
						ERROR,
						"Can't find the target directory for generated Kotlin files."
					)
					return@forEach
				}
			val file =
				File(kaptKotlinGeneratedDir, ko.packageName.replace(".", "/")).let {
					if (it.exists().not()) {
						it.mkdirs()
					}
					File(it, "$className${ko.suffix ?: "Preference"}.kt")
				}
			val text = """
			package ${ko.packageName}

			sealed class $className${ko.suffix
				?: "Preference"}<T : Any>(default: T, key: String? = null, resId: Int? = null) : com.sorrowblue.android.kotpref.KotPrefKey<T>(default, key, resId) {
				
				override val prefix = "${ko.prefix ?: className.toUpperCase(Locale.getDefault())}"
				
				${ko.params.map { (it.toString(ko)) }.joinToString("\n\t\t\t\t")}
			}
""".trimIndent()
			file.writeText(text)
		}
		return true
	}

	override fun getSupportedOptions(): Set<String> {
		return Collections.singleton("org.gradle.annotation.processing.aggregating")
	}

	private val Element.fields
		get() = enclosedElements.filter { it.kind.isField && it.simpleName.toString() != "INSTANCE" }


	private val VariableElement.kotlinPrimitive
		get() = when (val type = asType().toString()) {
			"int" -> "Int"
			"java.lang.String" -> "String"
			"boolean" -> "Boolean"
			"float" -> "Float"
			"long" -> "Long"
			"double" -> "Double"
			"java.util.Set<java.lang.String>" -> "Set<String>"
			else -> {
				processingEnv.messager.printMessage(ERROR, "Koup is not support $type.")
				throw TypeMismatchException("Koup is not support $type.")
//				""
			}
		}

	private val VariableElement.constantAutoValue
		get() = when (asType().toString()) {
			"float" -> "${constantValue}f"
			"java.lang.String" -> """ "$constantValue" """.trim(' ')
			"java.util.Set<java.lang.String>" -> "emptySet()"
			else -> constantValue?.toString() ?: "null"
		}

}

private data class KoupClass(
	val packageName: String,
	val className: String,
	val prefix: String?,
	val suffix: String?,
	val params: MutableList<Params> = mutableListOf()
)

private data class Params(
	val name: String,
	val type: String,
	var value: String,
	var key: String?,
	var resId: Int?
) {
	fun toString(co: KoupClass) =
		key?.let {
			"object $name :	${co.className}${co.suffix ?: "Preference"}<$type>($value, key = \"$it\")"
		} ?: resId?.let {
			"object $name :	${co.className}${co.suffix ?: "Preference"}<$type>($value, resId = $it)"
		} ?: "object $name :	${co.className}${co.suffix ?: "Preference"}<$type>($value, key = \"$name\")"
}
