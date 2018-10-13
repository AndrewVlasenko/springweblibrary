package by.project.library.springweblibrary;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "by.project.library.springweblibrary")
@EnableAspectJAutoProxy
public class ServletInitializer extends SpringBootServletInitializer {

}
