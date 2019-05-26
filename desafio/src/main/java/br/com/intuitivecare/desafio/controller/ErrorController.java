package br.com.intuitivecare.desafio.controller;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = "session")
@Component(value = "error")
@ELBeanName(value = "error")
@Join(path = "/erro", to = "error.jsf")
public class ErrorController {}
