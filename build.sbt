def commonSettings = Seq(
  organization := "com.manyangled",
  version := "0.3.0-SNAPSHOT",
  scalaVersion := "2.13.0-M5",
  //crossScalaVersions := Seq("2.12.4"),
  licenses += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0")),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "eu.timepit" %% "singleton-ops" % "0.3.1",
    //"com.chuusai" %% "shapeless" % "2.3.2",
    "org.typelevel" %% "spire-macros" % "0.16.1",
    "org.typelevel" %% "spire" % "0.16.1",
    "org.scalatest" %% "scalatest" % "3.0.7" % Test
  ),
  //addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  //scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xlog-implicits"),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/root-doc.txt"))

def docDepSettings = Seq(
  previewSite := {}
)

lazy val coulomb = (project in file("coulomb"))
  .settings(name := "coulomb")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_si_units = (project in file("coulomb-si-units"))
  .aggregate(coulomb)
  .dependsOn(coulomb)
  .settings(name := "coulomb-si-units")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_temp_units = (project in file("coulomb-temp-units"))
  .aggregate(coulomb, coulomb_si_units)
  .dependsOn(coulomb, coulomb_si_units)
  .settings(name := "coulomb-temp-units")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_mks_units = (project in file("coulomb-mks-units"))
  .aggregate(coulomb, coulomb_si_units)
  .dependsOn(coulomb, coulomb_si_units)
  .settings(name := "coulomb-mks-units")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_accepted_units = (project in file("coulomb-accepted-units"))
  .aggregate(coulomb, coulomb_si_units)
  .dependsOn(coulomb, coulomb_si_units)
  .settings(name := "coulomb-accepted-units")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_time_units = (project in file("coulomb-time-units"))
  .aggregate(coulomb, coulomb_si_units)
  .dependsOn(coulomb, coulomb_si_units)
  .settings(name := "coulomb-time-units")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_info_units = (project in file("coulomb-info-units"))
  .aggregate(coulomb, coulomb_si_units)
  .dependsOn(coulomb, coulomb_si_units)
  .settings(name := "coulomb-info-units")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_customary_units = (project in file("coulomb-customary-units"))
  .aggregate(coulomb, coulomb_si_units)
  .dependsOn(coulomb, coulomb_si_units)
  .settings(name := "coulomb-customary-units")
  .settings(commonSettings :_*)
  .settings(docDepSettings :_*)

lazy val coulomb_parser = (project in file("coulomb-parser"))
  .aggregate(coulomb)
  .dependsOn(coulomb)
  .settings(name := "coulomb-parser")
  .settings(commonSettings :_*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1"
    )
  )

lazy val coulomb_tests = (project in file("coulomb-tests"))
  .aggregate(coulomb, coulomb_si_units, coulomb_mks_units, coulomb_accepted_units, coulomb_time_units, coulomb_info_units, coulomb_customary_units, coulomb_temp_units, coulomb_parser)
  .dependsOn(coulomb, coulomb_si_units, coulomb_mks_units, coulomb_accepted_units, coulomb_time_units, coulomb_info_units, coulomb_customary_units, coulomb_temp_units, coulomb_parser)
  .settings(name := "coulomb-tests")
  .settings(commonSettings :_*)

lazy val coulomb_docs = (project in file("."))
  .aggregate(coulomb, coulomb_si_units, coulomb_mks_units, coulomb_accepted_units, coulomb_time_units, coulomb_info_units, coulomb_customary_units, coulomb_temp_units)
  .dependsOn(coulomb, coulomb_si_units, coulomb_mks_units, coulomb_accepted_units, coulomb_time_units, coulomb_info_units, coulomb_customary_units, coulomb_temp_units)
  .settings(name := "coulomb-docs")
  .settings(commonSettings :_*)

enablePlugins(ScalaUnidocPlugin, GhpagesPlugin)

siteSubdirName in ScalaUnidoc := "latest/api"

addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc)

git.remoteRepo := "git@github.com:erikerlandson/coulomb.git"
