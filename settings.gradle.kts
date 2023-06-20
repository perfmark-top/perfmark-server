pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        settings.extra["publishing.gitlab.host"]?.let {  host ->
            maven("https://${host}/api/v4/projects/11/packages/maven")
        }
    }
}

rootProject.name = "perfmark-server"
