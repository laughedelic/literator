/*
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
resolvers += "Era7 releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" %% "literator" % "0.2.0"
```

Then you can use `literateFile` or `literateDir` functions to generate docs for your sources. For example:

```scala
import ohnosequences.tools.Literator._

literateDir(new File("src/main/scala/"), "docs/code/")

literateFile(new File("src/main/scala/Literator.scala"), "Readme.md")
```

See ["Working with files"](#working-with-files) section for more details.


### Command line

To use this tool from command line, download the jar from [releases](https://github.com/laughedelic/literator/releases) and run it like

```bash
java -jar literator-0.2.0.jar  src/main/scala/  docs/code/
```

or create a wrapper:
```bash
#!/bin/sh
java -jar literator-0.2.0.jar "$@"
```
then do `chmod a+x literator` and you can do `./literator  src/main/scala/  docs/code/`.

See ["Command line interface"](#command-line-interface) section for a bit more information.


## The code

This file is the result of running Literator on it's own source file. The code is pretty straightforward and may be doesn't need much comments, but I use it just as a demonstration and test.


### Parsers

We will use parser combinators from the standard Scala library.
*/

package ohnosequences.tools

import scala.util.parsing.combinator._
import java.io._

case class LiteratorParsers(val lang: String = "scala") extends RegexParsers {

  // By default `RegexParsers` ignore ALL whitespaces in the input
  override def skipWhitespace = false

  /* A _chunk_ of source is either a block comment, or code */
  trait Chunk
  case class Comment(str: String) extends Chunk
  case class Code(str: String) extends Chunk


  /*~ Here are some useful generic parsers.
    ~ May be there are standard ones like this — I didn't find. */
  def eol:    Parser[String] = "\n"
  def spaces: Parser[String] = regex("""\s*""".r)
  def char:   Parser[String] = regex(".".r) // any symbol except EOL
  def many1(p: => Parser[String]): Parser[String] = p.+ ^^ (_.mkString)
  def many (p: => Parser[String]): Parser[String] = p.* ^^ (_.mkString)
  def emptyLine: Parser[String] = """^[ \t]*""".r ~> eol
  def anythingBut[T](p: => Parser[T]): Parser[String] = guard(not(p)) ~> (char | eol)


  /*  Here are the parsers for the comment opening and closing braces.
      One can override them, if it's needed for support of another language. 
      They return the offset of the braces, which will be useful later. */
  def commentStart = spaces <~ "/*" ^^ { _ + "  "}
  def commentEnd   = spaces <~ "*/"

  /*` Using `escapedCode` parser we can ignore escaped closing 
    ` comment brace inside of a comment. 
    ` 
    ` _Note:_ the only limitation is that you cannot use an escaped
    `  block of code with a closing comment brace inside of a 
    `  comment _with margin_.
    */
  def escapedCodeWith(esc: String) = esc ~> many(anythingBut(esc)) <~ esc ^^ { esc+_+esc }
  def escapedCode = escapedCodeWith("```") | 
                    escapedCodeWith("`")

  /*  When parsing block comments, we care about indentation and this is the
      only complex part of this code.

      Anyway, we need some convention on how to use comments with indentation:
      - if it's a _one line_ block comment, surrounding spaces are trimmed;
      - if right after the opening comment brace there is a _symbol with a space_,
        then it's treated as a margin delimiter and the following lines should
        start from any number of spaces and this delimiter — when parsed, it will
        be cutted off;
      - otherwise, nothing special happens, the result will be just everything 
        inside the comment braces.

      You can use almost any symbol as a margin delimiter. Take a look at the 
      [literator sources](src/main/scala/Literator.scala) for examples.

      _Note:_ you can use space as a delimiter, just put _two_ spaces after the
      opening comment brace and remember to indent the following lines to the 
      same level. See this comment in the source for example.
  */
  def comment: Parser[Comment] = {
    import java.util.regex.Pattern.quote

    def inner = escapedCode | anythingBut(commentEnd | eol)

    commentStart >> { offset => (
      spaces ~> many(inner) <~ commentEnd        // there is only one line
    | ". ".r.? >> {                              // delimiter convention: any char + space
        case None => eol.? ~>                    // if it starts from a newline, skip it
                              many(inner | eol)  // and just read everything
        case Some(delim) => {
          def margin = quote(offset + delim).r
                    (many(inner) <~ eol) ~       // rest of the line after delimiter
          (margin ~> many(inner) <~ eol.?        // other lines with the margin
                     | (emptyLine ^^^ "")).* ^^  // which can be empty
            { mkList(_).mkString("\n") }
        }
      } <~ commentEnd 
    )} ^^ Comment
  }

  /*. When parsing code blocks we should remember, that it
    . can contain a comment-opening brace inside of a string.
    . 
    . _Note:_ only double-quoted one-line strings are handled.
    */
  def code: Parser[Code] =
    many1("\".*/\\*.*\"".r | anythingBut(emptyLine.* ~ commentStart)) ^^ Code


  /*- Finally, we parse source as a list of chunks and
    - transform it to markdown, surrounding code blocks 
    - with markdown backticks syntax.
    */
  def chunk: Parser[Chunk] = code | comment

  def source: Parser[List[Chunk]] =
    (emptyLine.* ~> chunk).* <~ emptyLine.*

  def markdown: Parser[String] = source ^^ {
    _.map{
      case Comment(str) => str
      case Code(str) => if (str.isEmpty) ""
        else Seq( ""
                , "```"+lang
                , str
                , "```"
                , "").mkString("\n")
    }.mkString("\n")
  }

}


