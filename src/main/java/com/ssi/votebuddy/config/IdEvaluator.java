//package com.ssi.votebuddy.config;
//
//import org.springframework.security.access.PermissionEvaluator;
//import org.springframework.security.core.Authentication;
//
//import java.io.Serializable;
//
//public class IdEvaluator implements PermissionEvaluator {
//
//    private final
//
//    @Override
//    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//        if (targetDomainObject instanceof Long && "userMatch".equals(permission)) {
//            Long userId = (Long) targetDomainObject;
//            authentication.getPrincipal()
//        }
//
//        return false;
//    }
//
//    @Override
//    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
//
//
//        return false;
//    }
//}
