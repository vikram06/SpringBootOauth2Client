package com.jeenguyen.demo.oauth.server;

import com.google.common.collect.Sets;
import com.jeenguyen.demo.oauth.api.entities.*;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.AuthorityUtils;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.jeenguyen.demo.oauth.server","com.jeenguyen.demo.oauth.mvc"})
public class Application {

    private static Logger LOGGER = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        if (args .length > 0 && "init".equalsIgnoreCase(args[0])) {
            LOGGER.info("Start initializing the sample oauth data");

            MongoTemplate mongoTemplate = (MongoTemplate) context.getBean(MongoTemplate.class);

            mongoTemplate.dropCollection(MongoUser.class);
            mongoTemplate.dropCollection(MongoClientDetails.class);
            mongoTemplate.dropCollection(MongoAccessToken.class);
            mongoTemplate.dropCollection(MongoApproval.class);
            mongoTemplate.dropCollection(MongoAuthorizationCode.class);

            
            // init the client details
            MongoClientDetails clientDetails = new MongoClientDetails();
            clientDetails.setClientId("web-client");
            clientDetails.setClientSecret("web-client-secret");
            clientDetails.setSecretRequired(true);
            clientDetails.setResourceIds(Sets.newHashSet("project-man"));
            clientDetails.setScope(Sets.newHashSet("call-services", "call-medicine"));
            clientDetails.setAuthorizedGrantTypes(Sets.newHashSet("authorization_code", "refresh_token"));
         
            clientDetails.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_USER"));
            clientDetails.setAccessTokenValiditySeconds(3600);
            clientDetails.setRefreshTokenValiditySeconds(14400);
            clientDetails.setAutoApprove(false);
            mongoTemplate.save(clientDetails);

            LOGGER.info("Finish initializing the sample oauth data");
        }
    }

}
