let page = 0;
const createTop3 = (companies, content) => {
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
    companies.forEach((company, index) => {
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
            <h2 class="company-name">${company.companyName}</h2>
            <h1 class="place-${position}-title">${position}º lugar - ${company.finalScore} pontos</h1>
            <p class="branch">${company.segment}</p>
            <img src="${medalIcon}" alt="Medal" class="medal-icon">
        `;

        place.addEventListener('click', () => 
            exportPDF(company.id, company.companyName)
        );

        Top3Div.appendChild(place);
    });
    content.appendChild(Top3Div);
}

const createRanking = (companies, content) => {
    let rankingTable = document.querySelector('.ranking-table');
    if (!rankingTable) {
        rankingTable = document.createElement('table');
        rankingTable.classList.add('ranking-table');
        content.appendChild(rankingTable);
    } else {
        rankingTable.innerHTML = ''; // Limpa o conteúdo interno da tabela
    }

    const tbody = document.createElement('tbody');
    companies.forEach((company) => {
        if (company.ranking === 1 || company.ranking === 2 || company.ranking === 3) return;
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="ranking">${company.ranking}º - ${company.companyName}</td>
            <td class="branch">${company.segment}</td>
            <td class="size">${company.finalScore} pontos</td>
            <td class="pdf-img">
                <img src="/icons/file-link.svg" alt="BaixarPdf">
            </td>
        `;
        tr.classList.add(tbody.children.length % 2 === 0 ? 'even-row' : 'odd-row');

        const pdfBtn = tr.querySelector('.pdf-img')
        
        pdfBtn.addEventListener('click', () =>
            exportPDF(company.id, company.companyName)
        );

        tbody.appendChild(tr);
    });

    rankingTable.appendChild(tbody);
}

const addOptions = async (content) => {
    const segmentDropdown = document.createElement('select');
    segmentDropdown.classList.add('segment-dropdown');
    segmentDropdown.innerHTML = '<option value="">Ramo da empresa</option>';

    const companySizeDropdown = document.createElement('select');
    companySizeDropdown.classList.add('companySize-dropdown');
    companySizeDropdown.innerHTML = '<option value="">Porte da empresa</option>';

    const segmentOptions = new Set();
    const companySizeOptions = new Set(['Pequeno', 'Médio', 'Grande']);

    await fetch(`${ApiURL}/ranking/segments`, options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar segments');
            }
            return response.json();
        })
        .then(response => {
            response.forEach(segment => segmentOptions.add(segment));
        });

    segmentOptions.forEach(option => {
        const segment = document.createElement('option');
        segment.value = option;
        segment.innerHTML = option;
        segmentDropdown.appendChild(segment);
    });

    companySizeOptions.forEach(option => {
        const companySize = document.createElement('option');
        companySize.value = option;
        companySize.innerHTML = option;
        companySizeDropdown.appendChild(companySize);
    });

    segmentDropdown.addEventListener('change', () => {
        page = 0;
        updateRanking()
    });
    companySizeDropdown.addEventListener('change', () => {
        page = 0;
        updateRanking()
    });

    content.appendChild(segmentDropdown);
    content.appendChild(companySizeDropdown);
}

const updateRanking = () => {
    const segment = document.querySelector('.segment-dropdown').value;
    const companySize = document.querySelector('.companySize-dropdown').value;
    const search = document.querySelector('.search-bar').value;
    const queryParams = new URLSearchParams();
    if (segment) queryParams.append('segment', segment);
    if (companySize) queryParams.append('companySize', companySize);
    if (search) queryParams.append('tradeName', search);
    queryParams.append('page', page);
    const fullUrl = `${ApiURL}/ranking/score?${queryParams.toString()}`;

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
    searchBar.placeholder = 'Name da empresa';
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
    await fetch(`${ApiURL}/ranking/score?${queryParams.toString()}`, options)
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