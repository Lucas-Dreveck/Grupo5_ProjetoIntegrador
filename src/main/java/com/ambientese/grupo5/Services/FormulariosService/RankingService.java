package com.ambientese.grupo5.Services.FormulariosService;

import com.ambientese.grupo5.Model.FormularioModel;
import com.ambientese.grupo5.Model.Enums.PorteEnum;
import com.ambientese.grupo5.DTO.FormularioRanking;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import com.ambientese.grupo5.Repository.FormularioRepository;
import com.ambientese.grupo5.Specifications.FormularioSpecifications;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public FormularioModel buscarPorId(Long id) {
        return formularioRepository.findById(id).orElse(null);
    }

    public List<FormularioModel> classificarPorPontuacao() {
        return formularioRepository.findLatestByEmpresaOrderByPontuacaoFinalDesc();
    }

    @Transactional
    public List<FormularioRanking> classificarPorPontuacaoWithFilter(String nomeFantasia, String ramo, PorteEnum porte, int page, int size) {
        Specification<FormularioModel> spec = Specification.where(null);

        if (nomeFantasia != null && !nomeFantasia.isEmpty()) {
            spec = spec.and(FormularioSpecifications.hasNomeFantasia(nomeFantasia));
        }
        if (ramo != null && !ramo.isEmpty()) {
            spec = spec.and(FormularioSpecifications.hasRamo(ramo));
        }
        if (porte != null) {
            spec = spec.and(FormularioSpecifications.hasPorte(porte));
        }

        List<FormularioModel> latestFormularios = formularioRepository.findLatestByEmpresaOrderByPontuacaoFinalDesc();

        final Specification<FormularioModel> finalSpec = spec;  
        List<FormularioModel> filteredFormularios = latestFormularios.stream()
                .filter(formulario -> {
                    boolean matches = true;
                    if (finalSpec != null) {
                        if (nomeFantasia != null && !nomeFantasia.isEmpty()) {
                            matches = matches && formulario.getEmpresa().getNomeFantasia().toLowerCase().startsWith(nomeFantasia.toLowerCase());
                        }
                        if (ramo != null && !ramo.isEmpty()) {
                            matches = matches && formulario.getEmpresa().getRamo().equalsIgnoreCase(ramo);
                        }
                        if (porte != null) {
                            matches = matches && formulario.getEmpresa().getPorteEmpresas() == porte;
                        }
                    }
                    return matches;
                })
                .collect(Collectors.toList());

        int start = Math.min(page * size, filteredFormularios.size());
        int end = Math.min((page + 1) * size, filteredFormularios.size());
        

        List<FormularioRanking> resultado = filteredFormularios.subList(start, end).stream()
            .map(formulario -> new FormularioRanking(
                formulario.getEmpresa().getId(), // adicionado .getEmpresa()
                formulario.getEmpresa().getRanking(),
                formulario.getEmpresa().getNomeFantasia(),
                formulario.getCertificado(),
                formulario.getEmpresa().getRamo(),
                formulario.getEmpresa().getPorteEmpresas(),
                formulario.getPontuacaoFinal(),
                formulario.getPontuacaoSocial(),
                formulario.getPontuacaoAmbiental(),
                formulario.getPontuacaoGovernamental(),
                end == filteredFormularios.size()
            )).collect(Collectors.toList());

        return resultado;
    }

    public List<String> listarRamos() {
        return empresaRepository.findDistinctRamos();
    }
    
    public List<FormularioModel>classificarPorEixoAmbiental(){
        return formularioRepository.findAllByOrderByPontuacaoAmbientalAsc();
    }
    public List<FormularioModel>classificarPorEixoSocial (){
        return formularioRepository.findAllByOrderByPontuacaoSocialAsc();
    }
    public List<FormularioModel>classificarPorEixoGovernamental (){
        return formularioRepository.findAllByOrderByPontuacaoGovernamentalAsc();
    }
}
