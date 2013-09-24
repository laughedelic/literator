/*
Literator
=========

This is a very simple program, which reads a source code file and transforms block comments into normal text and surrounds code with special syntax. So the aim is just to get a readable document from a code, which is written in more or less [literate programming](http://en.wikipedia.org/wiki/Literate_programming) style. So the name is like "a thing which makes your sources literate", i.e. helps to use literate programming when it's not supported by the language.

So you can write your code and use markdown syntax in comments (which keeps your sources readable), and then transform it to a markdown document, from which you can generate a nice _html_ or _pdf_ or whatever else, using you [favourite markdown processor](http://johnmacfarlane.net/pandoc/).

The tool is written in Scala and first of all for Scala, because Scala doesn't have normal support for literate programming.


### Docco

Of course, there are plenty of [docco](http://jashkenas.github.io/docco/)-like tools, which generate htmls from your sources (also using markdown), but there are several reasons, why I don't like them.
- first of all, there is no normal Scala-clone of such tool and this is not nice, because I want to integrate this into normal release process of the Scala projects I develop;
- secondly, I want to keep things simple, and I like markdown as an "intermediate" format, for example it's handy to have just markdown documents on github, as it will render them nicely, and then generate from them htmls for a web-site, if needed, using your favourite tool and templates;
- finally, most of such tools support only one-line comments and ignore block comments, while I want the opposite: write comments as a normal text and have ignored small comments in code.


## The code

This file is the result of running Literator on it's own source file. The code is pretty straightforward and may be doesn't need much comments, but I use it just as a demonstration and test.


### Parsers

We will use parser combinators from the standard Scala library.
*/

package laughedelic.tools

import scala.util.parsing.combinator._

case class LiteratorParsers(val lang: String = "scala") extends RegexParsers {

  // By default `RegexParsers` ignore ALL whitespaces in the input
  override def skipWhitespace = false

  // Type aliases for readability
  type Docs = String
  type Code = String

  /*~ Here are some useful generic parsers.
    ~ May be there are standard ones like this — I didn't find.
    */
  def eol:    Parser[String] = "\n"
  def spaces: Parser[String] = regex("""\s*""".r)
  def char:   Parser[String] = regex(".".r) // any symbol except EOL
  def many(p: => Parser[String]): Parser[String] = p.* ^^ (_.mkString)
  def emptyLine: Parser[String] = """^[ \t]*""".r ~> eol
  def anythingBut[T](p: => Parser[T]): Parser[String] = guard(not(p)) ~> (char | eol)

  /** When parsing block comments, we care about identation, so there is a convention:
    * - if it's a _one line_ block comment, surrounding spaces are trimmed;
    * - if right after the opening comment brace there is a _symbol with a space_,
    *   then it's treated as a margin delimiter and the following lines should start
    *   from any number of spaces and then this delimiter — when parsed, it will be
    *   cutted off;
    * - otherwise, nothing special happens, the result will be just everything inside
    *   the comment braces.
    * 
    * You can use any symbol for the margin delimeter. Take a look at the 
    * `Literator.scala` source file for examples.
    */
  def docs: Parser[Docs] = {
    import java.util.regex.Pattern.quote

    // TODO: what if we want to mention comment braces inside of a comment?
    def innerSymb = anythingBut("*/" | eol) // symbols inside one line of the comment

    spaces ~> "/*" ~> (
      many(innerSymb) <~ "*/" ^^ { s => (s.trim)+"\n" } // only one line
    | """\S """.r.? ~                                   // or maybe a margin symbol
      many(innerSymb | eol) <~ "*/" ^^ {                // and then whatever
        case Some(m) ~ text => text.replaceAll("""(?m)^\s*"""+quote(m), "")
        case       _ ~ text => text
      }
    )
  }

  /*. When parsing code blocks we should remember, that it
    . can contain a comment-opening brace inside of a string.
    . (Note: only double-quoted strings are handled)
    . */
  def code: Parser[Code] =
    emptyLine.* ~>
    (many( "\".*/\\*.*\"".r | anythingBut("/*" | eol) ) <~ eol).* ^^ {
      _.reverse.dropWhile(_.trim.isEmpty).reverse.
        mkString("\n")
    }


  /*| A source is a set of "chunks", which are just pairs of text 
    | and following code.
    | 
    | But the source can start just with code, so `source` parser
    | checks if that's the case and adds an ampty text if needed.
    */
  def chunk: Parser[(Docs, Code)] =
    docs ~ code ^^ { case d ~ c => (d,c) }

  def source: Parser[List[(Docs, Code)]] =
      code.? ~ chunk.* ^^ {
        case Some(c) ~ rest => ("", c) :: rest
        case      _  ~ rest => rest
      }


  /*- Finally, we transform the list of source _chunks_ into markdown,
    - surrounding code blocks with markdown back-ticks syntax.
    */
  def markdown: Parser[String] = source ^^ { l =>
    def surroundCode(c: Code) =
      if (c.isEmpty) ""
      else s"\n\n```${lang}\n${c}\n```\n\n\n"

    ("" /: l) { case (acc, (docs, code)) =>
      acc + docs + surroundCode(code)
    }
  }

}

/*
### Command line interface

It just takes file name and outputs the result.
*/
object Main extends App {
  // TODO: determine language from the file extension
  val lit = LiteratorParsers()
  val text = scala.io.Source.fromFile(args(0)).mkString
  print( lit.parse(lit.markdown, text) )
}
