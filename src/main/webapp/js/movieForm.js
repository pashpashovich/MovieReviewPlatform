function editMovie(id, title, description, releaseYear, duration, language) {
    document.getElementById('id').value = id;
    document.getElementById('title').value = title;
    document.getElementById('description').value = description;
    document.getElementById('releaseYear').value = releaseYear;
    document.getElementById('duration').value = duration;
    document.getElementById('language').value = language;
    document.getElementById('method').value = 'PUT';
}
