package ar.edu.unnoba.pdyc.mymusic.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component 
@Configuration
@ComponentScan(basePackages = "ar.edu.unnoba.pdyc.mymusic.resources")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(PlaylistResource.class);
        register(SongResource.class);
    }
}
