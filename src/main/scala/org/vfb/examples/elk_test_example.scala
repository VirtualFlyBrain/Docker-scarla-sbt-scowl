package main.scala.org.vfb.examples
import main.scala.org.vfb.elk_test


object elk_test_example extends App {
     val et = elk_test("http://purl.obolibrary.org/obo/fbbt/fbbt-simple.owl")
     et.getSubClasses("FBbt_00003649")
     et.getAnnotations("FBbt_00003649")
}