package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.DTO.EmpresaCadastro;
import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Repository.EmpresaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ListarEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<EmpresaModel> getAllEmpresas() {
        return empresaRepository.findAll();
    }

    @Transactional
    public List<EmpresaCadastro> allPagedEmpresasWithFilter(String nome, int page, int size) {
        List<EmpresaModel> empresas;
        if (nome != null && !nome.isEmpty()) {
            empresas = empresaRepository.findAllByNomeFantasiaStartingWithIgnoreCaseOrderByNomeFantasiaAsc(nome);

            if (empresas.isEmpty()) {
                empresas = empresaRepository.findAllByRazaoSocialStartingWithIgnoreCaseOrderByRazaoSocialAsc(nome);

                if (empresas.isEmpty()) {
                    empresas = empresaRepository.findAllByRamoStartingWithIgnoreCaseOrderByNomeFantasiaAsc(nome);

                    if (empresas.isEmpty()) {
                        empresas = empresaRepository.findAllOrderByNomeFantasiaAsc().stream()
                                .filter(empresa -> empresa.getPorteEmpresas().toString().toLowerCase().startsWith(nome.toLowerCase()))
                                .collect(Collectors.toList());
                    }
                }
            }
        } else {
            empresas = empresaRepository.findAll();
        }

        List<EmpresaCadastro> resultado = paginarEmpresas(empresas, page, size);

        return resultado;
    }

    @Transactional
    public List<EmpresaCadastro> allPagedEmpresasWithFilter2(String nome, int page, int size) {
        List<EmpresaModel> empresas;
        if (nome != null && !nome.isEmpty()) {
            empresas = empresaRepository.findAllByNomeFantasiaStartingWithIgnoreCaseOrderByNomeFantasiaAsc(nome);

            if (empresas.isEmpty()) {
                empresas = empresaRepository.findAllByRazaoSocialStartingWithIgnoreCaseOrderByRazaoSocialAsc(nome);

                if (empresas.isEmpty()) {
                    empresas = empresaRepository.findAllByRamoStartingWithIgnoreCaseOrderByNomeFantasiaAsc(nome);

                    if (empresas.isEmpty()) {
                        empresas = empresaRepository.findAllOrderByNomeFantasiaAsc().stream()
                                .filter(empresa -> empresa.getPorteEmpresas().toString().toLowerCase().startsWith(nome.toLowerCase()))
                                .collect(Collectors.toList());
                    }
                }
            }
        } else {
            empresas = empresaRepository.findAllOrderByNomeFantasiaAsc();
        }

        List<EmpresaCadastro> resultado = paginarEmpresas(empresas, page, size);

        return resultado;
    }

    private List<EmpresaCadastro> paginarEmpresas(List<EmpresaModel> empresas, int page, int size) {
        int total = empresas.size();
        int start = Math.min(page * size, total);
        int end = Math.min((page + 1) * size, total);

        return empresas.subList(start, end).stream()
                .map(empresa -> new EmpresaCadastro(
                        empresa.getId(),
                        empresa.getNomeFantasia(),
                        empresa.getNomeSolicitante(),
                        empresa.getTelefoneSolicitante(),
                        empresa.getRazaoSocial(),
                        empresa.getCnpj(),
                        empresa.getInscricaoSocial(),
                        empresa.getEndereco(),
                        empresa.getEmail(),
                        empresa.getTelefoneEmpresas(),
                        empresa.getRamo(),
                        empresa.getPorteEmpresas(),
                        empresa.getRanking(),
                        end == total
                ))
                .collect(Collectors.toList());
    }
}
