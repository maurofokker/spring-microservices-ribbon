package com.maurofokker.poc.cloud;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

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
