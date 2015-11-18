package io.github.daclouds.armeria.service;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import com.google.gson.Gson;

public class MyHelloService implements HelloService.AsyncIface {
	
	Gson gson = new Gson();
	
    @Override
    public void hello(String name, AsyncMethodCallback resultHandler) throws TException {
        resultHandler.onComplete("Hello, " + name + '!');
    }

}