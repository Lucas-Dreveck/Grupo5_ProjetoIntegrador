const createGraphs = (evaluation) => {
    return (`
        <div class="graph">
            <svg viewBox="0 0 36 36" class="circular-chart">
                <path class="circle social-graph"
                stroke-dasharray="${evaluation.socialScore}, 100"
                d="M18 2.0845
                    a 15.9155 15.9155 0 0 1 0 31.831
                    a 15.9155 15.9155 0 0 1 0 -31.831"
                />
                <text x="18" y="20" text-anchor="middle" class="percentage">${evaluation.socialScore}%</text>
            </svg>
            <h2 class="subtitle">Social</h2>
        </div>
        <div class="graph">
            <svg viewBox="0 0 36 36" class="circular-chart">
                <path class="circle government-graph"
                stroke-dasharray="${evaluation.governmentScore}, 100"
                d="M18 2.0845
                    a 15.9155 15.9155 0 0 1 0 31.831
                    a 15.9155 15.9155 0 0 1 0 -31.831"
                />
                <text x="18" y="20" text-anchor="middle" class="percentage">${evaluation.governmentScore}%</text>
            </svg>
            <h2 class="subtitle">Governamental</h2>
        </div>
        <div class="graph">
            <svg viewBox="0 0 36 36" class="circular-chart">
                <path class="circle enviornmental-graph"
                stroke-dasharray="${evaluation.enviornmentalScore}, 100"
                d="M18 2.0845
                    a 15.9155 15.9155 0 0 1 0 31.831
                    a 15.9155 15.9155 0 0 1 0 -31.831"
                />
                <text x="18" y="20" text-anchor="middle" class="percentage">${evaluation.enviornmentalScore}%</text>
            </svg>
            <h2 class="subtitle">Ambiental</h2>
        </div>
    `);
}

const createTable = (pillar, data) => {
    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    table.classList.add('table');
    table.classList.add(`table-${pillar}`);

    thead.innerHTML = `
        <tr>
            <th class="question">Pergunta</th>
            <th class="answer">Resposta</th>
        </tr>
    `;
    table.appendChild(thead);

    data.forEach((dataAnswer, index) => {
        let answer = '';
        switch (dataAnswer.answer) {
            case 'NaoConforme':
                answer = "Não conforme";
                break;
            case 'NaoSeAdequa':
                answer = "Não aplicavel";
                break;
            case 'Conforme':
                answer ="Conforme";
                break;
        }
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="question">${dataAnswer.question.description}</td>
            <td class="answer">${answer}</td>
        `;
        tr.classList.add(index % 2 === 0 ? 'even-row' : 'odd-row')
        tbody.appendChild(tr);
    });

    table.appendChild(tbody);

    return table.outerHTML;
}

const renderTables = (answers) => {
    
    const socialData = answers.filter(answer => answer.question.pillar === 'Social');
    const governmentData = answers.filter(answer => answer.question.pillar === 'Governamental');
    const enviornmentData = answers.filter(answer => answer.question.pillar === 'Ambiental');
    
    const socialTable = createTable('social', socialData);
    const governmentTable = createTable('government', governmentData);
    const enviornmentalData = createTable('enviornmental', enviornmentData);
    
    return socialTable + governmentTable + enviornmentalData;
}

const onOpenResultEvaluation = (props) => {
    const result = document.querySelector('#result-content');
    const h1 = document.createElement('h1');
    h1.classList.add('title');
    h1.textContent = `Resultados ${props.company.tradeName}`;
    result.appendChild(h1);

    const graphics = document.createElement('div');
    graphics.classList.add('graphics');
    graphics.innerHTML = createGraphs(props);
    result.appendChild(graphics);
    
    const tables = document.createElement('div');
    tables.classList.add('tables');
    tables.innerHTML = renderTables(props.answers);
    result.appendChild(tables);

    const buttonHomeScreen = document.createElement('button');
    buttonHomeScreen.classList.add('btn-home');
    buttonHomeScreen.textContent = 'Voltar a tela principal';
    buttonHomeScreen.addEventListener('click', () => {
        getMainFrameContent('ranking');
    });

    const buttonExport = document.createElement('button');
    buttonExport.id = 'export-pdf';
    buttonExport.classList.add('btn-export');
    buttonExport.textContent = 'Ver PDF';
    buttonExport.addEventListener('click', () => {
        exportPDF(props.company.id, props.company.tradeName);
    });
    
    result.appendChild(buttonExport);
    result.appendChild(buttonHomeScreen);
}