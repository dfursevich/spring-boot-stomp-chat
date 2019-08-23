package by.fdf.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Dzmitry Fursevich
 */
@Configuration
@EnableMongoRepositories("by.fdf.chat.repository")
public class MongoConfig {
}
