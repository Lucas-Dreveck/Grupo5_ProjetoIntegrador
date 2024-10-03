let currentPageQuestion = 0;
function onOpenQuestion() {
    const priorBtn = document.getElementById('priorBtnQuestion');
    const nextBtn = document.getElementById('nextBtnQuestion');
    const search = document.getElementById('search');
    const searchBtn = document.querySelector('.imgSearch');
    const overlay = document.querySelector('.overlay');
    const divAdd = document.querySelector('.divAddQuestion');
    const divEdit = document.querySelector('.divEditQuestion');
    const divDelete = document.querySelector('.divDeleteQuestion');
    let currentid;

    nextDataPagePerg();

    nextBtn.addEventListener('click', () => {
        currentPageQuestion++;
        nextDataPagePerg();
    });

    priorBtn.addEventListener('click', () => {
       if (currentPageQuestion > 0) {
           currentPageQuestion--;
           nextDataPagePerg();
        }
    });

    searchBtn.addEventListener('click', () => {
        currentPageQuestion = 0;
        nextDataPagePerg();
    });

    search.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            currentPageQuestion = 0;
            nextDataPagePerg();
        }
    });

    document.querySelector('.tableQuestion').addEventListener('click', (event) => {

        const { currentPergunta, currentEixo } = processEventQuestions(event);
        if(event.target.classList.contains('imgEdit')) {

            currentid = event.target.getAttribute('data-id');
            document.getElementById('questionEdit').value = currentPergunta;
            document.getElementById('questionPillarEdit').value = currentEixo;

            divEdit.style.display = 'block';
            overlay.style.display = 'block';

        } else if(event.target.classList.contains('imgDelete')) {
            currentid = event.target.getAttribute('data-id');;
            divDelete.style.display = 'block';
            overlay.style.display = 'block';
            document.querySelector('.deleteMsg').innerText = `Deseja deletar a pergunta: \n "${currentPergunta}" \n \n`;
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

    document.getElementById('confirmAddQuestion').addEventListener('click', () => {
        const question = document.getElementById('questionAdd').value;
        const pillar = document.getElementById('questionPillarAdd').value;

        if(question === '' || pillar === '') {
            toastAlert('Preencha todos os campos', 'error');
            return;
        }

        const data = {
            description: question,
            pillar,
        };

        fetch(`${ApiURL}/auth/Question`, {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        })
            .then(async response => {
                if (!response.ok) {
                    throw new Error('Erro ao adicionar Pergunta');
                }
                return response.json();
            })
            .then(data => {
                toastAlert('Pergunta adicionada com sucesso!', 'success');
                divAdd.style.display = 'none';
                overlay.style.display = 'none';
                freeInputs();
                nextDataPagePerg();
            })
            .catch(error => {
                const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
                toastAlert(errorMessage, 'error');
            });
    });

    document.getElementById('confirmEditQuestion').addEventListener('click', () => {
        const question = document.getElementById('questionEdit').value;
        const pillar = document.getElementById('questionPillarEdit').value;

        const data = {
            description: question,
            pillar,
        };

        let id = parseInt(currentid);

        fetch(`${ApiURL}/auth/Question/${id}`, {
            method: 'PUT',
            headers,
            body: JSON.stringify(data)
        })
            .then(async response => {
                if (!response.ok) {
                    throw new Error('Erro ao editar Pergunta');
                }
                return response.json();
            })
            .then(data => {
                toastAlert('Pergunta editada com sucesso!', 'success');
                currentPageQuestion = 0;
                nextDataPagePerg();
                divEdit.style.display = 'none';
                overlay.style.display = 'none';
            })
            .catch(error => {
                const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
                toastAlert(errorMessage, 'error');
            });
    });

    document.getElementById('confirmDelete').addEventListener('click' , () => {
        const id = parseInt(currentid);
        fetch(`${ApiURL}/auth/Question/${id}`, {
            method: 'DELETE',
            headers
        })
            .then(async response => {
                if (!response.ok) {
                    return {
                        status: response.status,
                        text: await response.text()
                    };
                }
                return response.text();
            })
            .then(data => {
                if (data.status !== 200 && data.status !== 204 && data.status !== undefined) {
                    throw new Error(data.text);
                }
                toastAlert('Pergunta deletada com sucesso!', 'success');
                currentPageQuestion = 0;
                nextDataPagePerg();
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

function addTableLinesQuestions(data) {
    const table = document.querySelector('.tableQuestion>tbody');
    const prevBtn = document.getElementById('priorBtnQuestion');
    const nextBtn = document.getElementById('nextBtnQuestion');

    if(data.length === 0) {
        toastAlert('Nenhuma pergunta encontrada', 'error');
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
    if (currentPageQuestion > 0 ) {
        prevBtn.removeAttribute('disabled');
        prevBtn.classList.remove('disabled');
    } else {
        prevBtn.setAttribute('disabled', 'true');
        prevBtn.classList.add('disabled');
    };

    let count = 0;

    data.forEach((question, index) => {
        const newLine = document.createElement('tr');

        const colorClass = count % 2 === 0 ? 'blue' : '';
        count++;

        newLine.innerHTML = `
            <td class="thStyle thImg ${colorClass}">
                <img src="/icons/Questions-Registration/Question.png" alt="Pergunta" class="thImg">
            </td>
            <td class="thStyle ${colorClass}">${question.id}</td>
            <td class="thStyle ${colorClass}">${question.description}</td>
            <td class="thStyle ${colorClass}">${question.pillar}</td>
            <td class="thStyle ${colorClass}">
                <img src="/icons//Companies-Registration/edit.png" 
                    data-id="${question.id}"
                    data-Pergunta="${question.description}"
                    data-Eixo="${question.pillar}"
                alt="Editar" class="imgEdit imgStyle">
                <img src="/icons//Companies-Registration/delete.png"
                    data-id="${question.id}"
                    data-Pergunta="${question.description}"
                alt="Deletar" class="imgDelete imgStyle">                
            </td>
        `;

        table.appendChild(newLine);
    });
}

function processEventQuestions(event) {
    const currentid = event.target.getAttribute('data-id');
    const currentPergunta = event.target.getAttribute('data-Pergunta');
    const currentEixo = event.target.getAttribute('data-Eixo');

    return {
        currentid,
        currentPergunta,
        currentEixo,
    };
}

function nextDataPagePerg () {
    const search = document.getElementById('search').value;
    const queryParams = new URLSearchParams();
    if (search) queryParams.append('name', search);
    queryParams.append('page', currentPageQuestion);

    fetch(`${ApiURL}/auth/Question/search?${queryParams.toString()}`, {
        method: 'GET',
        headers
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar Perguntas');
            }
            return response.json();
        })
        .then(data => {
            const table = document.querySelector('.tableQuestion>tbody');
            const trs = Array.from(table.children);
            trs.forEach(tag => {
                    tag.parentNode.removeChild(tag);
                }
            );
            addTableLinesQuestions(data);
        })
        .catch(error => {
            const errorMessage = error.message ? error.message : 'Ocorreu um erro ao processar a solicitação';
            toastAlert(errorMessage, 'error');
        });
}