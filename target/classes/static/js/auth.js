// API 基础地址
const API_BASE = '';

// 当前验证码 UUID
let loginCaptchaUuid = '';
let registerCaptchaUuid = '';

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    refreshLoginCaptcha();
});

// ========== 表单切换 ==========
function showLoginForm() {
    document.getElementById('loginForm').classList.add('active');
    document.getElementById('registerForm').classList.remove('active');
    document.getElementById('successMessage').classList.remove('active');
    refreshLoginCaptcha();
}

function showRegisterForm() {
    document.getElementById('loginForm').classList.remove('active');
    document.getElementById('registerForm').classList.add('active');
    document.getElementById('successMessage').classList.remove('active');
    refreshRegisterCaptcha();
}

function showSuccess(title, text) {
    document.getElementById('loginForm').classList.remove('active');
    document.getElementById('registerForm').classList.remove('active');
    document.getElementById('successMessage').classList.add('active');
    document.getElementById('successTitle').textContent = title;
    document.getElementById('successText').textContent = text;
}

// ========== Toast 提示 ==========
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = 'toast ' + type;
    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// ========== 验证码相关 ==========
async function refreshLoginCaptcha() {
    try {
        const response = await fetch(`${API_BASE}/get-captcha`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({})
        });

        const result = await response.json();
        if (result.success && result.data) {
            loginCaptchaUuid = result.data.uuid;
            document.getElementById('loginCaptchaImage').src = 'data:image/png;base64,' + result.data.imageBase64;
        } else {
            showToast('获取验证码失败', 'error');
        }
    } catch (error) {
        console.error('刷新验证码失败:', error);
        showToast('网络错误，请稍后重试', 'error');
    }
}

async function refreshRegisterCaptcha() {
    try {
        const response = await fetch(`${API_BASE}/get-captcha`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({})
        });

        const result = await response.json();
        if (result.success && result.data) {
            registerCaptchaUuid = result.data.uuid;
            document.getElementById('registerCaptchaImage').src = 'data:image/png;base64,' + result.data.imageBase64;
        } else {
            showToast('获取验证码失败', 'error');
        }
    } catch (error) {
        console.error('刷新验证码失败:', error);
        showToast('网络错误，请稍后重试', 'error');
    }
}

// ========== 登录处理 ==========
async function handleLogin(event) {
    event.preventDefault();

    const account = document.getElementById('loginAccount').value.trim();
    const password = document.getElementById('loginPassword').value;
    const captcha = document.getElementById('loginCaptcha').value.trim();

    if (!account) {
        showToast('请输入账号', 'error');
        return;
    }
    if (!password) {
        showToast('请输入密码', 'error');
        return;
    }
    if (!captcha) {
        showToast('请输入验证码', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                account: account,
                password: password,
                captcha: captcha,
                uuid: loginCaptchaUuid
            })
        });

        const result = await response.json();

        if (result.success) {
            const token = result.data?.token || result.data;
            localStorage.setItem('token', token);
            showToast('登录成功！', 'success');

            setTimeout(() => {
                alert('登录成功！Token 已保存到 localStorage。\n\n在实际项目中，这里会跳转到首页或仪表盘。');
            }, 500);
        } else {
            showToast(result.message || '登录失败', 'error');
            refreshLoginCaptcha();
        }
    } catch (error) {
        console.error('登录失败:', error);
        showToast('网络错误，请稍后重试', 'error');
        refreshLoginCaptcha();
    }
}

// ========== 注册处理 ==========
async function handleRegister(event) {
    event.preventDefault();

    const email = document.getElementById('registerEmail').value.trim();
    const phone = document.getElementById('registerPhone').value.trim();
    const password = document.getElementById('registerPassword').value;
    const confirmPassword = document.getElementById('registerConfirmPassword').value;
    const captcha = document.getElementById('registerCaptcha').value.trim();

    if (!email) {
        showToast('请输入邮箱', 'error');
        return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showToast('请输入有效的邮箱地址', 'error');
        return;
    }

    if (!password) {
        showToast('请输入密码', 'error');
        return;
    }

    if (password.length < 6) {
        showToast('密码长度至少为 6 位', 'error');
        return;
    }

    if (password !== confirmPassword) {
        showToast('两次输入的密码不一致', 'error');
        return;
    }

    if (!captcha) {
        showToast('请输入验证码', 'error');
        return;
    }

    try {
        const requestBody = {
            email: email,
            password: password,
            code: captcha
        };

        if (phone) {
            requestBody.phone = phone;
        }

        const response = await fetch(`${API_BASE}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        const result = await response.json();

        if (result.success) {
            showSuccess('注册成功！', '您的账号已创建，请使用邮箱登录');
        } else {
            showToast(result.message || '注册失败', 'error');
            refreshRegisterCaptcha();
        }
    } catch (error) {
        console.error('注册失败:', error);
        showToast('网络错误，请稍后重试', 'error');
        refreshRegisterCaptcha();
    }
}
