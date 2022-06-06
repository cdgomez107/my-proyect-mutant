package meli;

import meli.processors.MutantProcessor;
import meli.processors.StatProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @Inject
    MutantProcessor mutantProcessor;

    @Inject
    StatProcessor statProcessor;

    @Override
    public void configure(){
        restConfiguration().bindingMode(RestBindingMode.json).setSkipBindingOnErrorCode(false);

        rest("/mutant")
                .post()
                .to("direct:validateMutant");

        from("direct:validateMutant")
                .process(exchange -> mutantProcessor.validateMutant(exchange));

        rest("/stats")
                .get()
                .to("direct:stats");

        from("direct:stats")
                .process(exchange -> statProcessor.getStats(exchange));
    }
}
