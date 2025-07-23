package project.learning_managment_system.learning_managment_system_dev.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthCoverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractRessourcesRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipleClaimName(jwt)
        );
    }
    private String getPrincipleClaimName(Jwt source){
        String claimName= JwtClaimNames.SUB;
        if(principleAttribute!=null){
            claimName=principleAttribute;
        }
        return source.getClaim(claimName);
    }
    private Collection<? extends GrantedAuthority> extractRessourcesRoles(Jwt jwt){
        Map<String,Object> ressource_access;
        Map<String,Object> ressource_id;
        Collection<String> resourceRoles;
        if(jwt.getClaim("resource_access")==null){
            return Set.of();
        }
        ressource_access=jwt.getClaim("resource_access");
        if(ressource_access.get(resourceId)==null){
            return Set.of();
        }
        ressource_id= (Map<String, Object>) ressource_access.get(resourceId);
        resourceRoles= (Collection<String>) ressource_id.get("roles");
        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
