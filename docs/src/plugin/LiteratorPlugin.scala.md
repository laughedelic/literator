## Literator SBT Pluging

```scala
package laughedelic.literator.plugin

import sbt._
import Keys._
import laughedelic.literator.lib._

object LiteratorPlugin extends sbt.Plugin {

  object Literator {
```

Setting keys

```scala
    lazy val docsMap = settingKey[Map[File, File]]("Mapping between input source and output docs directories")
    lazy val docsOutputDirs = settingKey[Seq[File]]("Output directories for the generated documentation")
    lazy val docsAddIndex = settingKey[Boolean]("If true, index will be added to each generated file")
    lazy val generateDocs = taskKey[Unit]("Generates markdown docs from code using literator tool")
```

Initial settings for the keys:

```scala
    lazy val settings: Seq[Setting[_]] = Seq(
      docsMap := Map(file(sourceDirectory.value.toString) -> file("docs/src/")),
      docsOutputDirs := docsMap.value.values.toSeq,
      docsAddIndex := true,
      generateDocs := { 
        val s: TaskStreams = streams.value
        docsMap.value map { case (in, out) =>
          s.log.info("Generating documentation for " + in)

          val errors = in.literate(Some(out), withIndex = docsAddIndex.value)
          errors foreach { s.log.error(_) }

          if (errors.nonEmpty) sys.error("Couldn't generate documentation due to parsing errors")
          else s.log.info("Documentation is written to  " + out)
        }
      }
    )
  }

}

```


------

### Index

+ scala
  + [LiteratorPlugin.scala][LiteratorPlugin.scala]

[LiteratorPlugin.scala]: LiteratorPlugin.scala.md