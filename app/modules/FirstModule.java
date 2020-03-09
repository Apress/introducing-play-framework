package modules;

import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;

import javax.inject.Inject;
import play.inject.ApplicationLifecycle;

import scala.collection.Seq;

public class FirstModule extends Module {

    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
                bind(Factorial.class).to(FactorialImpl.class)
        );
    }
}


