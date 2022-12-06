
async function auth() {
    let res = await fetch('http://localhost:8080/api/auth');
    return await res.json();
}

refreshPage();
EditModal();
DeleteModal();
newUser();

//Нажатие на кнопку Edit в таблице юзеров
function onEditButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        const tr = button.parentNode.parentNode;
        document.querySelector('#editId').value = tr.children[0].innerHTML;
        document.querySelector('#editName').value = tr.children[1].innerHTML;
        document.querySelector('#editAge').value = tr.children[2].innerHTML;
        document.querySelector('#editUsername').value = tr.children[3].innerHTML;
        document.querySelector('#editPassword').value = '';
        document.querySelector('#editForm').ariaModal = 'show';
    })
}

//Нажатие на кнопку Delete в таблице юзеров
function onDeleteButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        document.querySelector('#deleteRoles').innerHTML = '';

        const tr = button.parentNode.parentNode;
        document.querySelector('#deleteId').value = tr.children[0].innerHTML;
        document.querySelector('#deleteName').value = tr.children[1].innerHTML;
        document.querySelector('#deleteAge').value = tr.children[2].innerHTML;
        document.querySelector('#deleteUsername').value = tr.children[3].innerHTML;


        let roles = Array.from(tr.children[4].children).map(role => role.innerHTML);
        roles.forEach(role => {
            let option = document.createElement('option');
            option.text = role;
            document.querySelector('#deleteRoles').appendChild(option);
        })
        document.querySelector('#deleteForm').ariaModal = 'show';
    })
}

//Модальное окно Edit
async function EditModal() {
    let roles = await fetch("http://localhost:8080/api/roles");
    roles = await roles.json();
    roles.forEach(role => {
        if (document.querySelector('#editRoles').children.length < 2) {
            let option = document.createElement("option");
            option.value = role.id;
            option.text = role.roleName.substring(5, role.roleName.length);
            document.querySelector('#editRoles').appendChild(option);
        }
    });

    document.querySelector('#editBtnSubmit').addEventListener('click', async (e) => {
        e.preventDefault();
        const url = `http://localhost:8080/admin/api/edit/${document.querySelector('#editId').value}`;
        await fetch(url, {
            method: "PATCH",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: document.querySelector('#editId').value,
                name: document.querySelector('#editName').value,
                age: document.querySelector('#editAge').value,
                username: document.querySelector('#editUsername').value,
                password: document.querySelector('#editPassword').value,
                rolesSet: listOfRoles(document.querySelector('#editRoles'))
            })
        });
        await refreshPage();
        document.querySelector('#editForm').reset();
    });
}

//Модальное окно Delete
function DeleteModal() {
    document.querySelector('.deleteSubmit').addEventListener('click', async (e) => {
        e.preventDefault();
        let url = `http://localhost:8080/admin/api/delete/${document.querySelector('#deleteId').value}`;
        await fetch(url, {
            method: "DELETE"
        });
        await refreshPage();
        document.querySelector('#deleteForm').reset();
    });
}

//Таб нового юзера
async function newUser() {


    let roles = await fetch('http://localhost:8080/api/roles');
    roles = await roles.json();
    roles.forEach(role => {
        if (document.querySelector('#roles').children.length < 3) {
            let option = document.createElement("option");
            option.value = role.id;
            option.text = role.roleName.substring(5, role.roleName.length);
            document.querySelector('#roles').appendChild(option);
        }
    });

    document.querySelector('#newUserForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const url = 'http://localhost:8080/admin/api/new';
        let response = await fetch(url,{
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: document.querySelector('#name').value,
                age: document.querySelector('#age').value,
                username: document.querySelector('#username').value,
                password: document.querySelector('#password').value,
                rolesSet: listOfRoles(document.querySelector('#roles'))
            })
        });
        if (response.status === 406) {
            document.querySelector('#warning').classList.remove('d-none');
        } else {
            await refreshPage();
            document.querySelector('a.allUsers').classList.add('active');
            document.querySelector('a.newUser').classList.remove('active');
            document.querySelector('#allUsers').classList.add('active');
            document.querySelector('#newUser').classList.remove('active');
            document.querySelector('#newUserForm').reset();
        }
    })
}

//Заполнение верхней панели
async function upperPanel() {
    let user = await auth();
    document.getElementById("adminUsername").textContent = user.username;
    let roles = "";
    user.rolesSet.forEach(role => {
        roles += role.roleName.substring(5, role.roleName.length) + " ";
    })
    document.getElementById("adminRoles").textContent = roles;
}

//Преобразование выбранных option-ов в массив ролей
function listOfRoles(options) {
    let res = [];
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            res.push({id: options[i].value, roleName: options[i].text});
        }
    }
    return res;
}

//Обновление списка юзеров
async function refreshPage() {
    let response = await fetch("http://localhost:8080/admin/api");
    let users = await response.json();
    document.querySelector('#allUsersTBody').innerHTML = '';
    users.forEach(user => {
        let table = "";
        let roles = user.rolesSet.map(role => role.roleName.substring(5, role.roleName.length));
        let rolesInTable = '';
        roles.forEach(role => {rolesInTable += `<div>${role}</div>`});
        table += `<tr id="tr${user.id}">
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.name}</td>
            <td class="align-middle">${user.age}</td>
            <td class="align-middle">${user.username}</td>
            <td class="align-middle">${rolesInTable}</td>
            <td class="align-middle"><button class="btn btn-primary btn-sm editBtn" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>
            <td class="align-middle"><button class="btn btn-danger btn-sm deleteBtn" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button></td>
            </tr>`;
        document.querySelector('#allUsersTBody').innerHTML += table;
    });
    document.querySelectorAll('.editBtn').forEach(btn => {
        onEditButton(btn);
    });
    document.querySelectorAll('.deleteBtn').forEach(btn => {
        onDeleteButton(btn);
    })
    await upperPanel();
    await refreshUserPanel();
}

//Обновление панели юзера
async function refreshUserPanel() {
    const tbody = document.querySelector('#userTBody');

    let user = await auth();
    let roles = user.rolesSet.map(role => role.roleName.substring(5, role.roleName.length));
    let rolesInTable = '';
    roles.forEach(role => {rolesInTable += `<div>${role}</div>`});

    tbody.innerHTML = `<tr>
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.name}</td>
            <td class="align-middle">${user.age}</td>
            <td class="align-middle">${user.username}</td>
            <td class="align-middle">${rolesInTable}</td>
            </tr>`;
}










