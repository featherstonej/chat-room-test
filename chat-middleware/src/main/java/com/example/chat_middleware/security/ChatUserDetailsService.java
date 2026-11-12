package com.example.chat_middleware.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.chat_middleware.dao.UserDao;
import com.example.chat_middleware.dao.entities.Users;

@Service 
public class ChatUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public ChatUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional <Users> u = userDao.findByUserName(username);
        if (u.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new ChatUserPrincipal(u.get().getId(), u.get().getUserName(), u.get().getPassword());
    }    
    
}
