package com.pacewisdom.admin.securityconfig;


import com.pacewisdom.admin.Entity.User;
import com.pacewisdom.admin.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo repo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = repo.findUserByEmail(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }else
            throw new UsernameNotFoundException(username);
    }
}
