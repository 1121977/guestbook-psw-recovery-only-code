package ru.incubator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.incubator.tasks.*;

public class Main {

    public static void main(String... arg) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ERROR);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CheckerConfiguration.class);
        Checker checker = switch (arg.length == 0?"nothing":arg[0]) {
            case "check" -> {
                context.refresh();
                yield context.getBean(CheckerCheck.class);
            }
            case "put" -> {
                context.refresh();
                yield context.getBean(CheckerPut.class);
            }
            case "get" -> {
                context.refresh();
                yield context.getBean(CheckerGet.class);}
            default -> {
                context.refresh();
                yield context.getBean("CheckerDefault", CheckerDefault.class);
            }
        };
        checker.execute(arg);
        System.out.println(CheckerDefault.returnCode.getOutput());
        System.err.println(CheckerDefault.returnCode.getError());
        System.exit(CheckerDefault.returnCode.getCode());
    }
}
