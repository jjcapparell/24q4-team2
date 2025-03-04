plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    eclipse
    jacoco
    id("io.freefair.lombok") version "8.10.2"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.springframework.boot") version "3.2.2"
    id("com.adarshr.test-logger") version "4.0.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// Shadow JAR configuration
tasks.shadowJar {
    archiveBaseName.set("guessing-game")
    archiveVersion.set("2.0")
    mergeServiceFiles()
}

apply(plugin = "io.spring.dependency-management")

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation("at.favre.lib:bcrypt:0.10.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa");
    implementation("org.hibernate.orm:hibernate-community-dialects:6.6.3.Final");
    implementation("org.xerial:sqlite-jdbc:3.36.0.3");
}

application {
    // Define the main class for the application.
    // mainClass.set("com.codedifferently.q4.team2.GameLauncher")
    mainClass.set("com.codedifferently.q4.team2.Main")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.2".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {

    format("misc", {
        // define the files to apply `misc` to
        target("*.gradle", ".gitattributes", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithTabs() // or spaces. Takes an integer argument if you don't like 4
        endWithNewline()
    })

    java {
        // don't need to set target, it is inferred from java
        // apply a specific flavor of google-java-format
        googleJavaFormat()
        // fix formatting of type annotations
        formatAnnotations()
    }
}
