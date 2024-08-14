const createGraphs = (form) => {
    return (`
        <div class="graph">
            <svg viewBox="0 0 36 36" class="circular-chart">
                <path class="circle social-graph"
                stroke-dasharray="${form.pontuacaoSocial}, 100"
                d="M18 2.0845
                    a 15.9155 15.9155 0 0 1 0 31.831
                    a 15.9155 15.9155 0 0 1 0 -31.831"
                />
                <text x="18" y="20" text-anchor="middle" class="percentage">${form.pontuacaoSocial}%</text>
            </svg>
            <h2 class="subtitle">Social</h2>
        </div>
        <div class="graph">
            <svg viewBox="0 0 36 36" class="circular-chart">
                <path class="circle governamental-graph"
                stroke-dasharray="${form.pontuacaoGovernamental}, 100"
                d="M18 2.0845
                    a 15.9155 15.9155 0 0 1 0 31.831
                    a 15.9155 15.9155 0 0 1 0 -31.831"
                />
                <text x="18" y="20" text-anchor="middle" class="percentage">${form.pontuacaoGovernamental}%</text>
            </svg>
            <h2 class="subtitle">Governamental</h2>
        </div>
        <div class="graph">
            <svg viewBox="0 0 36 36" class="circular-chart">
                <path class="circle ambiental-graph"
                stroke-dasharray="${form.pontuacaoAmbiental}, 100"
                d="M18 2.0845
                    a 15.9155 15.9155 0 0 1 0 31.831
                    a 15.9155 15.9155 0 0 1 0 -31.831"
                />
                <text x="18" y="20" text-anchor="middle" class="percentage">${form.pontuacaoAmbiental}%</text>
            </svg>
            <h2 class="subtitle">Ambiental</h2>
        </div>
    `);
}

const createTable = (eixo, data) => {
    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    table.classList.add('table');
    table.classList.add(`table-${eixo}`);

    thead.innerHTML = `
        <tr>
            <th class="question">Pergunta</th>
            <th class="answer">Resposta</th>
        </tr>
    `;
    table.appendChild(thead);

    data.forEach((answer, index) => {
        let resposta = '';
        switch (answer.resposta) {
            case 'NaoConforme':
                resposta = "Não conforme";
                break;
            case 'NaoSeAdequa':
                resposta = "Não aplicavel";
                break;
            case 'Conforme':
                resposta ="Conforme";
                break;
        }
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="question">${answer.pergunta.descricao}</td>
            <td class="answer">${resposta}</td>
        `;
        tr.classList.add(index % 2 === 0 ? 'even-row' : 'odd-row')
        tbody.appendChild(tr);
    });

    table.appendChild(tbody);

    return table.outerHTML;
}

const renderTables = (answers) => {
    
    const socialData = answers.filter(answer => answer.pergunta.eixo === 'Social');
    const governamentalData = answers.filter(answer => answer.pergunta.eixo === 'Governamental');
    const ambientalData = answers.filter(answer => answer.pergunta.eixo === 'Ambiental');
    
    const socialTable = createTable('social', socialData);
    const governamentalTable = createTable('governamental', governamentalData);
    const ambientalTable = createTable('ambiental', ambientalData);
    
    return socialTable + governamentalTable + ambientalTable;
}

const onOpenResultAvaliacao = (props) => {
    const result = document.querySelector('#result-content');
    const h1 = document.createElement('h1');
    h1.classList.add('title');
    h1.textContent = `Resultados ${props.empresa.nomeFantasia}`;
    result.appendChild(h1);

    const graphics = document.createElement('div');
    graphics.classList.add('graphics');
    graphics.innerHTML = createGraphs(props);
    result.appendChild(graphics);
    
    const tables = document.createElement('div');
    tables.classList.add('tables');
    tables.innerHTML = renderTables(props.respostas);
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
        exportPDF(props.empresa.id, props.empresa.nomeFantasia);
    });
    
    result.appendChild(buttonExport);
    result.appendChild(buttonHomeScreen);
}