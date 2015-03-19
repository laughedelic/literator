## Language map

```scala
package laughedelic.literator.lib

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

```




[main/scala/FileUtils.scala]: FileUtils.scala.md
[main/scala/LanguageMap.scala]: LanguageMap.scala.md
[main/scala/LiteratorParsers.scala]: LiteratorParsers.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/Readme.md]: Readme.md.md
[test/scala/Test.scala]: ../../test/scala/Test.scala.md