package project.learning_managment_system.learning_managment_system_dev.Config;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.function.Function;

public class JwtExtractor {

   public String extractClaim(Jwt jwt, String claim){

       return jwt.getClaimAsString(claim);
   }
}
