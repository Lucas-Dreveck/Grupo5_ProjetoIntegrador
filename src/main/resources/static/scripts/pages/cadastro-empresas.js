let currentPage = 0;
function onOpenEmpresa() {
    const priorBtn = document.getElementById('priorBtnEmp');
    const nextBtn = document.getElementById('nextBtnEmp');
    const search = document.getElementById('search');
    const searchBtn = document.querySelector('.imgSearch');
    const overlay = document.querySelector('.overlay');
    const divAdd = document.querySelector('.divAddEmp');
    const divEdit = document.querySelector('.divEditEmp');
    const divDelete = document.querySelector('.divDeleteEmp');
    let currentid;

    nextDataPageEmpresas();

    nextBtn.addEventListener('click', () => {
        currentPage++;
        nextDataPageEmpresas();
    });

    priorBtn.addEventListener('click', () => {
       if (currentPage > 0) {
           currentPage--;
           nextDataPageEmpresas();
        }
    });

    searchBtn.addEventListener('click', () => {
        currentPage = 0;
        nextDataPageEmpresas();
    });

    search.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            currentPage = 0;
            nextDataPageEmpresas();
        }
    });

    document.querySelectorAll('.inpCnpjEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpCepEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpTelEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpTelSoEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpNumeroEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpInscricaoEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelector('.tableEmp').addEventListener('click', (event) => {

        const { currentNomeSo, currentTelSo, currentNomeFa,
            currentRazao, currentCnpj, currentInscricao,
            currentEmail, currentTelEmp, currentPorte, currentRamo,
            currentCep, currentUf, currentBairro, currentCidade, currentNumero,
            currentLogradouro } = processEventEmpresas(event);
        if(event.target.classList.contains('imgEdit')) {

            currentid = event.target.getAttribute('data-id');
            document.getElementById('nomeFaEdit').value = currentNomeFa;
            document.getElementById('nomeSoEdit').value = currentNomeSo;
            document.getElementById('telSoEdit').value = currentTelSo;
            document.getElementById('razaoEdit').value = currentRazao;
            document.getElementById('cnpjEdit').value = currentCnpj;
            document.getElementById('inscricaoEdit').value = currentInscricao;
            document.getElementById('emailEdit').value = currentEmail;
            document.getElementById('telEmpEdit').value = currentTelEmp;
            document.getElementById('ramoEdit').value = currentRamo;
            document.getElementById('porteEdit').value = currentPorte;
            document.getElementById('cepEdit').value = currentCep;
            document.getElementById('ufEdit').value = currentUf;
            document.getElementById('bairroEdit').value = currentBairro;
            document.getElementById('cidadeEdit').value = currentCidade;
            document.getElementById('numeroEdit').value = currentNumero;
            document.getElementById('ruaEdit').value = currentLogradouro;

            divEdit.style.display = 'block';
            overlay.style.display = 'block';

        } else if(event.target.classList.contains('imgDelete')) {
           currentid = event.target.getAttribute('data-id');
            divDelete.style.display = 'block';
            overlay.style.display = 'block';
            document.querySelector('.deleteMsg').innerHTML = `Deseja deletar a empresa ${currentNomeFa}?`;
        }
    });

    document.querySelector('.btnAdd').addEventListener('click', () => {
        divAdd.style.display = 'block';
        overlay.style.display = 'block';
    });

    document.querySelectorAll('.btnsCancel').forEach(btn => {
        btn.addEventListener('click', () => {
            overlay.style.display = 'none';
            divAdd.style.display = 'none';
            divEdit.style.display = 'none';
            divDelete.style.display = 'none';
        });
    });

    document.getElementById('confirmAddEmp').addEventListener('click', () => {
        const nomeFantasia = document.getElementById('nomeFa').value;
        const nomeSolicitante = document.getElementById('nomeSo').value;
        const telefoneSolicitante = document.getElementById('telSo').value;
        const razaoSocial = document.getElementById('razao').value;
        const cnpj = document.getElementById('CNPJ').value;
        const inscricaoSocial = document.getElementById('inscricao').value;
        const email = document.getElementById('email').value;
        const telefoneEmpresas = document.getElementById('telEmp').value;
        const ramo = document.getElementById('ramo').value;
        const porteEmpresas = document.getElementById('porte').value;
        const dataAlteracao = new Date();
        const numero = document.getElementById('numero').value;
        const cep = document.getElementById('cep').value;
        const logradouro = document.getElementById('rua').value;
        const cidade = document.getElementById('cidade').value;
        const bairro = document.getElementById('bairro').value;
        const uf = document.getElementById('uf').value;

        if(!nomeFantasia || !nomeSolicitante || !telefoneSolicitante || !razaoSocial || !cnpj || !inscricaoSocial || !email || !telefoneEmpresas || !ramo || !porteEmpresas) {
            toastAlert('Preencha todos os campos!', 'error');
            return;
        }

        const isNumber = parseInt(numero)
        if(!isNumber || isNumber < 0 || isNumber > 9999999999) {
            toastAlert('Insira um número válido', 'error');
            return;
        }

        const data = {
            nomeFantasia,
            nomeSolicitante,
            telefoneSolicitante,
            razaoSocial,
            cnpj,
            inscricaoSocial,
            email,
            telefoneEmpresas,
            ramo,
            endereco: {
                cep,
                numero,
                logradouro,
                cidade,
                bairro,
                uf
            },
            porteEmpresas,
            dataAlteracao
        };

        fetch(`${URL}/auth/Empresa/Add`, {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        })
            .then(async response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        try {
                            const errorData = JSON.parse(text);
                            throw new Error(errorData.message || 'Erro ao adicionar empresa');
                        } catch (e) {
                            throw new Error(text || 'Erro ao adicionar empresa');
                        }
                    });
                }
                return response.json();
            })
            .then(data => {
                toastAlert('Empresa adicionada com sucesso!', 'success');
                divAdd.style.display = 'none';
                overlay.style.display = 'none';
                nextDataPageEmpresas();
                currentPage = 0;
                document.getElementById('nomeFa').value = '';
                document.getElementById('nomeSo').value = '';
                document.getElementById('telSo').value = '';
                document.getElementById('razao').value = '';
                document.getElementById('CNPJ').value = '';
                document.getElementById('inscricao').value = '';
                document.getElementById('email').value = '';
                document.getElementById('telEmp').value = '';
                document.getElementById('ramo').value = '';
                document.getElementById('porte').value = '';
                document.getElementById('numero').value = '';
                document.getElementById('cep').value = '';
                document.getElementById('rua').value = '';
                document.getElementById('cidade').value = '';
                document.getElementById('bairro').value = '';
                document.getElementById('uf').value = '';
            })
            .catch(error => {
                const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
                toastAlert(errorMessage, 'error');
            });
    });

    document.getElementById('confirmEditEmp').addEventListener('click', () => {
        const nomeFantasia = document.getElementById('nomeFaEdit').value;
        const nomeSolicitante = document.getElementById('nomeSoEdit').value;
        const telefoneSolicitante = document.getElementById('telSoEdit').value;
        const razaoSocial = document.getElementById('razaoEdit').value;
        const cnpj = document.getElementById('cnpjEdit').value;
        const inscricaoSocial = document.getElementById('inscricaoEdit').value;
        const email = document.getElementById('emailEdit').value;
        const telefoneEmpresas = document.getElementById('telEmpEdit').value;
        const ramo = document.getElementById('ramoEdit').value;
        const porteEmpresas = document.getElementById('porteEdit').value;
        const dataAlteracao = new Date();
        const numero = document.getElementById('numeroEdit').value;
        const cep = document.getElementById('cepEdit').value;
        const logradouro = document.getElementById('ruaEdit').value;
        const cidade = document.getElementById('cidadeEdit').value;
        const bairro = document.getElementById('bairroEdit').value;
        const uf = document.getElementById('ufEdit').value;

        if(!nomeFantasia || !nomeSolicitante || !telefoneSolicitante || !razaoSocial || !cnpj || !inscricaoSocial || !email || !telefoneEmpresas || !ramo || !porteEmpresas) {
            toastAlert('Preencha todos os campos!', 'error');
            return;
        }

        const isNumber = parseInt(numero)
        if(!isNumber || isNumber < 0 || isNumber > 9999999999) {
            toastAlert('Insira um número válido', 'error');
            return;
        }

        const data = {
            nomeFantasia,
            nomeSolicitante,
            telefoneSolicitante,
            razaoSocial,
            cnpj,
            inscricaoSocial,
            email,
            telefoneEmpresas,
            ramo,
            endereco: {
                cep,
                numero,
                logradouro,
                cidade,
                bairro,
                uf
            },
            porteEmpresas,
            dataAlteracao
        };

        let id = parseInt(currentid);

        fetch(`${URL}/auth/Empresa/Edit/${id}`, {
            method: 'PUT',
            headers,
            body: JSON.stringify(data)
        })
            .then( async response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        try {
                            const errorData = JSON.parse(text);
                            throw new Error(errorData.message || 'Erro ao editar empresa');
                        } catch (e) {
                            throw new Error(text || 'Erro ao editar empresa');
                        }
                    });
                }
                return response.json();
            })
            .then(data => {
                toastAlert('Empresa editada com sucesso!', 'success');
                currentPage = 0;
                nextDataPageEmpresas();
                divEdit.style.display = 'none';
                overlay.style.display = 'none';
                document.getElementById('nomeFaEdit').value = '';
                document.getElementById('nomeSoEdit').value = '';
                document.getElementById('telSoEdit').value = '';
                document.getElementById('razaoEdit').value = '';
                document.getElementById('cnpjEdit').value = '';
                document.getElementById('inscricaoEdit').value = '';
                document.getElementById('emailEdit').value = '';
                document.getElementById('telEmpEdit').value = '';
                document.getElementById('ramoEdit').value = '';
                document.getElementById('porteEdit').value = '';
                document.getElementById('cepEdit').value = '';
                document.getElementById('ufEdit').value = '';
                document.getElementById('bairroEdit').value = '';
                document.getElementById('cidadeEdit').value = '';
                document.getElementById('numeroEdit').value = '';
                document.getElementById('ruaEdit').value = '';
            })
            .catch(error => {
                const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
                toastAlert(errorMessage, 'error');
            });
    });

    document.getElementById('confirmDelete').addEventListener('click' , () => {
        const id = parseInt(currentid);
        fetch(`${URL}/auth/Empresa/Delete/${id}`, {
            method: 'DELETE',
            headers,
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao deletar empresa');
            }
            return response.text();
        })
        .then(data => {
            toastAlert('Empresa deletada com sucesso!', 'success');
            currentPage = 0;
            nextDataPageEmpresas();
            divDelete.style.display = 'none';
            overlay.style.display = 'none';
        })
        .catch(error => {
            const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
            toastAlert(errorMessage, 'error');
        });
    });

    overlay.addEventListener('click', () => {
        overlay.style.display = 'none';
        divAdd.style.display = 'none';
        divEdit.style.display = 'none';
        divDelete.style.display = 'none';
    });
}

