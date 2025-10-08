/**
 * SCRIPT GERAL - ROTEAMENTO DE PÁGINAS
 * Este script detecta em qual página o usuário está e executa o código correspondente.
 */
document.addEventListener('DOMContentLoaded', function () {
    // Roteador: Verifica a existência de um elemento único da página e chama a função de inicialização
    if (document.getElementById('cadastro-form')) {
        initRegisterPage();
    }
    if (document.getElementById('login-form')) {
        // initLoginPage(); // Descomente quando a lógica de login estiver pronta
    }
    if (document.getElementById('user-guest')) {
        // initIndexPage(); // Descomente quando a lógica de index estiver pronta
    }
});

// =================================================================
// LÓGICA DA PÁGINA DE CADASTRO
// =================================================================
function initRegisterPage() {
    // Seletores dos elementos do formulário
    const form = document.getElementById('cadastro-form');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const birthDateInput = document.getElementById('birthDate');
    const cpfInput = document.getElementById('cpf');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const profileImageInput = document.getElementById('profileImage');
    const imagePreview = document.getElementById('image-preview');

    // Seletores dos requisitos da senha
    const req = {
        match: document.getElementById('match'),
        length: document.getElementById('length'),
        uppercase: document.getElementById('uppercase'),
        special: document.getElementById('special'),
        number: document.getElementById('number')
    };

    // Seletores do modal
    const modalElement = new bootstrap.Modal(document.getElementById('confirmation-modal'));
    const modalTitle = document.getElementById('modal-title');
    const modalBody = document.getElementById('modal-body');

    // --- VALIDAÇÕES EM TEMPO REAL ---

    passwordInput.addEventListener('input', validatePassword);
    confirmPasswordInput.addEventListener('input', validatePassword);

    cpfInput.addEventListener('input', () => {
        cpfInput.value = maskCpf(cpfInput.value);
        if (isValidCpf(cpfInput.value)) {
            cpfInput.classList.remove('is-invalid');
        } else {
            cpfInput.classList.add('is-invalid');
        }
    });

    birthDateInput.addEventListener('change', () => {
        if (isOver18(birthDateInput.value)) {
            birthDateInput.classList.remove('is-invalid');
        } else {
            birthDateInput.classList.add('is-invalid');
        }
    });

    profileImageInput.addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.src = e.target.result;
            }
            reader.readAsDataURL(file);
        }
    });

    // --- SUBMISSÃO DO FORMULÁRIO ---

    form.addEventListener('submit', async function(event) {
        event.preventDefault();

        if (!isFormValid()) {
            showModal('Erro de Validação', 'Por favor, corrija os campos em vermelho.', false);
            return;
        }

        const userData = {
            name: nameInput.value,
            email: emailInput.value,
            birthDate: birthDateInput.value,
            cpf: cpfInput.value.replace(/\D/g, ''),
            password: passwordInput.value,
        };

        try {
            const response = await fetch('/api/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                const createdUser = await response.json();
                showModal('Sucesso!', `Usuário ${createdUser.name} cadastrado com sucesso!`, true);
                form.reset();
                imagePreview.src = 'https://placehold.co/150x150?text=Sua+Foto';
                // Reset visual dos requisitos da senha
                Object.values(req).forEach(el => el.classList.remove('valid', 'invalid'));
            } else {
                const errorData = await response.json();
                showModal('Erro no Cadastro', errorData.message || 'Ocorreu um erro ao tentar cadastrar.', false);
            }
        } catch (error) {
            console.error('Erro de rede:', error);
            showModal('Erro de Conexão', 'Não foi possível se conectar ao servidor.', false);
        }
    });

    // --- FUNÇÕES AUXILIARES (ESCOPO DE CADASTRO) ---

    function validatePassword() {
        const pass = passwordInput.value;
        const conf = confirmPasswordInput.value;
        updateRequirement(req.match, conf && pass === conf);
        updateRequirement(req.length, pass.length >= 8 && pass.length <= 16);
        updateRequirement(req.uppercase, /[A-Z]/.test(pass));
        updateRequirement(req.special, /[!@#$%^&*(),.?":{}|<>]/.test(pass));
        updateRequirement(req.number, /\d/.test(pass));
    }

    function updateRequirement(element, isValid) {
        element.classList.toggle('valid', isValid);
        element.classList.toggle('invalid', !isValid);
    }

    function maskCpf(value) {
        return value.replace(/\D/g, '').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d{1,2})/, '$1-$2').substring(0, 14);
    }

    function isValidCpf(cpf) {
        cpf = cpf.replace(/[^\d]+/g,'');
        if(cpf === '' || cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;
        let add = 0;
        for (let i=0; i < 9; i ++) add += parseInt(cpf.charAt(i)) * (10 - i);
        let rev = 11 - (add % 11);
        if (rev === 10 || rev === 11) rev = 0;
        if (rev !== parseInt(cpf.charAt(9))) return false;
        add = 0;
        for (let i = 0; i < 10; i ++) add += parseInt(cpf.charAt(i)) * (11 - i);
        rev = 11 - (add % 11);
        if (rev === 10 || rev === 11) rev = 0;
        if (rev !== parseInt(cpf.charAt(10))) return false;
        return true;
    }

    function isOver18(dateString) {
        if (!dateString) return false;
        const today = new Date();
        const birthDate = new Date(dateString);
        let age = today.getFullYear() - birthDate.getFullYear();
        const m = today.getMonth() - birthDate.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) age--;
        return age >= 18;
    }

    function isFormValid() {
        const passwordOk = Object.values(req).every(el => el.classList.contains('valid'));
        return nameInput.value.trim() !== '' && emailInput.value.includes('@') && isOver18(birthDateInput.value) && isValidCpf(cpfInput.value) && passwordOk;
    }

    function showModal(title, message, isSuccess) {
        modalTitle.textContent = title;
        modalBody.textContent = message;
        modalTitle.classList.toggle('text-success', isSuccess);
        modalTitle.classList.toggle('text-danger', !isSuccess);
        modalElement.show();
    }
}


// =================================================================
// LÓGICA DA PÁGINA INDEX (DO SEU SCRIPT ANTIGO)
// =================================================================
function initIndexPage() {
    fetch('/api/user/status')
        .then(response => response.json())
        .then(data => {
            const userGuestDiv = document.getElementById('user-guest');
            const userLoggedInDiv = document.getElementById('user-logged-in');
            if (data.loggedIn) {
                userGuestDiv.style.display = 'none';
                document.getElementById('user-name').textContent = `Olá, ${data.name}!`;
                document.getElementById('user-avatar').src = data.avatarUrl || 'https://placehold.co/80x80/ced4da/6c757d?text=Avatar';
                userLoggedInDiv.style.display = 'flex';
            } else {
                userGuestDiv.style.display = 'flex';
                userLoggedInDiv.style.display = 'none';
            }
        });
}

// =================================================================
// LÓGICA DA PÁGINA DE LOGIN (DO SEU SCRIPT ANTIGO - A SER IMPLEMENTADA)
// =================================================================
function initLoginPage() {
    // Se o usuário já estiver logado, redireciona para o index
    fetch('/api/user/status').then(r => r.json()).then(data => {
        if (data.loggedIn) {
            window.location.href = '/index.html';
        }
    });

    const loginForm = document.getElementById('login-form');
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        // Lógica de submissão do login (a ser implementada)
        console.log('Formulário de login enviado');
        const errorDiv = document.getElementById('login-error');
        errorDiv.textContent = 'Funcionalidade de login ainda não implementada.';
        errorDiv.classList.remove('d-none');
    });
}

