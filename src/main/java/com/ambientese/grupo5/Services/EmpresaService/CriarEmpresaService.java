package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.DTO.EmpresaRequest;
import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Model.EnderecoModel;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import com.ambientese.grupo5.Repository.EnderecoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CriarEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ValidacaoCamposObrigatoriosService validacaoCamposObrigatoriosService;

    @Autowired
    private ValidacaoCNPJService validacaoCNPJService;

    @Autowired
    private MapearEmpresaService mapearEmpresaService;

    @Autowired
    private MapearEnderecoService mapearEnderecoService;

    public EmpresaModel criarEmpresa(EmpresaRequest empresaRequest) {
        validacaoCamposObrigatoriosService.validarCamposObrigatorios(empresaRequest);
        validacaoCNPJService.validarCnpjUnico(empresaRequest.getCnpj(), null);
        EnderecoModel enderecoModel = new EnderecoModel();
        mapearEnderecoService.mapearEndereco(enderecoModel, empresaRequest);
        EnderecoModel enderecoSalvo = enderecoRepository.save(enderecoModel);
        EmpresaModel empresaModel = new EmpresaModel();
        mapearEmpresaService.mapearEmpresa(empresaModel, empresaRequest);
        empresaModel.setEndereco(enderecoSalvo);
        return empresaRepository.save(empresaModel);
    }
}
