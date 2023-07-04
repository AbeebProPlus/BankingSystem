package com.system.moneybank.service.security;

import com.system.moneybank.config.OfficerDetails;
import com.system.moneybank.models.Customer;
import com.system.moneybank.models.Officer;
import com.system.moneybank.repository.CustomerRepo;
import com.system.moneybank.repository.OfficerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private OfficerRepo officerRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Officer> officer = officerRepo.findByUserName(username);
        if (officer.get().getRole().equals("OFFICER")) {
                return new OfficerDetails(officer.get());
            }
        throw new UsernameNotFoundException("User not found");
    }
}