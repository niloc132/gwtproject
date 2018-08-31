package local

plugins {
    `java-library`
    id("net.ltgt.errorprone-javacplugin")
}

dependencies {
    "errorprone"("com.google.errorprone:error_prone_core:2.3.1")
    "errorproneJavac"("com.google.errorprone:javac:9+181-r4173-1")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(arrayOf("-Werror", "-Xlint:all,-processing"))
    if (JavaVersion.current().isJava9Compatible()) {
        options.compilerArgs.addAll(arrayOf("--release", java.targetCompatibility.majorVersion))
    }
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
    (options as CoreJavadocOptions).apply {
        addBooleanOption("Xdoclint:all,-missing", true)
        // Workaround for https://github.com/gradle/gradle/issues/5630
        addStringOption("sourcepath", "")
    }
}

val Project.java: JavaPluginConvention
    get() = the()
