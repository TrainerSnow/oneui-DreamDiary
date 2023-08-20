pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
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
include(":core:data")
include(":core:common")
include(":core:ui")
include(":feature:dreams")
include(":core:form")
