const questionNumbers = 10;
let evaluationProps;
let allQuestions = [];

const addFile = () => {
    alert('W.I.P');
}

const renderQuestions = (questions, isLast) => {
    const otherCategory = document.querySelectorAll('.questions-category');
    const questionsCategory = document.createElement('div');
    if (otherCategory.length > 0) {
        questionsCategory.style.display = 'none';
    }
    questionsCategory.classList.add('questions-category');
    questionsCategory.innerHTML = `<h2>${evaluationProps.isNew ? questions[0].pillar : questions[0].questionPillar}</h2>`;
    questions.forEach(question => {
        if (!evaluationProps.isNew) {
            question = {
                id: question.questionId,
                description: question.questionDescription,
                pillar: question.questionPillar,
                answer: question.userAnswer
            }
        }
        const div = document.createElement('div');
        const FwhatsChecked = () => {
            const randomNumber = Math.random();
    
            if (randomNumber < 0.4) {
                return 1; // Retorna 1 com 40% de probabilidade
            } else if (randomNumber < 0.8) {
                return -1; // Retorna -1 com 40% de probabilidade
            } else {
                return 0; // Retorna 0 com 20% de probabilidade
            }
        }
        const whatsChecked = FwhatsChecked();
        div.classList.add('evaluation-question');
        div.innerHTML = `
            <h2>${question.description}</h2>
            <div class="evaluation-answer">
                <label>
                    <input type="radio" name="answer-${question.id}" value="Conforme" ${question.answer ? question.answer === "Conforme" ? 'checked' : null : (enviornment === 'dev' && whatsChecked === 1 ? 'checked' : null)}>
                    <p>Conforme</p>
                </label>
                <br>
                <label>
                    <input type="radio" name="answer-${question.id}" value="NaoConforme" ${question.answer ? question.answer === "NaoConforme" ? 'checked' : null : (enviornment === 'dev' && whatsChecked === -1 ? 'checked' : null)}>
                    <p>Não conforme</p>
                </label>
                <br>
                <label>
                    <input type="radio" name="answer-${question.id}" value="NaoSeAdequa" ${question.answer ? question.answer === "NaoSeAdequa" ? 'checked' : null : (enviornment === 'dev' && whatsChecked === 0 ? 'checked' : null)}>
                    <p>Não aplicavel</p>
                </label>
                <!-- <div class="add-file">
                    <button class="btn-file" onClick="addFile()">Adicionar Arquivo</button>
                </div> -->
            </div>
        `;
        return questionsCategory.appendChild(div);
    });

    const evaluation = document.querySelector('.evaluation-avaliation');
    const divBtns = document.createElement('div');
    divBtns.classList.add('btns');
    const btnSubmit = document.createElement('button');
    btnSubmit.id = isLast ? 'btn-submit' : 'btn-next';
    btnSubmit.classList.add(isLast ? 'btn-submit' : 'btn-next');
    btnSubmit.textContent = isLast ? 'Finalizar' : 'Próximo';
    btnSubmit.addEventListener('click', () => {
        if (isLast) {
            let isComplete = true;
            if (!isAllQuestionsAnswered(allQuestions)) {
                confirmationModal({
                    title: 'Atenção',
                    message: 'Você não respondeu todas as perguntas, deseja finalizar mesmo assim?',
                    confirmText: 'Finalizar avaliação',
                    cancelText: 'Cancelar',
                    onCancel: () => {
                        return;
                    },
                    onConfirm: () => {
                        isComplete = false;
                        sendQuestions(isComplete, true);
                    }
                });
            } else {
                sendQuestions(isComplete);
            }
        } else {
            sendQuestions(false);
            toastAlert('Respostas salvas com sucesso', 'success');
            const nextCategory = questionsCategory.nextElementSibling;
            questionsCategory.style.display = 'none';
            nextCategory.style.display = 'flex';
            evaluation.scrollTop = 0;
        }
    });
    if (otherCategory.length > 0) {
        const btnPrev = document.createElement('button');
        btnPrev.id = 'btn-prev';
        btnPrev.classList.add('btn-prev');
        btnPrev.textContent = 'Voltar';
        btnPrev.addEventListener('click', () => {
            const prevCategory = questionsCategory.previousElementSibling;
            questionsCategory.style.display = 'none';
            prevCategory.style.display = 'flex';
            evaluation.scrollTop = 0;
        });
        divBtns.appendChild(btnPrev);
    }

    divBtns.appendChild(btnSubmit);
    questionsCategory.appendChild(divBtns);
    evaluation.appendChild(questionsCategory);
}

