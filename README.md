Literator
=========

This is a very simple program, which reads a source file and _transforms block comments into normal text and surrounds code with markdown backticks syntax_. So the aim is just to get a readable document from a code, which is written in more or less [literate programming](http://en.wikipedia.org/wiki/Literate_programming) style. The name is like "a thing which makes your sources literate", i.e. helps to use literate programming when it's not supported by the language.

So you can write your code and use markdown syntax in comments (which keeps your sources readable), and then transform it to a markdown document, from which you can generate a nice _html_ or _pdf_ or whatever else, using you [favourite markdown processor](http://johnmacfarlane.net/pandoc/).

The tool is written (in Scala and) first of all for Scala, because it doesn't have normal support for literate programming. But the code can be easily abstracted over language — [open an issue](https://github.com/laughedelic/literator/issues/new), if you want support for something else.


#### Why not docco?

Of course, there are plenty of [docco](http://jashkenas.github.io/docco/)-like tools, which generate htmls from your sources (also using markdown), but there are several reasons, why I don't like them.
- first of all, there is no normal Scala-clone of such tool and this is not nice, because I want to integrate this into normal release process of the Scala projects I develop;
- secondly, I want to keep things simple, and I like markdown as an "intermediate" format, for example it's handy to have just markdown documents on github, as it will render them nicely, and then generate from them htmls for a web-site, if needed, using your favourite tool and templates;
- finally, most of such tools support only one-line comments and ignore block comments, while I want the opposite: write comments as a normal text and have ignored small comments in code;
- and yes, it's "quick and dirty" — I don't like such things, better to have something simple, but nice.


## Usage


### SBT dependency

To use it in Scala project add this dependency to your `build.sbt`:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" %% "literator" % "0.3.0"
```

Then you can use `literateFile` or `literateDir` functions to generate docs for your sources. For example:

```scala
import java.io._
import ohnosequences.tools.Literator._

literateDir(new File("src/main/scala/"), new File("docs/code/"))

literateFile(new File("src/main/scala/MainSource.scala"), "Readme.md")
```

See ["Working with files"](docs/src/main/scala/Literator.md) section for more details.


### Command line

To use this tool from command line, download the jar from [releases](https://github.com/laughedelic/literator/releases) and run it like

```bash
java -jar literator-0.3.0.jar  src/main/scala/  docs/code/
```

or create a wrapper:
```bash
#!/bin/sh
java -jar literator-0.3.0.jar "$@"
```
then do `chmod a+x literator` and you can do `./literator  src/main/scala/  docs/code/`.

See ["Command line interface"](docs/src/main/scala/LiteratorCLI.md) section for a bit more information.


## Demo/Documentation

You can see the result of running Literator on it's own sources in the [docs](docs) directory. The most interesting part (in terms of content and formatting) is [LiteratorParsers.scala](docs/src/main/scala/LiteratorParsers.md).