function addTableLinesEmpresas(data) {
    const table = document.querySelector('.tableEmp>tbody');
    const prevBtn = document.getElementById('priorBtnEmp');
    const nextBtn = document.getElementById('nextBtnEmp');
    if(data.length === 0) {
        toastAlert('Nenhuma empresa encontrada', 'error');
        nextBtn.setAttribute('disabled', 'true');
        nextBtn.classList.add('disabled');
    } else {
        if (data[0].finishList) {
            nextBtn.setAttribute('disabled', 'true');
            nextBtn.classList.add('disabled');
        } else {
            nextBtn.removeAttribute('disabled');
            nextBtn.classList.remove('disabled');
        };
    }
    if (currentPage > 0 ) {
        prevBtn.removeAttribute('disabled');
        prevBtn.classList.remove('disabled');
    } else {
        prevBtn.setAttribute('disabled', 'true');
        prevBtn.classList.add('disabled');
    };

    let count = 0;

    data.forEach((empresa, index) => {
        const newLine = document.createElement('tr');

        const classe = count % 2 === 0 ? 'azul' : '';
        count++;

        newLine.innerHTML = `
            <td class="thStyle thImg ${classe}">
                <img src="/icons//Cadastro-Empresa/Empresa.png" alt="Empresa" class="thImg">
            </td>
            <td class="thStyle ${classe}">${empresa.id}</td>
            <td class="thStyle ${classe}">${empresa.nomeFantasia}</td>
            <td class="thStyle ${classe}">${empresa.ramo}</td>
            <td class="thStyle ${classe}">${empresa.porteEmpresas}</td>
            <td class="thStyle ${classe}">
                <img src="/icons//Cadastro-Empresa/edit.png" 
                    data-id="${empresa.id}"
                    data-nomeFa="${empresa.nomeFantasia}"
                    data-nomeSo="${empresa.nomeSolicitante}"
                    data-telSo="${empresa.telefoneSolicitante}"
                    data-razao="${empresa.razaoSocial}"
                    data-cnpj="${empresa.cnpj}"
                    data-inscricao="${empresa.inscricaoSocial}"
                    data-email="${empresa.email}"
                    data-telEmp="${empresa.telefoneEmpresas}"
                    data-ramo="${empresa.ramo}"
                    data-porte="${empresa.porteEmpresas}"
                    data-cep="${empresa.endereco.cep}"
                    data-numero="${empresa.endereco.numero}"
                    data-logradouro="${empresa.endereco.logradouro}"
                    data-cidade="${empresa.endereco.cidade}"
                    data-bairro="${empresa.endereco.bairro}"
                    data-uf="${empresa.endereco.uf}"
                alt="Editar" class="imgEdit imgStyle">
                <img src="/icons//Cadastro-Empresa/delete.png"
                    data-id="${empresa.id}"
                    data-nomeFa="${empresa.nomeFantasia}"
                alt="Deletar" class="imgDelete imgStyle">                
            </td>
        `;

        table.appendChild(newLine);
    });
}

