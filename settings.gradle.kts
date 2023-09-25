pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "oneui-dream_diary"
include(":app")
include(":core:database")
include(":core:model")
include(":core:common")
include(":core:ui")
include(":feature:dreams")
include(":core:form")
include(":core:domain")
include(":csv")
include(":core:io")
include(":feature:export")
include(":feature:persons")
include(":feature:locations")
include(":feature:relations")
include(":feature:preferences")
include(":core:datastore")
include(":core:obfuscation")
include(":feature:statistics")
include(":feature:statistics:dreams")
