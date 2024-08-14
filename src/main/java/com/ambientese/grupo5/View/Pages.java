package com.ambientese.grupo5.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class Pages {

    @GetMapping("/signup")
    public String pageSignup(Model model, HttpServletRequest request) {
        return processRequest(request, "signup", "signup");
    }

    @GetMapping("/login")
    public String pageLogin(Model model, HttpServletRequest request) {
        return processRequest(request, "login", "login");
    }

    @GetMapping("/forgot-password")
    public String pageForgotPass(Model model, HttpServletRequest request) {
        return processRequest(request, "forgot-password", "forgot-password");
    }

    @GetMapping("/empresas")
    public String pageEmpresas(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/cadastro-empresas", "empresas");
    }

    @GetMapping("/funcionarios")
    public String pageFuncionarios(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/cadastro-funcionarios", "funcionarios");
    }

    @GetMapping("/perguntas")
    public String pagePerguntas(Model model, HttpServletRequest request) throws InterruptedException {
        String meuHeaderValue = request.getHeader("X-Requested-With");
        if (meuHeaderValue != null && meuHeaderValue.equals("InsideApplication")) {
            return "pages/cadastro-perguntas";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/ranking")
    public String pageRanking(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/ranking", "ranking");
    }

    @GetMapping("/start-avaliacao")
    public String pageStartAvaliacao(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/avaliacao/selecao-empresa", "start-avaliacao");
    }

    @GetMapping("/avaliacao")
    public String pageAvaliacao(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/avaliacao/avaliacao", "start-avaliacao");
    }

    @GetMapping("/result-avaliacao")
    public String pageResultAvaliacao(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/avaliacao/result-avaliacao", "start-avaliacao");
    }

    private String processRequest(HttpServletRequest request, String page, String route) {
        String meuHeaderValue = request.getHeader("X-Requested-With");
        if (meuHeaderValue != null && meuHeaderValue.equals("InsideApplication")) {
            return page;
        } else {
            return "redirect:/?page=" + route;
        }
    }    
}
