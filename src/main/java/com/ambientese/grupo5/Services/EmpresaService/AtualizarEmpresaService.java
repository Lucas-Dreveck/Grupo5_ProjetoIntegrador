package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.DTO.EmpresaRequest;
import com.ambientese.grupo5.Exception.ValidacaoException;
import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtualizarEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ValidacaoCamposObrigatoriosService validacaoCamposObrigatoriosService;

    @Autowired
    private ValidacaoCNPJService validacaoCNPJService;

    @Autowired
    private MapearEmpresaService mapearEmpresaService;

    public EmpresaModel atualizarEmpresa(Long id, EmpresaRequest empresaRequest) {
        EmpresaModel empresaModel = empresaRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Empresa não encontrada com o ID: " + id));
        validacaoCamposObrigatoriosService.validarCamposObrigatorios(empresaRequest);
        validacaoCNPJService.validarCnpjUnico(empresaRequest.getCnpj(), id); // Passa o ID da empresa sendo atualizada
        mapearEmpresaService.mapearEmpresa(empresaModel, empresaRequest);
        return empresaRepository.save(empresaModel);
    }
}
