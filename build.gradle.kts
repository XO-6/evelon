allprojects {
    apply(plugin = "java-library")

    version = "1.0.0.0-beta"
    group = "dev.httpmarco.evelon"

    repositories {
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
        "annotationProcessor"(rootProject.libs.lombok)
        "implementation"(rootProject.libs.bundles.utils)
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.encoding = "UTF-8"
    }
}
