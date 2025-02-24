const URL = ""
const ApiURL = "/api";
const enviornment = "dev";

const mainContent = document.querySelector(".main-content");
const allStyles = document.getElementById("allStyles");
const expandButton = document.querySelector(".expand-menu");
const sidebar = document.querySelector(".sidebar");
const menu = document.querySelector(".menu");
const menuItems = document.querySelectorAll(".main-list > li");
const allMenuButtons = document.querySelectorAll('.menu li');
const loginLogout = document.querySelector('.login-logout');
const loading = document.querySelector(".loading");

const headers = new Headers();
headers.append("X-Requested-With", "InsideApplication");
headers.append('Content-Type', 'application/json');
let token = sessionStorage.getItem('token');
if (token) {
    loginLogout.textContent = "Sair";
    headers.append('Authorization', `Bearer ${token}`);
}

const options = {
    method: 'GET',
    headers: headers
};

const freeInputs = () => {
    const inputs = document.querySelectorAll('input');
    const textareas = document.querySelectorAll('textarea');
    inputs.forEach(input => input.value = '');
    textareas.forEach(textarea => textarea.value = '');
}

function updateMenuButtons() {
    const userInfo = token ? jwt_decode(token) : null;
    const role = userInfo ? userInfo.role : "Guest";

    const permissions = {
        "Admin": [],
        "Gestor": [],
        "Consultor": ["employees"],
        "Guest": ["companies", "employees", "questions", "start-evaluation"]
    };

    // Menu buttons
    const menuButtons = {
        "companies": document.querySelector('.menu li[page="companies"]'),
        "employees": document.querySelector('.menu li[page="employees"]'),
        "questions": document.querySelector('.menu li[page="questions"]'),
        "start-evaluation": document.querySelector('.menu li[page="start-evaluation"]'),
    };

    // Hide or show buttons based on permissions
    Object.keys(menuButtons).forEach(button => {
        menuButtons[button].style.display = permissions[role]?.includes(button) ? 'none' : 'block';
    });

    const subitemRegistration = document.querySelectorAll('.menu .sub-list li');
    let allHidden = true;

    subitemRegistration.forEach(item => {
        if (item.style.display !== 'none') {
            allHidden = false;
        }
    });

    const itemRegistration = document.querySelector('.menu .main-list > li:first-child');
    itemRegistration && (itemRegistration.style.display = allHidden ? 'none' : 'block');
}


function loadSelectedPageScript(page, props) {
    switch (page) {
        case "login":
            onOpenLogin();
            break;
        case "forgot-password":
            onOpenForgotPassword();
            break;
        case "ranking":
            onOpenRanking();
            break;
        case "start-evaluation":
            onOpenStartEvaluation();
            break;
        case "evaluation":
            onOpenEvaluation(props);
            break;
        case "result-evaluation":
            onOpenResultEvaluation(props);
            break;
        case "companies":
            onOpenCompany();
            break;
        case "employees":
            onOpenEmployee();
            break;
        case "questions":
            onOpenQuestion();
            break;
        default:
            break;
    }
    loading.classList.add("hidden");
    mainContent.classList.remove("hidden");
}

function updateURLParameter(page, addToHistory = true) {
    const param = "page";
    let searchParams = new URLSearchParams(window.location.search);
    searchParams.set(param, page);
    let newUrl = window.location.pathname + '?' + searchParams.toString();
    
    if (addToHistory) {
        window.history.pushState({ path: newUrl }, '', newUrl);
    } else {
        window.history.replaceState({ path: newUrl }, '', newUrl);
    }
}

function getMainFrameContent(page, props, addToHistory = true) {
    if (page === 'login') {
        token = null;
        sessionStorage.removeItem('token');
        headers.delete('Authorization');
        loginLogout.textContent = "Login";
    }

    updateMenuButtons();
    fetch(`${URL}/${page}`, options)
        .then(response => {
            if (!response.ok) {
                if (response.status === 401) {
                    if (sessionStorage.getItem('token') === null) {
                        toastAlert("Você precisa estar logado para acessar essa página", "error");
                    } else {
                        toastAlert("Sua sessão expirou, faça login novamente", "error");
                    }
                } else if (response.status === 403) {
                    toastAlert("Você não tem permissão para acessar essa página", "error");
                } else {
                    toastAlert("Erro ao acessar a tela", "error");
                }
                throw new Error(`Erro ao recuperar tela: ${page}`);
            }
            return response.text();
        })
        .then(data => {
            allMenuButtons.forEach(button => button.classList.remove("active"));

            if (page !== 'login' && page !== 'evaluation' && page !== 'result-evaluation' && page !== 'forgot-password') {
                const selectedButton = document.querySelector(`.menu li[page="${page}"]`);
                selectedButton.classList.add("active");
            }
            const tempElement = document.createElement("div");
            tempElement.innerHTML = data;

            const contentDiv = tempElement.querySelector('content');
            const stylesDiv = tempElement.querySelector('styles');

            if (contentDiv) {
                mainContent.innerHTML = contentDiv.innerHTML;
                loadSelectedPageScript(page, props);
                updateURLParameter(page, addToHistory);
            }

            if (stylesDiv) {
                allStyles.innerHTML = stylesDiv.innerHTML;
            }
            return true;
        })
        .catch(error => {
            console.error(error);
            token = null;
            headers.delete('Authorization');
            sessionStorage.removeItem('token');
            getMainFrameContent("login");
            updateMenuButtons();
        });
}

