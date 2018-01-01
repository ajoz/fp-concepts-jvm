package io.github.ajoz.validation;

import java.util.Objects;

public class Person {
    final String name;
    final String email;
    final Integer age;

    Person(final String name,
           final String email,
           final Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(email, person.email) &&
                Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, age);
    }
}
