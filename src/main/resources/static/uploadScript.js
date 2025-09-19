const form = document.getElementById('upload-form');
const fileInput = document.getElementById('file');
const fileNameSpan = document.getElementById('file-name');
const fileError = document.getElementById('file-error');
const submitBtn = form.querySelector("button[type='submit']");
const pContainer = document.getElementById('progress-container');
const pBar = document.getElementById('progress-bar');
const pText = document.getElementById('progress-text');
const pSub = document.getElementById('progress-sub');

submitBtn.disabled = true;

fileInput.addEventListener('change', () => {
    const file = fileInput.files[0];
    if (!file) {
        submitBtn.disabled = true;
        fileNameSpan.textContent = 'Нажми для выбора файла';
        fileError.style.display = 'none';
        return;
    }

    const allowed = ['.mp4', '.avi'];
    const ok = allowed.some(ext => file.name.toLowerCase().endsWith(ext));
    fileNameSpan.textContent = file.name;

    if (!ok) {
        submitBtn.disabled = true;
        fileError.textContent = 'Разрешены только MP4 и AVI файлы!';
        fileError.style.display = 'block';
    } else {
        submitBtn.disabled = false;
        fileError.style.display = 'none';
    }
});

form.addEventListener('submit', (e) => {
    e.preventDefault();
    if (!fileInput.files[0]) return;

    pContainer.style.display = 'block';
    pBar.style.width = '0%';
    pText.textContent = '0%';
    pSub.textContent = '';

    submitBtn.disabled = true;

    const xhr = new XMLHttpRequest();
    xhr.open('POST', form.action, true);
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

    xhr.upload.onprogress = (ev) => {
        if (ev.lengthComputable) {
            const percent = Math.round(ev.loaded * 100 / ev.total);
            pBar.style.width = percent + '%';
            pText.textContent = percent + '%';
            const toMB = (b) => (b / (1024 * 1024)).toFixed(1) + ' MB';
            pSub.textContent = `${toMB(ev.loaded)} из ${toMB(ev.total)}`;
        } else {
            pText.textContent = '…';
            pSub.textContent = 'Размер неизвестен';
        }
    };

    xhr.onloadstart = () => {
        pContainer.style.display = 'block';
    };

    xhr.onerror = () => {
        fileError.textContent = 'Сеть отвалилась. Попробуй еще раз.';
        fileError.style.display = 'block';
        submitBtn.disabled = false;
    };

    xhr.ontimeout = () => {
        fileError.textContent = 'Таймаут запроса.';
        fileError.style.display = 'block';
        submitBtn.disabled = false;
    };

    xhr.onload = () => {
        if (xhr.status >= 200 && xhr.status < 300) {
            pBar.style.width = '100%';
            pText.textContent = '100%';
            document.open();
            document.write(xhr.responseText);
            document.close();
        } else {
            fileError.textContent = 'Ошибка загрузки: ' + xhr.status;
            fileError.style.display = 'block';
            submitBtn.disabled = false;
        }
    };

    const formData = new FormData(form);
    xhr.send(formData);
});