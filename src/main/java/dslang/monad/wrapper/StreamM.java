package dslang.monad.wrapper;

import java.util.function.Function;
import java.util.stream.Stream;

import dslang.monad.Monad;

public class StreamM<T> implements Monad<StreamM<?>, T>  {

        Stream<T> myStream = null;
        
        public Stream<T> unwrap(){
            return myStream;
        }
        
        public StreamM(Stream<T> stream) {
            myStream = stream;
        }
        
        static public <S> StreamM<S> of(Stream<S> stream){
            return new StreamM<S>(stream);
        }
        
        @Override
        public <U> Monad<StreamM<?>, U> unit(U u) {
            return new StreamM<U>(Stream.of(u));
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

        @Override
        public StreamM<?> getM() {
            return this;
        }
        

}
