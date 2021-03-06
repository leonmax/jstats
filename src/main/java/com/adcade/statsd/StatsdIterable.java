package com.adcade.statsd;

import java.util.Iterator;
import java.util.Stack;

import com.adcade.statsd.transport.Transport;

public class StatsdIterable extends DefaultService implements StatsdService, Iterable<String>{
    private Stack<String> output = new Stack<String>();

    @Override
    public Iterator<String> iterator(){
        return output.iterator();
    }

    public String lastMessage(){
        return output.peek();
    }

    public void clear(){
        output.clear();
    }

    public Transport getTransport(){
        return new Transport(){

            @Override
            public boolean doSend(String stat) {
                return output.add(stat);
            }

            @Override public void close() {}
        };
    }
}