function processEventEmpresas(event) {
    const currentid = event.target.getAttribute('data-id');
    const currentNomeFa = event.target.getAttribute('data-nomeFa');
    const currentNomeSo = event.target.getAttribute('data-nomeSo');
    const currentTelSo = event.target.getAttribute('data-telSo');
    const currentRazao = event.target.getAttribute('data-razao');
    const currentCnpj = event.target.getAttribute('data-cnpj');
    const currentInscricao = event.target.getAttribute('data-inscricao');
    const currentEmail = event.target.getAttribute('data-email');
    const currentTelEmp = event.target.getAttribute('data-telEmp');
    const currentRamo = event.target.getAttribute('data-ramo');
    const currentPorte = event.target.getAttribute('data-porte');
    const currentCep = event.target.getAttribute('data-cep');
    const currentNumero = event.target.getAttribute('data-numero');
    const currentLogradouro = event.target.getAttribute('data-logradouro');
    const currentCidade = event.target.getAttribute('data-cidade');
    const currentBairro = event.target.getAttribute('data-bairro');
    const currentUf = event.target.getAttribute('data-uf');

    return {
        currentid,
        currentNomeFa,
        currentNomeSo,
        currentTelSo,
        currentRazao,
        currentCnpj,
        currentInscricao,
        currentEmail,
        currentTelEmp,
        currentRamo,
        currentPorte,
        currentCep,
        currentNumero,
        currentLogradouro,
        currentCidade,
        currentBairro,
        currentUf
    };
}

