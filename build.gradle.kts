plugins {
    id("java")
}

group = "org.shuyan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework:spring-context:6.2.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.18.2")
    compileOnly("org.projectlombok:lombok:1.18.30")
}


tasks.test {
    useJUnitPlatform()
}

