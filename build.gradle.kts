plugins {
    id("com.codebootup.kotlin") version "1.0.0"
}

repositories{
    mavenCentral()
}

dependencies{
    implementation("org.springframework:spring-expression:6.0.6")
    testImplementation("org.thymeleaf:thymeleaf:3.1.1.RELEASE")
    testImplementation("com.codebootup.compare-directories:compare-directories:1.0.0")
}