package software.xdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "PMD.UseUtilityClass"})
@SpringBootApplication
public class Application
{
	public static void main(final String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}
