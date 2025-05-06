val junitVersion: String by project
val springContextVersion: String by project
val jacksonVersion: String by project
val lombokVersion: String by project
val aspectjVersion: String by project
val springjdbcVersion: String by project
val postgresqlVersion: String by project

plugins {
    id("java")
}

group = "org.shuyan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework:spring-context:$springContextVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:$jacksonVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    implementation("org.aspectj:aspectjrt:$aspectjVersion")
    implementation("org.aspectj:aspectjweaver:$aspectjVersion")
    implementation("org.springframework:spring-jdbc:$springjdbcVersion")
    runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
}


tasks.test {
    useJUnitPlatform()
}

