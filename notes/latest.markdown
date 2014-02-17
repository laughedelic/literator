* Library:
  + [#12](https://github.com/laughedelic/literator/pull/12): Fixed the way of writing to a file;
  + General refactoring and simplification of parsers;
  + Changed convention about indentation (see [docs](https://github.com/laughedelic/literator/blob/release/v0.5.0/docs/src/lib/LiteratorParsers.scala.md#block-comments-parsing));

* Command line application:
  + Using [Conscript](https://github.com/n8han/conscript) for simpler installation;
  + Using [Scallop](https://github.com/scallop/scallop) for command line options parsing;
  + Now it can generate docs for several source directories (as the sbt plugin could before);

* Sbt plugin:
  + Added `Literator.` namespace for the keys;
