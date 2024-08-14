    package com.ambientese.grupo5.DTO;

    import com.ambientese.grupo5.Model.EnderecoModel;
    import com.ambientese.grupo5.Model.Enums.PorteEnum;
    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Column;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.OneToOne;

    import javax.validation.constraints.NotBlank;
    import javax.validation.constraints.NotNull;
    import javax.validation.constraints.Pattern;

    public class EmpresaRequest {

            @NotNull
            @NotBlank
            private String nomeFantasia;

            @NotNull
            @NotBlank
            private String nomeSolicitante;

            @NotNull
            @NotBlank
            @Pattern(regexp = "^[0-9]+$")
            private String telefoneSolicitante;

            @NotNull
            @NotBlank
            private String razaoSocial;

            @NotNull
            @NotBlank
            @Pattern(regexp = "^[0-9]+$")
            private String cnpj;

        @Column(length = 20)
        private String inscricaoSocial;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "endereco_id")
        private EnderecoModel endereco;

        @NotNull
            @NotBlank
            private String email;

            @NotNull
            @NotBlank
            @Pattern(regexp = "^[0-9]+$")
            private String telefoneEmpresas;

            @NotNull
            @NotBlank
            private String ramo;

             @NotNull
         @NotBlank
         private PorteEnum porteEmpresas;


        public String getNomeFantasia() {
            return nomeFantasia;
        }

        public void setNomeFantasia(String nomeFantasia) {
            this.nomeFantasia = nomeFantasia;
        }

        public String getNomeSolicitante() {
            return nomeSolicitante;
        }

        public void setNomeSolicitante(String nomeSolicitante) {
            this.nomeSolicitante = nomeSolicitante;
        }

        public String getTelefoneSolicitante() {
            return telefoneSolicitante;
        }

        public void setTelefoneSolicitante(String telefoneSolicitante) {
            this.telefoneSolicitante = telefoneSolicitante;
        }

        public String getRazaoSocial() {
            return razaoSocial;
        }

        public void setRazaoSocial(String razaoSocial) {
            this.razaoSocial = razaoSocial;
        }

        public String getCnpj() {
            return cnpj;
        }

        public void setCnpj(String cnpj) {
            this.cnpj = cnpj;
        }

        public String getInscricaoSocial() {
            return inscricaoSocial;
        }

        public void setInscricaoSocial(String inscricaoSocial) {
            this.inscricaoSocial = inscricaoSocial;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefoneEmpresas() {
            return telefoneEmpresas;
        }

        public void setTelefoneEmpresas(String telefoneEmpresas) {
            this.telefoneEmpresas = telefoneEmpresas;
        }

        public String getRamo() {
            return ramo;
        }

        public void setRamo(String ramo) {
            this.ramo = ramo;
        }

        public PorteEnum getPorteEmpresas() {
            return porteEmpresas;
        }

        public void setPorteEmpresas(PorteEnum porteEmpresas) {
            this.porteEmpresas = porteEmpresas;
        }

        public EnderecoModel getEndereco() {
            return endereco;
        }

        public void setEndereco(EnderecoModel enderecoModel) {
            this.endereco = enderecoModel;
        }
    }
