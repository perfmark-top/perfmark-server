import io.github.perfmarktop.Deps
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.graalvm.buildtools.native") version "0.9.20"

    val kotlinVer = "1.8.21"
    kotlin("jvm") version kotlinVer
    kotlin("plugin.spring") version kotlinVer
    kotlin("plugin.jpa") version kotlinVer

    id("com.google.protobuf") version "0.9.1"
}

group = "io.github.perfmarktop"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Deps.Proto}"
    }
    generatedFilesBaseDir = "$projectDir/src/generated"
    plugins {
        register("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Deps.GrpcJava}"
        }
        create("grpcKt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${Deps.GrpcKotlin}"
        }
    }
    generateProtoTasks {
        for(task in all()) {
            task.builtins {
                create("kotlin")
            }
            task.plugins {
                create("grpc")
                create("grpcKt")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("src/main/kotlin")
        }
    }
    test {
        proto {
            srcDir("src/main/kotlin")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("com.fasterxml.jackson.core:jackson-databind")
    }
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("net.devh:grpc-server-spring-boot-starter:2.14.0.RELEASE")
    implementation("io.grpc:grpc-protobuf:${Deps.GrpcJava}")
    implementation("io.grpc:grpc-stub:${Deps.GrpcJava}")

    implementation("org.ini4j:ini4j:0.5.4")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.3")
    implementation("com.google.code.gson:gson")

    /* https://github.com/sgpublic/UniversalKTX */
    val uniktx = "1.0.0-alpha02"
    implementation("io.github.sgpublic:uniktx-kotlin-common:$uniktx")
    implementation("io.github.sgpublic:uniktx-kotlin-logback:$uniktx")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
