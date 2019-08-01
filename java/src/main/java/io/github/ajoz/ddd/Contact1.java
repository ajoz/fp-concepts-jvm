package io.github.ajoz.ddd;

public final class Contact1 {
    // which fields are optional and which are necessary
    public final String firstName; // needed
    public final String middleName; // optional
    public final String lastName; // needed

    // can we model somehow a verified email, unverified email or missing email?
    public final String email;
    public final boolean emailVerified;

    // We can have either a phone number or an email or an email and phone number
    // we cannot have a situation with a contact that has both information missing
    // can this only be modeled through the type system?
    public final String phoneNumber;
    public final boolean phoneVerified;

    // can the address be optional?
    public final String address;

    public Contact1(final String firstName,
                    final String middleName,
                    final String lastName,
                    final String email,
                    final boolean emailVerified,
                    final String phoneNumber,
                    final boolean phoneVerified,
                    final String address) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.phoneNumber = phoneNumber;
        this.phoneVerified = phoneVerified;
        this.address = address;
    }
}
