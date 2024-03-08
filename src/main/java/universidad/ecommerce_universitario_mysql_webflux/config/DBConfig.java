package universidad.ecommerce_universitario_mysql_webflux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;

public class DBConfig extends AbstractR2dbcConfiguration{

    @Value("${spring.r2dbc.url}")
    private String dbURl;
    
    @Override
    public ConnectionFactory connectionFactory(){
        return ConnectionFactories.get(dbURl);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory){
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        /* Esto es para ejecutar codigo SQL y enviar los datos de las tablas si es necesario */
        // CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
		// populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("DBTables_create.sql")));
		// populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("prueba.sql")));
		// initializer.setDatabaseCleaner(populator);
        return initializer;
    }
    
}