/* ### Working with files */

object Literator {

  // TODO: determine language from the file extension
  val literator = LiteratorParsers("scala")

  // traverses recursively given directory and lists all files
  def getFileTree(f: File): List[File] =
    f :: (if (f.isDirectory) f.listFiles.toList.flatMap(getFileTree) 
          else List())

  def writeFile(file: String, text: String) = {
    Some(new PrintWriter(file)).foreach{p => p.write(text); p.close}
  }

  /*- This is the key function. It takes a source file, tries to parse it
    - and either outputs the result, or writes it to the specified destination. 
    */
  def literateFile(f: File, destName: String = ""): literator.ParseResult[String] = {
    val src = scala.io.Source.fromFile(f).mkString

    val result = literator.parseAll(literator.markdown, src)
    result map {
      if (destName.isEmpty) print 
      else { text =>
        val destDir = new File(destName).getParentFile
        // `.getParentFile` may return null if you give it just a file name
        if (destDir != null && !destDir.exists) destDir.mkdirs
        writeFile(destName, text) 
      }
    }
    return result
  }

  /*` This function is a wrapper, convenient for projects. It takes 
    ` the base source directory, destination path, then takes each 
    ` source file, tries to parse it, writes result to the destination
    ` and returns the list parsing results.
    ` _Note:_ that it preserves the structure of the source directory.
    */
  def literateDir(srcBase: File, docsDest: String = ""): List[literator.ParseResult[String]] = {
    getFileTree(srcBase).filter(_.getName.endsWith(".scala")) map { f =>

      // constructing name for the output file, creating directories, etc.
      val relative = srcBase.toURI.relativize(f.toURI).getPath.toString
      val destDir = if (docsDest.isEmpty) "docs" else docsDest
      val dest = destDir.stripSuffix("/") +"/"+ relative
      val destName = dest.stripSuffix(".scala")+".md"
      literateFile(f, destName)

    }
  }

}

/*
### Command line interface

* Input: 
  + a directory with sources or a single file
  + (optional) destination: path prefix or a file name respectively
* Output:
  + if second argument is given, file(s) with documentation
  + if not, it's set to `docs/` or `stdout` respectively
*/

object LiteratorCLI extends App {
  if (args.length < 1) sys.error("Need at least one argument")
  else {
    val inp = new File(args(0))
    val dest = if (args.length > 1) args(1) else ""
    if (inp.isDirectory) Literator.literateDir(inp, dest)
    else Literator.literateFile(inp, dest)
  }
}
