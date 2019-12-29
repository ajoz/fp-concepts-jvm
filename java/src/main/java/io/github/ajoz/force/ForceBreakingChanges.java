package io.github.ajoz.force;

import java.util.List;
import java.util.function.Function;

/*
  Let's try to represent a business rule using types.

  The rule that we might consider is:
  > Our Navigation app supports contacts. A Contact must have an
  address or a coordinate.

  We can design a sealed type hierarchy that models this:
 */
abstract class ContactInfo1 {
    class AddressOnly extends ContactInfo1 {
        // fields that contain the address information
    }

    class CoordinatesOnly extends ContactInfo1 {
        // fields that contain latitude and longitude information
    }
}

    /*
      To allow for easier work with the data we can extract the information
      into separate types:
     */

class ContactAddressInfo {
    // fields that contain the address information
}

class ContactCoordinatesInfo {
    // fields that contain latitude and longitude information
}

/*
  We can now use the types as fields for our PointOfInterestInfo type:
 */
abstract class ContactInfo2 {
    class AddressOnly extends ContactInfo2 {
        ContactAddressInfo address;
    }

    class CoordinatesOnly extends ContactInfo2 {
        ContactCoordinatesInfo coordinates;
    }
}

/*
  What happens if the business rule changes:
  > Our Navigation app supports contacts. A Contact can have a
  list of addresses or a list of contacts.

  Seems simple enough, for us, instead of sealed type we can rework
  the ContactInfo type into something more resembling this:
 */
class ContactInfo3 {
    List<ContactAddressInfo> addresses;
    List<ContactCoordinatesInfo> coordinates;
}

/*
  How do we use the type? Simple we just iterate over
  the list of addresses and then we iterate over the
  list of coordinates.

  Is there a problem with that? What happens if the business
  rule changes again:

  > Our Navigation app supports contacts. A Contact can have a
  list of addresses or a list of coordinates or a list of phone numbers.

  Seems simple enough, we can just rework the ContactInfo and add
  another list into the mix.
 */
class ContactPhoneInfo {
    // fields that contain information about phone number
}

class ContactInfo4 {
    List<ContactAddressInfo> addresses;
    List<ContactCoordinatesInfo> coordinates;
    List<ContactPhoneInfo> phones;
}

/*
  Is this a good way to model the code? The types do not work
  to our adventage. Why? Simply we need to remember everyplace
  where we do iterate the lists and modify them. Depending on the
  unit tests we've created they may or may not inform us about
  the need to iterate over the phones list. I think there is hardly
  a chance it would happen. We could immediately land in a situation
  where our existing suite of tests would not break and would
  still show that everything is ok.

  This tells us that we need to get some "deeper insight into the domain"

  Maybe our understanding of the domain evolved? Do we still think about
  the separate lists of addresses, coordinates, phones? Maybe the rule
  is now:

  > Our Navigation app supports contacts. A Contact can have a
  list of methods to get to or communicate with.

  Let's change the type so it will be enforcing breaking changes:
 */
abstract class ContactMethod {
    class ByAddress extends ContactMethod {
        ContactAddressInfo address;
    }

    class ByCoordinates extends ContactMethod {
        ContactCoordinatesInfo coordinates;
    }

    class ByPhone extends ContactMethod {
        ContactPhoneInfo phone;
    }
}

class ContactInfo5 {
    List<ContactMethod> contactMethods;
}

/*
  In less outdated programming languages we can have sealed type hierarchies
  that we can relay that compiler will enforce exhaustivness for. What does it
  mean? Simply everywhere where ContactMethod is checked, compiler will force
  us to check for each case.

  A combination of if clause and an instanceof operator will not force the
  Java compiler's hand. We need to simulate it with some good old polymorphism:
 */
abstract class ContactMethod2 {
    /*
      We can define a method called fold that accepts 3 different functions.
      Each functions expects different case from the sum type and returns some
      result. Through polymorphism/subtyping we can deliver an implementations.
     */
    abstract <A> A fold(
            Function<ByAddress, A> onAddress,
            Function<ByCoordinates, A> onCoordinates,
            Function<ByPhone, A> onPhone
    );

    static class ByAddress extends ContactMethod2 {
        ContactAddressInfo address;

        @Override
        <A> A fold(Function<ByAddress, A> onAddress,
                   Function<ByCoordinates, A> onCoordinates,
                   Function<ByPhone, A> onPhone) {
            return onAddress.apply(this);
        }
    }

    static class ByCoordinates extends ContactMethod2 {
        ContactCoordinatesInfo coordinates;

        @Override
        <A> A fold(Function<ByAddress, A> onAddress,
                   Function<ByCoordinates, A> onCoordinates,
                   Function<ByPhone, A> onPhone) {
            return onCoordinates.apply(this);
        }
    }

    static class ByPhone extends ContactMethod2 {
        ContactPhoneInfo phone;

        @Override
        <A> A fold(Function<ByAddress, A> onAddress,
                   Function<ByCoordinates, A> onCoordinates,
                   Function<ByPhone, A> onPhone) {
            return onPhone.apply(this);
        }
    }
}

    /*
      Each time anyone is using the ContactMethod type and wants to
      get a result from it, needs to call fold method, which expects
      a function for each case.

      Still it is not as automatic as we would like it to be. Java is
      not: C#, Scala, F#, Kotlin, Haskell, Idris, Agda, Coq :-(
      This means that we need to force a lot of discipline when working
      with "sealed types" in Java and remember to update the fold method.

      After such update, the compiler will help us with finding every
      place the code is broken.
     */

/*
  Interesting thing here. Do we need a unit tests for the fold method?

  What do we have:
  - the fold method result is a generic type A
  - we do not know anything about the type A (it is not constrained)
  - we cannot construct the type A
  - we can only call one of the three functions passed to the fold method
  - we can only call a function for which we have arguments

  Can we return null?

  Technically yes, but let's just think that such an option does not
  exist.

  Can we crete different cases of ContactMethod?

  We have all the means to do it (i.e. the constructor) but we do
  not have the data to do it. If we are implementing fold for the
  ByCoordinates case, we do not have any normal mean to create
  ByAddress or ByPhone inside the ByCoordinates. We do not have the
  necessary data for their constructors.

  This means that if we will not return a null. Then the only way
  to implement it is to call the proper function and pass the argument.

  We just cannot do anything that would compile and give us a result
  that we do not expect.

  Do we need to unit test it?

  Is a test like this needed:
 */
@SuppressWarnings("ALL")
public class ForceBreakingChanges {
    void check_If_The_OnAddress_Is_Called_For_ByAddress_Fold() {
        // given:
        final ContactMethod2 sut = new ContactMethod2.ByAddress(/*Here we instantiate it*/);
        final String expected = "address";

        // when:
        final String actual = sut.fold(
                address -> "address",
                coordinates -> "coords",
                phone -> "phone"
        );

        // then:
        // Assert.assertEquals(expected, actual);
    }

    /*
      Is there a point to create such a test? Seems not? A simple example where the
      the correctly specified type can help you guarantee that the code is correct.

      But this is not a proof and the example is simple!
     */
}