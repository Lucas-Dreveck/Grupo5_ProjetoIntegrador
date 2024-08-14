let currentPageFuncionario = 0;
function onOpenFuncionario() {
    const priorBtn = document.getElementById('priorBtnFunc');
    const nextBtn = document.getElementById('nextBtnFunc');
    const search = document.getElementById('search');
    const searchBtn = document.querySelector('.imgSearch');
    const overlay = document.querySelector('.overlay');
    const divAdd = document.querySelector('.divAddFunc');
    const divEdit = document.querySelector('.divEditFunc');
    const divDelete = document.querySelector('.divDeleteFunc');
    let currentId;

    nextDataPageFuncionarios()

    nextBtn.addEventListener('click', () => {
        currentPageFuncionario++;
        nextDataPageFuncionarios();
    });

    priorBtn.addEventListener('click', () => {
       if (currentPageFuncionario > 0) {
           currentPageFuncionario--;
           nextDataPageFuncionarios();
        }
    });

    searchBtn.addEventListener('click', () => {
        currentPageFuncionario = 0;
        nextDataPageFuncionarios();
    });

    search.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            currentPageFuncionario = 0;
            nextDataPageFuncionarios();
        }
    });

    document.querySelectorAll('.inputNasc').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inputCpf').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.getElementById('confirmDelete').addEventListener('click', () => {
        const id = parseInt(currentId);
        fetch(`${URL}/auth/Funcionario/Delete/${id}`, {
            method: 'DELETE',
            headers,
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao deletar Funcionario');
            }
            return response.text();
        })
        .then(data => {
            toastAlert('Funcionario deletado com sucesso!', 'success');
            currentPageFuncionario = 0;
            nextDataPageFuncionarios();
            divDelete.style.display = 'none';
            overlay.style.display = 'none';
        })
        .catch(error => {
            const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
            toastAlert(errorMessage, 'error');
        });
    })

    document.querySelector('.tableFunc').addEventListener('click', (event) => {

        const {
            currentNome, currentCpf,
            currentDataNasc, currentLogin, currentCargo,
            currentEmail
        } = processEventFuncionarios(event);

        if (event.target.classList.contains('imgEdit')) {
            currentId = event.target.getAttribute('data-id');
            divEdit.style.display = 'block';
            overlay.style.display = 'block';
            document.getElementById('nomeEdit').value = currentNome;
            document.getElementById('cpfEdit').value = currentCpf;
            document.getElementById('dataNascEdit').value = currentDataNasc;
            document.getElementById('loginEdit').value = currentLogin;
            document.getElementById('cargoEdit').value = currentCargo;
            document.getElementById('emailEdit').value = currentEmail;
        } else if (event.target.classList.contains('imgDelete')) {
            currentId = event.target.getAttribute('data-id');
            divDelete.style.display = 'block';
            overlay.style.display = 'block';
            document.querySelector('.deleteMsg').innerHTML = `Deseja deletar o funcionário \n ${currentNome}?`;
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

    overlay.addEventListener('click', () => {
        overlay.style.display = 'none';
        divAdd.style.display = 'none';
        divEdit.style.display = 'none';
        divDelete.style.display = 'none';
    });

    document.getElementById('confirmAdd').addEventListener('click', () => {
        const nome = document.getElementById('nomeAdd').value;
        const cpf = document.getElementById('cpfAdd').value;
        const dataNascimento = document.getElementById('dataNascAdd').value;
        const login = document.getElementById('loginAdd').value;
        const cargo = document.getElementById('cargoAdd').value;
        const email = document.getElementById('emailAdd').value;
        const password = document.getElementById('senhaAdd').value;

        if (nome === '' || cpf === '' || dataNascimento === '' || login === '' || cargo === '' || email === '' || password === '') {
            toastAlert('Preencha todos os campos!', 'error');
            return
        }

        const data = {
            nome,
            cpf,
            email,
            dataNascimento,
            usuario: {
                login,
                password,
                isAdmin: false
            },
            cargo,
        };

        fetch(`${URL}/auth/Funcionario/Add`, {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        })
            .then(async response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        try {
                            const errorData = JSON.parse(text);
                            throw new Error(errorData.message || 'Erro ao adicionar Funcionario');
                        } catch (e) {
                            throw new Error(text || 'Erro ao adicionar Funcionario');
                        }
                    });
                }
                return response.json();
            })
            .then(data => {
                toastAlert("Funcionario cadastrado com sucesso!", "success");
                divAdd.style.display = 'none';
                overlay.style.display = 'none';
                currentPageFuncionario = 0;
                freeInputs();
                nextDataPageFuncionarios();
            })
            .catch(error => {
                toastAlert("Erro ao cadastrar funcionário!", "error");
            });
    });

    document.getElementById('confirmEdit').addEventListener('click', () => {
        const nome = document.getElementById('nomeEdit').value;
        const cpf = document.getElementById('cpfEdit').value;
        const dataNascimento = document.getElementById('dataNascEdit').value;
        const login = document.getElementById('loginEdit').value;
        const cargo = document.getElementById('cargoEdit').value;
        const email = document.getElementById('emailEdit').value;

        const data = {
            nome,
            cpf,
            dataNascimento,
            login,
            cargo,
            email,
        };

        let id = parseInt(currentId);
        fetch(`${URL}/auth/Funcionario/Edit/${id}`, {
            method: 'PUT',
            headers,
            body: JSON.stringify(data)
        })
            .then(async response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        try {
                            const errorData = JSON.parse(text);
                            throw new Error(errorData.message || 'Erro ao editar funcionario');
                        } catch (e) {
                            throw new Error(text || 'Erro ao editar funcionario');
                        }
                    });
                }
                return response.json();
            })
            .then(data => {
                toastAlert('Funcionário editado com sucesso!', 'success');
                divEdit.style.display = 'none';
                overlay.style.display = 'none';
                currentPageFuncionario = 0;
                nextDataPageFuncionarios();
            })
            .catch(error => {
                toastAlert('Erro ao editar funcionário!', 'error');
            });
    });
}

