plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.umizhang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.3.4")
    type.set("IC") // IC:社区版, IU:旗舰版

    plugins.set(
        listOf(
            "com.intellij.java"
        )
    )
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }

    runIde {
        ideDir.set(file("D:\\Program Files (x86)\\JetBrains\\IntelliJ IDEA 2023.3.4")) // 您的IDEA安装路径
    }

    test {
        useJUnitPlatform()
    }
}