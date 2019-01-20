package io.github.ajoz.pattern_matching.attempt2;

// Pattern matching with Exceptions
abstract class Foo extends Throwable {
    private Foo() {
    }

    static final class Bar extends Foo {
    }

    static final class Baz extends Foo {
    }

    static final class Qux extends Foo {
    }

    static final class Quux extends Foo {
    }

    public void match() throws Foo {
        throw this;
    }
}

public class PatternMatching {
    public static void main(final String[] args) {
        final Foo foo = new Foo.Baz();
        // just for laughs ;-)
        // it is exhaustive!!!!
        // but always needs the else clause :(
        try {
            foo.match();
        } catch (final Foo.Bar bar) {
            System.out.println("caught Bar");
        } catch (final Foo.Baz | Foo.Quux union) {
            System.out.println("caught Baz | Quux");
        } catch (final Foo.Qux qux) {
            System.out.println("caught Qux");
        } catch (final Throwable other) {
            System.out.println("caught other");
        }
    }
}
