package com.bnda.webapi.user;


import com.bnda.webapi.document.DocumentEntity;
import com.bnda.webapi.document.DocumentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
@Data
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentService documentService;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByEmail(usernameOrEmail);
        return userOptional.orElseThrow(()->new UsernameNotFoundException("Invalid email. User not Found!"));

    }

    public User saveUser(UserDTO userDTO, MultipartFile file) throws RuntimeException{

        if(findUserByEmail(userDTO.getEmail()).isPresent())
            throw new RuntimeException("User Already present");

        User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getMobile(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getRole());

        if(userDTO.getId()!=null && userDTO.getId()>0)
            user.setId(userDTO.getId());

        user.setDesignation(userDTO.getDesignation());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);

        if(file!=null && file.getSize()>0){
            try{
                DocumentEntity doc= documentService.saveDocument(file);
                user.setAvatarID(doc.getId());
            }catch (Exception ignored){}
        }
        try {
            user = userRepository.saveAndFlush(user);
        }catch (Exception e) {
            throw e;
        }

        return user;
    }

    public void update(User user){
        userRepository.save(user);
    }

    public User updateUser(UserDTO userDTO){
        User user= findById(userDTO.getId());
        user.setName(userDTO.getName());
        user.setMobile(userDTO.getMobile());
        user.setDesignation(userDTO.getDesignation());
        if(userDTO.getPassword()!=null && !userDTO.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if(userDTO.getFile()!=null && userDTO.getFile().getSize()>0){

            //String reson=Utils.checkFileValidity(userDTO.getFile());
            try{
                DocumentEntity doc= documentService.saveDocument(userDTO.getFile());
                user.setAvatarID(doc.getId());



            }catch (Exception ignored){}
        }
        return userRepository.save(user);
    }

    public User updatePassword(UserDTO userDTO){
        User user= findById(userDTO.getId());

        if(userDTO.getPassword()!=null && !userDTO.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }

}