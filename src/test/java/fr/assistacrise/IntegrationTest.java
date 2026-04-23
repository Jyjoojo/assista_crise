package fr.assistacrise;

import fr.assistacrise.config.AsyncSyncConfiguration;
import fr.assistacrise.config.EmbeddedSQL;
import fr.assistacrise.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        AssistaCriseApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        fr.assistacrise.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
