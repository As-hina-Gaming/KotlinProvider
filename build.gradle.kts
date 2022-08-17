import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly
import java.util.stream.Collectors

plugins {
    val kotlinVersion: String by System.getProperties()
    val shadowVersion: String by System.getProperties()

    kotlin("jvm").version(kotlinVersion)
    id("com.github.johnrengelman.shadow").version(shadowVersion)

    id("maven-publish")
}

group = "net.eratiem"
version = "1.7.10"

repositories {
    maven {
        url = uri("https://artifactory.bit-build.de/artifactory/all")

        bitBuildCredentials(this)
    }
}

dependencies {
    val kotlinVersion: String by System.getProperties()
    val paperApiVersion: String by project
    val velocityApiVersion: String by project

    implementation(kotlin("stdlib", kotlinVersion))

    compileOnly("io.papermc.paper", "paper-api", paperApiVersion)
    compileOnly("com.velocitypowered", "velocity-api", velocityApiVersion)
    annotationProcessor("com.velocitypowered", "velocity-api", velocityApiVersion)
}

lateinit var velocityJar: TaskProvider<ShadowJar>
lateinit var paperJar: TaskProvider<ShadowJar>

tasks {
    // Write Properties into plugin.yml
    withType<Copy> {
        outputs.upToDateWhen { false }

        filesMatching("plugin.yml") {
            val mainClass =
                "${project.group}.${project.name.toLowerCaseAsciiOnly()}.paper.${project.properties["mainClass"]}"
            val apiVersion =
                "(\\d+\\.\\d+){1}(\\.\\d+)?".toRegex().find(project.properties["paperApiVersion"] as String)!!.value
            val pluginDescription: String by project
            val pluginDependencies = getAsYamlList(project.properties["pluginDependencies"])
            val authors: String = getAsYamlList(project.properties["authors"])

            val props: LinkedHashMap<String, String> = linkedMapOf(
                "plugin_name" to project.name,
                "plugin_description" to pluginDescription,
                "plugin_version" to version.toString(),
                "plugin_main_class" to mainClass,
                "plugin_api_version" to apiVersion,
                "plugin_dependencies" to pluginDependencies,
                "plugin_authors" to authors
            )

            expand(props)
        }
    }

    jar {
        enabled = false
    }

    project.configurations.implementation.get().isCanBeResolved = true

    velocityJar = register<ShadowJar>("velocityJar") {
        group = "plugin"
        enabled = true

        archiveClassifier.set("")
        configurations = listOf(project.configurations.implementation.get())

        archiveClassifier.set("velocity")

        from(sourceSets.main.get().output) {
            exclude("${project.group.toString().replace('.', '/')}/${project.name.toLowerCaseAsciiOnly()}/paper/**")
            exclude("plugin.yml")
        }
    }

    paperJar = register<ShadowJar>("paperJar") {
        group = "plugin"
        enabled = true

        archiveClassifier.set("")
        configurations = listOf(project.configurations.implementation.get())

        archiveClassifier.set("paper")

        from(sourceSets.main.get().output) {
            exclude("${project.group.toString().replace('.', '/')}/${project.name.toLowerCaseAsciiOnly()}/velocity/**")
        }
    }

    build {
        dependsOn(velocityJar)
        dependsOn(paperJar)
    }

    //
    create("copyPluginToPaperServer") {
        dependsOn(build)
        group = "plugin"
        enabled = false

        val serverPath: String by project

        if (serverPath.isNotBlank() && File(serverPath).exists()) {
            outputs.upToDateWhen { false }
            val libsDir = File("${project.buildDir.absolutePath}${File.separator}libs")
            val destinationFile =
                File("$serverPath${File.separator}plugins${File.separator}${rootProject.name.toLowerCase()}.jar")
            val paperJarFiles: List<File>? =
                libsDir.listFiles()?.filter { it.extension == "jar" && it.name.contains("Paper") }

            if (paperJarFiles?.size == 1) {
                paperJarFiles[0].copyTo(
                    destinationFile,
                    true
                )
                enabled = destinationFile.exists()
            }
        }
    }

    create<Copy>("generateIntelliJRunConfig") {
        group = "plugin"
        enabled = true

        from("./runConfigs")
        destinationDir = File("./.idea/runConfigurations")
        include("intellij.xml")

        val serverPath: String by project
        enabled = serverPath.isNotBlank()

        val paperFile: File? =
            File(serverPath).listFiles()?.filter { it.name.matches("paper.*\\.jar".toRegex()) }?.get(0)
        enabled = paperFile != null

        if (paperFile != null && paperFile.exists()) {
            val props: LinkedHashMap<String, String> = linkedMapOf(
                "server_path" to File(serverPath).absolutePath,
                "project_dir" to "\$PROJECT_DIR\$"
            )

            filesMatching("intellij.xml") {
                expand(props)
            }
        }
    }

    // Compile Stuff
    val javaVersion = JavaVersion.VERSION_17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersion.toString().toInt())
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }
}



publishing {
    publications {
        create<MavenPublication>("artifactory") {
            groupId = project.group.toString()
            artifactId = project.name.toLowerCase()
            version = project.version.toString()

            artifact(paperJar)
            artifact(velocityJar)
        }
    }
    repositories {
        maven {
            url = uri(
                "https://artifactory.bit-build.de/artifactory/eratiem"
                        + (if (project.version.toString().contains("SNAPSHOT"))
                    "-snapshots" else "")
            )

            bitBuildCredentials(this)
        }
    }
}

fun getAsYamlList(commaSeparatedList: Any?): String {
    if (commaSeparatedList is String && commaSeparatedList.isNotBlank()) {
        return commaSeparatedList
            .split(",")
            .stream()
            .map { "\n  - $it" }
            .collect(Collectors.joining())
    }
    return ""
}

fun bitBuildCredentials(maven: MavenArtifactRepository) {
    maven.credentials {
        val mavenUsr: String by System.getProperties()
        val mavenPsw: String by System.getProperties()

        username = mavenUsr
        password = mavenPsw
    }
}