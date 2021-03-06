package com.adcade.statsd.test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.junit.Test;

import com.adcade.statsd.StatsdHandler;
import com.adcade.statsd.StatsdIterable;
import com.adcade.statsd.annotation.Counting;
import com.adcade.statsd.annotation.Timing;

public class TestStatsdHandler {

    public static interface DoSomethinger {
        void doSomething();
    }

    @Test
    public final void testStatsdHandler() throws UnknownHostException, IOException, InterruptedException {
        StatsdIterable service = new StatsdIterable();
        StatsdHandler.setStatsdClient(service);
        // implementation of DoSomethinger which annotated with Timing and Counting
        DoSomethinger dos = new DoSomethinger(){
            @Timing("some.timer")
            @Counting({ "some.counter1", "some.counter2" })
            public void doSomething(){}
        };
        // dynamic proxied and invoked doSomething()
        DoSomethinger proxy = (DoSomethinger) StatsdHandler.proxy(dos);
        proxy.doSomething();
        // check result
        Iterator<String> iter = service.iterator();
        String[] expected = {
            "some\\.timer\\:\\d+\\|ms",
            "some\\.counter1\\:1\\|c",
            "some\\.counter2\\:1\\|c"
        };
        TestStatsdClient.assertIterator(expected, iter, false);
    }

}
