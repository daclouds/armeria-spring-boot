package io.github.daclouds.armeria.service;

import io.github.daclouds.armeria.model.Person;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.Executor;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.linecorp.armeria.common.ServiceInvocationContext;
import com.linecorp.armeria.server.ServiceCodec;
import com.linecorp.armeria.server.ServiceInvocationHandler;

@Service
public class HiService extends com.linecorp.armeria.server.http.HttpService {
	
	private Gson gson = new Gson();

	@Override
	public ServiceCodec codec() {
		return new MyHttpServiceCodec(getClass().getSimpleName());
	}
	
	public String hi() {
		Person person = new Person();
		person.setName("daclouds");
		return gson.toJson(person);
	}
	
	@Override
	public ServiceInvocationHandler handler() {
		return new ServiceInvocationHandler() {
			
			@Override
			public void invoke(ServiceInvocationContext ctx,
					Executor blockingTaskExecutor, Promise<Object> promise)
					throws Exception {
				promise.trySuccess(hi());
			}
		};
	}

}
