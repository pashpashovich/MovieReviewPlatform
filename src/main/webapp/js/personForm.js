function editPerson(id, fullName, role) {
    document.getElementById('id').value = id;
    document.getElementById('fullName').value = fullName;
    document.getElementById('role').value = role;
    document.getElementById('method').value = 'PUT';

    const formTitle = document.getElementById('formTitle');
    formTitle.textContent = 'Редактирование человека';
}
