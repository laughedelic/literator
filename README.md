Literator
=========

[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/laughedelic/literator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

This is a simple tool, which **transforms your sources into markdown documentations for themselves**. It just reads a source file, turns block comments into normal text and surrounds code with markdown backticks syntax. So the aim is just to get a readable document from a code, which is written in more or less [literate programming](http://en.wikipedia.org/wiki/Literate_programming) style. The name is like "a thing which makes your sources literate", i.e. helps to use literate programming when it's not really supported by the language.

So you can write your code and use markdown syntax in comments (which keeps your sources readable), and then transform it to a markdown document, from which you can generate a nice _html_ or _pdf_ or whatever else, using [your favourite markdown processor](http://johnmacfarlane.net/pandoc/).

The tool is written in Scala and first of all for Scala, because it doesn't have normal support for literate programming. But it should work for [some other languages][lib/LanguageMap] — ~~randomly chosen~~ the most popular, of course. [Open an issue](https://github.com/laughedelic/literator/issues/new), if you want support for something else.

If you're wondering, why don't I use docco instead, see my answer [at the bottom](#why-not-docco) of this file.

> Note: Before `v0.7.0` there was a separate library, command line application and an sbt-plugin.

Some nice features:

- it provides convenient list of links definitions for internal references
- it (optionally) generates index of the files in the given directory and appends it to each produced markdown file


## Sbt dependency

To use this tool from sbt console, add the following to your `project/plugins.sbt`:

```scala
resolvers ++= Seq(
  Resolver.url("laughedelic sbt-plugins", url("http://dl.bintray.com/laughedelic/sbt-plugins"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("laughedelic" % "literator" % "<version>")
```

You can set `Literator.docsMap` key in your `build.sbt`, which contains `Map[File, File]` mapping between source directories and output documentation directories. By default it's just `Map(file("src/") -> file("docs/src/"))`. Using this map you can easily generate docs for several subprojects (like in Literator itself).

To run Literator from sbt, use `generateDocs` task.

Output directories are cleaned up before generating docs (you can turn it off with `docsCleanBefore := false`)

> TODO: list all settings in a table


### Release process integration

If you use [sbt-release](https://github.com/sbt/sbt-release) plugin, you can add docs generation step. See sbt-release documentation for details.


## Demo/Documentation

You can see the result of running Literator on its own sources in the [docs/src/](docs/src/) folder.


## FAQ

#### Why not docco?

Of course, there are plenty of [docco](http://jashkenas.github.io/docco/)-like tools, which generate htmls from your sources (also using markdown), but there are several reasons, why I don't like them.

- first of all, there is no normal Scala-clone of such tool and this is not nice, because I want to integrate this into normal release process of the Scala projects I develop;
- secondly, I want to keep things simple, and I like markdown as an "intermediate" format, for example it's handy to have just markdown documents on github, as it will render them nicely, and then generate from them htmls for a web-site, if needed, using your favourite tool and templates;
- finally, most of such tools support only one-line comments and ignore block comments, while I want the opposite: write comments as a normal text and have ignored small comments in code;
- and yes, it's "quick and dirty" — I don't like such things, better to have something simple, but nice.
