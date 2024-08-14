package com.ambientese.grupo5.Controller.EmpresaController;

import com.ambientese.grupo5.DTO.EmpresaRequest;
import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Services.EmpresaService.AtualizarEmpresaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth/Empresa")
@Validated
public class AtualizarEmpresaController {
    private final AtualizarEmpresaService atualizarEmpresa;

    public AtualizarEmpresaController(AtualizarEmpresaService atualizarEmpresa)
    {
        this.atualizarEmpresa = atualizarEmpresa;

    }
    @PutMapping("/Edit/{id}")
    public EmpresaModel atualizarEmpresa(@PathVariable Long id, @Valid @RequestBody EmpresaRequest empresaRequest) {
        return atualizarEmpresa.atualizarEmpresa(id, empresaRequest);
    }
}
