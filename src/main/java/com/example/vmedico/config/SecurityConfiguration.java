package com.example.vmedico.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import com.example.vmedico.config.PasswordEnconderTest;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	@Autowired
	private DataSource securityDataSource;
	
	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		  auth.jdbcAuthentication().dataSource(securityDataSource)
		  .usersByUsernameQuery(
		   "select username,password,enabled from user where username=?")
		  .authoritiesByUsernameQuery(
		   "select username, authority from user where username=?")
		  .passwordEncoder(passwordEncoder()) ;
		 } 
	
	@Bean
	public PasswordEncoder passwordEncoder(){
	    return new PasswordEnconderTest();
	}




	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/doctor/**").hasRole("DOCTOR")
			.antMatchers("/user/**").hasRole("USER")
			.antMatchers("/register").permitAll()
			.antMatchers("/confirm").permitAll()
			.antMatchers("/login/**").permitAll()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/images/**").permitAll()
			.antMatchers("/maven/**").permitAll()
			.antMatchers("/static/**").permitAll()
			.antMatchers("/resources/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/showMyLoginPage")
			.loginProcessingUrl("/authenticateTheUser")
			.permitAll()
			.successHandler(successHandler)
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/register");
		 
	}


	
	@Override
	public void configure(WebSecurity web) throws Exception {
	
		web.ignoring().antMatchers("/resources/**");
		
	}
	

	@Bean
	public UserDetailsManager userDetailsManager() {
		
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		
		jdbcUserDetailsManager.setDataSource(securityDataSource);
		
		return jdbcUserDetailsManager; 
	}
		
	
	
}

class PasswordEnconderTest implements PasswordEncoder {
	
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }
}
