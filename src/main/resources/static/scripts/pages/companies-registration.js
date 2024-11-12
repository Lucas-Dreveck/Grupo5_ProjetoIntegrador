let currentPage = 0;
function onOpenCompany() {
    const priorBtn = document.getElementById('priorBtnCompany');
    const nextBtn = document.getElementById('nextBtnCompany');
    const search = document.getElementById('search');
    const searchBtn = document.querySelector('.imgSearch');
    const overlay = document.querySelector('.overlay');
    const divAdd = document.querySelector('.divAddCompany');
    const divEdit = document.querySelector('.divEditCompany');
    const divDelete = document.querySelector('.divDeleteCompany');
    let currentid;

    nextDataPageCompanies();

    nextBtn.addEventListener('click', () => {
        currentPage++;
        nextDataPageCompanies();
    });

    priorBtn.addEventListener('click', () => {
       if (currentPage > 0) {
           currentPage--;
           nextDataPageCompanies();
        }
    });

    searchBtn.addEventListener('click', () => {
        currentPage = 0;
        nextDataPageCompanies();
    });

    search.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            currentPage = 0;
            nextDataPageCompanies();
        }
    });

    document.querySelectorAll('.inpCnpjEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpCepEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpCompanyPhone').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpApplicantsPhoneEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpNumberEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelectorAll('.inpInscriptionEmp').forEach(input => {
        input.addEventListener('keydown', justNumbers);
    });

    document.querySelector('.tableCompany').addEventListener('click', (event) => {

        const { currentLogo, currentApplicantsName, currentApplicantsPhone, currentTradeName,
            currentCompanyName, currentCnpj, currentInscription,
            currentEmail, currentCompanyPhone, currentCompanySize, currentSegment,
            currentCep, currentUf, currentNeighborhood, currentCity, currentNumber,
            currentStreet } = processEventCompanies(event);

        if(event.target.classList.contains('imgEdit')) {
            currentid = event.target.getAttribute('data-id');
            document.getElementById('logoEdit').value = currentLogo;
            document.getElementById('tradeNameEdit').value = currentTradeName;
            document.getElementById('applicantsNameEdit').value = currentApplicantsName;
            document.getElementById('applicantsPhoneEdit').value = currentApplicantsPhone;
            document.getElementById('companyNameEdit').value = currentCompanyName;
            document.getElementById('cnpjEdit').value = currentCnpj;
            document.getElementById('inscriptionEdit').value = currentInscription;
            document.getElementById('emailEdit').value = currentEmail;
            document.getElementById('companyPhoneEdit').value = currentCompanyPhone;
            document.getElementById('segmentEdit').value = currentSegment;
            document.getElementById('companySizeEdit').value = currentCompanySize;
            document.getElementById('cepEdit').value = currentCep;
            document.getElementById('ufEdit').value = currentUf;
            document.getElementById('neighborhoodEdit').value = currentNeighborhood;
            document.getElementById('cityEdit').value = currentCity;
            document.getElementById('numberEdit').value = currentNumber;
            document.getElementById('streetEdit').value = currentStreet;

            divEdit.style.display = 'block';
            overlay.style.display = 'block';

        } else if(event.target.classList.contains('imgDelete')) {
           currentid = event.target.getAttribute('data-id');
            divDelete.style.display = 'block';
            overlay.style.display = 'block';
            document.querySelector('.deleteMsg').innerHTML = `Deseja deletar a empresa ${currentTradeName}?`;
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

    document.getElementById('confirmAddCompany').addEventListener('click', () => {
        const imageUrl = document.getElementById('logo').value;
        const tradeName = document.getElementById('tradeName').value;
        const applicantsName = document.getElementById('applicantsName').value;
        const applicantsPhone = document.getElementById('applicantsPhone').value;
        const companyName = document.getElementById('companyName').value;
        const cnpj = document.getElementById('CNPJ').value;
        const socialInscription = document.getElementById('inscription').value;
        const email = document.getElementById('email').value;
        const companyPhone = document.getElementById('companyPhone').value;
        const segment = document.getElementById('segment').value;
        const companySize = document.getElementById('companySize').value;
        const number = document.getElementById('number').value;
        const cep = document.getElementById('cep').value;
        const patio = document.getElementById('street').value;
        const city = document.getElementById('city').value;
        const neighborhood = document.getElementById('neighborhood').value;
        const uf = document.getElementById('uf').value;

        if(!tradeName || !applicantsName || !applicantsPhone || !companyName || !cnpj || !socialInscription || !email || !companyPhone || !segment || !companySize) {
            toastAlert('Preencha todos os campos!', 'error');
            return;
        }

        const isNumber = parseInt(number)
        if(!isNumber || isNumber < 0 || isNumber > 9999999999) {
            toastAlert('Insira um número válido', 'error');
            return;
        }

        const data = {
            imageUrl,
            tradeName,
            applicantsName,
            applicantsPhone,
            companyName,
            cnpj,
            socialInscription,
            email,
            companyPhone,
            segment,
            address: {
                cep,
                number,
                patio,
                city,
                neighborhood,
                uf
            },
            companySize,
        };

        fetch(`${ApiURL}/auth/Company`, {
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
                nextDataPageCompanies();
                currentPage = 0;
                document.getElementById('logo').value = '';
                document.getElementById('tradeName').value = '';
                document.getElementById('applicantsName').value = '';
                document.getElementById('applicantsPhone').value = '';
                document.getElementById('companyName').value = '';
                document.getElementById('CNPJ').value = '';
                document.getElementById('inscription').value = '';
                document.getElementById('email').value = '';
                document.getElementById('companyPhone').value = '';
                document.getElementById('segment').value = '';
                document.getElementById('companySize').value = '';
                document.getElementById('number').value = '';
                document.getElementById('cep').value = '';
                document.getElementById('street').value = '';
                document.getElementById('city').value = '';
                document.getElementById('neighborhood').value = '';
                document.getElementById('uf').value = '';
            })
            .catch(error => {
                const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
                toastAlert(errorMessage, 'error');
            });
    });

    document.getElementById('confirmEditCompany').addEventListener('click', () => {
        const imageUrl = document.getElementById('logoEdit').value;
        const tradeName = document.getElementById('tradeNameEdit').value;
        const applicantsName = document.getElementById('applicantsNameEdit').value;
        const applicantsPhone = document.getElementById('applicantsPhoneEdit').value;
        const companyName = document.getElementById('companyNameEdit').value;
        const cnpj = document.getElementById('cnpjEdit').value;
        const socialInscription = document.getElementById('inscriptionEdit').value;
        const email = document.getElementById('emailEdit').value;
        const companyPhone = document.getElementById('companyPhoneEdit').value;
        const segment = document.getElementById('segmentEdit').value;
        const companySize = document.getElementById('companySizeEdit').value;
        const number = document.getElementById('numberEdit').value;
        const cep = document.getElementById('cepEdit').value;
        const patio = document.getElementById('streetEdit').value;
        const city = document.getElementById('cityEdit').value;
        const neighborhood = document.getElementById('neighborhoodEdit').value;
        const uf = document.getElementById('ufEdit').value;

        if(!tradeName || !applicantsName || !applicantsPhone || !companyName || !cnpj || !socialInscription || !email || !companyPhone || !segment || !companySize) {
            toastAlert('Preencha todos os campos!', 'error');
            return;
        }

        const isNumber = parseInt(number)
        if(!isNumber || isNumber < 0 || isNumber > 9999999999) {
            toastAlert('Insira um número válido', 'error');
            return;
        }

        const data = {
            imageUrl,
            tradeName,
            applicantsName,
            applicantsPhone,
            companyName,
            cnpj,
            socialInscription,
            email,
            companyPhone,
            segment,
            address: {
                cep,
                number,
                patio,
                city,
                neighborhood,
                uf
            },
            companySize,
        };

        let id = parseInt(currentid);

        fetch(`${ApiURL}/auth/Company/${id}`, {
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
                nextDataPageCompanies();
                divEdit.style.display = 'none';
                overlay.style.display = 'none';
                document.getElementById('logoEdit').value = '';
                document.getElementById('tradeNameEdit').value = '';
                document.getElementById('applicantsNameEdit').value = '';
                document.getElementById('applicantsPhoneEdit').value = '';
                document.getElementById('companyNameEdit').value = '';
                document.getElementById('cnpjEdit').value = '';
                document.getElementById('inscriptionEdit').value = '';
                document.getElementById('emailEdit').value = '';
                document.getElementById('companyPhoneEdit').value = '';
                document.getElementById('segmentEdit').value = '';
                document.getElementById('companySizeEdit').value = '';
                document.getElementById('cepEdit').value = '';
                document.getElementById('ufEdit').value = '';
                document.getElementById('neighborhoodEdit').value = '';
                document.getElementById('cityEdit').value = '';
                document.getElementById('numberEdit').value = '';
                document.getElementById('streetEdit').value = '';
            })
            .catch(error => {
                const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
                toastAlert(errorMessage, 'error');
            });
    });

    document.getElementById('confirmDelete').addEventListener('click' , () => {
        const id = parseInt(currentid);
        fetch(`${ApiURL}/auth/Company/${id}`, {
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
            nextDataPageCompanies();
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

function addTableLinesCompanies(data) {
    const table = document.querySelector('.tableCompany>tbody');
    const prevBtn = document.getElementById('priorBtnCompany');
    const nextBtn = document.getElementById('nextBtnCompany');
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

    data.forEach((company, index) => {
        const newLine = document.createElement('tr');

        company.tradeName=="Apple" && console.log(company);
        const colorClass = count % 2 === 0 ? 'blue' : '';
        count++;

        newLine.innerHTML = `
            <td class="thStyle thImg ${colorClass}">
                <img src="/icons//Companies-Registration/Company.png" alt="Empresa" class="thImg">
            </td>
            <td class="thStyle ${colorClass}">${company.id}</td>
            <td class="thStyle ${colorClass}">${company.tradeName}</td>
            <td class="thStyle ${colorClass}">${company.segment}</td>
        <td class="thStyle ${colorClass}">${company.companySize}</td>
            <td class="thStyle ${colorClass}">
                <img src="/icons//Companies-Registration/edit.png" 
                    data-id="${company.id}"
                    data-logo="${company.imageUrl}"
                    data-tradeName="${company.tradeName}"
                    data-applicantsName="${company.applicantsName}"
                    data-applicantsPhone="${company.applicantsPhone}"
                    data-companyName="${company.companyName}"
                    data-cnpj="${company.cnpj}"
                    data-inscription="${company.socialInscription}"
                    data-email="${company.email}"
                    data-companyPhone="${company.companyPhone}"
                    data-segment="${company.segment}"
                    data-companySize="${company.companySize}"
                    data-cep="${company.address.cep}"
                    data-number="${company.address.number}"
                    data-patio="${company.address.patio}"
                    data-city="${company.address.city}"
                    data-neighborhood="${company.address.neighborhood}"
                    data-uf="${company.address.uf}"
                alt="Editar" class="imgEdit imgStyle">
                <img src="/icons//Companies-Registration/delete.png"
                    data-id="${company.id}"
                    data-tradeName="${company.tradeName}"
                alt="Deletar" class="imgDelete imgStyle">                
            </td>
        `;

        table.appendChild(newLine);
    });
}

function processEventCompanies(event) {
    const currentid = event.target.getAttribute('data-id');
    const currentLogo = event.target.getAttribute('data-logo');
    const currentTradeName = event.target.getAttribute('data-tradeName');
    const currentApplicantsName = event.target.getAttribute('data-applicantsName');
    const currentApplicantsPhone = event.target.getAttribute('data-applicantsPhone');
    const currentCompanyName = event.target.getAttribute('data-companyName');
    const currentCnpj = event.target.getAttribute('data-cnpj');
    const currentInscription = event.target.getAttribute('data-inscription');
    const currentEmail = event.target.getAttribute('data-email');
    const currentCompanyPhone = event.target.getAttribute('data-companyPhone');
    const currentSegment = event.target.getAttribute('data-segment');
    const currentCompanySize = event.target.getAttribute('data-companySize');
    const currentCep = event.target.getAttribute('data-cep');
    const currentNumber = event.target.getAttribute('data-number');
    const currentStreet = event.target.getAttribute('data-patio');
    const currentCity = event.target.getAttribute('data-city');
    const currentNeighborhood = event.target.getAttribute('data-neighborhood');
    const currentUf = event.target.getAttribute('data-uf');

    return {
        currentid,
        currentLogo,
        currentTradeName,
        currentApplicantsName,
        currentApplicantsPhone,
        currentCompanyName,
        currentCnpj,
        currentInscription,
        currentEmail,
        currentCompanyPhone,
        currentSegment,
        currentCompanySize,
        currentCep,
        currentNumber,
        currentStreet,
        currentCity,
        currentNeighborhood,
        currentUf
    };
}

function nextDataPageCompanies () {
    const search = document.getElementById('search').value;
    const queryParams = new URLSearchParams();
    if (search) queryParams.append('name', search);
    queryParams.append('page', currentPage);

    fetch(`${ApiURL}/auth/Company/search?${queryParams.toString()}`, {
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
        const table = document.querySelector('.tableCompany>tbody');
        const trs = Array.from(table.children);
            trs.forEach(tag => {
                tag.parentNode.removeChild(tag);
            }
        );
        addTableLinesCompanies(data);
    })
    .catch(error => {
        const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
        toastAlert(errorMessage, 'error');
    });
}

function justNumbers(event) {
    const key = event.key;
    const keyCode = event.keyCode || event.which;
    const ctrlPressed = event.ctrlKey || event.metaKey;

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