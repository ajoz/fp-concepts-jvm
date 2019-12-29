package io.github.ajoz.illegal;

import java.net.InetAddress;

@SuppressWarnings("ALL")
public class IllegalStatesUnrepresentable1 {
    /*
      Based on the lecture by Yaron Minsky: https://youtu.be/-J8YyfrSwTk

      Choosing a data type is important to model the domain in the way
      that the illegal states are unrepresentable. Below there is a simple
      code that models a connection. It's built from the ConnectionState
      which shows if there is a connection, there is no connection, or
      the connecting is in progress. Except this we have also a ConnectionInfo
      holding a lot of information about the connection itself like server
      address, information about ping, information when the connection was
      initiated and when it was closed. In such ConnectionInfo object we might
      need also some way to identify the connection like session id.

      How do we usually create such code? We create an enum for the connection
      state:
     */
    enum ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTED
    }

    /*
      ConnectionInfo is just a heap of different fields that hardly have any
      cohesion with each other:
     */
    class ConnectionInfo {
        private ConnectionState connectionState;
        private InetAddress serverAddress;
        private long lastPingTimeInMs;
        private long connectionStartTimeInMs;
        private long connectionStopTimeInMs;
        private String sessionId;
        // no constructor or methods to make it more readable
    }

/*
  There are some obvious issues with this code:
  - lack of cohesion between fields, depending on the ConnectionState
    some of the fields should not be instantiated. Should the fields
    be part of the enum?
  - lack of information which fields are optional, can we have a
    ConnectionInfo with a null session id that is CONNECTED? It seems
    ridicules but our API allows it. We often lul our selves into false
    sense of security and just say that "nah, my code will never get to
    such state, there is no need to test it" but we forget that:
      -- we do not work alone, there are other people in the team
      -- we hardly ever work in a non multithreaded environment, some
         thread races are hard to predict in the beginning
      -- if we allow for mutability then we invite problems with open
         arms. How many times we had an object that models a Route Plan
         that is mutable and has a departure and destination. How many
         times we allowed to have a null departure and a null destination
         or a departure without destination? Nothing that we wanted to
         happen?
      -- it is not easy to understand such a mess of fields what are the
         intentions behind them

    Issues:
    -- lastPingTimeInMs - if there is no information about time available then
       what value should we use instead? -1 like in the good ol' C days?
       Isn't this too dangerous? It's like a forbidden fruit, maybe someday
       we will send error information through this field -2, -3, -4 etc
       sky is the limit
    -- connectionStartTimeInMs and connectionStopTimeInMs has the same problem
       as the lastPingTimeInMs but also shows a different issue. Can we have
       a connectionStartTimeInMs without a connectionStopTimeInMs? Surely yes!
       Does a connectionStopTimeInMs without a connectionStartTimeInMs has
       sense? Probably depending on the context and the domain.
    -- sessionId if the information about session id is unavailable what should
       we put in this field? a null? ok but is any String a valid session id?
       Is an empty string a valid session id? is a string composed out of only
       white chars a valid session id? This field also suffers from the same
       problem that lastPingTimeInMs did, null is super tempting and depending
       on the situation can mean different things like errors, exceptions,
       warnings etc. Do we like to decipher the fields meaning and values
       each time we read the code? I definitely do not!

    What should we do then?

    The answer is types!
    - do not use bare long and strings for modeling
    - show the optionality explicitly
    - something else?? something better let's check!
 */
}