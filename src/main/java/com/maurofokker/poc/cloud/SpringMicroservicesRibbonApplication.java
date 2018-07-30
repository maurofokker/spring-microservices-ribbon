package com.maurofokker.poc.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@RibbonClient(name = "simple-service-2", configuration = SimpleServiceConfiguration.class)  // name: of the service we are referring to with ribbon client; second config Class
public class SpringMicroservicesRibbonApplication {

    @Bean
    @LoadBalanced               // indicates rest template to be load balanced so its going to consult ribbon client config when making requests for a particular service
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    public RestTemplate restTemplate;

    @RequestMapping("/ribbonClient")
    public String ribbonClient() {
        return this.restTemplate.getForObject("http://simple-service-2/service", String.class); // will distribute all requests across different instances
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringMicroservicesRibbonApplication.class, args);
    }
}