// function disableBtns(data) {
//     priorBtn.disabled = (currentPageFuncionario <= 1);
//     nextBtn.disabled = (currentPageFuncionario >= data);
// }

function addTableLinesFuncionarios(data) {
    const table = document.querySelector('.tableFunc>tbody');
    const prevBtn = document.getElementById('priorBtnFunc');
    const nextBtn = document.getElementById('nextBtnFunc');

    if(data.length === 0) {
        toastAlert('Nenhum funcionario encontrado', 'error');
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
    if (currentPageFuncionario > 0 ) {
        prevBtn.removeAttribute('disabled');
        prevBtn.classList.remove('disabled');
    } else {
        prevBtn.setAttribute('disabled', 'true');
        prevBtn.classList.add('disabled');
    };

    let count = 0;

    data.forEach(funcionario => {
        const newLine = document.createElement('tr');

        const classe = count % 2 === 0 ? 'azul' : '';
        count++;

        newLine.innerHTML = `
            <td class="thStyle imgFunc ${classe}">
                <img src="/icons/Cadastro-Funcionario/Businessman.png" alt="Funcionario" class="imgFunc">
            </td>
            <td class="thStyle ${classe}">${funcionario.id}</td>
            <td class="thStyle ${classe}">${funcionario.nome}</td>
            <td class="thStyle ${classe}">${funcionario.cargo.descricao}</td>
            <td class="thStyle ${classe}">
                <img src="/icons/Cadastro-Funcionario/edit.png" 
                    data-id="${funcionario.id}"
                    data-nome="${funcionario.nome}"
                    data-cpf="${funcionario.cpf}"
                    data-nasc="${funcionario.dataNascimento}"
                    data-cargo="${funcionario.cargo.descricao}"
                    data-email="${funcionario.email}"
                    data-login="${funcionario.usuario.login}"
                alt="Editar" class="imgEdit imgStyle">
                <img src="/icons/Cadastro-Funcionario/delete.png"
                    data-id="${funcionario.id}"
                    data-nome="${funcionario.nome}"
                alt="Deletar" class="imgDelete imgStyle">                
            </td>
        `;

        table.appendChild(newLine);
    });
}

function processEventFuncionarios(event) {
    const currentId = event.target.getAttribute('data-id');
    const currentNome = event.target.getAttribute('data-nome');
    const currentCpf = event.target.getAttribute('data-cpf');
    const currentDataNasc = event.target.getAttribute('data-nasc');
    const currentLogin = event.target.getAttribute('data-login');
    const currentCargo = event.target.getAttribute('data-cargo');
    const currentEmail = event.target.getAttribute('data-email');

    return {
        currentId,
        currentNome,
        currentCpf,
        currentDataNasc,
        currentLogin,
        currentCargo,
        currentEmail,
    };
}

function nextDataPageFuncionarios() {
    const search = document.getElementById('search').value;
    const queryParams = new URLSearchParams();
    if (search) queryParams.append('nome', search);
    queryParams.append('page', currentPageFuncionario);

    fetch(`${URL}/auth/Funcionario/search?${queryParams.toString()}`, {
        method: 'GET',
        headers,
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar os dados!');
            }
            return response.json();
        })
        .then(data => {
            const table = document.querySelector('.tableFunc>tbody');
            const trs = Array.from(table.children);
                trs.forEach(tag => {
                    tag.parentNode.removeChild(tag);
                }
            );
            addTableLinesFuncionarios(data);
        })
        .catch(error => console.error('Erro ao buscar os dados:', error));
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