#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.boot.SpringApplication;

public class Application{

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppConfig.class);
        app.setShowBanner(false);
        app.run(args);
    }

}
