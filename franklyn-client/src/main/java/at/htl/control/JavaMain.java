package at.htl.control;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;

@QuarkusMain
public class JavaMain implements QuarkusApplication {

    @Inject
    ApiCalls apiCalls;

    @Override
    public int run(String... args) throws Exception {
        Long id = apiCalls.enterPIN();
        apiCalls.enterName(id.toString());
        Quarkus.waitForExit();
        return 0;
    }
}
