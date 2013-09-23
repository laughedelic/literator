package laughedelic.tools

import scala.util.parsing.combinator._

object CodeParsers extends RegexParsers {

  override def skipWhitespace = false

  type Prose = String
  type Code = String

  val lang: String = "scala"

  def eol: Parser[String] = regex("""\n""".r)
  def space: Parser[String] = regex("""\s*""".r)
  def anythingBut[T](p: => Parser[T]): Parser[String] = guard(not(p)) ~> (".".r | eol)
  def many(p: => Parser[String]): Parser[String] = p.* ^^ (_.mkString)
  def inComment: Parser[Prose] = many(anythingBut("*/"))

  def prose: Parser[Prose] =
    space ~>
    "/*" ~> inComment <~ "*/" <~
    space ^^ { _.stripMargin }

  def surroundCode(c: Code): String = {
    val cod = c.trim
    if (cod.isEmpty) cod
    else s"""|```${lang}
             |${cod}
             |```""".stripMargin
  }

  def code: Parser[Code] =
      // '/*' sequence inside of a string
      many( "\".*/\\*.*\"".r | anythingBut("/*") )

  def block: Parser[(Prose, Code)] =
    prose ~ code ^^ { case p ~ c => (p,c) }

  def source: Parser[List[(Prose, Code)]] =
      code.? ~ block.* ^^ {
        case Some(c) ~ rest => ("", c) :: rest
        case      _  ~ rest => rest
      }

  def markdown: Parser[String] = source ^^ { l =>
    ("" /: l) { case (acc, (p,c)) =>
      acc +"\n"+ p +"\n"+ surroundCode(c)
    }
  }

}

object Main extends App {
  import CodeParsers._
  val text = scala.io.Source.fromFile(args(0)).mkString
  println(parse(markdown, text))
}
