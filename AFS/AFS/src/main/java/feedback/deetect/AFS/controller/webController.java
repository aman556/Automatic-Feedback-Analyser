package feedback.deetect.AFS.controller;

import feedback.deetect.AFS.model.User;
import feedback.deetect.AFS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class webController {

    @Autowired
    private UserRepository repo;

    @GetMapping("")
    public String login(){
        return "login";
    }
    @GetMapping("/register")
    public  String showSignUpForm(Model model){
        model.addAttribute("user",new User());

        return "register";
    }
    @PostMapping("/process_register")
    public String processRegistration(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        repo.save(user);
        //currentUserEmail=user.getEmail();
        return "first";
    }
}
