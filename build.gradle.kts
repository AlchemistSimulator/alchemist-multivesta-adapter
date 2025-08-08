plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.taskTree)
}

group = "it.unibo.alchemist"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.alchemist)
    implementation(libs.alchemist.loading)
    with(libs.apache.commons) {
        implementation(cli)
        implementation(io)
    }
    implementation(libs.guava)
    implementation(libs.logback)
    implementation(libs.kotlin.stdlib)
    implementation(libs.resourceloader)
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

multiJvm {
    jvmVersionForCompilation = 17
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(
            *org.gradle.api.tasks.testing.logging.TestLogEvent
                .values(),
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

signing {
    if (System.getenv("CI") == "true") {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}

publishOnCentral {
    repoOwner = "AlchemistSimulator"
    projectLongName.set("Alchemist-MultiVeSTa adapter")
    projectDescription.set("Uses alchemist within multivesta")
    publishing {
        publications {
            withType<MavenPublication> {
                pom {
                    developers {
                        developer {
                            name.set("Gianmarco Magnani")
                        }
                        developer {
                            name.set("Danilo Pianini")
                            email.set("danilo.pianini@gmail.com")
                            url.set("http://www.danilopianini.org/")
                        }
                    }
                }
            }
        }
    }
}
