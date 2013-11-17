## Language map

```scala
package ohnosequences.tools.literator

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

+ src
  + main
    + scala
      + [FileUtils.scala][main/scala/FileUtils.scala]
      + [LanguageMap.scala][main/scala/LanguageMap.scala]
      + [LiteratorCLI.scala][main/scala/LiteratorCLI.scala]
      + [LiteratorParsers.scala][main/scala/LiteratorParsers.scala]
      + [package.scala][main/scala/package.scala]
  + test
    + scala
      + [TestCode.scala][test/scala/TestCode.scala]

[main/scala/FileUtils.scala]: FileUtils.scala.md
[main/scala/LanguageMap.scala]: LanguageMap.scala.md
[main/scala/LiteratorCLI.scala]: LiteratorCLI.scala.md
[main/scala/LiteratorParsers.scala]: LiteratorParsers.scala.md
[main/scala/package.scala]: package.scala.md
[test/scala/TestCode.scala]: ../../test/scala/TestCode.scala.md
