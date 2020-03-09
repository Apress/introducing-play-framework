name := """bookshop"""
organization := "com.stackrules.example"

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(guice,javaJdbc,cacheApi, javaWs,javaJpa,caffeine,
                            "org.hibernate" % "hibernate-entitymanager" % "5.3.7.Final",
                            "com.h2database" % "h2" % "1.4.200" )
EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)