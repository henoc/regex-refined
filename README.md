# regex-refined

[refined](https://github.com/fthomas/refined) for regex string, and some type-safe regex utilities.

## build.sbt

```sbt
libraryDependencies += "com.github.henoc" %% "regex-refined" % "0.1.0"
```

## Usage

### Refinement

There are more examples in test files.

```scala
import regex_string._
import eu.timepit.refined.api._
import eu.timepit.refined.W
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.auto._

// Regex string should have one caputuring-group
val _: String Refined GroupCount[Equal[W.`1`.T]] = "a(b)c"

// Regex string should have a group name "integer"
val _: String Refined HasGroupName[W.`"integer"`.T] = "[+-]?((?<integer>[0-9]*)[.])?[0-9]+"

// Regex string should be full match pattern
val _: String Refined FullMatchPattern = "^abc$"

// Regex string should be correct as js-regex
val _: String Refined JsRegex = """abx[\b]cde"""
```

### Pattern matching

`r` string interpolator extractor checks the number of groups in compile time.

```scala
"2018-11-18" match {
  case r"""(\d+$year)-(\d+$month)-(\d+$day)""" => println(s"year = $year, month = $month, day = $day")
  case _ => println("no!")
}
```

The variable positions of interpolator is not concerned, so you can also write:

```scala
r"""(\d+)-(\d+)-(\d+)$year$month$day"""
r"""(?x)   (\d+)-(\d+)-(\d+)    # $year, $month, $day"""
```

But this throws compile error:

```scala
r"""(?x)   (\d+)-(\d+)-\d+    # $year, $month, $day"""
```
