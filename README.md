Literator
=========

This is a simple tool, which **transforms your sources into markdown documentations for themselves**. It just reads a source file, turns block comments into normal text and surrounds code with markdown backticks syntax. So the aim is just to get a readable document from a code, which is written in more or less [literate programming](http://en.wikipedia.org/wiki/Literate_programming) style. The name is like "a thing which makes your sources literate", i.e. helps to use literate programming when it's not really supported by the language.

So you can write your code and use markdown syntax in comments (which keeps your sources readable), and then transform it to a markdown document, from which you can generate a nice _html_ or _pdf_ or whatever else, using [your favourite markdown processor](http://johnmacfarlane.net/pandoc/).

The tool is written in Scala and first of all for Scala, because it doesn't have normal support for literate programming. But it should work for [some other languages][lib/LanguageMap] — ~~randomly chosen~~ the most popular, of course. [Open an issue](https://github.com/laughedelic/literator/issues/new), if you want support for something else.

If you're wondering, why don't I use docco instead, see my answer [at the bottom](#why-not-docco) of this file.

Literator consists of three components:

- Library
- Command line application
- Sbt plugin


## Library

If you want to use the library in your Scala code, first, add this dependency to your `build.sbt`:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" %% "literator-lib" % "0.4.0"
```

Then you can use `literate` method of `File` to generate docs for your sources. For example:

```scala
import ohnosequences.literator.lib._

new File("src/main/scala/").literate(Some(new File("docs/src/")))
```

Some nice features:

- it provides convenient list of links definitions for internal references 
- it (optionally) generates index of the files in the given directory and appends it to each produced markdown file 

See [it's source documentation][lib/package] for more details.


## Command line application

To use this tool from the command line, download jar from [releases](https://github.com/laughedelic/literator/releases) and run it like

```bash
java -jar literator-app-0.4.0.jar  src/main/scala/  docs/src/
```

or create a wrapper:
```bash
#!/bin/sh
java -jar literator-app-0.4.0.jar "$@"
```
then do `chmod a+x literator` and you can do `./literator  src/main/scala/  docs/src/`.

See [it's source documentation][app/LiteratorApp] for a bit more information.

> So far it's very primitive app, but I'm planning to add some nice [scallop](https://github.com/scallop/scallop) options parsing and distribute it using [conscript](https://github.com/n8han/conscript).


## Sbt plugin

To use this tool from sbt console, add the following to your `project/plugins.sbt`:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

addSbtPlugin("ohnosequences" % "literator-plugin" % "0.4.0")
```

And this to your `build.sbt`:

```scala
literatorSettings
```

Now you can set `docsMap` key, which contains `Map[File, File]` mapping between source directories and output documentation directories. By default it's just `Map(file("src/") -> file("docs/src/"))`. Using this map you can easily generate docs for several subprojects (like in this Literator itself).

To run Literator from sbt, just use `generateDocs` task.


### Cleaning docs

If you change something in your source files hierarchy, you should clean old generated docs. In this case I recommend adding the following to your `build.sbt`:

```scala
cleanFiles ++= docsOutputDirs.value
```

With this setting, docs directory will be clean every time you call `clean` task. Ensure that your docs output directories don't contain any non-generated files.


### Release process integration

If you use [sbt-release](https://github.com/sbt/sbt-release) plugin, you can add docs generation step as follows (in `build.sbt`):

```scala
import sbtrelease._
import ReleaseKeys._

// ... your settings

literatorSettings

lazy val genDocsForRelease = ReleaseStep({st => Project.extract(st).runTask(generateDocs, st)._1 })

releaseProcess := genDocsForRelease +: releaseProcess.value
```

This will add docs generation step to the beginning of release process. It's easy to add it somewhere else, see [sbt-release documentation](https://github.com/sbt/sbt-release) for details.


## Demo/Documentation

You can see the result of running Literator on it's own sources in the [docs/src/](docs/src/) folder. The most interesting part (in terms of content and formatting) is [LiteratorParsers.scala][lib/LiteratorParsers] source.


## FAQ

#### Why not docco?

Of course, there are plenty of [docco](http://jashkenas.github.io/docco/)-like tools, which generate htmls from your sources (also using markdown), but there are several reasons, why I don't like them.

- first of all, there is no normal Scala-clone of such tool and this is not nice, because I want to integrate this into normal release process of the Scala projects I develop;
- secondly, I want to keep things simple, and I like markdown as an "intermediate" format, for example it's handy to have just markdown documents on github, as it will render them nicely, and then generate from them htmls for a web-site, if needed, using your favourite tool and templates;
- finally, most of such tools support only one-line comments and ignore block comments, while I want the opposite: write comments as a normal text and have ignored small comments in code;
- and yes, it's "quick and dirty" — I don't like such things, better to have something simple, but nice.


[lib/FileUtils]: docs/src/lib/FileUtils.scala.md
[lib/LanguageMap]: docs/src/lib/LanguageMap.scala.md
[lib/LiteratorParsers]: docs/src/lib/LiteratorParsers.scala.md
[lib/package]: docs/src/lib/package.scala.md
[app/LiteratorApp]: docs/src/app/LiteratorApp.scala.md
[plugin/LiteratorPlugin]: docs/src/plugin/LiteratorPlugin.scala.md
