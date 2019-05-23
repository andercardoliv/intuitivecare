package br.com.intuitivecare.desafio.log;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class LoggerConfiguration {

	@Bean
	@Scope(scopeName = SCOPE_PROTOTYPE)
	public Logger loger(InjectionPoint ip) {
		return LoggerFactory.getLogger(ip.getMethodParameter().getContainingClass());
	}
	
}
