package ru.web.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.web.dto.SignInForm;
import ru.web.models.Account;
import ru.web.repositories.AccountsRepository;
import ru.web.repositories.BlackListRepository;
import ru.web.security.details.AccountUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Slf4j
public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String TOKEN = "token";
    private final ObjectMapper objectMapper;
    private final AccountsRepository accountsRepository;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, AccountsRepository accountsRepository) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.accountsRepository = accountsRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SignInForm form = objectMapper.readValue(request.getReader(), SignInForm.class);
            log.info("Attempt authentication - email {}, password {}", form.getEmail(), form.getPassword());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(form.getEmail(),
                    form.getPassword());

            return super.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AccountUserDetails userDetails = (AccountUserDetails) authResult.getPrincipal();
        Account account = userDetails.getAccount();
        String token = UUID.randomUUID().toString();
        account.setToken(token);
        accountsRepository.save(account);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), Collections.singletonMap(TOKEN, token));
    }
}
