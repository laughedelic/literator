/* ## Language map */

package ohnosequences.literator.lib

case class Comment(start: String, end: String, line: String)

case class Language(syntax: String, ext: String, comment: Comment)

object LanguageMap {

  def clike = Comment("/*", "*/", "//")

  val langs = List(
        Language("c", "c", clike)
      , Language("cpp", "cpp", clike)
      , Language("csharp", "cs", clike)
      , Language("objc", "m", clike)
      , Language("java", "java", clike)
      , Language("javascript", "js", clike)
      , Language("scala", "scala", clike)
      , Language("php", "php", clike)
      , Language("haskell", "hs", Comment("{-", "-}", "--"))
      , Language("clojure", "clj", Comment("(comment ", ")", ";"))
      , Language("applescript", "applescript", Comment("(*", "*)", "--"))
      , Language("pascal", "pas", Comment("(*", "*)", "//"))
      )

  val langMap: Map[String, Language] = Map(langs map { l => (l.ext, l) }: _*)

}
