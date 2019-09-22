package com.sorrowblue.koup.compiler

import com.google.auto.service.AutoService
import com.sorrowblue.koup.annotation.KoupKey
import com.sorrowblue.koup.annotation.KoupSharedPreference
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
	"com.sorrowblue.koup.annotation.KoupSharedPreference",
	"com.sorrowblue.koup.annotation.KoupKey"
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
			roundEnv.getElementsAnnotatedWith(KoupSharedPreference::class.java).map {
				val thisPackage = (it.enclosingElement as PackageElement).qualifiedName.toString()
				val thisClass = it.simpleName.toString()

				val prefix = it.getAnnotation(KoupSharedPreference::class.java)?.prefix
				KoupClass(
					thisPackage,
					thisClass,
					prefix,
					it.fields.map {
						Params(
							it.simpleName.toString(),
							(it as VariableElement).kotlinPrimitive,
							(it as VariableElement).constantAutoValue,
							""
						)
					}.toMutableList()
				)
			}
		roundEnv.getElementsAnnotatedWith(KoupKey::class.java).map { element ->
			val thisClass = element.enclosingElement.simpleName.toString()
			koupSharedPreferences.find { it.className == thisClass }?.let {
				val fieldName = element.simpleName.toString().replace("\$annotations", "")
				it.params.forEach {
					if (it.name == fieldName) {
						it.key = element.getAnnotation(KoupKey::class.java).key
					}
				}
			}
		}
		koupSharedPreferences.forEach {
			val className = it.className
			val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
				?: run {
					processingEnv.messager.printMessage(
						ERROR,
						"Can't find the target directory for generated Kotlin files."
					)
					return@forEach
				}
			val file =
				File(kaptKotlinGeneratedDir, it.packageName.replace(".", "/")).let {
					if (it.exists().not()) {
						it.mkdirs()
					}
					File(it, "Koup_$className.kt")
				}

			val text = """
			package ${it.packageName}

			sealed class Koup_$className<T : Any>(def: T, key: String) : com.sorrowblue.koup.KoupInfo<T>(def, key) {
				
				override val prefix = "${it.prefix ?: className.toUpperCase(Locale.getDefault())}"
				
				${it.params.map { (it.toString(className)) }.joinToString("\n\t\t\t\t")}
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
			"double" -> "Double"
			"java.util.Set<java.lang.String>" -> "Set<String>"
			else -> {
				processingEnv.messager.printMessage(ERROR, "Koup is not support $type.")
//				throw TypeMismatchException("Koup is not support $type.")
				""
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
	val params: MutableList<Params> = mutableListOf()
)

private data class Params(val name: String, val type: String, var value: String, var key: String?) {
	fun toString(className: String) =
		"object $name :	Koup_$className<$type>($value, \"$key\")"
}
