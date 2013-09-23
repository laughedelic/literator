def source: Parser[List[String ~ String]] =
  (blockComment ~ code).*

  /*
   |
   |### Bundles
   |
   |This is the heart of Statika library. Bundles...
   |
   |  A bundle is supposed to be a lightweight cover for any kind of "modules" of
   |    a system, such as a tool (program) installer, data, code library, etc.
   |
   */


def blockComment: Parser[String] =
  whiteSpace ~> "/*" ~>
  (insideComment <~ eol).* ~    // lines
  (insideComment <~ "*/") ^^ {  // last
    case lines ~ last => (lines :+ last).mkString("\n").stripMargin
  }


def aa = 3

/*
  */
