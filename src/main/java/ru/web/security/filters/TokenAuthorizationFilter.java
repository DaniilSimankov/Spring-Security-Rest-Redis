package ru.web.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.web.models.Account;
import ru.web.repositories.AccountsRepository;
import ru.web.repositories.BlackListRepository;
import ru.web.security.config.SecurityConfiguration;
import ru.web.security.details.AccountUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {
    private final AccountsRepository accountsRepository;
    private final ObjectMapper objectMapper;
    private final BlackListRepository blackListRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(SecurityConfiguration.LOGIN_FILTER_PROCESSES_URL)
                || request.getRequestURI().equals(SecurityConfiguration.LOGOUT_FILTER_PROCESSES_URL)) {
            filterChain.doFilter(request, response);
        } else {
            String tokenHeader = request.getHeader("Authorization");
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring("Bearer ".length());

//                TODO: подключить Redis
//                if(blackListRepository.exists(token)){
//                    logger.warn("Token in blacklist");
//                    filterChain.doFilter(request,response);
//                }

                Optional<Account> account = accountsRepository.findByToken(token);

                if (account.isPresent()) {
                    AccountUserDetails userDetails = new AccountUserDetails(account.get());
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(token, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } else {
                    logger.warn("Wrong token");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", "user not found with token"));
                }
            } else {
                logger.warn("Token is missing");
                filterChain.doFilter(request, response);
            }
        }
    }
}
