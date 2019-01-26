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
  minecraft("com.mojang:minecraft:19w04b")
  mappings("net.fabricmc:yarn:19w04b.2")
  modCompile("net.fabricmc:fabric-loader:0.3.3.101")

  // Fabric API. This is technically optional, but you probably want it anyway.
  modCompile("net.fabricmc:fabric:0.1.5.82")

  compile(files("qcommon-croco-1.0.4-dev.jar"))
  compile("com.google.code.findbugs:jsr305:3.0.2")
}
