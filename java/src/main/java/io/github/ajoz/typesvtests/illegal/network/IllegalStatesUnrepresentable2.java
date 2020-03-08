package io.github.ajoz.typesvtests.illegal.network;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

public class IllegalStatesUnrepresentable2 {
    /*
      Let's rework the entire example to fix some of the
      issues noted in the previous part:
      - usage of bare String instead of specialized type
      - usage of bare long instead of a specialized type
      - lack of explicit optionality
     */
    enum ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTED
    }

    /*
      First let's add missing types. For example instead of
      a bare String for the session Id type we can use an
      explicit type with a proper name.
     */
    class SessionId {
        // internal implementation is not important for the example
    }

    /*
      Instead of long for time in milliseconds we can use
      a java.time.LocalTime or java.time.Duration

      Everything that can be absent is wrapped with an
      java.util.Optional.

      Looks better but still allows for illegal states:
      - connected with no connectionStartTime
      - connected with no sessionId
      - disconnected with no connectionStopTime
      We could list and list the issues for some time

      What about our earlier idea to mix the ConnectionState
      with information that correspond to it?
     */
    class ConnectionInfo {
        private ConnectionState connectionState;
        private InetAddress serverAddress;
        private Optional<Duration> lastPingTime;
        private Optional<LocalTime> connectionStartTime;
        private Optional<LocalTime> connectionStopTime;
        private Optional<SessionId> sessionId;
        // no constructor or methods to make it more readable
    }
}