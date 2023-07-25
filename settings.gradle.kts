pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        /*maven {
            url = uri("http://dl.bintray.com/amulyakhare/maven")
            isAllowInsecureProtocol = true

        }*/

        maven { url = uri("https://jitpack.io") }

    }
}

rootProject.name = "Contacts"
include(":app")
 