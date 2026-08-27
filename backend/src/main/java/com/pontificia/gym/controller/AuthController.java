package com.pontificia.gym.controller;

import com.pontificia.gym.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public CommandLineRunner initUsersRunner() {
        return args -> {
            usuarioService.inicializarUsuariosPorDefecto();
        };
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Has cerrado sesión correctamente");
        }

        return "auth/login";
    }
}
