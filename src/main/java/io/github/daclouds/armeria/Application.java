package io.github.daclouds.armeria;

import io.github.daclouds.armeria.service.HelloService;
import io.github.daclouds.armeria.service.HiService;
import io.github.daclouds.armeria.service.MyHelloService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.thrift.ThriftProtocolFactories;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.VirtualHost;
import com.linecorp.armeria.server.VirtualHostBuilder;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.http.file.HttpFileService;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.server.thrift.ThriftService;

@SpringBootApplication
@Configuration
@ComponentScan
public class Application {

	@Bean
	public static HiService hiService() {
		return new HiService();
	}
	
    public static void main(String[] args) {
    	HelloService.AsyncIface helloHandler = new MyHelloService();

		ServerBuilder sb = new ServerBuilder();
		sb.port(8080, SessionProtocol.HTTP);

		VirtualHostBuilder vhb = new VirtualHostBuilder();
		VirtualHost vh = vhb
				.serviceAt(
						"/hello",
						new ThriftService(helloHandler,
								ThriftProtocolFactories.BINARY))
//								.decorate(LoggingService::new))
				.serviceAt(
						"/hi",
						hiService())
				.serviceUnder("/static/",
						HttpFileService.forClassPath("/static"))
				.serviceUnder("/docs/", new DocService())		
				.build();

		sb.defaultVirtualHost(vh);

		Server server = sb.build();
		server.start();
		
		SpringApplication.run(Application.class, args);
    }
}
