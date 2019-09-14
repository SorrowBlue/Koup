package com.sorrowblue.koup.compiler

import com.sorrowblue.koup.annotation.SharedPreferenceKoup
import org.yanex.takenoko.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.tools.Diagnostic.Kind.*
import com.google.auto.service.AutoService
import javax.lang.model.element.*


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.sorrowblue.koup.annotation.SharedPreferenceKoup")
//@SupportedAnnotationTypes("org.kotlin.annotationProcessor.TestAnnotation")
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
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
            processingEnv.messager.printMessage(ERROR, "Can't find the target directory for generated Kotlin files.")
            return false
        }

        annotatedElements.forEach {
            val enclosing = it.enclosingElement
            val packageElement = enclosing as PackageElement
            val generatedKtFile = kotlinFile(packageElement.qualifiedName.toString().plus(".preferences")) {
                this.packageName
                for (element in annotatedElements) {
                    val typeElement = element.toTypeElementOrNull() ?: continue
                    classDeclaration("${it.simpleName}") {
                        property("prefix", KoType.STRING) {
                            initializer("\"${it.simpleName}\"")
                        }
                    }
                    property("simpleClassName") {
                        receiverType(typeElement.qualifiedName.toString())
                        getterExpression("this::class.java.simpleName")
                    }
                }
            }

            File("$kaptKotlinGeneratedDir/preferences", "testGenerated.kt").apply {
                parentFile.mkdirs()
                writeText(generatedKtFile.accept(PrettyPrinter(PrettyPrinterConfiguration())))
            }
        }


        //
//        roundEnv?.getElementsAnnotatedWith(SharedPreferenceKoup::class.java)
//            ?.forEach {
//                val className = it.simpleName.toString()
//                println("classname=$className")
//                if (it.kind != ElementKind.PACKAGE) {
//                    val enclosing = it.enclosingElement
//                    val packageElement = enclosing as PackageElement
//                    generateClass2(
//                        packageElement.qualifiedName.split(".").joinToString("/"),
//                        className,
//                        (it.enclosedElements.first {
//                            it.kind.isField && it.simpleName.toString() == "prefix"
//                        } as VariableElement).constantValue.toString(),
//                        it.enclosedElements.filter {
//                            it.kind.isField && it.simpleName.toString() != "INSTANCE" && it.simpleName.toString() != "prefix"
//                        }.map {
//                            val varElement = it as VariableElement
//                            "object ${varElement.simpleName} : ${className}Pref<${varElement.kotlinPrimitive}>(${varElement.constantAutoValue})"
//                        }
//                    )
//                }
//            }

        return true
    }

    fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            processingEnv.messager.printMessage(ERROR, "Invalid element type, class expected", this)
            return null
        }

        return this
    }

    private fun generateClass2(
        packagename: String,
        className: String,
        prefix: String,
        joinToString: List<String>
    ) {
        val fileName = className + "Pref"

        val kaptKotlinGeneratedDir =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] + "/" + packagename + "/preferences/"
        File(kaptKotlinGeneratedDir).mkdirs()
        val file = File(kaptKotlinGeneratedDir, "$fileName.kt")

        val text = """
			package ${packagename.split("/").joinToString(".")}.preferences

			import com.sorrowblue.preflin.PreflinInfo
			
			sealed class $fileName<T : Any>(def: T) : PreflinInfo<T>(def) {
				
				override val prefix = "$prefix"
				
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
