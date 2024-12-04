function editMovie(id, title, description, releaseYear, duration, language, selectedGenres, selectedActors, selectedDirectors, selectedProducers) {
    document.getElementById('id').value = id;
    document.getElementById('title').value = title;
    document.getElementById('description').value = description;
    document.getElementById('releaseYear').value = releaseYear;
    document.getElementById('duration').value = duration;
    document.getElementById('language').value = language;
    document.getElementById('method').value = 'PUT';

    document.getElementById('formTitle').innerText = 'Редактирование фильма';

    updateSelect(document.getElementById('genres'), selectedGenres);
    updateSelect(document.getElementById('actors'), selectedActors);
    updateSelect(document.getElementById('directors'), selectedDirectors);
    updateSelect(document.getElementById('producers'), selectedProducers);
}

function updateSelect(selectElement, selectedValues) {
    const selectedSet = new Set(selectedValues);
    Array.from(selectElement.options).forEach(option => {
        option.selected = selectedSet.has(option.value);
    });
}

function resetForm() {
    document.getElementById('id').value = '';
    document.getElementById('title').value = '';
    document.getElementById('description').value = '';
    document.getElementById('releaseYear').value = '';
    document.getElementById('duration').value = '';
    document.getElementById('language').value = '';
    document.getElementById('method').value = 'POST';

    document.getElementById('formTitle').innerText = 'Добавление фильма';

    resetSelect(document.getElementById('genres'));
    resetSelect(document.getElementById('actors'));
    resetSelect(document.getElementById('directors'));
    resetSelect(document.getElementById('producers'));
}

function resetSelect(selectElement) {
    Array.from(selectElement.options).forEach(option => {
        option.selected = false;
    });
}

function validateForm() {
    const language = document.getElementById("language").value;
    if (!language) {
        alert("Пожалуйста, выберите язык.");
        return false;
    }
    return true;
}