function expandButtonClicked() {
    expandButton.classList.toggle("active");
    menu.classList.toggle("expanded");
}

function menuButtonClicked(event) {
    const button = event.currentTarget;
    const page = button.getAttribute("page");

    if (button.classList.contains("active")) {
        return;
    }

    mainContent.classList.add("hidden");
    loading.classList.remove("hidden");

    getMainFrameContent(page, null, true);
}

function toastAlert(message, type = 'info') {
    const activeToast = document.querySelector('.toast.show');
    if (activeToast) {
        activeToast.remove();
    }
    const toast = document.createElement('div');
    toast.classList.add('toast', type);
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add('show'), 100);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function confirmationModal({ title, message, confirmText = "Confirmar", cancelText = "Cancelar", haveCancel = true, onConfirm, onCancel }) {
    const modalOverlay = document.createElement('div');
    modalOverlay.classList.add('modal-overlay');

    const modal = document.createElement('div');
    modal.classList.add('modal');

    modal.innerHTML = `
        <div class="modal-content">
            <h2>${title}</h2>
            <p>${message}</p>
            <div class="modal-buttons">
                <button class=${haveCancel ? "btn-confirm" : "btn-confirms"}>${confirmText}</button>
                <button class=${haveCancel ? "btn-cancel" : "btn-confirms"}>${cancelText}</button>
            </div>
        </div>
    `;

    document.body.appendChild(modalOverlay);
    document.body.appendChild(modal);

    let confirmButton;
    let cancelButton;

    if (haveCancel) {
        confirmButton = modal.querySelector('.btn-confirm');
        cancelButton = modal.querySelector('.btn-cancel');
    } else {
        confirmButton = modal.querySelectorAll('.btn-confirms')[0];
        cancelButton = modal.querySelectorAll('.btn-confirms')[1];
    }

    confirmButton.addEventListener('click', () => {
        if (onConfirm) onConfirm();
        closeModal();
    });

    cancelButton.addEventListener('click', () => {
        if (onCancel) onCancel();
        closeModal();
    });

    modalOverlay.addEventListener('click', () => {
        closeModal();
    });

    function closeModal() {
        modalOverlay.remove();
        modal.remove();
    }
}

function exportPDF(companyId, tradeName) {
    fetch(`${ApiURL}/pdf/${companyId}`, options)
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao baixar PDF');
        }
        return response.blob();
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
    })
    .catch(error => {
        toastAlert('Erro ao baixar PDF', 'error');
    });
}

function frameSetup() {
    document.addEventListener("click", function(event) {
        if (!sidebar?.contains(event.target) && expandButton?.classList.contains("active")) {
            expandButton.classList.remove("active");
            menu?.classList.remove("expanded");
        }
    });

    expandButton?.addEventListener("click", expandButtonClicked);

    menuItems?.forEach(item => {
        const subList = item.nextElementSibling;

        if (subList?.classList.contains('sub-list')) {
            item.addEventListener('click', () => subList.classList.toggle('show'));

            subList.querySelectorAll('li').forEach(subItem => subItem.addEventListener("click", menuButtonClicked));
        } else {
            item.addEventListener("click", menuButtonClicked);
        }
    });

    loginLogout?.addEventListener("click", menuButtonClicked);
    getMainFrameContent("ranking", null, false);
}

window.addEventListener('popstate', (event) => {
    const urlParams = new URLSearchParams(window.location.search);
    const page = urlParams.get('page');
    if (page) {
        getMainFrameContent(page, null, false);
    } else {
        getMainFrameContent('ranking', null, false);
    }
});

frameSetup();

document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const page = urlParams.get('page');
    if (page) {
        getMainFrameContent(page, null, false);
    } else {
        getMainFrameContent('ranking', null, false);
    }
});
