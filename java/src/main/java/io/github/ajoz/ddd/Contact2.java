package io.github.ajoz.ddd;

import java.util.Optional;

public class Contact2 {
    public String firstName;
    public Optional<String> middleName;
    public String lastName;

    public Email email;
    public PhoneNumber phoneNumber;

    public Optional<Address> address;
}

class Address {
    private final String address;

    public Address(final String street) {
        this.address = street;
    }
}

abstract class PhoneNumber {
    private final String phoneNumber;

    private PhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static final class Verified extends PhoneNumber {
        private Verified(String phoneNumber) {
            super(phoneNumber);
        }
    }

    public static final class Unverified extends PhoneNumber {
        private Unverified(String phoneNumber) {
            super(phoneNumber);
        }
    }
}

abstract class Email {
    private final String email;

    private Email(final String email) {
        this.email = email;
    }

    public static final class Verified extends Email {
        public Verified(String email) {
            super(email);
        }
    }

    public static final class Unverified extends Email {
        public Unverified(String email) {
            super(email);
        }
    }
}
