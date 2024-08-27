package com.ambientese.grupo5.Controller;


import com.ambientese.grupo5.DTO.EmpresaCadastro;
import com.ambientese.grupo5.DTO.EmpresaRequest;
import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import com.ambientese.grupo5.Services.EmpresaService.AtualizarEmpresaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import com.ambientese.grupo5.Services.EmpresaService.CriarEmpresaService;
import com.ambientese.grupo5.Services.EmpresaService.DeletarEmpresaService;
import com.ambientese.grupo5.Services.EmpresaService.ListarEmpresaService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/auth/Empresa")
@Tag(name = "Empresa", description = "Endpoints para gerenciamento de empresas")
public class EmpresaController {
    
    @Autowired
    private CriarEmpresaService criarEmpresaService;

    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private ListarEmpresaService listarEmpresaService;

    @Autowired
    private AtualizarEmpresaService atualizarEmpresaService;

    @Autowired
    private DeletarEmpresaService deletarEmpresaService;

    @GetMapping
    public ResponseEntity<List<EmpresaModel>> getAllEmpresas() {
        List<EmpresaModel> empresaModels = listarEmpresaService.getAllEmpresas();
        return ResponseEntity.ok(empresaModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmpresaPorId(@PathVariable Long id) {
        Optional<EmpresaModel> empresa = empresaRepository.findById(id);
        return empresa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmpresaCadastro>> buscarEmpresas(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<EmpresaCadastro> resultado = listarEmpresaService.allPagedEmpresasWithFilter(nome, page, size);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/avaliacao/search")
    public ResponseEntity<List<EmpresaCadastro>> empresasParaAvaliacao(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<EmpresaCadastro> resultado = listarEmpresaService.allPagedEmpresasWithFilter2(nome, page, size);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/Add")
    public EmpresaModel criarEmpresa (@RequestBody EmpresaRequest empresaModel) {
        return criarEmpresaService.criarEmpresa(empresaModel);
    }

    @PutMapping("/Edit/{id}")
    public EmpresaModel atualizarEmpresa(@PathVariable Long id, @Valid @RequestBody EmpresaRequest empresaRequest) {
        return atualizarEmpresaService.atualizarEmpresa(id, empresaRequest);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<?> deletarEmpresa(@PathVariable Long id) {
        deletarEmpresaService.deleteEmpresa(id);
        return ResponseEntity.ok().build();
    }
}
