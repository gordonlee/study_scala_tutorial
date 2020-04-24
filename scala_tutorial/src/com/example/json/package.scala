// src/com/example/json/package.scala

package com.example

package object json {
  class JSONObject {}  // 노출할 멤버를 적절히 정의한다.
  def fromString(strings: String): JSONObject = { null }
}
