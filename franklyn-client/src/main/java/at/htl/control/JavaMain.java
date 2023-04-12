package at.htl.control;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.quartz.*;

import javax.inject.Inject;

@QuarkusMain
public class JavaMain implements QuarkusApplication {

    @Inject
    ApiCalls apiCalls;

    @Override
    public int run(String... args) throws Exception {
        System.out.println(" __          __  _                            _          ______               _    _             ____  _ \n" +
                " \\ \\        / / | |                          | |        |  ____|             | |  | |           |___ \\| |\n" +
                "  \\ \\  /\\  / /__| | ___ ___  _ __ ___   ___  | |_ ___   | |__ _ __ __ _ _ __ | | _| |_   _ _ __   __) | |\n" +
                "   \\ \\/  \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\  |  __| '__/ _` | '_ \\| |/ / | | | | '_ \\ |__ <| |\n" +
                "    \\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) | | |  | | | (_| | | | |   <| | |_| | | | |___) |_|\n" +
                "     \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/  |_|  |_|  \\__,_|_| |_|_|\\_\\_|\\__, |_| |_|____/(_)\n" +
                "                                                                                      __/ |              \n" +
                "                                                                                     |___/               ");
        Long id = apiCalls.enterPIN();                      // get ExamId
        Long code = apiCalls.enterName(id.toString());      // get ExamineeId
        System.out.println("Do NOT close this window!");
        System.out.println("Good Luck!");
        if(code == -100L){                                  // exit if student declines to enroll again
            return 1;
        }
        apiCalls.getIntervall(id.toString());               // get intervall for Screenshots
        apiCalls.setScheduler();                            // set Scheduler adjusted to the intervall
        Quarkus.waitForExit();
        return 0;
    }
}
