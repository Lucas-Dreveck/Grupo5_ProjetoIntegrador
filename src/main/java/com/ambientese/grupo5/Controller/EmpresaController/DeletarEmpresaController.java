package com.ambientese.grupo5.Controller.EmpresaController;

import com.ambientese.grupo5.Services.EmpresaService.DeletarEmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/Empresa")
@Validated
public class DeletarEmpresaController {

    private final DeletarEmpresaService deletarEmpresa;

    public DeletarEmpresaController(DeletarEmpresaService deletarEmpresa) {
        this.deletarEmpresa = deletarEmpresa;
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<?> deletarEmpresa(@PathVariable Long id) {
        deletarEmpresa.deleteEmpresa(id);
        return ResponseEntity.ok().build();
    }
}
