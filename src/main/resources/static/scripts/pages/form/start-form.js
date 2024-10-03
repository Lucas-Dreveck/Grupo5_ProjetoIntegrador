let isDropdownOpened = false;

let settedCompany = null;

const fetchCompanies = (input, dropdown) => {
    const filter = input.value.toLowerCase();
    const queryParams = new URLSearchParams();
    queryParams.append('name', filter ? filter : 'a')
    const fullUrl = `${ApiURL}/auth/Company/form/search?${queryParams.toString()}`;
    fetch(fullUrl, options)
        .then (response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar empresas');
            }
            return response.json();
        })
        .then (response => {
            dropdown.innerHTML = '';
            response.forEach(company => {
                const li = document.createElement('li');
                li.textContent = company.tradeName;
                li.addEventListener('click', () => {
                    input.value = company.tradeName;
                    settedCompany = company
                    dropdown.innerHTML = '';
                });
                dropdown.appendChild(li);
            });
        });
}

function clearSelectedCompanyIfNotMatch(input) {
    const inputValue = input.value.toLowerCase();
    if (settedCompany && settedCompany.tradeName.toLowerCase() !== inputValue) {
        settedCompany = null;
    }
}

async function verifyActiveForm() {
    return fetch(`${ApiURL}/auth/haveActiveForm/${settedCompany.id}`, options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao recuperar dados');
            }
            return response.json();
        })
        .then(data => {
            return data;
        });
}

const onOpenStartForm = () => {
    const input = document.querySelector('.search');
    const dropdown = document.querySelector('.dropdown-content');
    const content = document.querySelector('.content-container');

    input.addEventListener('input', () => {
        fetchCompanies(input, dropdown)
        isDropdownOpened = true;
        clearSelectedCompanyIfNotMatch(input);
    });

    input.addEventListener('focus', () => {
        if (!isDropdownOpened) {
            fetchCompanies(input, dropdown);
            isDropdownOpened = true;
            clearSelectedCompanyIfNotMatch(input);
        }
    });

    input.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            const selected = dropdown.querySelector('li');
            if (selected) {
                input.value = selected.textContent;
                dropdown.innerHTML = '';
                isDropdownOpened = false;
                clearSelectedCompanyIfNotMatch(input);
            }
        }
    });

    content.addEventListener('click', function(event) {
        if (!input.contains(event.target) && !dropdown.contains(event.target)) {
            dropdown.innerHTML = '';
            isDropdownOpened = false;
            clearSelectedCompanyIfNotMatch(input);
        }
    });

    const btnNext = document.querySelector('.btn-next');
    btnNext.addEventListener('click', async () => {
        if (settedCompany) {
            const hasActiveForm = await verifyActiveForm();
            if (!hasActiveForm) {
                getMainFrameContent('form', {
                    company: settedCompany,
                    isNew: true
                });
                settedCompany = null;
            } else {
                confirmationModal({
                    title: 'Atenção',
                    message: 'A company selecionada já possui uma avaliação em andamento. Deseja iniciar uma nova avaliação ou continuar a avaliação existente?',
                    confirmText: 'Iniciar nova avaliação',
                    cancelText: 'Continuar avaliação',
                    haveCancel: false,
                    onConfirm: () => {
                        getMainFrameContent('form', {
                            company: settedCompany,
                            isNew: true
                        });
                        settedCompany = null;
                    },
                    onCancel: () => {
                        getMainFrameContent('form', {
                            company: settedCompany,
                            isNew: false
                        });
                        settedCompany = null;
                    }
                });
            }
        } else {
            toastAlert('Por favor, selecione uma empresa antes de avançar', 'error');
        }
    });
}
