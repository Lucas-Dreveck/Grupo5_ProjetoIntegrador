package com.ambientese.grupo5.Services.EmpresaService;
import org.springframework.stereotype.Service;

@Service
public class ValidacaoTelefoneService {

    public boolean isValidTelefone (String telefone) {
        return telefone != null;
    }
}
