apply { plugin("kotlin") }
apply { plugin("jps-compatible") }

jvmTarget = "1.6"

dependencies {
    val compile by configurations
    val compileOnly by configurations
    compile(project(":compiler:psi"))
    compile(project(":compiler:container"))
    compileOnly(intellijCoreDep()) { includeJars("intellij-core", "annotations") }
}


sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}