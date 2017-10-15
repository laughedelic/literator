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