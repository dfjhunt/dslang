package dslang.monad.wrapper;

import java.util.function.Function;
import java.util.stream.Stream;

import dslang.monad.Monad;
import dslang.monad.MonadWrapper;

public class StreamM<T> implements MonadWrapper<StreamM<?>, T, Stream<T>>  {

        Stream<T> myStream = null;
        
        public StreamM(Stream<T> stream) {
            myStream = stream;
        }
        
        static public <S> StreamM<S> of(Stream<S> stream){
            return new StreamM<S>(stream);
        }
        
        @Override
        public <U> Monad<StreamM<?>, U> unit(U u) {
            return sunit(u);
        }
        
        public static <U> Monad<StreamM<?>, U> sunit(U u) {
            return new StreamM<U>(Stream.of(u));
        }
        
        @Override
        public Stream<T> unwrap(){
            return myStream;
        }

        public static <U> Stream<U> unwrap(Monad<StreamM<?>,U> m){
            return ((StreamM<U>)m).unwrap();
        }
        
        @Override
        public <U> StreamM<U> map(Function<? super T, ? extends U> mapper) {
            return new StreamM<U>(myStream.map(mapper));
        }

        @Override
        public <U> Monad<StreamM<?>, U> flatMap(Function<? super T, ? extends Monad<StreamM<?>, U>> mapper) {
            Function<? super T, Stream<U>> newMapper = mapper.andThen(o->((StreamM<U>)o).unwrap());
            return new StreamM<U>(myStream.flatMap(newMapper));
        }
        

}
