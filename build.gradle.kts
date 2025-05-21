val junitVersion: String by project
val springContextVersion: String by project
val springTxVersion: String by project
val springOrmVersion: String by project
val springTestVersion: String by project
val jacksonVersion: String by project
val lombokVersion: String by project
val logbackVersion: String by project
val aspectjVersion: String by project
val postgresqlVersion: String by project
val h2dbVersion: String by project
val hibernateCoreVersion: String by project
val hibernateEhcacheVersion: String by project
val jakartaTransactionVersion:String by project

plugins {
    id("java")
}

group = "com.shuyan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework:spring-test:$springTestVersion")
    testImplementation("com.h2database:h2:$h2dbVersion")
    implementation("org.springframework:spring-context:$springContextVersion")
    implementation("org.springframework:spring-tx:$springTxVersion")
    implementation("org.springframework:spring-orm:$springOrmVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.aspectj:aspectjrt:$aspectjVersion")
    implementation("org.aspectj:aspectjweaver:$aspectjVersion")
    implementation("org.hibernate.orm:hibernate-core:$hibernateCoreVersion")
    implementation("org.hibernate.orm:hibernate-ehcache:$hibernateEhcacheVersion")
    implementation("jakarta.transaction:jakarta.transaction-api:$jakartaTransactionVersion")
    runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
}


tasks.test {
    useJUnitPlatform()
}

