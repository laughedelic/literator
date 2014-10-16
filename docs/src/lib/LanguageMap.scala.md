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


------

### Index

+ scala
  + [FileUtils.scala][FileUtils.scala]
  + [LanguageMap.scala][LanguageMap.scala]
  + [LiteratorParsers.scala][LiteratorParsers.scala]
  + [package.scala][package.scala]

[FileUtils.scala]: FileUtils.scala.md
[LanguageMap.scala]: LanguageMap.scala.md
[LiteratorParsers.scala]: LiteratorParsers.scala.md
[package.scala]: package.scala.md
[Readme.markdown]: Readme.markdown.md