let page = 0;
const createTop3 = (companys, content) => {
    const setMedal = (index) => {
        if (index === 3) {
            return '/icons/Medals/bronze-medal.svg';
        } else if (index === 1) {
            return '/icons/Medals/silver-medal.svg';
        } else {
            return '/icons/Medals/gold-medal.svg';
        }
    }

    const Top3Div = document.createElement('div');
    Top3Div.classList.add('top3');
    companys.forEach((company, index) => {
        let position;
        if (index === 0) {
            position = '2';
        } else if (index === 1) {
            position = '1';
        } else {
            position = '3';
        }
        const medalIcon = setMedal(index + 1);
        const place = document.createElement('div');
        place.classList.add(`place-${position}`);
        place.innerHTML = `
            <h2 class="company-name">${company.empresaNome}</h2>
            <h1 class="place-${position}-title">${position}º lugar - ${company.pontuacaoFinal} pontos</h1>
            <p class="branch">${company.ramo}</p>
            <img src="${medalIcon}" alt="Medal" class="medal-icon">
        `;

        place.addEventListener('click', () => 
            exportPDF(company.id, company.empresaNome)
        );

        Top3Div.appendChild(place);
    });
    content.appendChild(Top3Div);
}

const createRanking = (companys, content) => {
    let rankingTable = document.querySelector('.ranking-table');
    if (!rankingTable) {
        rankingTable = document.createElement('table');
        rankingTable.classList.add('ranking-table');
        content.appendChild(rankingTable);
    } else {
        rankingTable.innerHTML = ''; // Limpa o conteúdo interno da tabela
    }

    const tbody = document.createElement('tbody');
    companys.forEach((company) => {
        if (company.ranking === 1 || company.ranking === 2 || company.ranking === 3) return;
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="ranking">${company.ranking}º - ${company.empresaNome}</td>
            <td class="branch">${company.ramo}</td>
            <td class="size">${company.pontuacaoFinal} pontos</td>
            <td class="pdf-img">
                <img src="/icons/file-link.svg" alt="BaixarPdf">
            </td>
        `;
        tr.classList.add(tbody.children.length % 2 === 0 ? 'even-row' : 'odd-row');

        const pdfBtn = tr.querySelector('.pdf-img')
        
        pdfBtn.addEventListener('click', () =>
            exportPDF(company.id, company.empresaNome)
        );

        tbody.appendChild(tr);
    });

    rankingTable.appendChild(tbody);
}

const addOptions = async (content) => {
    const ramoDropdown = document.createElement('select');
    ramoDropdown.classList.add('ramo-dropdown');
    ramoDropdown.innerHTML = '<option value="">Ramo da empresa</option>';

    const porteDropdown = document.createElement('select');
    porteDropdown.classList.add('porte-dropdown');
    porteDropdown.innerHTML = '<option value="">Porte da empresa</option>';

    const ramoOptions = new Set();
    const porteOptions = new Set(['Pequeno', 'Médio', 'Grande']);

    await fetch(`${URL}/ranking/ramos/list`, options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar ramos');
            }
            return response.json();
        })
        .then(response => {
            response.forEach(ramo => ramoOptions.add(ramo));
        });

    ramoOptions.forEach(option => {
        const ramo = document.createElement('option');
        ramo.value = option;
        ramo.innerHTML = option;
        ramoDropdown.appendChild(ramo);
    });

    porteOptions.forEach(option => {
        const porte = document.createElement('option');
        porte.value = option;
        porte.innerHTML = option;
        porteDropdown.appendChild(porte);
    });

    ramoDropdown.addEventListener('change', () => {
        page = 0;
        updateRanking()
    });
    porteDropdown.addEventListener('change', () => {
        page = 0;
        updateRanking()
    });

    content.appendChild(ramoDropdown);
    content.appendChild(porteDropdown);
}

const updateRanking = () => {
    const ramo = document.querySelector('.ramo-dropdown').value;
    const porte = document.querySelector('.porte-dropdown').value;
    const search = document.querySelector('.search-bar').value;
    const queryParams = new URLSearchParams();
    if (ramo) queryParams.append('ramo', ramo);
    if (porte) queryParams.append('porte', porte);
    if (search) queryParams.append('nomeFantasia', search);
    queryParams.append('page', page);
    const fullUrl = `${URL}/ranking/pontuacao?${queryParams.toString()}`;

    fetch(fullUrl, options)
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar ranking');
            return response.json();
        })
        .then(response => {
            createRanking(response, document.querySelector('.ranking-table-container'));
            const btnNext = document.querySelector('.btn-next');
            const btnPrev = document.querySelector('.btn-prev');

            if (response[0].finishList) {
                btnNext.classList.add('disabled');
                btnNext.setAttribute('disabled', 'true');
            } else {
                btnNext.classList.remove('disabled');
                btnNext.removeAttribute('disabled');
            }

            if (page === 0) {
                btnPrev.classList.add('disabled');
                btnPrev.setAttribute('disabled', 'true');
            } else {
                btnPrev.classList.remove('disabled');
                btnPrev.removeAttribute('disabled');
            }
        });
}

const createSearch = (content) => {
    const search = document.createElement('div');
    search.classList.add('search');
    const searchBar = document.createElement('input');
    searchBar.classList.add('search-bar');
    searchBar.placeholder = 'Nome da empresa';
    const searchButton = document.createElement('button');
    searchButton.classList.add('search-button');
    searchButton.innerHTML = `
        <img src="/icons/Buttons/search.svg" alt="Search" class="search-icon">
    `;

    searchBar.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            page = 0;
            updateRanking();
        }
    });
    searchButton.addEventListener('click', () => {
        page = 0;
        updateRanking()
    });

    search.appendChild(searchBar);
    search.appendChild(searchButton);

    content.appendChild(search);
}

const onOpenRanking = async () => {
    const content = document.querySelector('.content-container');

    const sideContent = document.createElement('div');
    sideContent.classList.add('side-content');
    const optionsDiv = document.createElement('div');
    optionsDiv.classList.add('options');
    const rankingContent = document.createElement('div');
    rankingContent.classList.add('ranking-content');
    const rankingTableContainer = document.createElement('div');
    rankingTableContainer.classList.add('ranking-content');

    const queryParams = new URLSearchParams();
    await fetch(`${URL}/ranking/pontuacao?${queryParams.toString()}`, options)
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar ranking');
            return response.json();
        })
        .then(async response => {
            createTop3([response[1], response[0], response[2]], sideContent);
            createRanking(response, rankingTableContainer);
            await addOptions(optionsDiv);
            createSearch(optionsDiv);
            sideContent.appendChild(optionsDiv);
        });

    const btnPrev = document.createElement('button');
    btnPrev.classList.add('btn-prev');
    btnPrev.classList.add('disabled');
    btnPrev.setAttribute('disabled', 'true');
    btnPrev.innerHTML = `
        <img src="/icons/Buttons/arrow-left.svg" alt="Previous" class="arrow-icon">
    `;
    btnPrev.addEventListener('click', () => {
        if (page > 0) {
            page--;
            updateRanking();
        }
    });
    const btnNext = document.createElement('button');
    btnNext.classList.add('btn-next');
    btnNext.innerHTML = `
        <img src="/icons/Buttons/arrow-right.svg" alt="Next" class="arrow-icon">
    `;
    btnNext.addEventListener('click', () => {
        page++;
        updateRanking();
    });

    const buttons = document.createElement('div');
    buttons.classList.add('btns-page');

    buttons.appendChild(btnPrev);
    buttons.appendChild(btnNext);

    rankingContent.appendChild(rankingTableContainer);
    rankingContent.appendChild(buttons);

    content.appendChild(sideContent);
    content.appendChild(rankingContent);
}