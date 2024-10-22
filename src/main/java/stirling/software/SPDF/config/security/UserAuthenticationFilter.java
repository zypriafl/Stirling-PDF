package stirling.software.SPDF.config.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stirling.software.SPDF.config.security.saml2.CustomSaml2AuthenticatedPrincipal;
import stirling.software.SPDF.config.security.session.SessionPersistentRegistry;
import stirling.software.SPDF.model.ApiKeyAuthenticationToken;
import stirling.software.SPDF.model.User;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final SessionPersistentRegistry sessionPersistentRegistry;
    private final boolean loginEnabledValue;

    public UserAuthenticationFilter(
            @Lazy UserService userService,
            SessionPersistentRegistry sessionPersistentRegistry,
            @Qualifier("loginEnabled") boolean loginEnabledValue) {
        this.userService = userService;
        this.sessionPersistentRegistry = sessionPersistentRegistry;
        this.loginEnabledValue = loginEnabledValue;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!loginEnabledValue) {
            // If login is not enabled, just pass all requests without authentication
            filterChain.doFilter(request, response);
            return;
        }
        String requestURI = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check for session expiration (unsure if needed)
        //        if (authentication != null && authentication.isAuthenticated()) {
        //            String sessionId = request.getSession().getId();
        //            SessionInformation sessionInfo =
        //                    sessionPersistentRegistry.getSessionInformation(sessionId);
        //
        //            if (sessionInfo != null && sessionInfo.isExpired()) {
        //                SecurityContextHolder.clearContext();
        //                response.sendRedirect(request.getContextPath() + "/login?expired=true");
        //                return;
        //            }
        //        }

        // Check for API key in the request headers if no authentication exists
        if (authentication == null || !authentication.isAuthenticated()) {
            String apiKey = request.getHeader("X-API-Key");
            if (apiKey != null && !apiKey.trim().isEmpty()) {
                try {
                    // Use API key to authenticate. This requires you to have an authentication
                    // provider for API keys.
                    Optional<User> user = userService.getUserByApiKey(apiKey);
                    if (!user.isPresent()) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write("Invalid API Key.");
                        return;
                    }
                    List<SimpleGrantedAuthority> authorities =
                            user.get().getAuthorities().stream()
                                    .map(
                                            authority ->
                                                    new SimpleGrantedAuthority(
                                                            authority.getAuthority()))
                                    .collect(Collectors.toList());
                    authentication = new ApiKeyAuthenticationToken(user.get(), apiKey, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (AuthenticationException e) {
                    // If API key authentication fails, deny the request
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Invalid API Key.");
                    return;
                }
            }
        }

        // If we still don't have any authentication, deny the request
        if (authentication == null || !authentication.isAuthenticated()) {
            String method = request.getMethod();
            String contextPath = request.getContextPath();

            if ("GET".equalsIgnoreCase(method) && !(contextPath + "/login").equals(requestURI)) {
                response.sendRedirect(contextPath + "/login"); // redirect to the login page
                return;
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter()
                        .write(
                                "Authentication required. Please provide a X-API-KEY in request header.\n"
                                        + "This is found in Settings -> Account Settings -> API Key\n"
                                        + "Alternatively you can disable authentication if this is unexpected");
                return;
            }
        }

        // Check if the authenticated user is disabled and invalidate their session if so
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = null;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                username = ((OAuth2User) principal).getName();
            } else if (principal instanceof CustomSaml2AuthenticatedPrincipal) {
                username = ((CustomSaml2AuthenticatedPrincipal) principal).getName();
            } else if (principal instanceof String) {
                username = (String) principal;
            }

            List<SessionInformation> sessionsInformations =
                    sessionPersistentRegistry.getAllSessions(principal, false);

            if (username != null) {
                boolean isUserExists = userService.usernameExistsIgnoreCase(username);
                boolean isUserDisabled = userService.isUserDisabled(username);

                if (!isUserExists || isUserDisabled) {
                    for (SessionInformation sessionsInformation : sessionsInformations) {
                        sessionsInformation.expireNow();
                        sessionPersistentRegistry.expireSession(sessionsInformation.getSessionId());
                    }
                }

                if (!isUserExists) {
                    response.sendRedirect(request.getContextPath() + "/logout?badcredentials=true");
                    return;
                }
                if (isUserDisabled) {
                    response.sendRedirect(request.getContextPath() + "/logout?userIsDisabled=true");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String[] permitAllPatterns = {
            contextPath + "/login",
            contextPath + "/register",
            contextPath + "/error",
            contextPath + "/images/",
            contextPath + "/public/",
            contextPath + "/css/",
            contextPath + "/fonts/",
            contextPath + "/js/",
            contextPath + "/pdfjs/",
            contextPath + "/pdfjs-legacy/",
            contextPath + "/api/v1/info/status",
            contextPath + "/site.webmanifest"
        };

        for (String pattern : permitAllPatterns) {
            if (uri.startsWith(pattern)
                    || uri.endsWith(".svg")
                    || uri.endsWith(".png")
                    || uri.endsWith(".ico")) {
                return true;
            }
        }

        return false;
    }
}
