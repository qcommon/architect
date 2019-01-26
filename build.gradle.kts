plugins {
  id("fabric-loom") version "0.2.0-SNAPSHOT"
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
  mappings("net.fabricmc:yarn:18w50a.89")
  modCompile("net.fabricmc:fabric-loader:0.3.1.84")

  // Fabric API. This is technically optional, but you probably want it anyway.
  modCompile("net.fabricmc:fabric:0.1.3.68")

  compile(files("qcommon-croco-1.0.4-dev.jar"))
  compile("com.google.code.findbugs:jsr305:3.0.2")
}
