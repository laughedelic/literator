## Parsers

```scala
package ohnosequences.tools.literator

import scala.util.parsing.combinator._

case class LiteratorParsers(val lang: Language) extends RegexParsers {

  // By default `RegexParsers` ignore ALL whitespaces in the input
  override def skipWhitespace = false
```

A _chunk_ of source is either a block comment, or code

```scala
  trait Chunk
  case class Comment(str: String) extends Chunk
  case class Code(str: String) extends Chunk
```

Here are some useful generic parsers.
May be there are standard ones like this — I didn't find.

```scala
  def eol:    Parser[String] = "\n"
  def spaces: Parser[String] = regex("""\s*""".r)
  def char:   Parser[String] = regex(".".r) // any symbol except EOL
  def many1(p: => Parser[String]): Parser[String] = p.+ ^^ (_.mkString)
  def many (p: => Parser[String]): Parser[String] = p.* ^^ (_.mkString)
  def emptyLine: Parser[String] = """^[ \t]*""".r ~> eol
  def anythingBut[T](p: => Parser[T]): Parser[String] = guard(not(p)) ~> (char | eol)
```

Here are the parsers for the comment opening and closing braces.
One can override them, if it's needed for support of another language. 
They return the offset of the braces, which will be useful later.

```scala
  def commentStart = spaces <~ lang.comment.start ^^ { _ + "  "}
  def commentEnd   = spaces <~ lang.comment.end
```

Using `escapedCode` parser we can ignore escaped closing 
comment brace inside of a comment. 

_Note:_ the only limitation is that you cannot use an escaped
 block of code with a closing comment brace inside of a 
 comment _with margin_.

```scala
  def escapedCodeWith(esc: String) = esc ~> many(anythingBut(esc)) <~ esc ^^ { esc+_+esc }
  def escapedCode = escapedCodeWith("```") | 
                    escapedCodeWith("`")
```

When parsing block comments, we care about indentation and this is the
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

```scala
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
```

When parsing code blocks we should remember, that it
can contain a comment-opening brace inside of a string.
So code is just strings or anything but comment.

```scala
  def str: Parser[String] =
    ("\"\"\"" | "\"") >> { q => many("\\\"" | anythingBut(q)) <~ q ^^ { q+_+q } }

  def code: Parser[Code] =
    many1(str | anythingBut(emptyLine.* ~ commentStart)) ^^ Code
```

Finally, we parse source as a list of chunks and
transform it to markdown, surrounding code blocks 
with markdown backticks syntax.

```scala
  def chunk: Parser[Chunk] = code | comment

  def source: Parser[List[Chunk]] =
    (emptyLine.* ~> chunk).* <~ emptyLine.*

  def markdown: Parser[String] = source ^^ {
    _.map{
      case Comment(str) => str
      case Code(str) => if (str.isEmpty) ""
        else Seq( ""
                , "```"+lang.syntax
                , str
                , "```"
                , "").mkString("\n")
    }.mkString("\n")
  }

}

```


------

### Index

+ src
  + main
    + scala
      + [FileUtils.scala](FileUtils.scala.md)
      + [LanguageMap.scala](LanguageMap.scala.md)
      + [LiteratorCLI.scala](LiteratorCLI.scala.md)
      + [LiteratorParsers.scala](LiteratorParsers.scala.md)
      + [package.scala](package.scala.md)
  + test
    + scala
      + [TestCode.scala](../../test/scala/TestCode.scala.md)
