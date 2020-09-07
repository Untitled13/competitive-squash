

public class Pair<A extends Comparable<A>, B extends Comparable<B>> implements Comparable<Pair<A,B>> {
    A left;
    B right;
    public Pair(A a, B b) {
        left = a;
        right = b;
    }
    public Pair(A a) {
        left = a;
    }
    public A left() {
        return left;
    }
    public <C extends Comparable<C>> Pair<C,B> left(C a) {
        return new Pair<C,B>(a,right());
    }
    public B right() {
        return right;
    }
    public <C extends Comparable<C>> Pair<A,C> right(C b) {
        return new Pair<A,C>(left(), b);
    }
    public boolean equals(Pair<A,B> pair) {
        return left() == pair.left() && right() == pair.right();
    }
    public int compareTo(Pair<A,B> other) {
        int n = left().compareTo(other.left());
        return n==0 ? right().compareTo(other.right()) : n;
    }
}
