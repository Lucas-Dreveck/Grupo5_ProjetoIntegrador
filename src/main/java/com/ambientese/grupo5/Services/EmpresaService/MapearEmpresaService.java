package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.DTO.EmpresaRequest;
import com.ambientese.grupo5.Model.EmpresaModel;
import org.springframework.stereotype.Service;

@Service
public class MapearEmpresaService {
    public void mapearEmpresa(EmpresaModel empresaModel, EmpresaRequest empresaRequest) {
        empresaModel.setNomeFantasia(empresaRequest.getNomeFantasia());
        empresaModel.setNomeSolicitante(empresaRequest.getNomeSolicitante());
        empresaModel.setTelefoneSolicitante(empresaRequest.getTelefoneSolicitante());
        empresaModel.setRazaoSocial(empresaRequest.getRazaoSocial());
        empresaModel.setCnpj(empresaRequest.getCnpj());
        empresaModel.setInscricaoSocial(empresaRequest.getInscricaoSocial());
        empresaModel.setEmail(empresaRequest.getEmail());
        empresaModel.setTelefoneEmpresas(empresaRequest.getTelefoneEmpresas());
        empresaModel.setRamo(empresaRequest.getRamo());
        empresaModel.setPorteEmpresas(empresaRequest.getPorteEmpresas());
        empresaModel.setEndereco(empresaRequest.getEndereco());
    }
}
