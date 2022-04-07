package com.eneyeitech.maintenance_service_api.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private UserService userService;

    @Autowired
    private LockedAccounts lockedAccounts;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        Object userName = e.getAuthentication().getPrincipal();
        Object credentials = e.getAuthentication().getCredentials();
        System.out.println("Failed login using USERNAME " + userName);
        System.out.println("Failed login using PASSWORD " + credentials);
        String email = (String) userName;

        if (!userService.isAdmin(email)) {
            loginAttemptService.loginFailed(email.toLowerCase());
        }
        if(loginAttemptService.isBlocked(email.toLowerCase())){

            lockedAccounts.lockUser(email);
        }

        /**if (xfHeader == null) {
         loginAttemptService.loginFailed(request.getRemoteAddr());
         } else {
         loginAttemptService.loginFailed(xfHeader.split(",")[0]);
         }*/
    }
}