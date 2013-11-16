/* ## Language map */

package ohnosequences.tools.literator

case class Language(syntax: String, opening: String, closing: String) 

object LanguageMap {

  def CLike(syntax: String) = Language(syntax, "/*", "*/")
  def PascalLike(syntax: String) = Language(syntax, "(*", "*)")

  val langMap: Map[String, Language] = Map(
        "c"     -> CLike("c")
      , "cpp"   -> CLike("cpp")
      , "cs"    -> CLike("csharp")
      , "m"     -> CLike("objc")
      , "java"  -> CLike("java")
      , "js"    -> CLike("javascript")
      , "scala" -> CLike("scala")
      , "php"   -> CLike("php")
      , "hs"    -> Language("haskell", "{-", "-}")
      , "clj"   -> Language("clojure", "(comment ", ")")
      , "pas"   -> PascalLike("pascal")
      , "ml"    -> PascalLike("ocaml")
      , "sml"   -> PascalLike("sml")
      , "applescript" -> PascalLike("applescript")
      )

}
