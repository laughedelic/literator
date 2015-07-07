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


## Usage

Just add following line to your `project/plugins.sbt`:

```scala
addSbtPlugin("laughedelic" % "literator" % "<version>")
```

You can find the latest version in the list of [releases](https://github.com/laughedelic/literator/releases).

### Setting keys

|               Key |       Type        | Default                  |
|------------------:|:-----------------:|:-------------------------|
|         `docsMap` | `Map[File, File]` | `src/` → `docs/src/`     |
|  `docsOutputDirs` |    `Seq[File]`    | same as `docsMap` values |
|    `docsAddIndex` |     `Boolean`     | `false`                  |
| `docsCleanBefore` |     `Boolean`     | `true`                   |

You can set `docsMap` key in your `build.sbt` to map source directories to output documentation directories. Using this map you can easily generate docs for several subprojects or integrate certain parts of the source documentation in the general docs (especially if you're using some service like [Read the Docs](https://readthedocs.org)).

To run Literator from sbt, use `generateDocs` task.

Note that output directories are _cleaned up_ before generating docs (you can turn it off with `docsCleanBefore := false`), so be careful if you're mixing generated docs with handwritten.

> TODO: write about index and internal links usage


### Release process integration

If you use [sbt-release](https://github.com/sbt/sbt-release) plugin, you can add docs generation step. See sbt-release documentation for details.


## Example/Demo

If you have a piece of code like this (it's from the literator sources):

```scala
/* ### Block comments parsing */

/* - When parsing the comment opening brace, we remember the offset for the content
     Note that [scaladoc-style comments](http://docs.scala-lang.org/style/scaladoc.html)
     are ignored.
*/
def commentStart: PS =
  spaces ~ (lang.comment.start ^^ { _.replaceAll(".", " ") }) ~
  (((spaces ~ guard(eol)) ^^^ None) | // if the first row is empty, ignore it
   (guard(not("*")) ~> " ".?)) ^^     // not scaladoc and an optional space
  { case sps ~ strt ~ sp => sps + strt + sp.getOrElse("") }

/* - Closing comment brace is just ignored */
def commentEnd: PS = spaces ~ lang.comment.end ^^^ ""
```

Then it will be transformed into the following markdown:

----

### Block comments parsing

- When parsing the comment opening brace, we remember the offset for the content
  Note that [scaladoc-style comments](http://docs.scala-lang.org/style/scaladoc.html)
  are ignored.

```scala
def commentStart: PS =
  spaces ~ (lang.comment.start ^^ { _.replaceAll(".", " ") }) ~
  (((spaces ~ guard(eol)) ^^^ None) | // if the first row is empty, ignore it
   (guard(not("*")) ~> " ".?)) ^^     // not scaladoc and an optional space
  { case sps ~ strt ~ sp => sps + strt + sp.getOrElse("") }
```

- Closing comment brace is just ignored

```scala
def commentEnd: PS = spaces ~ lang.comment.end ^^^ ""
```

----

You can see the result of running Literator on its own sources in the [docs/src/](docs/src/) folder.



## FAQ

#### Why not docco?

Of course, there are plenty of [docco](http://jashkenas.github.io/docco/)-like tools, which generate htmls from your sources (also using markdown), but there are several reasons, why I don't like them.

- first of all, there is no normal Scala-clone of such tool and this is not nice, because I want to integrate this into normal release process of the Scala projects I develop;
- secondly, I want to keep things simple, and I like markdown as an "intermediate" format, for example it's handy to have just markdown documents on github, as it will render them nicely, and then generate from them htmls for a web-site, if needed, using your favourite tool and templates;
- finally, most of such tools support only one-line comments and ignore block comments, while I want the opposite: write comments as a normal text and have ignored small comments in code;
- and yes, it's "quick and dirty" — I don't like such things, better to have something simple, but nice.
