package classesForTest.fields;


import java.util.stream.Stream;

public class ClassContainingStream {

    private final Stream<Integer> stream_Integer = Stream.of(1);
    private Stream<String> stream_String;
    private Stream<Object> stream_Object;
    private Stream<A> stream_A;
    private Stream stream;
    private A a;

    private class A {}
}
