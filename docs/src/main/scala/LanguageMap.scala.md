## Language map

```scala
package ohnosequences.tools.literator

case class Language(syntax: String, opening: String, closing: String) 

object LanguageMap {

  val langMap: Map[String, Language] = Map(
    "scala" -> Language("scala", "/*", "*/")
  , "java"  -> Language("java",  "/*", "*/")
  , "hs"    -> Language("haskell", "{-", "-}")
  )

}

```


------

### Index

+ src
  + main
    + scala
      + [FileUtils.scala](FileUtils.scala.md)
      + [LanguageMap.scala](LanguageMap.scala.md)
      + [LiteratorCLI.scala](LiteratorCLI.scala.md)
      + [LiteratorParsers.scala](LiteratorParsers.scala.md)
      + [package.scala](package.scala.md)
  + test
    + scala
      + [TestCode.scala](../../test/scala/TestCode.scala.md)
