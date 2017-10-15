## Parsers

```scala
package laughedelic.literator.lib

import scala.util.parsing.combinator._

case class LiteratorParsers(val lang: Language) extends RegexParsers {

  // By default `RegexParsers` ignores ALL whitespaces in the input
  override def skipWhitespace = false
```

A _chunk_ of source is either a block comment, or code

```scala
  sealed trait Chunk
  case class Comment(str: String) extends Chunk
  case class Code(str: String) extends Chunk
```

### Some useful generic parsers

```scala
  type PS = Parser[String]

  // implicit class UsefulCombinators(p: PS) {
  //   // combine two parsers and concatenate their results
  //   def ~+[T](q: Parser[Option[T]]) = p ~ q ^^ { case a ~ b => a + b.getOrElse("").toString }
  //   def ~+[T](q: Parser[List[T]])   = p ~ q ^^ { case a ~ b => a + b.mkString }
  //   def ~+(q: Parser[String])       = p ~ q ^^ { case a ~ b => a + b }
  // }

  def eol:    PS = "\r".? ~> "\n"
  def spaces: PS = regex("""[ \t]*""".r)
  def char:   PS = regex(".".r) // any symbol except EOL
  def emptyLine: PS = spaces ~> eol
  def anythingBut[T](p: => Parser[T]): PS = guard(not(p)) ~> (char | eol)
```

This parser is one of the most useful:

```scala
  def surrounded(left: PS, right: PS, inner: PS = char, offset: String = "") = {
    left ~ (guard(not(right)) ~> inner).* ~ right ^^ 
    { case l ~ x ~ r => (l + x.mkString + r).replaceAllLiterally("\n"+offset, "\n") }
  }
```

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

- Comments can look like this:

  ```
  /* Just a block comment on one line */

  /* Margin is determined by the opening comment brace identation
     and an optional one space (like in this case).
     Next lines should have at least the same identation. */
   
  /*
    Alternatively you can start text from the next line.
    The rule is simple: at least the identation of the 
    opening comment brace.
  */
  ```

Note, that all this identation business is _not strict_, i.e. the offset 
is just stripped after parsing, so if you have a line having less identation, 
it will have "literal" identation, as you see in the code.


```scala
  def comment: Parser[Comment] = {
    // Inside of a block comment we can have 
    def inner: PS =
      // escaped source code
      surrounded("```", "```", char | eol) | 
      surrounded("`", "`") |
      // inner comments
      surrounded(lang.comment.start, lang.comment.end, char | eol) |
      // or anything else
      char | eol

    commentStart >> { offset => surrounded("", commentEnd, inner, offset) } ^^ Comment
  }
```

### Code blocks parsing
When parsing code blocks we should remember, that it
can contain a comment-opening brace inside of a string.
 
Also block comments may appear isinde of inline comments 
(which we treat as code).


```scala
  def code: Parser[Code] = {
    def string: PS = 
      surrounded("\"\"\"", "\"\"\"", char | eol) | // three quotes string
      surrounded("\"", "\"", "\\\"" | char)        // normal string may contain escaped quote

    def lineComment: PS = surrounded(lang.comment.line, eol)

    (string | lineComment | anythingBut(emptyLine.* ~ commentStart)).+ ^^ { _.mkString } ^^ Code
  }
```

### Sources parsing

Finally, we parse source as a list of chunks and
transform it to markdown, surrounding code blocks 
with markdown backticks syntax.


```scala
  def chunk: Parser[Chunk] = (code: Parser[Chunk]) | (comment: Parser[Chunk])

  def source: Parser[List[Chunk]] =
    (emptyLine.* ~> chunk).* <~ emptyLine.*

  def markdown: PS = source ^^ {
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

+ scala
  + lib
    + [FileUtils.scala][lib/FileUtils.scala]
    + [LanguageMap.scala][lib/LanguageMap.scala]
    + [LiteratorParsers.scala][lib/LiteratorParsers.scala]
    + [package.scala][lib/package.scala]
  + plugin
    + [LiteratorPlugin.scala][plugin/LiteratorPlugin.scala]

[lib/FileUtils.scala]: FileUtils.scala.md
[lib/LanguageMap.scala]: LanguageMap.scala.md
[lib/LiteratorParsers.scala]: LiteratorParsers.scala.md
[lib/package.scala]: package.scala.md
[plugin/LiteratorPlugin.scala]: ../plugin/LiteratorPlugin.scala.md
[Readme.md]: ../Readme.md.md