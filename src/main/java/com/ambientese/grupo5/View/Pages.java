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

    @GetMapping("/companies")
    public String pageCompanies(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/companies-registration", "companies");
    }

    @GetMapping("/employees")
    public String pageEmployees(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/employees-registration", "employees");
    }
//lembrar que alterei aqui
    @GetMapping("/questions")
    public String pageQuestions(Model model, HttpServletRequest request) throws InterruptedException {
        return processRequest(request, "pages/questions-registration", "questions");
    }

    @GetMapping("/ranking")
    public String pageRanking(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/ranking", "ranking");
    }

    @GetMapping("/start-form")
    public String pageStartForm(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/form/start-form", "start-form");
    }

    @GetMapping("/form")
    public String pageForm(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/form/form", "start-form");
    }

    @GetMapping("/result-form")
    public String pageResultForm(Model model, HttpServletRequest request) {
        return processRequest(request, "pages/form/result-form", "start-form");
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
