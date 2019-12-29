package io.github.ajoz.illegal;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

public class IllegalStatesUnrepresentable3 {
    class SessionId {
        // internal implementation is not important for the example
    }

    /*
      Instead of an enum we could use a sealed type hierarchy
      that has three distinct types each with its own specific
      states.

      Java does not have sealed classes or any other language
      features that would make creating Algebraic Data Types
      easier.
     */
    abstract class ConnectionState {
        class Connecting extends ConnectionState {
            LocalTime connectionStartTime;
        }

        // should connected state have the information about
        // connection start time is the problem if the domain
        class Connected extends ConnectionState {
            SessionId sessionId;
            Optional<Duration> lastPingTime;
        }

        // should disconnected state have the information about
        // last connection duration is the problem of the domain
        class Disconnected extends ConnectionState {
            LocalTime connectionStopTime;
        }
    }

    /*
      Now the connection info can have only a server address
      and the state, which in turn contains proper information
      cohesive with what the state represents.
     */
    class ConnectionInfo {
        private InetAddress serverAddress;
        private ConnectionState connectionState;
        // no constructor or methods to make it more readable
    }
}