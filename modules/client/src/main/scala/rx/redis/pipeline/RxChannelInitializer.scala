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

package rx.redis.pipeline

import rx.redis.resp.RespType

import rx.Observer

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

import java.util

private[redis] class RxChannelInitializer(optimizeForThroughput: Boolean) extends ChannelInitializer[SocketChannel] {
  def initChannel(ch: SocketChannel): Unit = {
    if (optimizeForThroughput) {
      ch.config().setPerformancePreferences(0, 1, 3)
      ch.config().setTcpNoDelay(false)
    } else {
      ch.config().setPerformancePreferences(0, 3, 1)
      ch.config().setTcpNoDelay(true)
    }

    val queue = new util.LinkedList[Observer[RespType]]()

    ch.pipeline().
      addLast("resp-codec", new RespCodec).
      addLast("rx-adapter", new RxAdapter(queue)).
      addLast("rx-closer", new RxCloser(queue))
  }
}
