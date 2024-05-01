package Log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Adrian
 */
public class Log {
    private static final Logger LOG = LogManager.getLogger(Log.class);

    public static void main(String[] args) {
        configureLogger();
        logMessage("Este es un mensaje de prueba.");
    }

    private static void configureLogger() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration config = context.getConfiguration();

        Layout<String> layout = PatternLayout.newBuilder()
                .withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        RollingFileAppender appenderBuilder = RollingFileAppender.newBuilder()
                .withFileName("logs\\timestamp.log")
                .withAppend(true)
                .withLayout(layout)
                .withName("TimestampAppender")
                .build();

        appenderBuilder.start();
        //config.addAppender(appenderBuilder.build());

        context.updateLoggers();
    }

    private static void logMessage(String message) {
        LOG.info(message);
    }
}
