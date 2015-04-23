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

package rx.redis.api

import rx.lang.scala.Observable

import rx.redis.clients.RawClient
import rx.redis.util.{ DefaultRedisHost, DefaultRedisPort }

object RxRedis {
  def apply(host: String = DefaultRedisHost, port: Int = DefaultRedisPort): Client = {
    new Client(RawClient(host, port))
  }

  @deprecated("use shutdown", "0.4.0")
  def await(client: Client): Unit = {
    shutdown(client)
  }

  def disconnect(client: Client): Observable[Unit] = {
    client.disconnect()
  }

  def shutdown(client: Client): Unit = {
    disconnect(client).
      onErrorReturn(_ ⇒ ()).
      toBlocking.lastOption.getOrElse(())
  }
}
