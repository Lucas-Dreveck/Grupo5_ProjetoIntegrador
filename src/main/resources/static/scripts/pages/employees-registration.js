let currentPageEmployee = 0;
function onOpenEmployee() {
    const priorBtn = document.getElementById('priorBtnEmployee');
    const nextBtn = document.getElementById('nextBtnEmployee');
    const search = document.getElementById('search');
    const searchBtn = document.querySelector('.imgSearch');
    const overlay = document.querySelector('.overlay');
    const divAdd = document.querySelector('.divAddEmployee');
    const divEdit = document.querySelector('.divEditEmployee');
    const divDelete = document.querySelector('.divDeleteEmployee');
    let currentId;

    nextDataPageEmployees()

    nextBtn.addEventListener('click', () => {
        currentPageEmployee++;
        nextDataPageEmployees();
    });

    priorBtn.addEventListener('click', () => {
       if (currentPageEmployee > 0) {
           currentPageEmployee--;
           nextDataPageEmployees();
        }
    });

    searchBtn.addEventListener('click', () => {
        currentPageEmployee = 0;
        nextDataPageEmployees();
    });

    search.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            currentPageEmployee = 0;
            nextDataPageEmployees();
        }
    });

    document.querySelectorAll('.inputBirth').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inputCpf').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.getElementById('confirmDelete').addEventListener('click', () => {
        const id = parseInt(currentId);
        fetch(`${ApiURL}/auth/Employee/${id}`, {
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
            currentPageEmployee = 0;
            nextDataPageEmployees();
            divDelete.style.display = 'none';
            overlay.style.display = 'none';
        })
        .catch(error => {
            const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
            toastAlert(errorMessage, 'error');
        });
    })

    document.querySelector('.tableEmployee').addEventListener('click', (event) => {

        const {
            currentName, currentCpf,
            currentBirthDate, currentLogin, currentRole,
            currentEmail
        } = processEventEmployees(event);

        if (event.target.classList.contains('imgEdit')) {
            currentId = event.target.getAttribute('data-id');
            divEdit.style.display = 'block';
            overlay.style.display = 'block';
            document.getElementById('nameEdit').value = currentName;
            document.getElementById('cpfEdit').value = currentCpf;
            document.getElementById('birthDateEdit').value = currentBirthDate;
            document.getElementById('loginEdit').value = currentLogin;
            document.getElementById('roleEdit').value = currentRole;
            document.getElementById('emailEdit').value = currentEmail;
        } else if (event.target.classList.contains('imgDelete')) {
            currentId = event.target.getAttribute('data-id');
            divDelete.style.display = 'block';
            overlay.style.display = 'block';
            document.querySelector('.deleteMsg').innerHTML = `Deseja deletar o funcionário \n ${currentName}?`;
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

    document.getElementById('confirmAddEmployee').addEventListener('click', () => {
        const name = document.getElementById('nameAdd').value;
        const cpf = document.getElementById('cpfAdd').value;
        const birthDate = document.getElementById('birthDateAdd').value;
        const login = document.getElementById('loginAdd').value;
        const role = document.getElementById('roleAdd').value;
        const email = document.getElementById('emailAdd').value;
        const password = document.getElementById('passwordAdd').value;

        if (name === '' || cpf === '' || birthDate === '' || login === '' || role === '' || email === '' || password === '') {
            toastAlert('Preencha todos os campos!', 'error');
            return
        }

        const data = {
            name,
            cpf,
            email,
            birthDate,
            user: {
                login,
                password,
                isAdmin: false
            },
            role,
        };

        fetch(`${ApiURL}/auth/Employee`, {
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
                currentPageEmployee = 0;
                freeInputs();
                nextDataPageEmployees();
            })
            .catch(error => {
                toastAlert("Erro ao cadastrar funcionário!", "error");
            });
    });

    document.getElementById('confirmEditEmployee').addEventListener('click', () => {
        const name = document.getElementById('nameEdit').value;
        const cpf = document.getElementById('cpfEdit').value;
        const birthDate = document.getElementById('birthDateEdit').value;
        const login = document.getElementById('loginEdit').value;
        const role = document.getElementById('roleEdit').value;
        const email = document.getElementById('emailEdit').value;

        const data = {
            name,
            cpf,
            birthDate,
            login,
            role,
            email,
        };

        let id = parseInt(currentId);
        fetch(`${ApiURL}/auth/Employee/${id}`, {
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
                currentPageEmployee = 0;
                nextDataPageEmployees();
            })
            .catch(error => {
                toastAlert('Erro ao editar funcionário!', 'error');
            });
    });
}

// function disableBtns(data) {
//     priorBtn.disabled = (currentPageEmployee <= 1);
//     nextBtn.disabled = (currentPageEmployee >= data);
// }

function addTableLinesEmployees(data) {
    const table = document.querySelector('.tableEmployee>tbody');
    const prevBtn = document.getElementById('priorBtnEmployee');
    const nextBtn = document.getElementById('nextBtnEmployee');

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
    if (currentPageEmployee > 0 ) {
        prevBtn.removeAttribute('disabled');
        prevBtn.classList.remove('disabled');
    } else {
        prevBtn.setAttribute('disabled', 'true');
        prevBtn.classList.add('disabled');
    };

    let count = 0;

    data.forEach(employee => {
        const newLine = document.createElement('tr');

        const colorClass = count % 2 === 0 ? 'blue' : '';
        count++;

        newLine.innerHTML = `
            <td class="thStyle imgEmployee ${colorClass}">
                <img src="/icons/Employees-Registration/Businessman.png" alt="Funcionario" class="imgEmployee">
            </td>
            <td class="thStyle ${colorClass}">${employee.id}</td>
            <td class="thStyle ${colorClass}">${employee.name}</td>
            <td class="thStyle ${colorClass}">${employee.role.description}</td>
            <td class="thStyle ${colorClass}">
                <img src="/icons/Employees-Registration/edit.png" 
                    data-id="${employee.id}"
                    data-name="${employee.name}"
                    data-cpf="${employee.cpf}"
                    data-nasc="${employee.birthDate}"
                    data-role="${employee.role.description}"
                    data-email="${employee.email}"
                    data-login="${employee.user.login}"
                alt="Editar" class="imgEdit imgStyle">
                <img src="/icons/Employees-Registration/delete.png"
                    data-id="${employee.id}"
                    data-name="${employee.name}"
                alt="Deletar" class="imgDelete imgStyle">                
            </td>
        `;

        table.appendChild(newLine);
    });
}

function processEventEmployees(event) {
    const currentId = event.target.getAttribute('data-id');
    const currentName = event.target.getAttribute('data-name');
    const currentCpf = event.target.getAttribute('data-cpf');
    const currentBirthDate = event.target.getAttribute('data-nasc');
    const currentLogin = event.target.getAttribute('data-login');
    const currentRole = event.target.getAttribute('data-role');
    const currentEmail = event.target.getAttribute('data-email');

    return {
        currentId,
        currentName,
        currentCpf,
        currentBirthDate,
        currentLogin,
        currentRole,
        currentEmail,
    };
}

function nextDataPageEmployees() {
    const search = document.getElementById('search').value;
    const queryParams = new URLSearchParams();
    if (search) queryParams.append('name', search);
    queryParams.append('page', currentPageEmployee);

    fetch(`${ApiURL}/auth/Employee/search?${queryParams.toString()}`, {
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
            const table = document.querySelector('.tableEmployee>tbody');
            const trs = Array.from(table.children);
                trs.forEach(tag => {
                    tag.parentNode.removeChild(tag);
                }
            );
            addTableLinesEmployees(data);
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