/* ## Language map */

package ohnosequences.tools.literator

case class Language(syntax: String, opening: String, closing: String) 

object LanguageMap {

  val langMap: Map[String, Language] = Map(
    "scala" -> Language("scala", "/*", "*/")
  , "java"  -> Language("java",  "/*", "*/")
  , "hs"    -> Language("haskell", "{-", "-}")
  )

}
