package com.kristof.exp.ConfigService.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Comparator;
import java.util.HashMap;

@Service
public class RoleService implements Comparator<String> {
    private final HashMap<String, Integer> roles = new HashMap<>();
    public RoleService() {
        roles.put("admin", 3);
        roles.put("technical", 2);
        roles.put("user", 1);
    }
    @Override
    public int compare(String userRoleName, String necessaryRoleName) {
        int userRolePositionInHierarchy = roles.getOrDefault(userRoleName, 0);
        int necessaryRolePositionInHierarchy = roles.getOrDefault(necessaryRoleName, 0);
        return Integer.compare(userRolePositionInHierarchy, necessaryRolePositionInHierarchy);
    }
    /**
     * Extracting role claim from JWT, and comparing it to the necessary role
     * @param decodedJWT verified JWT token
     * @param necessaryRoleName the necessary role's name
     * @return true if user has rights
     * @throws AuthenticationException if user doesn't have right
     */
    public boolean checkIfUserHasRole(DecodedJWT decodedJWT, String necessaryRoleName) throws AuthenticationException {
        String userRoleName = decodedJWT.getClaim("role").asString();
        if (compare(userRoleName, necessaryRoleName) >= 0) {
            return true;
        } throw new AuthenticationException("No rights to perform the action");
    }
}
