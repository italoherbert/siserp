package italo.siserp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SisERPApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisERPApplication.class, args);
	}
	/*
	
	@Bean	
	public Docket siserpApi() {
		return new Docket( DocumentationType.SWAGGER_2 ).select().apis( 
				RequestHandlerSelectors.basePackage( "italo.siserp" ) ).build();
	}
	*/

}
