package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Model.FormularioModel;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import com.ambientese.grupo5.Repository.FormularioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeletarEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @Transactional
    public void deleteEmpresa(Long id) {
        EmpresaModel empresa = empresaRepository.findById(id).orElse(null);
        if (empresa != null) {
            List<FormularioModel> formularios = formularioRepository.findByEmpresaId(empresa.getId());
            for (FormularioModel formulario : formularios) {
                formularioRepository.delete(formulario);
            }
            empresaRepository.delete(empresa);
        }
    }
}
