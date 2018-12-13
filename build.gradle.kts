plugins {
  id("fabric-loom") version "0.1.0-SNAPSHOT"
}

base {
  archivesBaseName = "qcommon-architect"
}

group = "therealfarfetchd.qcommon.architect"
version = "1.0.0"

minecraft {
}

dependencies {
  minecraft("com.mojang:minecraft:18w50a")
  mappings("net.fabricmc:yarn:18w50a.2")
  modCompile("net.fabricmc:fabric-loader:0.2.0.64")

  // Fabric API. This is technically optional, but you probably want it anyway.
  modCompile("net.fabricmc:fabric:0.1.1.47")

  compile(files("qcommon-croco-1.0.2-dev.jar"))
  compile("com.google.code.findbugs:jsr305:3.0.2")
}