const isAllQuestionsAnswered = (allQuestions) => {
    for (let i = 0; i < allQuestions.length; i++) {
        const answer = document.querySelector(`input[name="answer-${evaluationProps.isNew ? allQuestions[i].id : allQuestions[i].questionId}"]:checked`);
        if (!answer) {
            return false;
        }
    }

    return true;
};

const sendQuestions = (isComplete, hasFinished) => {
    const questions = [];
    allQuestions.forEach(question => {
        const answer = document.querySelector(`input[name="answer-${evaluationProps.isNew ? question.id : question.questionId}"]:checked`);
        questions.push(
            {
                questionId: evaluationProps.isNew ? question.id : question.questionId,
                userAnswer: answer?.value ? answer.value : null,
                questionPillar: evaluationProps.isNew ? question.pillar : question.questionPillar,
            }
        );
    });
    const fullURL = `${ApiURL}/auth/processAnswers?companyId=${evaluationProps.company.id}&isComplete=${isComplete}`;
    const body = questions
    fetch(fullURL, {
        method: 'POST',
        headers,
        body: JSON.stringify(body)
    }).then(response => {
        if (!response.ok) {
            throw new Error('Erro ao enviar dados');
        }
        return response.json();
    }).then(data => {
        if (isComplete) {
            toastAlert('Respostas enviadas com sucesso', 'success');
            getMainFrameContent('result-evaluation', data);
        } else if (hasFinished) {
            toastAlert('Respostas salvas com sucesso', 'success');
            getMainFrameContent('ranking');
        }
    }).catch(() => {
        toastAlert('Erro ao enviar dados', 'error');
    });
}

const onOpenEvaluation = (props) => {
    evaluationProps = props;
    const evaluation = document.querySelector('.evaluation-avaliation')
    evaluation.addEventListener('submit', (event) => {
        if (event.explicitOriginalTarget !== document.getElementById("btn-submit")) {
            event.preventDefault();
        }
    });
    const government = [];
    const environmental = [];
    const social = [];

    fetch(`${ApiURL}/auth/evaluation/${props.isNew}?companyId=${props.company.id}`, options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao recuperar dados');
            }
            return response.json();
        })
        .then(data => {
            if (props.isNew) {
                data.questions.forEach(item => {
                    if (item.pillar === 'Governamental') {
                        if (government.length < questionNumbers) {
                            government.push(item);
                        }
                    } else if (item.pillar === 'Ambiental') {
                        if (environmental.length < questionNumbers) {
                            environmental.push(item);
                        }    
                    } else if (item.pillar === 'Social') {
                        if (social.length < questionNumbers) {
                            social.push(item);
                        }
                    }
                });
            } else {
                data.evaluationRequests.forEach(item => {
                    delete item.questionNumber;
                    if (item.questionPillar === 'Governamental') {
                        government.push(item);
                    } else if (item.questionPillar === 'Ambiental') {
                        environmental.push(item);
                    } else if (item.questionPillar === 'Social') {
                        social.push(item);
                    }
                });
            }
            
            allQuestions = [...government, ...environmental, ...social];

            renderQuestions(government)
            renderQuestions(environmental)
            renderQuestions(social, true)
        })
        .catch(err => {
            console.error(err);
        });
};