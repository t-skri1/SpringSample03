package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.example.security.MyAccessDeniedHandler;
import com.example.security.MyAuthenticationSuccessHandler;
import com.example.security.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService());
    }

    @Bean
    public UserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Basic認証
     */
    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public class WebSecurityBasicConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .antMatcher("/api/**")
                .authorizeRequests()
                    .antMatchers("/api/admin/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                    .and()
                    .httpBasic()
            ;
        }
    }

    /**
     * Form認証
     */
    @Configuration
    public class WebSecurityFormConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            CharacterEncodingFilter filter = new CharacterEncodingFilter();
            filter.setEncoding("UTF-8");
            filter.setForceEncoding(true);

              http
                .addFilterBefore(filter, CsrfFilter.class)
                .authorizeRequests()

                //どのロールでもアクセス可
                .antMatchers("/", "/resources/**", "/webjars/**", "/auth/**", "/adminReg/**").permitAll()

                // ロールにるアクセス設定
                // '/userReg/'で始まるURLには、'ADMIN'ロールのみアクセス可
                .antMatchers("/userReg/**").hasRole("ADMIN")

                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginProcessingUrl("/login")
                    .loginPage("/auth/login")
                    .failureUrl("/auth/login-error")
                    .successHandler(new MyAuthenticationSuccessHandler())
                    .permitAll()
                .and()
                // ログアウト処理の設定
                    .logout()
                    // ログアウト処理のURL
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    // ログアウト成功時の遷移先URL
                    .logoutSuccessUrl("/auth/login")
                    // ログアウト時に削除するクッキー名
                    .deleteCookies("JSESSIONID")
                    // ログアウト時のセッション破棄を有効化
                    .invalidateHttpSession(true)
                    .permitAll()
                .and()
                //403エラー時の遷移先設定
                    .exceptionHandling()
                    .accessDeniedHandler(new MyAccessDeniedHandler())

                ;
        }

    }
}