package fr.unice.polytech.equipe.j.annotations;

import fr.unice.polytech.equipe.j.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    String value();
    HttpMethod method() default HttpMethod.GET;
}
