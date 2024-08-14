package com.ambientese.grupo5.Services.FuncionarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.DTO.FuncionarioRequest;
import com.ambientese.grupo5.Model.FuncionarioModel;
import com.ambientese.grupo5.Repository.CargoRepository;
import com.ambientese.grupo5.Repository.FuncionarioRepository;
import com.ambientese.grupo5.Services.UsuarioService.CriarUsuarioService;

@Service
public class CriarFuncionarioService {
    
    @Autowired 
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private CriarUsuarioService criarUsuarioService;

    public FuncionarioModel criarFuncionario(FuncionarioRequest funcionarioRequest) {
        FuncionarioModel funcionarioModel = new FuncionarioModel();
        funcionarioModel.setNome(funcionarioRequest.getNome());
        funcionarioModel.setCpf(funcionarioRequest.getCpf());
        funcionarioModel.setEmail(funcionarioRequest.getEmail());
        funcionarioModel.setDataNascimento(funcionarioRequest.getDataNascimento());
        funcionarioModel.setCargo(cargoRepository.findByDescricao(funcionarioRequest.getCargo()));
        funcionarioModel.setUsuario(criarUsuarioService.createUsuario(funcionarioRequest.getUsuario()));
        return funcionarioRepository.save(funcionarioModel);
    }
}
