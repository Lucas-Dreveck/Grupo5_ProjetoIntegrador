package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.Exception.ValidacaoException;
import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidacaoCNPJService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) {
            return false;
        }
        int[] digitos = new int[cnpj.length()];
        for (int i = 0; i < cnpj.length(); i++) {
            digitos[i] = Character.getNumericValue(cnpj.charAt(i));
        }
        int soma = 0;
        soma += digitos[0] * 5;
        soma += digitos[1] * 4;
        soma += digitos[2] * 3;
        soma += digitos[3] * 2;
        soma += digitos[4] * 9;
        soma += digitos[5] * 8;
        soma += digitos[6] * 7;
        soma += digitos[7] * 6;
        soma += digitos[8] * 5;
        soma += digitos[9] * 4;
        soma += digitos[10] * 3;
        soma += digitos[11] * 2;
        int resto = soma % 11;
        int dv1 = resto < 2 ? 0 : 11 - resto;
        if (digitos[12] != dv1) {
            return false;
        }
        soma = 0;
        soma += digitos[0] * 6;
        soma += digitos[1] * 5;
        soma += digitos[2] * 4;
        soma += digitos[3] * 3;
        soma += digitos[4] * 2;
        soma += digitos[5] * 9;
        soma += digitos[6] * 8;
        soma += digitos[7] * 7;
        soma += digitos[8] * 6;
        soma += digitos[9] * 5;
        soma += digitos[10] * 4;
        soma += digitos[11] * 3;
        soma += digitos[12] * 2;
        resto = soma % 11;
        int dv2 = resto < 2 ? 0 : 11 - resto;
        return digitos[13] == dv2;
    }

    public void validarCnpjUnico(String cnpj, Long empresaId) {
        Optional<EmpresaModel> empresaExistente = empresaRepository.findByCnpj(cnpj);
        empresaExistente.ifPresent(empresa -> {
            if (!empresa.getId().equals(empresaId)) {
                throw new ValidacaoException("Já existe uma empresa cadastrada com este CNPJ");
            }
        });
    }
}