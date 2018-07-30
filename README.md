# Spring Cloud Microservice with Ribbon client side load balancer

* Part of [micro services demo](https://github.com/maurofokker/microservices-demo)
* Ribbon configuration
  * Create a service configuration 
    ```java
    public class SimpleServiceConfiguration {
    
        @Autowired
        public IClientConfig ribbonClientConfig;
    
        // this is going to define how the client is gonna ping the different
        // instances that ribbon will be load balancing to make sure that they're available
        @Bean
        public IPing ping(IClientConfig config) {
            return new PingUrl();   // return an implementation of IPing
        }
    
        // to define the load balancing strategy (round robin, availability filtering rule... etc - see reference)
        // this case will use Availability Filtering rule, that will filtered the client that are distributing the requests to
        // based upon some different criteria found in the availability filtering
        @Bean
        public IRule rule(IClientConfig config) {
            return new AvailabilityFilteringRule();
        }
    }
    ```
  * Edit `application.properties` with configurations for service that will be load balancing with ribbon (in this case [simple-service-2](https://github.com/maurofokker/spring-microservices-simple-service-2))
    ```properties
    simple-service-2.ribbon.eureka.server=false
    simple-service-2.ribbon.ServerListRefreshInterval=15000
    simple-service-2.ribbon.listOfServers=localhost:7777,localhost:8888,localhost:9999
    ```
    * `[simple-service-2].`: is the name given to the service and is going to be used when define the Ribbon Client
    * `simple-service-2.ribbon.eureka.server=false` tell the service is not going to use eureka server
    * `simple-service-2.ribbon.ServerListRefreshInterval=15000` for server list refresh interval
    * `simple-service-2.ribbon.listOfServers=localhost:7777,localhost:8888,localhost:9999` list of instances established for micro service [simple-service-2](https://github.com/maurofokker/spring-microservices-simple-service-2)
  * Enable `RibbonClient`
    ```java
    @RibbonClient(name = "simple-service-2", configuration = SimpleServiceConfiguration.class) 
    ```
    * `name`: of the service we are referring to with ribbon client
    * `configuration`: configuration class that defines load balancing strategy
  * Use `@LoadBalanced` annotation that indicates RestTemplate is going to be load balanced and going to consult ribbon client config when making requests for a particular service
    ```java
    @Bean
    @LoadBalanced              
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    ```
  * Use service name given in `@RibbonClient` annotation and `configuration.properties` to distribute all requests across different instances when using resttemplate
    ```java
      @RequestMapping("/ribbonClient")
      public String ribbonClient() {
          return this.restTemplate.getForObject("http://simple-service-2/service", String.class); 
      }
    ```
