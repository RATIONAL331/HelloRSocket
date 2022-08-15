package com.rational331.rsocket;

import com.rational331.rsocket.service.SocketAcceptorImpl;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;

/** 기존 방식의 문제점
 * 1. HTTP는 기본적으로 3-way Handshake 과정이 필수적으로 진행된다.
 * 2. 또한 비연결성 특성으로 다시 연결해주어야 한다. 그리고 실시간적이지 못하다. (서버측에서 무언가가 바뀌었을 때 클라이언트가 요청해야 알 수 있음)
 * 3. 서비스간에 통신을 할 때 직렬화 & 역직렬화 하는 과정이 필요하다. (항상 CPU 시간이 필요하다.)
 * 4. 쓰레드를 블록킹한다.
 */

/** RSocket을 사용하면 좋은 점
 * 1. HTTP통신을 하지 않는다. TCP 레이어와 같은 계층(5/6 Layer)에서 작동한다.
 * 2. 연결을 끊지 않는다. 또한 무언가 바뀌었을 때 클라이언트가 인지할 수 있게 할 수 있다.
 * 3. 서비스간에 통신은 바이너리(이진) 형태로 통신한다. (기계 친화적이긴 하지만, 사용자 친화적이진 않음)
 * 4. 쓰레드를 블록킹하지 않는다. (Asynchronous & Non-Blocking)
 */

/**
 * Spring WebFlux              | Spring RSocket
 * ----------------------------|-----------------------------
 * Non-blocking & Asynchronous | Non-blocking & Asynchronous
 * ----------------------------|-----------------------------
 * Request - Response          | Request - Response
 *                             | Request - Stream
 *                             | Bi-directional Streaming
 *                             | Fire & Forget
 * ----------------------------|-----------------------------
 * HTTP                        | TCP, WebSocket, UDP(Aeron)
 * ----------------------------|-----------------------------
 * Layer 7                     | Layer 5, 6
 * ----------------------------|-----------------------------
 * JSON                        | Binary
 * ----------------------------|-----------------------------
 * Client initiated a request  | Publisher & Subscriber
 * ----------------------------|-----------------------------
 * -                           | Stream resume
 * ----------------------------|-----------------------------
 * -                           | Backpressure
 * ----------------------------|-----------------------------
 * Tools Support               | New Protocol. Lacks of tools
 * ----------------------------|-----------------------------
 */


public class Server {
	public static void main(String[] args) {
		RSocketServer socketServer = RSocketServer.create(new SocketAcceptorImpl());
		CloseableChannel closeableChannel = socketServer.bindNow(TcpServerTransport.create(6565));

		// keep listening
		closeableChannel.onClose().block();
	}
}
