package co.com.seti.model.common.jwt;

import java.util.Map;

public interface JwtUtilGateway {
    String generateToken(String user, Map<String, Object> claims);
}
