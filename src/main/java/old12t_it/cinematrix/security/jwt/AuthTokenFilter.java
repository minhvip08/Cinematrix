package old12t_it.cinematrix.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.security.service.UserDetailsServiceImp;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtService;
    private final UserDetailsServiceImp userDtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //parse JWT string from HTTP request header
            String jwt = parseJWT(request);
            //if can parse JWT and it is form valid then get user's info from it
            if (jwt != null && jwtService.validateToken(jwt)) {
                String userEmail = jwtService.extractUserEmail(jwt);
                UserDetails userDt = userDtService.loadUserByUsername(userEmail);
                //creat authentication obj from userdetail
                UsernamePasswordAuthenticationToken passAuthen = new UsernamePasswordAuthenticationToken(userDt, null,userDt.getAuthorities());
                passAuthen.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(passAuthen);
            }

        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }   

    String parseJWT(HttpServletRequest rq) {
        // Get JWT from cookie
        var cookie = rq.getCookies();
        if (cookie != null) {
            for (var c : cookie) {
                if (c.getName().equals("accessToken")) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
