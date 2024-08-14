package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.DTO.EmpresaRequest;
import com.ambientese.grupo5.Model.EnderecoModel;
import org.springframework.stereotype.Service;

@Service
public class MapearEnderecoService {

    public void mapearEndereco(EnderecoModel enderecoModel, EmpresaRequest empresaRequest) {
        enderecoModel.setCep(empresaRequest.getEndereco().getCep());
        enderecoModel.setNumero(empresaRequest.getEndereco().getNumero());
        enderecoModel.setLogradouro(empresaRequest.getEndereco().getLogradouro());
        enderecoModel.setComplemento(empresaRequest.getEndereco().getComplemento());
        enderecoModel.setCidade(empresaRequest.getEndereco().getCidade());
        enderecoModel.setBairro(empresaRequest.getEndereco().getBairro());
        enderecoModel.setUF(empresaRequest.getEndereco().getUF());
    }
}
