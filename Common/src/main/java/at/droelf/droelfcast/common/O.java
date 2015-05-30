package at.droelf.droelfcast.common;

public class O<T> {
    private T d;

    public O(T d){
        this.d = d;
    }

    public boolean e(){
        return d == null;
    }

    public T d(){
        if(e()){
            throw new OException();
        }
        return d;
    }

    public static <T> O<T> c(T d){
        return new O(d);
    }

    public static <T> O<T> n(){
        return O.c(null);
    }

    static class OException extends RuntimeException{
        public OException() {
            super("Something strong happend, don't access an O without checking e(), tststss");
        }
    }
}