function nextDataPageEmpresas () {
    const search = document.getElementById('search').value;
    const queryParams = new URLSearchParams();
    if (search) queryParams.append('nome', search);
    queryParams.append('page', currentPage);

    fetch(`${URL}/auth/Empresa/search?${queryParams.toString()}`, {
        method: 'GET',
        headers,
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao buscar empresas');
        }
        return response.json();
    })
    .then(data => {
        const table = document.querySelector('.tableEmp>tbody');
        const trs = Array.from(table.children);
            trs.forEach(tag => {
                tag.parentNode.removeChild(tag);
            }
        );
        addTableLinesEmpresas(data);
    })
    .catch(error => {
        const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
        toastAlert(errorMessage, 'error');
    });
}

function justNumbers(event) {
    var key = event.key;
    var keyCode = event.keyCode || event.which;
    var ctrlPressed = event.ctrlKey || event.metaKey;

    if(ctrlPressed && keyCode === 65 || keyCode === 67 || keyCode === 86 || keyCode === 88) {
        return;
    }

    if (!/^[0-9]$/.test(key) &&
        keyCode !== 8 &&
        keyCode !== 46 &&
        !(keyCode >= 37 && keyCode <= 40) &&
        keyCode !== 36 &&
        keyCode !== 9 &&
        keyCode !== 17 &&
        keyCode !== 35) {
        event.preventDefault();
        toastAlert('Insira apenas números', 'error');
    }
}