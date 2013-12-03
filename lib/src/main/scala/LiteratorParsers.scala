/* ## Parsers */

package laughedelic.literator.lib

import scala.util.parsing.combinator._

case class LiteratorParsers(val lang: Language) extends RegexParsers {

  // By default `RegexParsers` ignores ALL whitespaces in the input
  override def skipWhitespace = false

  /* A _chunk_ of source is either a block comment, or code */
  trait Chunk
  case class Comment(str: String) extends Chunk
  case class Code(str: String) extends Chunk

  /* Here are some useful generic parsers. */
  type PS = Parser[String]

  implicit class UsefulCombinators(p: PS) {
    // combine two parsers and concatenate their results
    def ~+[T](q: Parser[T]) = p ~ q ^^ { 
      case a ~     (b: String) => a + b
      case a ~ Some(b: String) => a + b
      case a ~   (bs: List[_]) => a + bs.mkString
      case a ~               _ => a
    }
  }

  def eol:    PS = "\n"
  def spaces: PS = regex("""[ \t]*""".r)
  def char:   PS = regex(".".r) // any symbol except EOL
  def emptyLine: PS = spaces ~> eol
  def anythingBut[T](p: => Parser[T]): PS = guard(not(p)) ~> (char | eol)

  /* This parser is one of the most useful: */
  def surrounded(left: PS, right: PS, inner: PS = char, offset: String = "") = {
    left ~+ (guard(not(right)) ~> inner).* ~+ right ^^ 
    { _.replaceAllLiterally("\n"+offset, "\n") }
  }


  /* When parsing the comment opening, we remember the offset for the content
     Note that [scaladoc-style comments](http://docs.scala-lang.org/style/scaladoc.html) 
     are ignored.
  */
  def commentStart = spaces ~+ (lang.comment.start ^^ { _.replaceAll(".", " ") }) <~ 
    ((guard(not("*")) | (spaces ~ guard(eol))) ^^^ "") ~+ (" ").?

  /* Closing comment brace is just ignored */
  def commentEnd = spaces ~ lang.comment.end ^^^ ""

  /* Comments can look like this:

     ```
     /* Just a block comment on one line */
     var now = System.currentTimeMillis

     /* Margin is determined by the opening comment brace identation
        and an optional one space (like in this case).
        Next lines should have at least the same identation. */
     val bar = qux(foo)
      
     /*
       Alternatively you can start text from the next line.
       The rule is the same: at least the identation of the 
       opening comment brace + optional space (not in this case).
     */
     lazy val buz = 1/0
     ```

     Here is the parser for it:
  */
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

  /* When parsing code blocks we should remember, that it
     can contain a comment-opening brace inside of a string.
 
     Also block comments may appear isinde of inline comments 
     (which we treat as code).
  */
  def code: Parser[Code] = {
    def string: PS = 
      surrounded("\"\"\"", "\"\"\"", char | eol) | // three quotes string
      surrounded("\"", "\"", "\\\"" | char)        // normal string may contain escaped quote

    def lineComment: PS = surrounded(lang.comment.line, eol)

    (string | lineComment | anythingBut(emptyLine.* ~ commentStart)).+ ^^ { _.mkString } ^^ Code
  }


  /* Finally, we parse source as a list of chunks and
     transform it to markdown, surrounding code blocks 
     with markdown backticks syntax.
  */
  def chunk: Parser[Chunk] = code | comment

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
