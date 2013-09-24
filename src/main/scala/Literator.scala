/*

#Literator


This is a very simple program, which reads a source code file and transforms block comments into normal text and surrounds code with special syntax. So the aim is just to get a readable document from a code, which is written in more or less [literate programming](http://en.wikipedia.org/wiki/Literate_programming) style. So the name is like "a thing which makes your sources literate", i.e. helps to use literate programming when it's not supported by the language.

So you can write your code and use markdown syntax in comments (which keeps your sources readable), and then transform it to a markdown document, from which you can generate a nice _html_ or _pdf_ or whatever else, using you [favourite markdown processor](http://johnmacfarlane.net/pandoc/).

The tool is written in Scala and first of all for Scala, because Scala doesn't have normal support for literate programming.


### Docco

Of course, there are plenty of [docco](http://jashkenas.github.io/docco/)-like tools, which generate htmls from your sources (also using markdown), but there are several reasons, why I don't like them.
- first of all, there is no normal Scala-clone of such tool and this is not nice, because I want to integrate this into normal release process of the Scala projects I develop;
- secondly, I want to keep things simple, and I like markdown as an "intermediate" format, for example it's handy to have just markdown documents on github, as it will render them nicely, and then generate from them htmls for a web-site, if needed, using your favourite tool and templates;
- finally, most of such tools support only one-line comments and ignore block comments, while I want the opposite: write comments as a normal text and have ignored small comments in code.

*/

/*

## The code

We will use parser combinators from the standard Scala library

*/

package laughedelic.tools

import scala.util.parsing.combinator._

/* This class extends `RegexParsers` and has some parameters, such as language: */
case class LiteratorParsers(val lang: String = "scala") extends RegexParsers {

  /* By default `RegexParsers` ignore all whitespaces in the input. */
  override def skipWhitespace = false

  /* Type aliases for readability. */
  type Docs = String
  type Code = String

  /* Here are some useful generic parsers.
   * May be there are standard ones like this — I didn't find.
   */
  def eol: Parser[String] = "\n"
  def space: Parser[String] = regex("""[ \t]*""".r)
  def anythingBut[T](p: => Parser[T]): Parser[String] = guard(not(p)) ~> (".".r | eol)
  def many(p: => Parser[String]): Parser[String] = p.* ^^ (_.mkString)
  def emptyLine: Parser[String] = """^[ \t]*""".r ~> eol

  /* Parsing block comments is easy: */
  def docs: Parser[Docs] =
    emptyLine.* ~> space ~>
    "/*" ~> many(anythingBut("*/")) <~ "*/" <~
    space <~ emptyLine.* ^^ { _.stripMargin('*') }

  /* When parsing code we should remember, that it
   * can contain a comment-opening sequence inside of a string.
   * (Note: only double-quoted strings are handled)
   */
  def code: Parser[Code] =
    emptyLine.* ~>
    (many( "\".*/\\*.*\"".r | anythingBut("/*" | eol) ) <~ eol).* ^^ {
      _.reverse.dropWhile(_.trim.isEmpty).reverse.
        mkString("\n")
    }

  /* A "chunk" of source is a pair of text and following code.
   * But the source can start with code, so `source` parsers
   * checks if that's the case.
   */
  def chunk: Parser[(Docs, Code)] =
    docs ~ code ^^ { case p ~ c => (p,c) }

  def source: Parser[List[(Docs, Code)]] =
      code.? ~ chunk.* ^^ {
        case Some(c) ~ rest => ("", c) :: rest
        case      _  ~ rest => rest
      }

  /* This function trims the whitespace around code and surrounds
   * it with markdown back-ticks syntax
   */
  def surroundCode(c: Code): String = {
    if (c.isEmpty) ""
    else s"""|```${lang}
             |${c}
             |```""".stripMargin
  }

  /* Finally, we transform a list of source _chunks_ into markdown */
  def markdown: Parser[String] = source ^^ { l =>
    ("" /: l) { case (acc, (doc, code)) =>
      acc +
      (if (doc.isEmpty) "" else "\n\n"+ doc) +
      (if (code.isEmpty) "" else "\n\n"+ surroundCode(code))
    }
  }

}

/*

### Command line interface

It just takes file name and outputs the result.

*/
object Main extends App {
  val lit = LiteratorParsers()
  val text = scala.io.Source.fromFile(args(0)).mkString
  print( lit.parse(lit.markdown, text) )
}
