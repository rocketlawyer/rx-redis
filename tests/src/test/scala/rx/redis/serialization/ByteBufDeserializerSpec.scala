/*
 * Copyright 2014 – 2015 Paul Horn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rx.redis.serialization

import rx.redis.resp.respBytes
import rx.redis.serialization.ByteBufDeserializer.ParseAllResult
import rx.redis.util.Utf8

import io.netty.buffer.UnpooledByteBufAllocator
import org.scalatest.{ FunSuite, Inside }

class ByteBufDeserializerSpec extends FunSuite with Inside {

  val alloc = UnpooledByteBufAllocator.DEFAULT

  test("apply should parse only one item") {
    val resp = "$3\r\nfoo\r\n$3\r\nbar\r\n"
    val result = ByteBufDeserializer(resp, Utf8, alloc)
    assert(result == respBytes("foo"))
  }

  test("foreach should execute an callback for each item") {
    val resp = "$3\r\nfoo\r\n$3\r\nbar\r\n"
    val expected = Iterator(respBytes("foo"), respBytes("bar"))
    ByteBufDeserializer.foreach(resp, Utf8, alloc) {
      actual ⇒ assert(actual == expected.next())
    }
  }

  test("foreach should return whether there is data left") {
    val resp = "$3\r\nfoo\r\n$3\r\nbar\r\n$2\r"
    val remainder = ByteBufDeserializer.foreach(resp, Utf8, alloc)(_ ⇒ ())
    assert(remainder)

    val resp2 = "$3\r\nfoo\r\n$3\r\nbar\r\n"
    val remainder2 = ByteBufDeserializer.foreach(resp2, Utf8, alloc)(_ ⇒ ())
    assert(!remainder2)
  }

  test("parseAll should parse all items") {
    val resp = "$3\r\nfoo\r\n$3\r\nbar\r\n"
    val result = ByteBufDeserializer.parseAll(resp, Utf8, alloc)
    assert(result == ParseAllResult(List(respBytes("foo"), respBytes("bar")), hasRemainder = false))
  }

  test("parseAll should also include whether there is data left") {
    val resp = "$3\r\nfoo\r\n$3\r\nbar\r\n$2\r"
    val result = ByteBufDeserializer.parseAll(resp, Utf8, alloc)
    assert(result == ParseAllResult(List(respBytes("foo"), respBytes("bar")), hasRemainder = true))
  }
}
